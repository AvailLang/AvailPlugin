/*
 * AvailLexer.java
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
package org.availlang.plugin.lexer;
import com.intellij.lexer.LexerBase;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An {@link AvailLexer} is a {@link LexerBase}.
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class AvailLexer extends LexerBase
{
	private CharSequence buffer;
	private int startOffset = 0;
	private int endOffset = 0;
	private int initialState = 0;

	@Override
	public void start (
		final @NotNull CharSequence buffer,
		final int startOffset,
		final int endOffset,
		final int initialState)
	{
		this.buffer = buffer;
		this.startOffset = startOffset;
		this.endOffset = endOffset;
		this.initialState = initialState;
	}

	@Override
	public int getState ()
	{
		return 0;
	}

	@Override
	public @Nullable
	IElementType getTokenType ()
	{
		return null;
	}

	@Override
	public int getTokenStart ()
	{
		return 0;
	}

	@Override
	public int getTokenEnd ()
	{
		return 0;
	}

	@Override
	public void advance ()
	{

	}

	@Override
	public @NotNull CharSequence getBufferSequence ()
	{
		return buffer;
	}

	@Override
	public int getBufferEnd ()
	{
		return 0;
	}
}
