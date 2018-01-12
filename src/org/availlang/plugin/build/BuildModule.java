/*
 * BuildModule.java
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
package org.availlang.plugin.build;
import com.avail.builder.AvailBuilder;
import com.avail.builder.ResolvedModuleName;
import com.avail.utility.Nulls;
import com.avail.utility.evaluation.Continuation0;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task.Backgroundable;
import com.intellij.openapi.progress.util.ProgressWindow;
import org.availlang.plugin.actions.AvailAction;
import org.availlang.plugin.actions.DisplayAndBuildModules;
import org.availlang.plugin.core.AvailComponent;
import org.availlang.plugin.language.AvailLanguage;
import org.availlang.plugin.psi.AvailPsiFile;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Iterator;

/**
 * A {@code BuildModule} is {@link AnAction} that causes an {@link
 * AvailLanguage} to build.
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class BuildModule
extends AvailAction
{
	@Override
	public void actionPerformed (final AnActionEvent event)
	{
		final ProgressManager manager = ProgressManager.getInstance();
		final AvailPsiFile psiFile = psiFile(event);
		assert psiFile != null;
		final ResolvedModuleName resolvedModuleName =
			psiFile.resolvedModuleName();
		if (resolvedModuleName != null)
		{
			build(false, event, manager, resolvedModuleName);
		}
	}


	public static void build (
		final boolean loadingOnly,
		final @NotNull AnActionEvent event,
		final @NotNull ProgressManager manager,
		final @NotNull ResolvedModuleName resolvedModuleName)
	{
		build(loadingOnly, event, manager, resolvedModuleName, () -> {});
	}

	/**
	 * The action to perform upon successful completion of {@linkplain
	 * BuildModule#build(boolean, AnActionEvent, ProgressManager,
	 * ResolvedModuleName) building a module}.
	 *
	 * @param toBuildList
	 *        The {@link Iterator} of {@link ResolvedModuleName}s to build.
	 * @param manager
	 *        The {@link ProgressManager}.
	 * @param event
	 *        The {@link AnActionEvent} that triggered the {@link
	 *        DisplayAndBuildModules}.
	 */
	public static void buildModules (
		final boolean loadingOnly,
		final @NotNull Iterator<ResolvedModuleName> toBuildList,
		final @NotNull ProgressManager manager,
		final @NotNull AnActionEvent event,
		final @NotNull Continuation0 done)
	{
		if (toBuildList.hasNext())
		{
			final ResolvedModuleName name = toBuildList.next();
			final AvailComponent component =
				AvailComponent.getInstance(Nulls.stripNull(getEventProject(event)));
			if (component.builder().getLoadedModule(name) == null)
			{
				BuildModule.build(
					loadingOnly,
					event,
					manager,
					name,
					() -> buildModules(
						loadingOnly, toBuildList, manager, event, done));
			}
			else
			{
				buildModules(loadingOnly, toBuildList, manager, event, done);
			}
		}
		else
		{
			done.value();
		}
	}

	/**
	 * Build a {@link ResolvedModuleName}.
	 *
	 * @param event
	 *        The {@link AnActionEvent}.
	 * @param manager
	 *        The {@link ProgressManager}.
	 * @param resolvedModuleName
	 *        The {@code ResolvedModuleName} to build.
	 */
	public static void build (
		final boolean loadingOnly,
		final @NotNull AnActionEvent event,
		final @NotNull ProgressManager manager,
		final @NotNull ResolvedModuleName resolvedModuleName,
		final @NotNull Continuation0 onSuccess)
	{
		final AvailBuilder builder =
			AvailComponent.getInstance(
				Nulls.stripNull(getEventProject(event))).builder();
		final String label = loadingOnly
			? String.format(
				"Loading %s",
				resolvedModuleName.qualifiedName())
			: String.format(
				"Building %s",
				resolvedModuleName.qualifiedName());
		manager.runProcessWithProgressAsynchronously(
			new Backgroundable(
				event.getProject(),
				label,
				true)
			{
				@Override
				public void onSuccess ()
				{
					onSuccess.value();
				}

				@Override
				public void run (@NotNull final ProgressIndicator progress)
				{
					final ProgressWindow window = (ProgressWindow) progress;
					window.setTitle(label);
					builder.buildTarget(
						resolvedModuleName,
						(moduleName, moduleSize, position) -> {},
						(position, globalCodeSize) ->
						{
							assert position != null;
							assert globalCodeSize != null;
							if (window.isCanceled())
							{
								builder.cancel();
							}
							else
							{
								final long perThousand =
									(position * 1000L) / globalCodeSize;
								final double percent =
									perThousand / 1000.0d;
								progress.setText(String.format(
									"%,dB (%3.1f%%)",
									position,
									percent * 100.d));
								progress.setFraction(percent);
							}
						}
					);
				}
			},
			new ProgressWindow(true, event.getProject()));
	}

	@Override
	protected @NotNull String customMenuItem (
		final @Nullable AvailPsiFile psiFile)
	{
		assert psiFile != null;
		return  "Build '"
			+ psiFile.getVirtualFile().getNameWithoutExtension() + "'";
	}

	@Override
	protected boolean customVisibilityCheck (
		final @NotNull AnActionEvent event,
		final @Nullable AvailPsiFile psiFile)
	{
		if (psiFile == null)
		{
			return false;
		}
		// It can only be visible if the file has not yet been loaded.
		return !AvailComponent.getInstance(
			Nulls.stripNull(getEventProject(event))).isLoaded(psiFile);
	}
}
