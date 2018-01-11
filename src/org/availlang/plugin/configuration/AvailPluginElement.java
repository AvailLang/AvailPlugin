/*
 * AvailPluginElement.java
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
import com.avail.utility.configuration.ConfigurationException;
import com.avail.utility.configuration.XMLElement;
import org.availlang.plugin.configuration.AvailPluginConfiguration.ConfigurationXMLSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * An {@code Element} represents a legal element of the {@link AvailPluginConfiguration}
 * schema for the Avail plugin.
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public enum AvailPluginElement
implements XMLElement<AvailPluginConfiguration, AvailPluginElement, AvailPluginState>
{
	/**
	 * The top level element.
	 */
	AVAIL
	{
		@Override
		public Set<AvailPluginElement> allowedParents ()
		{
			return Collections.emptySet();
		}

		@Override
		public void endElement (final AvailPluginState state) throws SAXException
		{
			super.endElement(state);
		}

		@Override
		public @NotNull List<AvailPluginElement> allowedChildren ()
		{
			return Arrays.asList(ROOTS, SDKS);
		}
	},

	/**
	 * A collection of {@link ModuleRoot}s.
	 */
	ROOTS
	{
		@Override
		public Set<AvailPluginElement> allowedParents ()
		{
			return Collections.singleton(AVAIL);
		}

		@Override
		public void endElement (final AvailPluginState state)
		{
			assert state.configuration().rootDirs.size() > 0;
			assert state.configuration().repos.size()
				== state.configuration().rootDirs.size();
		}

		@Override
		public @NotNull List<AvailPluginElement> allowedChildren ()
		{
			return Collections.singletonList(ROOT);
		}
	},

	/**
	 * The {@link ModuleRoot} directory location.
	 */
	ROOT
	{
		@Override
		public Set<AvailPluginElement> allowedParents ()
		{
			return Collections.singleton(ROOTS);
		}

		@Override
		public void startElement (
			final AvailPluginState state,
			final Attributes attributes)
		{
			state.startAccumulator();
		}

		@Override
		public void endElement (final AvailPluginState state)
		{
			state.stopAccumulator();
			state.configuration().rootDirs.add(state.accumulatorContents());
		}

		@Override
		public @NotNull List<AvailPluginElement> allowedChildren ()
		{
			return Arrays.asList(REPO, SRC_DIR);
		}
	},

	/**
	 * The collection of {@link #SDK}s.
	 */
	SDKS
	{
		@Override
		public Set<AvailPluginElement> allowedParents ()
		{
			return Collections.singleton(AVAIL);
		}

		@Override
		public void endElement (final AvailPluginState state)
		{
			assert state.configuration().sdkRootDirs.size() > 0;
			assert state.configuration().sdkRootDirs.size()
				== state.configuration().sdkRepos.size();
		}

		@Override
		public @NotNull List<AvailPluginElement> allowedChildren ()
		{
			return Collections.singletonList(SDK);
		}
	},

	/**
	 * The {@linkplain #SRC_DIR source directory} and corresponding {@linkplain
	 * #REPO repository} location for an Avail SDK that drives the Avail
	 * Intellij plugin.
	 */
	SDK
	{
		@Override
		public Set<AvailPluginElement> allowedParents ()
		{
			return Collections.singleton(SDKS);
		}

		@Override
		public void startElement (
			final AvailPluginState state,
			final Attributes attributes)
		{
			state.startAccumulator();
		}

		@Override
		public void endElement (final AvailPluginState state)
		{
			state.stopAccumulator();
			state.configuration().rootDirs.add(state.accumulatorContents());
		}

		@Override
		public @NotNull List<AvailPluginElement> allowedChildren ()
		{
			return Arrays.asList(REPO, SRC_DIR);
		}
	},

	/**
	 * The location of a {@link ModuleRoot} source file.
	 */
	SRC_DIR
	{
		@Override
		public String qName ()
		{
			return "src-dir";
		}

		@Override
		public Set<AvailPluginElement> allowedParents ()
		{
			return new HashSet<>(Arrays.asList(ROOT, SDK));
		}

		@Override
		public void startElement (
			final AvailPluginState state,
			final Attributes attributes)
		{
			state.startAccumulator();
		}

		@Override
		public void endElement (final AvailPluginState state)
		{
			state.stopAccumulator();
			final AvailPluginElement parent = state.parentElement();
			if (parent == ROOT)
			{
				state.configuration().rootDirs.add(state.accumulatorContents());
			}
			else if (parent == SDK)
			{
				state.configuration().sdkRootDirs.add(
					state.accumulatorContents());
			}
		}

		@Override
		@NotNull String xmlContent (
			final @Nullable AvailPluginElement parent,
			final @NotNull ConfigurationXMLSource source)
		throws ConfigurationException
		{
			assert parent != null;
			if (parent == ROOT)
			{
				final String value =
					source.rootDirs.hasNext()
						? source.rootDirs.next()
						: "";
				return encapsulateXMLContent(parent, value);
			}
			else if (parent == SDK)
			{
				final String value =
					source.sdkRootDirs.hasNext()
						? source.sdkRootDirs.next()
						: "";
				return encapsulateXMLContent(parent, value);
			}
			throw new ConfigurationException(
				String.format(
					"%s is not a valid parent for %s",
					parent.name(),
					name()));
		}
	},

	/**
	 * The {@link ModuleRoot} directory location.
	 */
	REPO
	{
		@Override
		public Set<AvailPluginElement> allowedParents ()
		{
			return new HashSet<>(Arrays.asList(ROOT, SDK));
		}

		@Override
		public void startElement (
			final AvailPluginState state,
			final Attributes attributes)
		{
			state.startAccumulator();
		}

		@Override
		public void endElement (final AvailPluginState state)
		{
			state.stopAccumulator();
			final AvailPluginElement parent = state.parentElement();
			if (parent == ROOT)
			{
				state.configuration().repos.add(state.accumulatorContents());
			}
			else if (parent == SDK)
			{
				state.configuration().sdkRepos.add(state.accumulatorContents());
			}
		}

		@Override
		@NotNull String xmlContent (
				final @Nullable AvailPluginElement parent,
				final @NotNull ConfigurationXMLSource source)
			throws ConfigurationException
		{
			assert parent != null;
			if (parent == ROOT)
			{
				final String value =
					source.repos.hasNext()
						? source.repos.next()
						: "";
				return encapsulateXMLContent(parent, value);
			}
			else if (parent == SDK)
			{
				final String value =
					source.sdkRepos.hasNext()
						? source.sdkRepos.next()
						: "";
				return encapsulateXMLContent(parent, value);
			}
			throw new ConfigurationException(
				String.format(
					"%s is not a valid parent for %s",
					parent.name(),
					name()));
		}
	};

	/**
	 * The root {@code Element}.
	 */
	private static final @NotNull AvailPluginElement root = AVAIL;

	/**
	 * The DTD ordered {@link List} of {@link AvailPluginElement}s that belong
	 * to this {@code AvailPluginElement}.
	 *
	 * @return A {@code List}.
	 */
	public @NotNull List<AvailPluginElement> allowedChildren ()
	{
		return Collections.emptyList();
	}

	@Override
	public String qName ()
	{
		return name().toLowerCase();
	}

	/**
	 * Answer the XML opening tag for this {@link AvailPluginElement}.
	 *
	 * @return A {@code String}.
	 */
	@NotNull String elementOpenTag ()
	{
		return String.format("<%s>", qName());
	}

	/**
	 * Answer the XML opening tag for this {@link AvailPluginElement}.
	 *
	 * @return A {@code String}.
	 */
	@NotNull String elementCloseTag ()
	{
		return String.format("</%s>", qName());
	}

	/**
	 * Answer the {@code String} XML enclosed content of this {@link
	 * AvailPluginElement} from the provided {@link ConfigurationXMLSource}.
	 *
	 * @param parent
	 *        The parent {@code AvailPluginElement} requesting this {@code
	 *        AvailPluginElement}'s content.
	 * @param source
	 *        The {@code ConfigurationXMLSource}.
	 * @return A {@code String}.
	 * @throws ConfigurationException Thrown when there is a complication with
	 *         the data or the parents.
	 */
	@NotNull String xmlContent (
			final @Nullable AvailPluginElement parent,
			final @NotNull ConfigurationXMLSource source)
		throws ConfigurationException
	{
		final StringBuilder sb = new StringBuilder(elementOpenTag());
		for (final AvailPluginElement child : allowedChildren())
		{
			sb.append(child.xmlContent(this, source));
		}
		return sb.append(elementCloseTag()).toString();
	}

	/**
	 * Answer the {@code String} content for an {@link AvailPluginConfiguration}
	 * XML file for the this {@link AvailPluginElement}.
	 *
	 * @param parent
	 *        The {@code AvailPluginElement} that holds this {@code
	 *        AvailPluginElement}.
	 * @param content
	 *        The String content.
	 * @return A String.
	 */
	@NotNull String encapsulateXMLContent (
		final @Nullable AvailPluginElement parent,
		final @NotNull String content)
	{
		return String.format(
			"%s%s%s", elementOpenTag(), content, elementCloseTag());
	}

	/**
	 * Construct the content of an XML file from the provided {@link
	 * AvailPluginConfiguration}.
	 *
	 * @param configuration
	 *        The {@code AvailPluginConfiguration}.
	 * @return A {@code String}.
	 */
	public static @NotNull String xmlFileContent (
			final @NotNull AvailPluginConfiguration configuration)
		throws ConfigurationException
	{
		final StringBuilder sb = new StringBuilder();
		sb.append(
			"<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>\n");
		sb.append("<!DOCTYPE application SYSTEM \"config/configuration.dtd\">");
		return sb.append(
			root.xmlContent(null, configuration.source())).toString();
	}

	@Override
	public void startElement (
		final AvailPluginState state,
		final Attributes attributes)
	{
		// no implementation
	}

	@Override
	public void endElement (final AvailPluginState state) throws SAXException
	{
		// no implementation
	}
}
