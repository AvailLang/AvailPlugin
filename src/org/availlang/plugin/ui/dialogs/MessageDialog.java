package org.availlang.plugin.ui.dialogs;
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * A {@code MessageDialog} is a simple dialog popup that provides a message and
 * an ok button.
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class MessageDialog
{
	/**
	 * The {@link DialogBuilder} that is to be shown.
	 */
	private final @NotNull DialogBuilder dialogBuilder;

	/**
	 * Show the {@link MessageDialog}.
	 */
	public void show ()
	{
		dialogBuilder.show();
	}

	/**
	 * Construct a {@link MessageDialog}.
	 *
	 * @param title
	 *        The dialog title.
	 * @param message
	 *        The {@code String} message to display.
	 */
	public MessageDialog (
		final @NotNull String title,
		final @NotNull String message)
	{
		final JTextPane component = new JTextPane();
		Messages.configureMessagePaneUi(component, message);
		component.setText(message);
		component.setEditable(false);
		this.dialogBuilder = new DialogBuilder();
		this.dialogBuilder.setTitle(title);
		this.dialogBuilder.setCenterPanel(component);
		this.dialogBuilder.removeAllActions();
		this.dialogBuilder.addOkAction();
		this.dialogBuilder.setButtonsAlignment(0);
	}
}
