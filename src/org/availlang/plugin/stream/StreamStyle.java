/*
 * StreamStyle.java
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

package org.availlang.plugin.stream;
import com.avail.environment.AvailWorkbench;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.editor.markup.EffectType;
import com.intellij.openapi.editor.markup.TextAttributes;
import org.availlang.plugin.ui.console.AvailToolWindowFactory;
import org.intellij.lang.annotations.JdkConstants;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * A {@code StreamStyle} is an abstraction of the styles of streams used by the
 * {@link AvailToolWindowFactory}.
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public enum StreamStyle
{
	/** The stream style used to echo user input. */
	IN_ECHO("input", new Color(32, 144, 32), 0),

	/** The stream style used to display normal output. */
	OUT("output", ConsoleViewContentType.NORMAL_OUTPUT),

	/** The stream style used to display error output. */
	ERR("error", ConsoleViewContentType.ERROR_OUTPUT),

	/** The stream style used to display informational text. */
	WARNING("warning", Color.YELLOW, 0),

	/** The stream style used to display informational text. */
	INFO("info", new Color(120, 170, 255), 2),

	/** The stream style used to echo commands. */
	COMMAND("command", Color.MAGENTA, 0),

	/** Progress updates produced by a build. */
	BUILD_PROGRESS("build", new Color(128, 96, 0), 0);

	/** The name of this style. */
	final String styleName;

	/** The foreground color for this style. */
	final Color foregroundColor;

	/**
	 * The associated {@link ConsoleViewContentType}.
	 */
	public final @NotNull ConsoleViewContentType consoleViewContentType;

	/**
	 * Construct a new {@link AvailWorkbench.StreamStyle}.
	 *
	 * @param styleName
	 *        The name of this style.
	 * @param foregroundColor
	 *        The color of foreground text in this style.
	 * @param fontStyle
	 *        The {@link JdkConstants} font style.
	 */
	StreamStyle (
		final @NotNull String styleName,
		final @NotNull Color foregroundColor,
		final @JdkConstants.FontStyle int fontStyle)
	{
		this.styleName = styleName;
		this.foregroundColor = foregroundColor;
		final TextAttributes textAttributes = new TextAttributes(
			foregroundColor,
			null,
			null,
			EffectType.BOXED,
			fontStyle);
		this.consoleViewContentType =
			new ConsoleViewContentType(styleName, textAttributes);
	}

	/**
	 * Construct a new {@link AvailWorkbench.StreamStyle}.
	 *
	 * @param styleName
	 *        The name of this style.
	 * @param consoleViewContentType
	 *        The {@link ConsoleViewContentType}.
	 */
	StreamStyle (
		final @NotNull String styleName,
		final @NotNull ConsoleViewContentType consoleViewContentType)
	{
		this.styleName = styleName;
		this.foregroundColor =
			consoleViewContentType.getAttributes().getForegroundColor();
		this.consoleViewContentType = consoleViewContentType;
	}
}
