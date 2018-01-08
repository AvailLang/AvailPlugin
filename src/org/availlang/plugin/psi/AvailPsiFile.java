package org.availlang.plugin.psi;
import com.avail.builder.ModuleName;
import com.avail.builder.ModuleNameResolver;
import com.avail.builder.ModuleRoot;
import com.avail.builder.ModuleRoots;
import com.avail.builder.ResolvedModuleName;
import com.avail.builder.UnresolvedDependencyException;
import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.availlang.plugin.core.AvailComponent;
import org.availlang.plugin.file.AvailFileType;
import org.availlang.plugin.language.AvailLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.File;

/**
 * An {@code AvailPsiFile} is a {@link PsiFileBase}.
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class AvailPsiFile
extends PsiFileBase
{
	@Override
	public @NotNull FileType getFileType ()
	{
		return AvailFileType.soleInstance;
	}

	/**
	 * Construct a new {@link AvailPsiFile}.
	 *
	 * @param viewProvider
	 *        The {@link FileViewProvider}.
	 */
	public AvailPsiFile (final @NotNull FileViewProvider viewProvider)
	{
		super(viewProvider, AvailLanguage.soleInstance);
	}

	private @Nullable ResolvedModuleName resolvedModuleName;

	/**
	 * Answer the {@link ResolvedModuleName} that corresponds to this
	 * {@link AvailPsiFile}.
	 *
	 * @return The resolved module name, or {@code null} if module name
	 *         resolution failed.
	 */
	public @Nullable ResolvedModuleName resolvedModuleName ()
	{
		if (resolvedModuleName == null)
		{
			final String path = getVirtualFile().getPath();
			final AvailComponent component = AvailComponent.getInstance();
			final ModuleNameResolver resolver =
				component.resolver();
			final ModuleRoots roots = component.moduleRoots();
			for (final ModuleRoot root : roots.roots())
			{
				final File sourceDirectory = root.sourceDirectory();
				if (sourceDirectory != null)
				{
					final String sourceDirectoryPath =
						sourceDirectory.getPath();
					if (path.startsWith(sourceDirectoryPath))
					{
						final String fullyQualifiedName = String.format(
							"/%s/%s",
							root.name(),
							path
								.substring(sourceDirectoryPath.length() + 1)
								.replace(".avail", ""));
						final ModuleName moduleName =
							new ModuleName(fullyQualifiedName);
						try
						{
							resolvedModuleName =
								resolver.resolve(moduleName, null);
						}
						catch (final UnresolvedDependencyException e)
						{
							// Nothing.
						}
						break;
					}
				}
			}
		}
		return resolvedModuleName;
	}

	@Override
	public String toString ()
	{
		return "Avail File";
	}

	@Override
	public Icon getIcon (int flags)
	{
		return super.getIcon(flags);
	}
}
