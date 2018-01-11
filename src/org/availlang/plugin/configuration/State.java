package org.availlang.plugin.configuration;
import com.avail.utility.configuration.XMLConfiguratorState;
import org.jetbrains.annotations.NotNull;

/**
 * A {@code State} encapsulates the state of a {@link Configuration} for the
 * Avail plugin.
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class State
extends XMLConfiguratorState<Configuration, Element, State>
{
	/**
	 * Construct a {@link State}.
	 *
	 * @param configuration
	 *        The {@link Configuration}.
	 */
	public State (final @NotNull Configuration configuration)
	{
		super(configuration);
	}
}
