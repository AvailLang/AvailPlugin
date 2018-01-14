/*
 * AvailPluginState.java
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
import com.avail.builder.ModuleRoot;
import com.avail.persistence.IndexedRepositoryManager;
import com.avail.utility.configuration.XMLConfiguratorState;
import org.availlang.plugin.configuration.AvailPluginConfiguration.AvailRename;
import org.availlang.plugin.configuration.AvailPluginConfiguration.AvailRoot;
import org.availlang.plugin.exceptions.ConfigurationException;
import org.jetbrains.annotations.NotNull;

/**
 * A {@code State} encapsulates the state of a {@link AvailPluginConfiguration} for the
 * Avail plugin.
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class AvailPluginState
extends XMLConfiguratorState<AvailPluginConfiguration, AvailPluginElement, AvailPluginState>
{
	/**
	 * The corresponding {@link ModuleRoot#name()}.
	 */
	public @NotNull String rootName = "";

	/**
	 * The {@link ModuleRoot}'s {@link IndexedRepositoryManager} location.
	 */
	public @NotNull String rootRepository = "";

	/**
	 * The {@link ModuleRoot#sourceDirectory}'s location or {@code null}.
	 */
	public @NotNull String sourceDirectory = "";

	/**
	 * The module name to be replaced (the key).
	 */
	public @NotNull String source = "";

	/**
	 * The module name replacement.
	 */
	public @NotNull String target = "";

	/**
	 * Answer an {@link AvailRoot} then reset {@link #rootName}, {@link
	 * #rootRepository}, and {@link #sourceDirectory}.
	 *
	 * @return An {@code AvailRoot}.
	 */
	public @NotNull AvailRoot availRoot(final boolean isSDK)
	{
		if (rootName.isEmpty())
		{
			throw new ConfigurationException(
				"Avail configuration file missing root name.");
		}
		if (isSDK && rootRepository.isEmpty())
		{
			throw new ConfigurationException(
				"Avail configuration file missing repository path.");
		}
		final AvailRoot root = configuration().availRoot(
			configuration(),
			isSDK,
			rootName,
			rootRepository,
			sourceDirectory.isEmpty() ? null : sourceDirectory);
		rootName = "";
		rootRepository = "";
		sourceDirectory = "";
		return root;
	}

	/**
	 * Answer an {@link AvailRename} then reset {@link #source} and {@link
	 * #target}.
	 *
	 * @return An {@code AvailRename}.
	 */
	public @NotNull AvailRename availRename()
	{
		if (source.isEmpty())
		{
			throw new ConfigurationException(
				"Avail configuration file missing rename source.");
		}
		if (target.isEmpty())
		{
			throw new ConfigurationException(
				"Avail configuration file missing rename target.");
		}
		final AvailRename rename = new AvailRename(source, target);
		source = "";
		target = "";
		return rename;
	}

	/**
	 * Construct a {@link AvailPluginState}.
	 *
	 * @param configuration
	 *        The {@link AvailPluginConfiguration}.
	 */
	AvailPluginState (final @NotNull AvailPluginConfiguration configuration)
	{
		super(configuration);
	}
}
