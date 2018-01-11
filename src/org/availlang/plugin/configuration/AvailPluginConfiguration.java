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
import com.avail.builder.ModuleRoot;
import com.avail.utility.configuration.Configuration;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A {@code Configuration} represents the settings of the Avail plugin.
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
@State(
	name = "AvailPluginConfiguration",
	storages = @Storage("availPlugin.xml"))
public class AvailPluginConfiguration
implements Configuration, PersistentStateComponent<AvailPluginConfiguration>
{
	/**
	 * The {@link List} of {@link ModuleRoot} locations. These correspond
	 * positionally with the {@link #repos}.
	 */
	public final @NotNull List<String> rootDirs = new ArrayList<>();

	/**
	 * The {@link List} of Avail repository locations. These correspond
	 * positionally with the {@link #rootDirs}.
	 */
	public final @NotNull List<String> repos = new ArrayList<>();

	/**
	 * The {@link List} of {@link ModuleRoot} locations for the core Avail
	 * libraries used. These correspond positionally with the {@link #sdkRepos}.
	 */
	public final @NotNull List<String> sdkRootDirs = new ArrayList<>();

	/**
	 * The {@link List} of Avail repository locations for the core Avail
	 * libraries used. These correspond positionally with the {@link #sdkRootDirs}.
	 */
	public final @NotNull List<String> sdkRepos = new ArrayList<>();

	/**
	 * The default location for the XML file representing this {@link
	 * AvailPluginConfiguration}.
	 */
	public static final @NotNull String defaultPathLocationString =
		".idea/avail.xml";

	@Override
	public boolean isValid ()
	{
		return rootDirs.size() > 0 && rootDirs.size() == repos.size();
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

	@Override
	public @Nullable AvailPluginConfiguration getState ()
	{
		return this;
	}

	@Override
	public void loadState (final AvailPluginConfiguration state)
	{
		XmlSerializerUtil.copyBean(state, this);
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

	/**
	 * A {@code ConfigurationXMLSource} is class that encapsulates the parent
	 * {@link AvailPluginConfiguration}'s data for writing to an XML file.
	 */
	class ConfigurationXMLSource
	{
		/**
		 * The {@link Iterator} of {@link ModuleRoot} locations. These
		 * correspond positionally with the {@link #repos}.
		 */
		final @NotNull Iterator<String> rootDirs =
			AvailPluginConfiguration.this.rootDirs.iterator();

		/**
		 * The {@link Iterator} of Avail repository locations. These correspond
		 * positionally with the {@link #rootDirs}.
		 */
		final @NotNull Iterator<String> repos =
			AvailPluginConfiguration.this.repos.iterator();

		/**
		 * The {@link Iterator} of {@link ModuleRoot} locations for the core
		 * Avail libraries used. These correspond positionally with the {@link
		 * #sdkRepos}.
		 */
		final @NotNull Iterator<String> sdkRootDirs =
			AvailPluginConfiguration.this.sdkRootDirs.iterator();

		/**
		 * The {@link Iterator} of Avail repository locations for the core Avail
		 * libraries used. These correspond positionally with the {@link
		 * #sdkRootDirs}.
		 */
		final @NotNull Iterator<String> sdkRepos =
			AvailPluginConfiguration.this.sdkRepos.iterator();
	}
}
