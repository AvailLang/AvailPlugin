/*
 * AvailPluginConfiguration.java
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
package org.availlang.plugin.configuration;
import com.avail.AvailRuntime;
import com.avail.builder.ModuleNameResolver;
import com.avail.builder.ModuleRoot;
import com.avail.persistence.IndexedRepositoryManager;
import com.avail.utility.Nulls;
import com.avail.utility.configuration.Configuration;
import com.avail.utility.configuration.XMLConfigurator;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import org.availlang.plugin.exceptions.ConfigurationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * An {@code AvailPluginConfiguration} is a {@link Configuration} that
 * represents the settings of the Avail plugin for a given {@link Project}.
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class AvailPluginConfiguration
implements Configuration, ProjectComponent
{
	/**
	 * The default configuration location.
	 */
	private static final @NotNull String relativeConfigFileLocation =
		".idea/availPlugin.xml";

	/**
	 * The location where the Avail root {@linkplain IndexedRepositoryManager
	 * repositories} are written.
	 */
	private static final @NotNull String relativeRootRepositoryDirectory =
		"repositories";

	/**
	 * The location where the Avail root source files are written.
	 */
	private static final @NotNull String relativeRootsDirectory =
		"roots";

	/**
	 * The version of all newly constructed {@link AvailPluginConfiguration}s
	 * (the Avail plugin version).
	 */
	public static @NotNull String version = "1.0";

	/**
	 * Answer the String version of the platform specific path for the provided
	 * String path.
	 *
	 * @param path
	 *        The path to make platform-specific.
	 * @return A platform-specific String representation of a path.
	 */
	static @NotNull String transformPlatformPath (
		final @NotNull String path)
	{
		final String[] directoryPath = path.contains("/")
			? path.split("/")
			: path.split("\\\\");
		final String platformSpecificPath =
			String.join(File.separator, directoryPath);
		return platformSpecificPath;
	}

	/**
	 * Write the provided {@code byte} {@code array} to a file.
	 *
	 * @param outputLocation
	 *        The file location for the output of data.
	 * @param data
	 *        The {@code byte} {@code array} to write.
	 * @return A String representing the full write location of the file.
	 */
	public static @NotNull String writeByteArrayToFile (
		final @NotNull String outputLocation,
		final byte[] data) throws IOException
	{
		final FileOutputStream fileOutputStream =
			new FileOutputStream(outputLocation);
		fileOutputStream.write(data);
		return outputLocation;
	}

	/**
	 * Have the current contents of this {@link AvailPluginConfiguration} been
	 * change since it was last written to the {@linkplain #configFileLocation
	 * configuration file}? {@code true} indicates yes; {@code false} otherwise.
	 */
	private boolean isDirty = true;

	/**
	 * Indicate that this {@link AvailPluginConfiguration} is now {@linkplain
	 * #isDirty dirty} and needs to be written to the {@linkplain
	 * #configFileLocation configuration file}.
	 */
	public void markDirty ()
	{
		isDirty = true;
	}

	/**
	 * The {@link #version} of this {@link AvailPluginConfiguration}.
	 */
	public @NotNull String configurationVersion = version;

	/**
	 * The absolute location of the project.
	 */
	public @NotNull String basePath = "";

	private Project project;

	/**
	 * The String absolute path of the root repository directory.
	 */
	public @NotNull String repositoryDirectory = "";

	/**
	 * The String absolute path of the roots directory.
	 */
	public @NotNull String rootsDirectory = "";

	/**
	 * The String absolute path of the configuration file.
	 */
	public @NotNull String configFileLocation = "";

	/**
	 * Answer the String path relative to the current {@link Project}'s
	 * location for the provided relative path.
	 *
	 * @param relativePath
	 *        The String relative path in the project directory.
	 * @return A String.
	 */
	private @NotNull String projectBasedPath (
		final @NotNull String relativePath)
	{
		return
			String.format("%s%s%s",
				transformPlatformPath(basePath),
				File.separator,
				transformPlatformPath(relativePath));
	}

	/**
	 * The {@link Map} of {@link AvailRoot#name}s to {@link AvailRoot}s that
	 * comprise all the Avail SDKs being used in this {@link Project}.
	 */
	public final @NotNull Map<String, AvailRoot> sdkMap = new HashMap<>();

	/**
	 * The {@link Map} of {@link AvailRoot#name}s to {@link AvailRoot}s that
	 * comprise all the Avail roots being developed in this {@link Project}.
	 */
	public final @NotNull Map<String, AvailRoot> rootMap = new HashMap<>();

	/**
	 * The {@link Map} of {@link AvailRename#source}s to {@link AvailRename}s
	 * that should be present in the {@link ModuleNameResolver}.
	 */
	public final @NotNull Map<String, AvailRename> renameMap = new HashMap<>();

	/**
	 * Is this a valid {@link AvailPluginConfiguration}? {@code true} indicates
	 * it is; {@code false}.
	 */
	private boolean isValid = false;

	/**
	 * Check to ensure the configuration isn't malformed or contains any
	 * duplicates.
	 *
	 * @return A String explanation of the validation result. If not problems,
	 *         answers an empty String.
	 */
	private @NotNull String validate ()
	{
		// TODO is there any validation required?
		isValid = true;
		return "";
	}

	@Override
	public boolean isValid ()
	{
		return isValid;
	}

	/**
	 * Answer a new {@link ConfigurationXMLSource}.
	 *
	 * @return A {@code ConfigurationXMLSource}.
	 */
	public @NotNull ConfigurationXMLSource source ()
	{
		return new ConfigurationXMLSource();
	}

	/**
	 * Answer the {@link AvailPluginConfiguration} held by the {@link
	 * ServiceManager}.
	 *
	 * @return An {@code AvailPluginConfiguration}.
	 */
	public static @NotNull AvailPluginConfiguration getInstance ()
	{
		return ServiceManager.getService(AvailPluginConfiguration.class);
	}

	@Override
	public void projectOpened ()
	{
		initializeEnvironmentStructure();
		final Path path = Paths.get(configFileLocation);
		try (
			final InputStream in = Files.newInputStream(
				path, StandardOpenOption.READ))
		{
			new XMLConfigurator<>(
				this,
				new AvailPluginState(this),
				AvailPluginElement.class,
				in).updateConfiguration();
			final String validationMessage = validate();
			if (!isValid)
			{
				throw new ConfigurationException(
					String.format(
						"%s failed validation\n%s",
						configFileLocation,
						validationMessage));
			}
			isDirty = false;
		}
		catch (final @NotNull NoSuchFileException ex)
		{
			// This is a pre-existing project newly using the plugin or the file
			// was deleted, either way, the project needs this file.
			writeConfigFile();
		}
		catch (final @NotNull IOException ex)
		{
			throw new ConfigurationException(
				String.format("Failed to load %s", configFileLocation), ex);
		}
		catch (final @NotNull
			com.avail.utility.configuration.ConfigurationException ex)
		{
			throw new ConfigurationException(
				String.format("Problem with %s", configFileLocation), ex);
		}
	}

	/**
	 * Initialize the {@link Project}'s Avail plugin-specific file structure.
	 */
	public void initializeEnvironmentStructure ()
	{
		final File repoDir = new File(repositoryDirectory);
		if (!repoDir.exists())
		{
			repoDir.mkdirs();
		}
		final File rootDir = new File(rootsDirectory);
		if (!rootDir.exists())
		{
			rootDir.mkdirs();
		}
		rootMap.forEach((name, root) ->
		{
			root.initializeLocations();
			final File srcDir = new File(root.sourceDirectory);
			if (!srcDir.exists())
			{
				srcDir.mkdirs();
			}
		});
		final File configDir = new File(String.format(
			"%s%s.idea", basePath, File.separator));
		if (!configDir.exists())
		{
			configDir.mkdir();
		}
	}

	/**
	 * Write this {@link AvailPluginConfiguration} as an XML file to {@link
	 * #configFileLocation}.
	 */
	public void writeConfigFile ()
	throws ConfigurationException
	{
		if (isDirty)
		{
			final String xmlContent;
			try
			{
				xmlContent = AvailPluginElement.xmlFileContent(this);
			}
			catch (final @NotNull
				com.avail.utility.configuration.ConfigurationException e)
			{
				throw new ConfigurationException(e.getMessage(), e);
			}
			try
			{
				writeByteArrayToFile(
					configFileLocation, xmlContent.getBytes("UTF-8"));
				isDirty = false;
			}
			catch (final UnsupportedEncodingException e)
			{
				// Really, this shouldn't happen
				throw new ConfigurationException(
					String.format(
						"Could not write %s as a UTF-8 file",
						configFileLocation));
			}
			catch (final IOException e)
			{
				throw new ConfigurationException(
					String.format(
						"Could not write file, %s",
						configFileLocation));
			}
		}
	}

	/**
	 * Initialize all the important paths for the Avail project.
	 *
	 * @param basePath
	 *        The location of the Avail module.
	 */
	public void initializePaths (
		final @NotNull String basePath)
	{
		this.basePath = basePath;
		this.configFileLocation = projectBasedPath(relativeConfigFileLocation);
		this.repositoryDirectory = projectBasedPath(
			relativeRootRepositoryDirectory);
		this.rootsDirectory = projectBasedPath(relativeRootsDirectory);
	}

	/**
	 * Construct a {@link AvailRoot}.
	 *
	 * @param configuration
	 *        The owning {@link AvailPluginConfiguration}.
	 * @param isSdk
	 *        Does this {@link AvailRoot} represent an Avail SDK? {@code
	 *        true} indicates it does; {@code false} otherwise.
	 * @param name
	 *        The {@link ModuleRoot#name()}.
	 * @param repository
	 *        The {@link ModuleRoot}'s {@link IndexedRepositoryManager}
	 *        location.
	 * @param sourceDirectory
	 *        The {@link ModuleRoot#sourceDirectory}'s location or {@code
	 *        null}.
	 */
	public @NotNull AvailRoot availRoot (
		final @NotNull AvailPluginConfiguration configuration,
		final boolean isSdk,
		final @NotNull String name,
		final @NotNull String repository,
		final @Nullable String sourceDirectory)
	{
		return new AvailRoot(
			configuration, isSdk, name, repository, sourceDirectory);
	}

	/**
	 * Answer an {@link AvailRoot} from a {@link ModuleRoot}.
	 *
	 * @param isSdk
	 *        Does this {@link AvailRoot} represent an Avail SDK? {@code
	 *        true} indicates it does; {@code false} otherwise.
	 * @param root
	 *        The {@code ModuleRoot}.
	 */
	public @NotNull AvailRoot availRoot (
		final boolean isSdk,
		final @NotNull ModuleRoot root)
	{
		return new AvailRoot(isSdk, root);
	}

	/**
	 * Construct an {@link AvailPluginConfiguration}.
	 */
	public AvailPluginConfiguration () { }

	/**
	 * Construct an {@link AvailPluginConfiguration}.
	 *
	 * @param project
	 *        The {@link Project} holding onto this {@code
	 *        AvailPluginConfiguration}.
	 */
	public AvailPluginConfiguration (
		final @NotNull Project project)
	{
		this.project = project;
		this.basePath = project.getBasePath();
		this.configFileLocation = projectBasedPath(relativeConfigFileLocation);
		this.repositoryDirectory = projectBasedPath(
			relativeRootRepositoryDirectory);
		this.rootsDirectory = projectBasedPath(relativeRootsDirectory);
	}

	/**
	 * An {@code AvailRoot} contains the components to create a {@link
	 * ModuleRoot}.
	 */
	public class AvailRoot
	{
		/**
		 * Does this {@link AvailRoot} represent an Avail SDK? {@code true}
		 * indicates it does; {@code false} otherwise.
		 */
		private final boolean isSdk;

		/**
		 * Have the {@link AvailRoot} locations been initialized? {@code true}
		 * indicates yes; {@code false} otherwise.
		 */
		private boolean rootInitialized = false;

		/**
		 * The corresponding {@link ModuleRoot#name()}.
		 */
		public final @NotNull String name;

		/**
		 * The {@link ModuleRoot}'s {@link IndexedRepositoryManager} location.
		 */
		public @NotNull String repository = "";

		/**
		 * The {@link ModuleRoot#sourceDirectory}'s location or {@code null}.
		 */
		private @Nullable String sourceDirectory;

		/**
		 * Answer the String {@link #sourceDirectory}. If {@link
		 * #sourceDirectory} is {@code null}, the answered String will be empty.
		 *
		 * @return A {@code String}.
		 */
		public @NotNull String getSourceDirectory ()
		{
			return sourceDirectory == null ? "" : sourceDirectory;
		}

		/**
		 * Answer the {@link File} for the corresponding {@link
		 * #sourceDirectory}. If {@link #sourceDirectory} is {@code null}, the
		 * {@code null} will be answered.
		 *
		 * @return A {@code File} or {@code null}.
		 */
		public @Nullable File getSourceDirectoryFile ()
		{
			return sourceDirectory == null ? null : new File(sourceDirectory);
		}

		/**
		 * Answer a {@link ModuleRoot} that corresponds with the components in
		 * this {@link AvailRoot}.
		 *
		 * @return A {@code ModuleRoot}.
		 */
		public @NotNull ModuleRoot moduleRoot ()
		{
			return new ModuleRoot(
				name,
				new File(repository),
				getSourceDirectoryFile());
		}

		void initializeLocations ()
		{
			if (!rootInitialized)
			{
				this.sourceDirectory =
					String.format(
						"%s%s%s",
						AvailPluginConfiguration.this.rootsDirectory,
						File.separator,
						name);
				this.repository = String.format(
					"%s%s%s.repo",
					AvailPluginConfiguration.this.repositoryDirectory,
					File.separator,
					name);
				rootInitialized = true;
			}
		}

		/**
		 * Construct a {@link AvailRoot}.
		 *
		 * @param configuration
		 *        The owning {@link AvailPluginConfiguration}.
		 * @param isSdk
		 *        Does this {@link AvailRoot} represent an Avail SDK? {@code
		 *        true} indicates it does; {@code false} otherwise.
		 * @param name
		 *        The {@link ModuleRoot#name()}.
		 * @param repository
		 *        The {@link ModuleRoot}'s {@link IndexedRepositoryManager}
		 *        location.
		 * @param sourceDirectory
		 *        The {@link ModuleRoot#sourceDirectory}'s location or {@code
		 *        null}.
		 */
		public AvailRoot (
			final @NotNull AvailPluginConfiguration configuration,
			final boolean isSdk,
			final @NotNull String name,
			final @NotNull String repository,
			final @Nullable String sourceDirectory)
		{
			this.name = name;
			this.isSdk = isSdk;
			if (isSdk)
			{
				this.repository = repository;
				this.sourceDirectory = sourceDirectory;
				rootInitialized = true;
			}
			else
			{
				this.sourceDirectory =
					String.format(
						"%s%s%s",
						configuration.rootsDirectory,
						File.separator,
						name);
				this.repository = String.format(
					"%s%s%s.repo",
					configuration.repositoryDirectory,
					File.separator,
					name);
			}
		}

		/**
		 * Construct an {@link AvailRoot} from a {@link ModuleRoot}.
		 *
		 * @param isSdk
		 *        Does this {@link AvailRoot} represent an Avail SDK? {@code
		 *        true} indicates it does; {@code false} otherwise.
		 * @param root
		 *        The {@code ModuleRoot}.
		 */
		public AvailRoot (
			final boolean isSdk,
			final @NotNull ModuleRoot root)
		{
			this.isSdk = isSdk;
			this.name = root.name();
			this.repository = root.repository().fileName().getAbsolutePath();
			this.sourceDirectory =
				root.sourceDirectory() == null
					? null
					: root.sourceDirectory().getAbsolutePath();
			rootInitialized = true;
		}

		@Override
		public String toString ()
		{
			return "{" + name + ", " + repository + ","
				+ sourceDirectory + '}';
		}
	}

	/**
	 * An {@code AvailRename} is a holder for an instance of a {@link
	 * ModuleNameResolver}'s {@linkplain
	 * ModuleNameResolver#addRenameRule(String, String) module rename rule}.
	 */
	public static class AvailRename
	{
		/**
		 * The module name to be replaced (the key).
		 */
		public final @NotNull String source;

		/**
		 * The module name replacement.
		 */
		public final @NotNull String target;

		/**
		 * Construct the {@link AvailRename}.
		 *
		 * @param source
		 *        The module name to be replaced (the key).
		 * @param target
		 *        The module name replacement.
		 */
		public AvailRename (
			final @NotNull String source,
			final @NotNull String target)
		{
			this.source = source;
			this.target = target;
		}

		@Override
		public String toString ()
		{
			return "\"" + source + "\" → \"" + target + "\"";
		}
	}

	/**
	 * A {@code ConfigurationXMLSource} is class that encapsulates the parent
	 * {@link AvailPluginConfiguration}'s data for writing to an XML file.
	 */
	class ConfigurationXMLSource
	{
		/**
		 * The {@link Iterator} of {@link AvailRoot}s for the Avail roots being
		 * developed in this project.
		 */
		final @NotNull Iterator<AvailRoot> roots =
			AvailPluginConfiguration.this.rootMap.values().iterator();

		/**
		 * The {@link Iterator} of {@link AvailRoot}s for the Avail SDKs used
		 * in this project.
		 */
		final @NotNull Iterator<AvailRoot> sdks =
			AvailPluginConfiguration.this.sdkMap.values().iterator();

		/**
		 * The {@link Iterator} of {@link AvailRename}s for the {@link
		 * AvailRuntime}s {@link ModuleNameResolver}.
		 */
		final @NotNull Iterator<AvailRename> renames =
			AvailPluginConfiguration.this.renameMap.values().iterator();

		/**
		 * The current {@link AvailRoot} from {@link #rootMap} being written to
		 * an XML source.
		 */
		private @Nullable AvailRoot currentRoot;

		/**
		 * Answer the next {@link AvailRoot} in {@link #rootMap} or {@link
		 * #sdkMap}.
		 *
		 * @return An {@code AvailRoot}.
		 */
		@NotNull AvailRoot getCurrentRoot (final boolean isSdk)
		throws ConfigurationException
		{
			final String rootType = isSdk ? "SDK" : "root";
			return Nulls.stripNull(
				currentRoot,
				() -> new ConfigurationException(
					String.format(
						"Avail configuration corrupted; Avail %s info cannot "
							+ "be written to %s",
						rootType,
						configFileLocation)));
		}

		/**
		 * Answer whether or not there is another {@link AvailRoot} in {@link
		 * #roots}. If there is, populate {@link #currentRoot}.
		 *
		 * @return {@code true} if there is another {@link AvailRoot}; {@code
		 *         false} otherwise;
		 */
		boolean hasRoot ()
		{
			if (!roots.hasNext())
			{
				return false;
			}
			currentRoot = roots.next();
			return true;
		}

		/**
		 * Answer whether or not there is another {@link AvailRoot} in {@link
		 * #sdks}. If there is, populate {@link #currentRoot}.
		 *
		 * @return {@code true} if there is another {@link AvailRoot}; {@code
		 *         false} otherwise;
		 */
		boolean hasSdk ()
		{
			if (!sdks.hasNext())
			{
				return false;
			}
			currentRoot = sdks.next();
			return true;
		}

		/**
		 * The current {@link AvailRoot} from {@link #rootMap} being written to
		 * an XML source.
		 */
		private @Nullable AvailRename currentRename;

		/**
		 * Answer the next {@link AvailRename} in {@link #renameMap}
		 *
		 * @return An {@code AvailRoot}.
		 */
		@NotNull AvailRename getCurrentRename ()
		throws ConfigurationException
		{
			return Nulls.stripNull(
				currentRename,
				() -> new ConfigurationException(
					String.format(
						"Avail configuration corrupted; Avail rename info "
							+ "cannot be written to %s",
						configFileLocation)));
		}

		/**
		 * Answer whether or not there is another {@link AvailRename} in {@link
		 * #renames}. If there is, populate {@link #currentRename}.
		 *
		 * @return {@code true} if there is another {@link AvailRoot}; {@code
		 *         false} otherwise;
		 */
		boolean hasRename ()
		{
			if (!renames.hasNext())
			{
				return false;
			}
			currentRename = renames.next();
			return true;
		}
	}
}
