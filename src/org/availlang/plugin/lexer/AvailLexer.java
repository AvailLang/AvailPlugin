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
