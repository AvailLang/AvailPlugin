/*
 * AvailConsoleState.java
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
import com.avail.AvailRuntime;
import com.avail.builder.AvailBuilder;
import com.avail.builder.ModuleNameResolver;
import com.avail.linking.EntryPoint;
import com.intellij.execution.impl.ConsoleState;
import com.intellij.execution.impl.ConsoleViewImpl;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.openapi.project.Project;
import org.availlang.plugin.core.AvailComponent;
import org.availlang.plugin.stream.StreamStyle;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A {@code AvailConsoleState} is TODO: Document this!
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class AvailConsoleState
extends ConsoleState
{
	/**
	 * The core {@link AvailComponent} that drives this plugin for the current
	 * {@link Project}.
	 */
	private final @NotNull AvailComponent component;

	/**
	 * The active {@link ModuleNameResolver}.
	 */
	public final @NotNull ModuleNameResolver resolver;

	/**
	 * The active {@link AvailRuntime}.
	 */
	public final @NotNull  AvailRuntime runtime;

	/**
	 * The active {@link AvailBuilder}.
	 */
	public final @NotNull  AvailBuilder builder;

	/**
	 * Is an {@link EntryPoint} presently being run? {@code true} indicates yes;
	 * {@code false} otherwise.
	 */
	private final @NotNull AtomicBoolean isRunningEntryPoint =
		new AtomicBoolean(false);

	@Override
	public boolean isRunning ()
	{
		return true;
	}

	@Override
	public @NotNull ConsoleState attachTo (
		final @NotNull ConsoleViewImpl console,
		final ProcessHandler processHandler)
	{
		return this;
	}

	@Override
	public @NotNull ConsoleState dispose ()
	{
		// not disposable
		return this;
	}

	@Override
	public void sendUserInput (final @NotNull String input) throws IOException
	{
		// TODO hand it to the builder if executing an entry point?
		if (isRunningEntryPoint.get())
		{

		}
		else
		{
			component.outputStream.writeText(input, StreamStyle.IN_ECHO);
		}
	}

	public AvailConsoleState (
		final @NotNull AvailComponent component)
	{
		this.component = component;
		this.resolver = component.resolver;
		this.runtime = component.runtime;
		this.builder = component.builder;
	}
}
