/*
 * AvailComponent.java
 * Copyright © 1993-2018, The Avail Foundation, LLC.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * * Neither the name of the copyright holder nor the names of the contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.availlang.plugin.core;
import com.avail.AvailRuntime;
import com.avail.builder.*;
import com.avail.builder.AvailBuilder.LoadedModule;
import com.avail.descriptor.ModuleDescriptor;
import com.avail.environment.nodes.AbstractBuilderFrameTreeNode;
import com.avail.environment.nodes.ModuleOrPackageNode;
import com.avail.environment.nodes.ModuleRootNode;
import com.avail.persistence.IndexedRepositoryManager;
import com.avail.persistence.IndexedRepositoryManager.ModuleArchive;
import com.avail.persistence.IndexedRepositoryManager.ModuleVersion;
import com.avail.persistence.IndexedRepositoryManager.ModuleVersionKey;
import com.avail.utility.Mutable;
import com.avail.utility.evaluation.Continuation0;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.ui.treeStructure.Tree;
import org.availlang.plugin.actions.events.DummyEvent;
import org.availlang.plugin.build.BuildModule;
import org.availlang.plugin.build.ClearRepo;
import org.availlang.plugin.core.utility.ModuleEntryPoints;
import org.availlang.plugin.exceptions.AvailPluginException;
import org.availlang.plugin.psi.AvailPsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.avail.utility.Nulls.stripNull;

/**
 * An {@code AvailComponent} is an {@link ApplicationComponent} used for
 * initializing a {@link ModuleNameResolver}, an {@link AvailRuntime}, an {@link
 * AvailBuilder}.
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class AvailComponent
implements ApplicationComponent
{
	/**
	 * Provide the sole instance of {@link AvailComponent}.
	 *
	 * @return The single {@code AvailComponent}.
	 */
	public static @NotNull AvailComponent getInstance ()
	{
		final Application application = ApplicationManager.getApplication();
		final AvailComponent component =
			application.getComponent(AvailComponent.class);
		assert component != null;
		return component;
	}

	/**
	 * The map of {@link ModuleRoot} {@linkplain ModuleRoot#name name} to the
	 * {@code ModuleRoot} for the {@code ModuleRoots} being developed in the
	 * active project.
	 */
	private @NotNull Map<String, ModuleRoot> moduleRootMap =
		new ConcurrentHashMap<>();

	/**
	 * Clear the provided {@link ModuleRoot}'s {@link IndexedRepositoryManager}.
	 *
	 * @param rootName
	 *        The name of the {@code ModuleRoot} to clear.
	 * @param event
	 *        The {@link AnActionEvent} that prompted this clearing.
	 * @param onSuccess
	 *        The {@link Continuation0} to run after clearing the repository.
	 */
	public void clearRepository (
		final @NotNull String rootName,
		final @NotNull AnActionEvent event,
		final @NotNull Continuation0 onSuccess)
	{
		final ModuleRoot root = moduleRootMap.getOrDefault(rootName, null);
		if (root == null)
		{
			AvailPluginException.dialog(String.format(
				"%s is not a registered Avail root so it cannot be cleared!",
				rootName));
		}
		else
		{
			ClearRepo.clearRepository(root, event, onSuccess);
		}
	}

	/**
	 * Clear the provided {@link ModuleRoot}'s {@link IndexedRepositoryManager}.
	 *
	 * @param event
	 *        The {@link AnActionEvent} that prompted this clearing.
	 * @param onSuccess
	 *        The {@link Continuation0} to run after clearing the repository.
	 */
	public void clearAllRootRepositories (
		final @NotNull AnActionEvent event,
		final @NotNull Continuation0 onSuccess)
	{
		final Iterator<ModuleRoot> roots = moduleRootMap.values().iterator();
		if (!roots.hasNext())
		{
			AvailPluginException.dialog(
				"There are no registered Avail roots, so there is nothing to "
					+ "clear!");
		}
		else
		{
			clearNextRepository(roots, event, onSuccess);
		}
	}

	/**
	 * The map of {@link ModuleRoot} {@linkplain ModuleRoot#name name} to the
	 * {@code ModuleRoot} for the Avail SDK {@code ModuleRoots}.
	 */
	private @NotNull Map<String, ModuleRoot> sdkRootMap =
		new ConcurrentHashMap<>();

	private void initializeSDK ()
	{
		if (sdkRootMap.isEmpty())
		{
			return;
		}
		buildAllRoots(
			sdkRootMap.values().iterator(),
			new DummyEvent());
	}

	private void buildAllRoots (
		final @NotNull Iterator<ModuleRoot> roots,
		final @NotNull AnActionEvent event)
	{
		if (!roots.hasNext())
		{
			return;
		}
		final ModuleRoot root = roots.next();
		BuildModule.buildModules(
			true,
			topLevelResolvedNames(root).iterator(),
			ProgressManager.getInstance(),
			event,
			() -> buildAllRoots(roots, event));
	}

	/**
	 * Clear the {@link Iterator#next() next} {@link ModuleRoot} in the provided
	 * {@link Iterator}. If the {@linkplain Iterator#hasNext() is empty}, run
	 * the provided {@link Continuation0}.
	 *
	 * @param roots
	 *        An {@link Iterator} containing the roots to clear.
	 * @param event
	 *        The {@link AnActionEvent} that prompted this clearing.
	 * @param onSuccess
	 *        The {@code Continuation0} to run after clearing the repository.
	 */
	private void clearNextRepository (
		final @NotNull Iterator<ModuleRoot> roots,
		final @NotNull AnActionEvent event,
		final @NotNull Continuation0 onSuccess)
	{
		ClearRepo.clearRepository(
			roots.next(),
			event,
			() ->
			{
				if (roots.hasNext())
				{
					clearNextRepository(roots, event, onSuccess);
				}
				else
				{
					onSuccess.value();
				}
			});
	}

	/**
	 * The active {@link ModuleNameResolver}.
	 */
	private @Nullable ModuleNameResolver resolver;

	/**
	 * The active {@link AvailRuntime}.
	 */
	private @Nullable AvailRuntime runtime;

	/**
	 * The active {@link AvailBuilder}.
	 */
	private @Nullable AvailBuilder builder;

	/**
	 * The {@link Map} from the {@link ModuleRoot#name()} to all of its {@link
	 * ModuleEntryPoints}.
	 */
	private @NotNull Map<String, List<ModuleEntryPoints>> rootEntryPointMap =
		new HashMap<>();

	/**
	 * The {@linkplain ModuleDescriptor module} {@linkplain JTree tree}.
	 *
	 * // TODO [RAA] may use this in another view
	 */
	public final Tree moduleTree = new Tree(
		new DefaultMutableTreeNode("(packages hidden root)"));

	/**
	 * Answer the {@link ModuleNameResolver}.
	 *
	 * @return The {@code ModuleNameResolver}.
	 */
	public @NotNull ModuleNameResolver resolver ()
	{
		return stripNull(resolver);
	}

	/**
	 * Answer the {@link AvailBuilder}.
	 *
	 * @return An {@code AvailBuilder}.
	 */
	public @NotNull AvailBuilder builder ()
	{
		return  stripNull(builder);
	}

	/**
	 * Answer the {@link AvailRuntime}.
	 *
	 * @return The {@code AvailRuntime}.
	 */
	public @NotNull AvailRuntime runtime ()
	{
		return  stripNull(runtime);
	}

	/**
	 * Answer the {@link List} of {@link ModuleEntryPoints} for the provided
	 * {@link ModuleRoot#name()}.
	 *
	 * @param rootName
	 *        The name of the root to retrieve the list for.
	 * @return A list.
	 */
	public @NotNull List<ModuleEntryPoints> moduleEntryPoints (
		final @NotNull String rootName)
	{
		return rootEntryPointMap.getOrDefault(
			rootName, Collections.emptyList());
	}

	/**
	 * Answer the {@link List} of {@code String} entry points for the given
	 * {@link ResolvedModuleName}.
	 *
	 * @param resolvedModuleName
	 *        The {@code ResolvedModuleName}.
	 * @return A list.
	 */
	public @NotNull List<String> entryPoints (
		final @NotNull ResolvedModuleName resolvedModuleName)
	{
		final List<ModuleEntryPoints> mepList =
			moduleEntryPoints(resolvedModuleName.rootName());
		for (final ModuleEntryPoints mep : mepList)
		{
			if (mep.resolvedModuleName().equals(resolvedModuleName))
			{
				return mep.entryPoints();
			}
		}
		return Collections.emptyList();
	}

	/**
	 * Answer the available entry points.
	 *
	 * @return A {@code String} {@link List} of available entry points.
	 */
	public @NotNull List<String> availableEntryPoints ()
	{
		final List<String> entryPoints = new ArrayList<>();
		final AvailBuilder builder = AvailComponent.getInstance().builder();
		for (final LoadedModule loadedModule : builder.loadedModulesCopy())
		{
			if (!loadedModule.entryPoints().isEmpty())
			{
				entryPoints.addAll(loadedModule.entryPoints());
			}
		}
		return entryPoints;
	}

	/**
	 * Calculate available entry points.
	 *
	 * @return A {@code String} {@link List} of available entry points.
	 */
	public @NotNull List<LoadedModule> loadedModules ()
	{
		return AvailComponent.getInstance().builder().loadedModulesCopy();
	}

	/**
	 * Determine if hte {@link AvailPsiFile} has been loaeded.
	 *
	 * @return {@code true} if it has; {@code false} otherwise.
	 */
	public boolean isLoaded (final @NotNull AvailPsiFile psiFile)
	{
		for (final LoadedModule loadedModule : loadedModules())
		{
			if (loadedModule.name.equals(psiFile.resolvedModuleName()))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Calculate available entry points.
	 *
	 * @return A {@code String} {@link List} of available entry points.
	 */
	public @Nullable LoadedModule loadedModule (
		final @NotNull AvailPsiFile psiFile)
	{
		for (final LoadedModule loadedModule : loadedModules())
		{
			if (loadedModule.name.equals(psiFile.resolvedModuleName()))
			{
				return loadedModule;
			}
		}
		return null;
	}

	/**
	 * Answer the {@link ModuleRoots}.
	 *
	 * @return A {@code ModuleRoots}.
	 */
	public @NotNull ModuleRoots moduleRoots ()
	{
		return resolver().moduleRoots();
	}

	/**
	 * Answer the {@link List} of {@link ResolvedModuleName}s for the top level
	 * Avail modules for the given {@link ModuleRoot}.
	 *
	 * @param moduleRoot
	 *        The {@link ModuleRoot}.
	 * @return A {@code List}.
	 */
	public @NotNull List<ResolvedModuleName> topLevelResolvedNames (
		final @NotNull ModuleRoot moduleRoot)
	{
		final List<ResolvedModuleName> names = new ArrayList<>();
		final File sourceDirectory = stripNull(moduleRoot.sourceDirectory());
		for (final File fileEntry : stripNull(sourceDirectory.listFiles())) {
			final String fullyQualifiedName = String.format(
				"/%s/%s",
				moduleRoot.name(),
				fileEntry.getAbsolutePath()
					.substring(sourceDirectory.getAbsolutePath().length() + 1)
					.replace(".avail", ""));
			final ModuleName moduleName =
				new ModuleName(fullyQualifiedName);
			try
			{
				names.add(AvailComponent.getInstance().resolver()
					.resolve(moduleName, null));
			}
			catch (final UnresolvedDependencyException e)
			{
				// Nothing.
			}
		}
		return names;
	}

	/**
	 * Re-parse the package structure from scratch.
	 */
	public void refresh ()
	{
		resolver().clearCache();
		final TreeNode modules = newModuleTree();
		moduleTree.setModel(new DefaultTreeModel(modules));
//		for (int i = moduleTree.getRowCount() - 1; i >= 0; i--)
//		{
//			moduleTree.expandRow(i);
//		}

		refreshModuleEntryPoints();
	}

	/**
	 * Answer a {@linkplain TreeNode tree node} that represents the (invisible)
	 * root of the Avail module tree.
	 *
	 * @return The (invisible) root of the module tree.
	 */
	private @NotNull TreeNode newModuleTree ()
	{
		final ModuleRoots roots = resolver().moduleRoots();
		final DefaultMutableTreeNode treeRoot = new DefaultMutableTreeNode(
			"(packages hidden root)");
		// Put the invisible root onto the work stack.
		final Deque<DefaultMutableTreeNode> stack = new ArrayDeque<>();
		stack.add(treeRoot);
		for (final ModuleRoot root : roots.roots())
		{
			// Obtain the path associated with the module root.
			root.repository().reopenIfNecessary();
			final File rootDirectory = stripNull(root.sourceDirectory());
			try
			{
				Files.walkFileTree(
					Paths.get(rootDirectory.getAbsolutePath()),
					EnumSet.of(FileVisitOption.FOLLOW_LINKS),
					Integer.MAX_VALUE,
					moduleTreeVisitor(stack, root));
			}
			catch (final IOException e)
			{
				e.printStackTrace();
				stack.clear();
				stack.add(treeRoot);
			}
		}
		@SuppressWarnings("unchecked")
		final Enumeration<AbstractBuilderFrameTreeNode> enumeration =
			treeRoot.preorderEnumeration();
		// Skip the invisible root.
		enumeration.nextElement();
		while (enumeration.hasMoreElements())
		{
			enumeration.nextElement().sortChildren();
		}
		return treeRoot;
	}

	/**
	 * Refresh the {@link #rootEntryPointMap}.
	 */
	private void refreshModuleEntryPoints ()
	{
		rootEntryPointMap.clear();
		builder().traceDirectories(
			(resolvedName, moduleVersion) ->
			{
				assert resolvedName != null;
				assert moduleVersion != null;

				final List<String> entryPoints =
					moduleVersion.getEntryPoints();
				if (!entryPoints.isEmpty())
				{
					rootEntryPointMap.computeIfAbsent(
							resolvedName.rootName(),
							k -> new ArrayList<>())
						.add(new ModuleEntryPoints(entryPoints, resolvedName));
				}
			});
	}

	/**
	 * Answer a {@link FileVisitor} suitable for recursively exploring an
	 * Avail root. A new {@code FileVisitor} should be obtained for each Avail
	 * root.
	 *
	 * @param stack
	 *        The stack on which to place Avail rootDirs and packages.
	 * @param moduleRoot
	 *        The {@link ModuleRoot} within which to scan recursively.
	 * @return A {@code FileVisitor}.
	 */
	private FileVisitor<Path> moduleTreeVisitor (
		final Deque<DefaultMutableTreeNode> stack,
		final ModuleRoot moduleRoot)
	{
		final String extension = ModuleNameResolver.availExtension;
		final Mutable<Boolean> isRoot = new Mutable<>(true);
		return new FileVisitor<Path>()
		{
			@Override
			public FileVisitResult preVisitDirectory (
				final @Nullable Path dir,
				final @Nullable BasicFileAttributes unused)
			{
				assert dir != null;
				final DefaultMutableTreeNode parentNode = stack.peekFirst();
				if (isRoot.value)
				{
					// Add a ModuleRoot.
					isRoot.value = false;
					assert stack.size() == 1;
					final ModuleRootNode node =
						new ModuleRootNode(builder, moduleRoot);
					parentNode.add(node);
					stack.addFirst(node);
					return FileVisitResult.CONTINUE;
				}
				final String fileName = dir.getFileName().toString();
				if (fileName.endsWith(extension))
				{
					final String localName = fileName.substring(
						0, fileName.length() - extension.length());
					final ModuleName moduleName;
					if (parentNode instanceof ModuleRootNode)
					{
						// Add a top-level package.
						final ModuleRootNode strongParentNode =
							(ModuleRootNode) parentNode;
						final ModuleRoot thisRoot =
							strongParentNode.moduleRoot();
						assert thisRoot == moduleRoot;
						moduleName = new ModuleName(
							"/" + moduleRoot.name() + "/" + localName);
					}
					else
					{
						// Add a non-top-level package.
						assert parentNode instanceof ModuleOrPackageNode;
						final ModuleOrPackageNode strongParentNode =
							(ModuleOrPackageNode) parentNode;
						assert strongParentNode.isPackage();
						final ResolvedModuleName parentModuleName =
							strongParentNode.resolvedModuleName();
						// The (resolved) parent is a package representative
						// module, so use its parent, the package itself.
						moduleName = new ModuleName(
							parentModuleName.packageName(), localName);
					}
					final ResolvedModuleName resolved;
					try
					{
						resolved = resolver().resolve(moduleName, null);
					}
					catch (final UnresolvedDependencyException e)
					{
						// The directory didn't contain the necessary package
						// representative, so simply skip the whole directory.
						return FileVisitResult.SKIP_SUBTREE;
					}
					final ModuleOrPackageNode node = new ModuleOrPackageNode(
						builder(), moduleName, resolved, true);
					parentNode.add(node);
					if (resolved.isRename())
					{
						// Don't examine modules inside a package which is the
						// source of a rename.  They wouldn't have resolvable
						// dependencies anyhow.
						return FileVisitResult.SKIP_SUBTREE;
					}
					stack.addFirst(node);
					return FileVisitResult.CONTINUE;
				}
				return FileVisitResult.SKIP_SUBTREE;
			}

			@Override
			public FileVisitResult postVisitDirectory (
				final @Nullable Path dir,
				final @Nullable IOException ex)
			{
				assert dir != null;
				// Pop the node from the stack.
				stack.removeFirst();
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile (
				final @Nullable Path file,
				final @Nullable BasicFileAttributes attrs)
			throws IOException
			{
				assert file != null;
				final DefaultMutableTreeNode parentNode = stack.peekFirst();
				if (isRoot.value)
				{
					throw new IOException("Avail root should be a directory");
				}
				final String fileName = file.getFileName().toString();
				if (fileName.endsWith(extension))
				{
					final String localName = fileName.substring(
						0, fileName.length() - extension.length());
					final ModuleName moduleName;
					if (parentNode instanceof ModuleRootNode)
					{
						// Add a top-level module (directly in a root).
						final ModuleRootNode strongParentNode =
							(ModuleRootNode) parentNode;
						final ModuleRoot thisRoot =
							strongParentNode.moduleRoot();
						assert thisRoot == moduleRoot;
						moduleName = new ModuleName(
							"/" + moduleRoot.name() + "/" + localName);
					}
					else
					{
						// Add a non-top-level module.
						assert parentNode instanceof ModuleOrPackageNode;
						final ModuleOrPackageNode strongParentNode =
							(ModuleOrPackageNode) parentNode;
						assert strongParentNode.isPackage();
						final ResolvedModuleName parentModuleName =
							strongParentNode.resolvedModuleName();
						moduleName = new ModuleName(
							parentModuleName.packageName(), localName);
					}
					try
					{
						final ResolvedModuleName resolved =
							resolver().resolve(moduleName, null);
						final ModuleOrPackageNode node =
							new ModuleOrPackageNode(
								builder(), moduleName, resolved, false);
						if (resolved.isRename() || !resolved.isPackage())
						{
							parentNode.add(node);
						}
					}
					catch (final UnresolvedDependencyException e)
					{
						// TODO MvG - Find a better way of reporting broken
						// dependencies. Ignore for now (during directory scan).
						throw new RuntimeException(e);
					}
				}
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFileFailed (
				final @Nullable Path file,
				final @Nullable IOException ex)
			{
				return FileVisitResult.CONTINUE;
			}
		};
	}

	/**
	 * Get the {@linkplain ModuleVersion module version} for the {@linkplain
	 * ResolvedModuleName named} {@linkplain ModuleDescriptor module}.
	 *
	 * @param moduleName
	 *        A resolved module name.
	 * @return A module version, or {@code null} if no version was
	 *         available.
	 */
	private @Nullable ModuleVersion getModuleVersion (
		final ResolvedModuleName moduleName)
	{
		final IndexedRepositoryManager repository =
			moduleName.repository();
		final ModuleArchive archive = repository.getArchive(
			moduleName.rootRelativeName());
		final byte [] digest = archive.digestForFile(moduleName);
		final ModuleVersionKey versionKey =
			new ModuleVersionKey(moduleName, digest);
		return archive.getVersion(versionKey);
	}

	@Override
	public void initComponent ()
	{
		try
		{
			final String rootsString = System.getenv("AVAIL_ROOTS");
			final ModuleRoots roots =
				new ModuleRoots(rootsString == null ? "" : rootsString);

			final String renames = System.getenv("AVAIL_RENAMES");
			final Reader reader;
			if (renames == null)
			{
				// Load the renames from preferences further down.
				reader = new StringReader("");
			}
			else
			{
				// Load the renames from the file specified on the command line...
				final File renamesFile = new File(renames);
				reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(renamesFile), StandardCharsets.UTF_8));
			}
			final RenamesFileParser renameParser = new RenamesFileParser(
				reader, roots);
			final ModuleNameResolver resolver = renameParser.parse();
			this.resolver = resolver;
			this.runtime = new AvailRuntime(resolver);
			this.builder = new AvailBuilder(runtime());
		}
		catch (final Exception e)
		{
			throw new RuntimeException(
				"AvailComponent failed to initialize", e);
		}
	}

	@Override
	public @NotNull String getComponentName ()
	{
		return "Avail";
	}
}
