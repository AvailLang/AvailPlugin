package org.availlang.plugin.configuration;
import com.avail.builder.ModuleRoot;
import com.avail.utility.configuration.XMLElement;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.Collections;
import java.util.Set;

/**
 * An {@code Element} represents a legal element of the {@link Configuration}
 * schema for the Avail plugin.
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public enum Element
implements XMLElement<Configuration, Element, State>
{
	/**
	 * The top level element.
	 */
	AVAIL
	{
		@Override
		public Set<Element> allowedParents ()
		{
			return Collections.emptySet();
		}

		@Override
		public void endElement (final State state) throws SAXException
		{
			super.endElement(state);
		}
	},

	/**
	 * A collection of {@link ModuleRoot}s.
	 */
	ROOTS
	{
		@Override
		public Set<Element> allowedParents ()
		{
			return Collections.singleton(AVAIL);
		}

		@Override
		public void endElement (final State state) throws SAXException
		{
			assert state.configuration().roots.size() > 0;
			assert state.configuration().repos.size()
				== state.configuration().roots.size();
		}
	},

	/**
	 * The {@link ModuleRoot} directory location.
	 */
	ROOT
	{
		@Override
		public Set<Element> allowedParents ()
		{
			return Collections.singleton(ROOTS);
		}

		@Override
		public void startElement (
			final State state,
			final Attributes attributes) throws SAXException
		{
			state.startAccumulator();
		}

		@Override
		public void endElement (final State state) throws SAXException
		{
			state.stopAccumulator();
			state.configuration().roots.add(state.accumulatorContents());
		}
	},

	/**
	 * The {@link ModuleRoot} directory location.
	 */
	REPO
	{
		@Override
		public Set<Element> allowedParents ()
		{
			return Collections.singleton(ROOTS);
		}

		@Override
		public void startElement (
			final State state,
			final Attributes attributes) throws SAXException
		{
			state.startAccumulator();
		}

		@Override
		public void endElement (final State state) throws SAXException
		{
			state.stopAccumulator();
			state.configuration().repos.add(state.accumulatorContents());
		}
	};


	@Override
	public String qName ()
	{
		return name().toLowerCase();
	}

	@Override
	public void startElement (
		final State state,
		final Attributes attributes) throws SAXException
	{
		// no implementation
	}

	@Override
	public void endElement (final State state) throws SAXException
	{
		// no implementation
	}
}
