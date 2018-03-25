/*
 * AvailOutputStream.java
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

package org.availlang.plugin.stream;
import com.avail.io.ConsoleInputChannel;
import com.avail.io.TextInterface;
import com.avail.utility.Nulls;
import com.intellij.execution.impl.ConsoleViewImpl;
import org.availlang.plugin.core.AvailComponent;
import org.availlang.plugin.ui.console.AvailConsole;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * An {@code AvailPluginTextStream} is the class that redirects text to the
 * appropriate input/output stream for the running Avail plugin.
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class AvailPluginTextStream
{
	/**
	 * The {@link AvailComponent} that owns this {@link AvailPluginTextStream}.
	 */
	private final @NotNull AvailComponent availComponent;

	/**
	 * The {@link ConsoleViewImpl} that is the {@link JPanel} of the console
	 * that this {@link AvailPluginTextStream} should write to.
	 */
	private ConsoleViewImpl consoleView;

	/**
	 * The {@link TextInterface}.
	 */
	private TextInterface textInterface;

	/**
	 * Answer the {@link TextInterface}.
	 *
	 * @return A {@code TextInterface}.
	 */
	public @NotNull TextInterface textInterface ()
	{
		return textInterface;
	}

	/**
	 * Set the {@link #consoleView}.
	 *
	 * @param consoleView
	 *        The {@link ConsoleViewImpl} to set.
	 */
	public void setConsoleView (final @NotNull AvailConsole consoleView)
	{
		this.consoleView = consoleView;
		this.textInterface =
			new TextInterface(
//				new ConsoleInputChannel(System.in),
				new PluginConsoleInputChannel(consoleView, StreamStyle.IN_ECHO),
				new PluginConsoleOutputChannel(consoleView, StreamStyle.OUT),
				new PluginConsoleOutputChannel(consoleView, StreamStyle.ERR));
		availComponent.runtime.setTextInterface(textInterface);
		availComponent.builder.setTextInterface(textInterface);
		try
		{
			System.setOut(new BuildPrintStream(
				(OutputStream) textInterface.outputChannel()));
			System.setErr(new BuildPrintStream(
				(OutputStream) textInterface.errorChannel()));
			// TODO change input stream? System.setIn(inputStream);
		}
		catch (final UnsupportedEncodingException e)
		{
			// Java must support UTF_8.
			throw new RuntimeException(e);
		}
	}

	/**
	 * Answer the {@link ConsoleViewImpl} held by this {@link
	 * AvailPluginTextStream}.
	 *
	 * @return An {@code AvailPluginTextStream}.
	 */
	public @NotNull ConsoleViewImpl consoleView ()
	{
		return Nulls.stripNull(consoleView);
	}

	/**
	 * Write text to the transcript with the given {@link StreamStyle}.
	 *
	 * @param text The text to write.
	 * @param streamStyle The style to write it in.
	 */
	public synchronized void writeText (
		final String text,
		final StreamStyle streamStyle)
	{
		int size = text.length();
		assert size > 0;
		consoleView.print(text, streamStyle.consoleViewContentType);
	}

	/**
	 * Construct an {@link AvailPluginTextStream}.
	 *
	 * @param availComponent
	 *        The {@link AvailComponent} that owns this {@link
	 *        AvailPluginTextStream}.
	 */
	public AvailPluginTextStream (final @NotNull AvailComponent availComponent)
	{
		this.availComponent = availComponent;
	}

	/** A PrintStream specialization for better println handling. */
	public class BuildPrintStream extends PrintStream
	{
		/**
		 * Because you can't inherit constructors.
		 *
		 * @param out
		 *        The wrapped {@link OutputStream}.
		 * @throws UnsupportedEncodingException
		 *         Because Java won't let you catch the pointless exception
		 *         thrown by the super constructor.
		 */
		BuildPrintStream (final @NotNull OutputStream out)
		throws UnsupportedEncodingException
		{
			super(out, false, StandardCharsets.UTF_8.name());
		}

		@Override
		public void println (final String x)
		{
			printf("%s\n", x);
		}

		@Override
		public void println (final Object x)
		{
			printf("%s\n", x);
		}
	}
}