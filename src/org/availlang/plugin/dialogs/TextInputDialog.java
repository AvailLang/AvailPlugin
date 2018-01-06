package org.availlang.plugin.dialogs;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages.InputDialog;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A {@code TextInputDialog} is an {@link InputDialog}.
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class TextInputDialog
extends InputDialog
{
	/**
	 * Construct a {@link TextInputDialog}.
	 *
	 * @param project
	 *        The {@link Project}.
	 * @param prompt
	 *        The input prompt.
	 * @param title
	 *        The title of the dialog.
	 * @param initialValue
	 *        The initial value in the dialog.
	 */
	public TextInputDialog (
		final @Nullable Project project,
		final @NotNull String prompt,
		final @Nls String title,
		final @Nullable String initialValue)
	{
		super(project, prompt, title, null, initialValue, null);
	}
}
