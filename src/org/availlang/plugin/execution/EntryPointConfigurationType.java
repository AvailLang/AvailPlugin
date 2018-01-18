/*
 * EntryPointConfigurationType.java
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

package org.availlang.plugin.execution;
import com.avail.linking.EntryPoint;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import org.availlang.plugin.icons.AvailIcon;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * An {@code EntryPointConfigurationType} is a {@link ConfigurationType} for
 * configuring Avail {@link EntryPoint}s.
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class EntryPointConfigurationType
implements ConfigurationType
{
	/**
	 * A single-element array containing one {@link
	 * EntryPointConfigurationFactory}.
	 */
	private final @NotNull ConfigurationFactory[] factories =
		new ConfigurationFactory[1];

	@Nls
	@Override
	public String getDisplayName ()
	{
		return "Avail Entry Point";
	}

	@Nls
	@Override
	public String getConfigurationTypeDescription ()
	{
		return "Run an Avail Entry Point";
	}

	@Override
	public Icon getIcon ()
	{
		return AvailIcon.availEntryPointIcon;
	}

	@NotNull
	@Override
	public String getId ()
	{
		return "Avail Entry Point";
	}

	@Override
	public ConfigurationFactory[] getConfigurationFactories ()
	{
		return factories;
	}

	/**
	 * Construct an {@link EntryPointConfigurationFactory}.
	 */
	public EntryPointConfigurationType ()
	{
		this.factories[0] = new EntryPointConfigurationFactory(this);
	}
}
