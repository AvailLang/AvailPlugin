/*
 * PluginConsoleInputChannel.java
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
import com.avail.environment.AvailWorkbench;
import com.avail.environment.AvailWorkbench.BuildInputStream;
import com.avail.io.TextInputChannel;
import com.intellij.execution.impl.ConsoleViewImpl;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.channels.CompletionHandler;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.System.arraycopy;

/**
 * A {@code PluginConsoleInputChannel} is TODO: Document this!
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class PluginConsoleInputChannel
extends ByteArrayInputStream
implements TextInputChannel
{
	/**
	 * The {@link ConsoleView} to {@linkplain ConsoleView#print(String,
	 * ConsoleViewContentType) output} the text to.
	 */
	private final @NotNull ConsoleViewImpl consoleView;

	/**
	 * The {@link StreamStyle} for the output.
	 */
	private final @NotNull StreamStyle streamStyle;

	/**
	 * Clear the {@linkplain BuildInputStream input stream}. All pending
	 * data is discarded and the stream position is reset to zero
	 * ({@code 0}).
	 */
	public synchronized void clear ()
	{
		count = 0;
		pos = 0;
	}

	/**
	 * Update the content of the {@link PluginConsoleInputChannel}.
	 */
	public synchronized void update ()
	{
//		final String text = inputField.getText() + "\n";
		final String text = "" + "\n"; // TODO get a hold of the text somehow!
		final byte[] bytes = text.getBytes();
		if (pos + bytes.length >= buf.length)
		{
			final int newSize = max(
				buf.length << 1, bytes.length + buf.length);
			final byte[] newBuf = new byte[newSize];
			arraycopy(buf, 0, newBuf, 0, buf.length);
			buf = newBuf;
		}
		arraycopy(bytes, 0, buf, count, bytes.length);
		count += bytes.length;
//		writeText(text, IN_ECHO);
		notifyAll();
	}

	/**
	 * The specified command string was just entered.  Present it in the
	 * {@link AvailWorkbench.StreamStyle#COMMAND} style.  Force an extra leading new line
	 * to keep the text area from looking stupid.  Also end with a new line.
	 * The passed command should not itself have a new line included.
	 *
	 * @param commandText
	 *        The command that was entered, with no leading or trailing line
	 *        breaks.
	 */
	public synchronized void feedbackForCommand (
		final String commandText)
	{
		final String textToInsert = "\n" + commandText + "\n";
//		writeText(textToInsert, COMMAND);
	}

	@Override
	public boolean markSupported ()
	{
		return false;
	}

	@Override
	public void mark (final int readAheadLimit)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public synchronized void reset ()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public synchronized int read ()
	{
		// Block until data is available.
		try
		{
			while (pos == count)
			{
				wait();
			}
		}
		catch (final InterruptedException e)
		{
			return -1;
		}
		return buf[pos++] & 0xFF;
	}

	@Override
	public synchronized int read (
		final @Nullable byte[] readBuffer,
		final int start,
		final int requestSize)
	{
		assert readBuffer != null;
		if (requestSize <= 0)
		{
			return 0;
		}
		// Block until data is available.
		try
		{
			while (pos == count)
			{
				wait();
			}
		}
		catch (final InterruptedException e)
		{
			return -1;
		}
		final int bytesToTransfer = min(requestSize, count - pos);
		arraycopy(buf, pos, readBuffer, start, bytesToTransfer);
		pos += bytesToTransfer;
		return bytesToTransfer;
	}

	@Override
	public <A> void read (
		final CharBuffer buffer,
		final @Nullable A attachment,
		final CompletionHandler<Integer, A> handler)
	{
		final int charsRead;
		try
		{
			buffer.length();
			charsRead = consoleView.getText().length();
//			charsRead = in.read(buffer);
			if (charsRead == 0)
			{
				throw new IOException("end of stream");
			}
		}
		catch (final IOException e)
		{
			handler.failed(e, attachment);
			return;
		}
		handler.completed(charsRead, attachment);
	}

	@Override
	public boolean isOpen ()
	{
		return true;
	}

	public PluginConsoleInputChannel (
		final @NotNull ConsoleViewImpl consoleView,
		final @NotNull StreamStyle streamStyle)
	{
		super(new byte[1024], 0, 0);
		this.consoleView = consoleView;
		this.streamStyle = streamStyle;
	}
}
