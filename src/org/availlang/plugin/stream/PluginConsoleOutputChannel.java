/*
 * PluginConsoleOutputChannel.java
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

import com.avail.io.TextOutputChannel;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.CharBuffer;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;

/**
 * A {@code PluginConsoleOutputChannel} provides a faux {@linkplain
 * TextOutputChannel asynchronous interface} to a synchronous {@linkplain
 * PrintStream output stream} for the Avail plugin.
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public final class PluginConsoleOutputChannel
extends ByteArrayOutputStream
implements TextOutputChannel
{
	/**
	 * The {@link ConsoleView} to {@linkplain ConsoleView#print(String,
	 * ConsoleViewContentType) output} the text to.
	 */
	private final @NotNull ConsoleView consoleView;

	/**
	 * The {@link StreamStyle} for the output.
	 */
	private final @NotNull StreamStyle streamStyle;

	@Override
	public boolean isOpen ()
	{
		// The standard output stream is always open; we do not permit it to be
		// closed, at any rate.
		return false;
	}

	@Override
	public <A> void write (
		final CharBuffer buffer,
		final @Nullable A attachment,
		final CompletionHandler<Integer, A> handler)
	{
		consoleView.print(
			buffer.toString(),
			streamStyle.consoleViewContentType);
		handler.completed(buffer.limit(), attachment);
	}

	@Override
	public <A> void write (
		final String data,
		final @Nullable A attachment,
		final CompletionHandler<Integer, A> handler)
	{
		consoleView.print(
			data,
			streamStyle.consoleViewContentType);
		handler.completed(data.length(), attachment);
	}

	@Override
	public void close ()
	{
		// Do nothing.
	}

	/**
	 * Transfer any data in my buffer into the updateQueue, starting up a UI
	 * task to transfer them to the document as needed.
	 */
	private void queueForTranscript ()
	{
		final String text;
		try
		{
			text = toString(StandardCharsets.UTF_8.name());
		}
		catch (final UnsupportedEncodingException e)
		{
			assert false : "Somehow Java doesn't support characters";
			throw new RuntimeException(e);
		}
		if (text.isEmpty())
		{
			// Nothing new to display.
			return;
		}
		reset();
		consoleView.print(text, streamStyle.consoleViewContentType);
	}

	@Override
	public synchronized void write (final int b)
	{
		super.write(b);
		queueForTranscript();
	}

	@Override
	public synchronized void write (final @Nullable byte[] b)
	throws IOException
	{
		assert b != null;
		super.write(b);
		queueForTranscript();
	}

	@Override
	public synchronized void write (
		final @Nullable byte[] b,
		final int off,
		final int len)
	{
		assert b != null;
		super.write(b, off, len);
		queueForTranscript();
	}

	/**
	 * Construct a {@link PluginConsoleOutputChannel}.
	 *
	 * @param consoleView
	 *        The {@link ConsoleView} to {@linkplain ConsoleView#print(String,
	 *         ConsoleViewContentType) output} the text to.
	 * @param streamStyle
	 *        The {@link StreamStyle} for the output.
	 */
	PluginConsoleOutputChannel (
		final @NotNull ConsoleView consoleView,
		final @NotNull StreamStyle streamStyle)
	{
		this.consoleView = consoleView;
		this.streamStyle = streamStyle;
	}
}
