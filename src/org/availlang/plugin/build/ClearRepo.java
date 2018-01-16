/*
 * CleanRepo.java
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
import com.avail.builder.ModuleRoot;
import com.avail.persistence.IndexedFileException;
import com.avail.persistence.IndexedRepositoryManager;
import com.avail.utility.evaluation.Continuation0;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task.Backgroundable;
import com.intellij.openapi.progress.util.ProgressWindow;
import com.intellij.openapi.project.Project;
import org.availlang.plugin.actions.AvailAction;
import org.availlang.plugin.core.AvailComponent;
import org.availlang.plugin.exceptions.AvailPluginException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * A {@link ClearRepo} is an {@link AvailAction} for {@linkplain
 * IndexedRepositoryManager#clear() clear} the Avail environment of a given
 * {@link ModuleRoot}.
 *
 * @author Richard A Arriaga &lt;rarriaga@safetyweb.org&gt;
 */
public class ClearRepo
extends AvailAction
{
	@Override
	public void actionPerformed (final AnActionEvent event)
	{
		// TODO get the repo name from somewhere and call clear repository.
//		getAvailComponent(event).clearRepository(rootName, event, () -> {});
	}

	/**
	 * Clear the provided {@link ModuleRoot}'s {@link IndexedRepositoryManager}.
	 *
	 * @param root
	 *        The {@code ModuleRoot} to clear.
	 * @param component
	 *        The {@link AvailComponent} held by this {@link Project}.
	 * @param onSuccess
	 *        The {@link Continuation0} to run after clearing the repository.
	 */
	public static void clearRepository (
		final @NotNull ModuleRoot root,
		final @NotNull AvailComponent component,
		final @NotNull Continuation0 onSuccess)
	{
		final ProgressManager manager = ProgressManager.getInstance();
		final String label = String.format("Clearing %s", root.name());
		manager.runProcessWithProgressAsynchronously(
			new Backgroundable(
				component.getProject(),
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
					window.setIndeterminate(true);
					try
					{
						root.repository().clear();
					}
					catch (final @NotNull IOException | IndexedFileException e)
					{
						AvailPluginException.dialog(
							String.format("Failed to clean %s", root.name()))
						.show();
					}
				}
			},
			new ProgressWindow(true, component.getProject()));
	}
}
