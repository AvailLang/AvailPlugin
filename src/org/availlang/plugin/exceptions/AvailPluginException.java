/*
 * PluginException.java
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

package org.availlang.plugin.exceptions;
import org.availlang.plugin.ui.dialogs.MessageDialog;
import org.jetbrains.annotations.NotNull;

/**
 * A {@code PluginException} is the root {@link RuntimeException} that will be
 * thrown by any plugin operation that should be able to be handled.
 *
 * @author Richard A Arriaga &lt;rarriaga@safetyweb.org&gt;
 */
public class AvailPluginException
extends RuntimeException
{
	/**
	 * Construct a {@link AvailPluginException}.
	 *
	 * @param message
	 *        The {@code String} message that can be added to a log and placed
	 *        in a popup {@link MessageDialog}.
	 */
	public AvailPluginException (final @NotNull String message)
	{
		super(message);
	}

	/**
	 * Construct a {@link AvailPluginException}.
	 *
	 * @param message
	 *        The {@code String} message that can be added to a log and placed
	 *        in a popup {@link MessageDialog}.
	 * @param cause
	 *        The {@link Throwable} that caused this {@code
	 *        ConfigurationException}.
	 */
	public AvailPluginException (
		final @NotNull String message,
		final @NotNull Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * Answer a {@link MessageDialog} that explains this {@link
	 * AvailPluginException} to the user.
	 *
	 * @return A {@code MessageDialog}.
	 */
	public @NotNull MessageDialog errorDialog ()
	{
		return new MessageDialog("Avail Plugin Error", getMessage());
	}

	/**
	 * Answer a {@link MessageDialog} that explains an {@link
	 * AvailPluginException} to the user.
	 *
	 * @return A {@code MessageDialog}.
	 */
	public static @NotNull MessageDialog dialog (final @NotNull String message)
	{
		return new AvailPluginException(message).errorDialog();
	}
}
