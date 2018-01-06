package org.availlang.plugin.psi;
import com.intellij.psi.tree.IElementType;
import org.availlang.plugin.language.AvailLanguage;
import org.jetbrains.annotations.NotNull;

/**
 * An {@code AvailElementType} is an {@link IElementType}.
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class AvailElementType extends IElementType
{
	/**
	 * Construct a {@link AvailElementType}.
	 *
	 * @param debugName
	 *        The name of the element type, used for debugging purposes.
	 */
	public AvailElementType (
		final @NotNull String debugName)
	{
		super(debugName, AvailLanguage.soleInstance);
	}
}
