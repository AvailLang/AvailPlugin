package org.availlang.plugin.parser;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.availlang.plugin.language.AvailLanguage;
import org.availlang.plugin.lexer.AvailLexer;
import org.availlang.plugin.psi.AvailPsiFile;
import org.jetbrains.annotations.NotNull;

/**
 * An {@code AvailParserDefinition} is a {@link ParserDefinition} specific to
 * {@link AvailLanguage} files.
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class AvailParserDefinition
implements ParserDefinition
{
	public static final IFileElementType fileElementType =
		new IFileElementType(AvailLanguage.soleInstance);

	@Override
	public @NotNull Lexer createLexer (final Project project)
	{
		return new AvailLexer();
	}

	@Override
	public PsiParser createParser (final Project project)
	{
		return new AvailParser();
	}

	@Override
	public IFileElementType getFileNodeType ()
	{
		return fileElementType;
	}

	@NotNull
	@Override
	public TokenSet getCommentTokens ()
	{
		return TokenSet.EMPTY;
	}

	@NotNull
	@Override
	public TokenSet getStringLiteralElements ()
	{
		return TokenSet.EMPTY;
	}

	@NotNull
	@Override
	public PsiElement createElement (final ASTNode node)
	{
		return new ASTWrapperPsiElement(node);
	}

	@Override
	public PsiFile createFile (final FileViewProvider viewProvider)
	{
		return new AvailPsiFile(viewProvider);
	}

	@Override
	public SpaceRequirements spaceExistanceTypeBetweenTokens (
		final ASTNode left,
		final ASTNode right)
	{
		return SpaceRequirements.MAY;
	}
}
