package org.availlang.plugin.core;
import com.avail.AvailRuntime;
import com.avail.builder.AvailBuilder;
import com.avail.builder.ModuleNameResolver;
import com.avail.builder.ModuleRoots;
import com.avail.builder.RenamesFileParser;
import com.avail.utility.Nulls;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

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
	public static @NotNull AvailComponent getInstance ()
	{
		final Application application = ApplicationManager.getApplication();
		final AvailComponent component =
			application.getComponent(AvailComponent.class);
		assert component != null;
		return component;
	}

	private @Nullable ModuleNameResolver resolver;

	private @Nullable AvailRuntime runtime;

	private @Nullable AvailBuilder builder;

	public @NotNull ModuleNameResolver getResolver ()
	{
		return stripNull(resolver);
	}

	public @NotNull AvailBuilder getBuilder ()
	{
		return  stripNull(builder);
	}

	public @NotNull AvailRuntime getRuntime ()
	{
		return  stripNull(runtime);
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
			this.builder = new AvailBuilder(runtime);
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
