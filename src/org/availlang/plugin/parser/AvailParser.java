package org.availlang.plugin.parser;
import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

/**
 * An {@code AvailParser} is a {@link PsiParser}.
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class AvailParser implements PsiParser
{
	@Override
	public @NotNull ASTNode parse (
		@NotNull final IElementType root, @NotNull final PsiBuilder builder)
	{
		return builder.getTreeBuilt();
	}
}