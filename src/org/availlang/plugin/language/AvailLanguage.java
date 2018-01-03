package org.availlang.plugin.language;
import com.intellij.lang.Language;
import org.jetbrains.annotations.NotNull;

/**
 * An {@code AvailLanguage} is a {@link Language} that represents Avail for an
 * IntelliJ plugin..
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class AvailLanguage
extends Language
{
	/**
	 * The sole {@link AvailLanguage}.
	 */
	public static final @NotNull AvailLanguage soleInstance =
		new AvailLanguage();

	/**
	 * Construct an {@link AvailLanguage}.
	 */
	private AvailLanguage ()
	{
		super("Avail");
	}
}
