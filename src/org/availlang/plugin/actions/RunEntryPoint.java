/*
 * RunEntryPoint.java
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
package org.availlang.plugin.actions;

import com.avail.builder.AvailBuilder;
import com.avail.builder.AvailBuilder.LoadedModule;
import com.avail.builder.ResolvedModuleName;
import com.avail.linking.EntryPoint;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task.Backgroundable;
import com.intellij.openapi.progress.util.ProgressWindow;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.availlang.plugin.core.AvailComponent;
import org.availlang.plugin.psi.AvailPsiFile;
import org.availlang.plugin.ui.dialogs.EntryPointOptionDialog;
import org.availlang.plugin.ui.dialogs.MessageDialog;
import org.availlang.plugin.ui.dialogs.TextInputDialog;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * A {@code RunEntryPoint} is a {@link AnAction} that allows you to run an
 * entry
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class RunEntryPoint
extends AvailAction
{
	/**
	 * The {@link List} of {@code String} {@link EntryPoint} that can be run.
	 */
	private @NotNull List<String> entryPoints = Collections.emptyList();

	/**
	 * The {@link ResolvedModuleName} of the module that contains the {@link
	 * EntryPoint}s.
	 */
	private ResolvedModuleName name;

	@Override
	public void actionPerformed (final AnActionEvent event)
	{
		final Project project = getProject(event);
		final ProgressManager manager = ProgressManager.getInstance();
		final AvailBuilder builder = AvailComponent.getInstance().builder();
		final ResolvedModuleName resolvedModuleName = this.name;
		assert resolvedModuleName != null;
		final EntryPointOptionDialog optionDialog = entryPointOptionDialog(
			project,
			resolvedModuleName,
			entryPoints);
		optionDialog.show();

		if (optionDialog.getExitCode() == DialogWrapper.OK_EXIT_CODE)
		{
			final String entryPoint = optionDialog.selectedEntryPoint();
			final TextInputDialog dialog =
				new TextInputDialog(
					project,
					"",
					"Run Entry Point",
					entryPoint);
			dialog.show();
			if (dialog.getExitCode() == DialogWrapper.OK_EXIT_CODE)
			{
				final String command = dialog.getInputString();
				if (command != null && !command.isEmpty())
				{
					runEntryPoint(event, manager, builder, command);
				}
				else
				{
					new MessageDialog(
						"ERROR",
						"No Entry Point Command Entered!").show();
				}
			}
		}
	}

	/**
	 * Answer an {@link EntryPointOptionDialog}.
	 *
	 * @param project
	 *       The {@link Project}.
	 * @param resolvedModuleName
	 *        The {@link ResolvedModuleName} to show entry points for.
	 * @param entryPoints
	 *        The {@link List} of entry points to display.
	 * @return An {@code EntryPointOptionDialog}.
	 */
	public static @NotNull EntryPointOptionDialog entryPointOptionDialog (
		final @NotNull Project project,
		final @NotNull ResolvedModuleName resolvedModuleName,
		final @NotNull List<String> entryPoints)
	{
		final EntryPointOptionDialog optionDialog =
			new EntryPointOptionDialog(project, entryPoints);
		optionDialog.setTitle(String.format(
			"Choose %s Entry Point to Run",
			resolvedModuleName.localName()));
		return optionDialog;
	}

	@SuppressWarnings("WeakerAccess")
	public static void runEntryPoint (
		final @NotNull AnActionEvent event,
		final @NotNull ProgressManager manager,
		final @NotNull AvailBuilder builder,
		final @NotNull String command)
	{
		manager.runProcessWithProgressAsynchronously(
			new Backgroundable(
				event.getProject(),
				"Running: " + command,
				false)
			{
				@Override
				public void run (
					final @NotNull ProgressIndicator progress)
				{
					final ProgressWindow window =
						(ProgressWindow) progress;
					window.setTitle("Running: " + command);
					window.setIndeterminate(true);
					final Semaphore done = new Semaphore(0);
					builder.attemptCommand(
						command,
						(commands, proceed) ->
						{
							assert proceed != null;
							assert commands != null;
							proceed.value(commands.get(0));
						},
						(result, cleanup) ->
						{
							assert cleanup != null;
							cleanup.value(done::release);
						},
						() -> System.err.println("[Failed] Run: "
							+ command));
					done.acquireUninterruptibly();
				}
			},
			new ProgressWindow(true, event.getProject()));
	}

	@Override
	protected boolean customVisibilityCheck (
		final @Nullable AvailPsiFile psiFile)
	{
		if (psiFile == null)
		{
			return false;
		}
		final LoadedModule loadedModule =
			AvailComponent.getInstance().loadedModule(psiFile);
		if (loadedModule == null)
		{
			return false;
		}
		this.name = psiFile.resolvedModuleName();
		this.entryPoints = loadedModule.entryPoints();
		return !entryPoints.isEmpty();
	}

	@NotNull
	@Override
	protected String customMenuItem (final @Nullable AvailPsiFile psiFile)
	{
		assert psiFile != null;
		return  "Run Entry Point for '"
			+ psiFile.getVirtualFile().getNameWithoutExtension() + "'";
	}
}
