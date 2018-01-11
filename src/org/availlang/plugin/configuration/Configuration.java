package org.availlang.plugin.configuration;
import com.avail.builder.ModuleRoot;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@code Configuration} represents the settings of the Avail plugin.
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class Configuration
implements com.avail.utility.configuration.Configuration
{
	/**
	 * The {@link List} of {@link ModuleRoot} locations. These correspond
	 * positionally with the {@link #repos}.
	 */
	public final @NotNull List<String> roots = new ArrayList<>();

	/**
	 * The {@link List} of Avail repository locations. These correspond
	 * positionally with the {@link #roots}.
	 */
	public final @NotNull List<String> repos = new ArrayList<>();

	/**
	 * The default location for the XML file representing this {@link
	 * Configuration}.
	 */
	public static final @NotNull String defaultPathLocationString =
		".idea/avail.xml";

	@Override
	public boolean isValid ()
	{
		return roots.size() > 0 && roots.size() == repos.size();
	}
}
