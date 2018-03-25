/*
 * AvailConsole.java
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

package org.availlang.plugin.ui.console;
import com.intellij.execution.impl.ConsoleViewImpl;
import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;

/**
 * A {@code AvailConsole} is a {@link ConsoleViewImpl} that is used to
 * specifically interact with the running Avail VM.
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class AvailConsole
extends ConsoleViewImpl
{
	/**
	 * The {@link AvailConsoleState} that dictates the current state of this
	 * {@link AvailConsole}.
	 */
	public final AvailConsoleState availConsoleState;

	/**
	 * Construct a new {@link AvailConsole}.
	 *
	 * @param project
	 *        The active Avail {@link Project}.
	 * @param viewer
	 * @param initialState
	 *        The {@link AvailConsoleState} that dictates the current state of
	 *        this {@link AvailConsole}.
	 * @param usePredefinedMessageFilter
	 *
	 */
	public AvailConsole (
		final @NotNull Project project,
		final boolean viewer,
		final @NotNull AvailConsoleState initialState,
		final boolean usePredefinedMessageFilter)
	{
		super(
			project,
			GlobalSearchScope.allScope(project),
			viewer,
			initialState,
			usePredefinedMessageFilter);
		this.availConsoleState = initialState;
	}
}
