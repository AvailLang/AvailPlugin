package org.availlang.plugin.language;
import com.intellij.lang.Language;
import org.jetbrains.annotations.NotNull;

/**
 * An {@code AvailLanguage} is a {@link Language} that represents Avail for an
 * IntelliJ plugin..
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class AvailRepoLanguage
extends Language
{
	/**
	 * The sole {@link AvailRepoLanguage}.
	 */
	public static final @NotNull AvailRepoLanguage soleInstance =
		new AvailRepoLanguage();

	/**
	 * Construct an {@link AvailRepoLanguage}.
	 */
	private AvailRepoLanguage ()
	{
		super("Avail Repository");
	}
}
