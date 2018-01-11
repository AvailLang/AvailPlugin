/*
 * MessageDialog.java
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
