/*
 * DisplayAndBuildModules.java
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
import com.avail.builder.ResolvedModuleName;
import com.avail.utility.evaluation.Continuation0;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.availlang.plugin.build.BuildModule;
import org.availlang.plugin.file.psi.AvailPsiFile;
import org.availlang.plugin.stream.StreamStyle;
import org.availlang.plugin.ui.dialogs.ResolvedModuleNameOptionDialog;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;

/**
 * A {@code DisplayAndBuildModules} is an {@link AvailAction} that is menu
 * driven, displaying an option, when chosen, provides a list of modules that
 * can be built.
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class DisplayAndBuildModules
extends AvailAction
{
	/**
	 * The {@code String} to display in the initial menu.
	 */
	private final @NotNull String menuItemText;

	/**
	 * The {@link List} of top-level {@link ResolvedModuleName}s that can be
	 * built.
	 */
	private final @NotNull List<ResolvedModuleName> names;

	/**
	 * Is the module selection restricted to single select?
	 *
	 * @return  {@code true} indicates it is; {@code false} otherwise.
	 */
	protected boolean isSingleSelect ()
	{
		return false;
	}

	/**
	 * Answer the title for the {@linkplain ResolvedModuleNameOptionDialog}.
	 *
	 * @return A {@code String}.
	 */
	protected @NotNull String moduleSelectDialogTitle ()
	{
		return "Choose Modules to Build";
	}

	@Override
	public void actionPerformed (final AnActionEvent event)
	{
		final Project project = getProject(event);
		final ProgressManager manager = ProgressManager.getInstance();
		final ResolvedModuleNameOptionDialog dialog =
			resolvedModuleNameOptionDialog(project);
		dialog.show();

		if (dialog.getExitCode() == DialogWrapper.OK_EXIT_CODE)
		{
			final long start = System.currentTimeMillis();
			final ResolvedModuleName resolvedModuleName =
				dialog.getSelection();
			firstThen(
				project,
				dialog,
				() ->
				{
					final Iterator<ResolvedModuleName> toBuildList =
						dialog.getChosenElements().iterator();

					BuildModule.buildModules(
						false,
						false,
						toBuildList,
						manager,
						getAvailComponent(event),
						() ->
						{
							final long runTime =
								System.currentTimeMillis() - start;
							getAvailComponent(event).outputStream
								.writeText(
									String.format(
										"Build complete (%d milliseconds)\n",
										runTime),
									StreamStyle.INFO);
							done(project, manager, event, resolvedModuleName);
						});
				});
		}
	}

	/**
	 * Run the body of this method then run the {@link Continuation0}.
	 *
	 * @param project
	 *        The current {@link Project}.
	 * @param dialog
	 *        The ResolvedModuleNameOptionDialog with the selected modules
	 *        to build.
	 * @param next
	 *        The {@code Continuation0} to run at the conclusion of this
	 *        method.
	 */
	public void firstThen (
		final @NotNull Project project,
		final @NotNull ResolvedModuleNameOptionDialog dialog,
		final @NotNull Continuation0 next)
	{
		next.value();
	}

	private @NotNull
	ResolvedModuleNameOptionDialog resolvedModuleNameOptionDialog (
		final @NotNull Project project)
	{
		final ResolvedModuleNameOptionDialog dialog =
			new ResolvedModuleNameOptionDialog(project, names);
		dialog.setTitle(moduleSelectDialogTitle());
		if (isSingleSelect())
		{
			dialog.setSingleSelect();
		}
		return dialog;
	}

	/**
	 * Action to perform when all selected Avail modules loaded.
	 *
	 * @param project
	 *        The {@linkplain Project}.
	 * @param manager
	 *        The {@link ProgressManager}.
	 * @param event
	 *        The {@link AnActionEvent} that triggered the {@link
	 *        DisplayAndBuildModules}.
	 */
	protected void done (
		final @NotNull Project project,
		final @NotNull ProgressManager manager,
		final @NotNull AnActionEvent event,
		final @NotNull ResolvedModuleName resolvedModuleName)
	{
		// Do nothing
	}

	@Override
	protected @NotNull String customMenuItem (
		final @Nullable AvailPsiFile psiFile)
	{
		return menuItemText;
	}

	/**
	 * Construct a {@link DisplayAndBuildModules}.
	 *
	 * @param menuItemText
	 *        The {@code String} to display in the initial menu.
	 * @param names
	 *        The {@link List} of top-level {@link ResolvedModuleName}s that can
	 *        be built.
	 */
	public DisplayAndBuildModules (
		final @NotNull String menuItemText,
		final @NotNull List<ResolvedModuleName> names)
	{
		this.menuItemText = menuItemText;
		this.names = names;
	}
}
