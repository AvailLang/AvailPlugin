/*
 * AvailAction.java
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
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;
import org.availlang.plugin.psi.AvailPsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * An {@code AvailAction} is an {@link AnAction} specific to {@link
 * AvailPsiFile}s.
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public abstract class AvailAction
extends AnAction
{
	/**
	 * Answer the {@link Icon} associated with this action.
	 *
	 * @return An {@code Icon} if available or {@code null}.
	 */
	protected @Nullable Icon icon ()
	{
		return null;
	}

	/**
	 * Answer the {@link Project} for the given {@link AnActionEvent}.
	 *
	 * @param event
	 *        The {@code AnActionEvent} that provides the {@code Project}.
	 * @return A {@code Project}.
	 */
	protected @NotNull Project getProject (final @NotNull AnActionEvent event)
	{
		final Project project = event.getProject();
		assert project != null : String.format(
			"%s performed received AnActionEvent, %s, "
				+ "that did not have a Project",
			getClass().getSimpleName(),
			event.getPresentation());
		return project;
	}

	/**
	 * Construct an {@link AvailAction}.
	 */
	public AvailAction () {}

	/**
	 * Construct an {@link AvailAction}.
	 *
	 * @param text
	 *        The {@link Presentation} text.
	 * @param description
	 *        The {@code Presentation} description.
	 * @param icon
	 *        The {@code Presentation} {@link Icon}.
	 */
	AvailAction (
		final @Nullable String text,
		final @Nullable String description,
		final @Nullable Icon icon)
	{
		super(text, description, icon);
	}

	/**
	 * Answer a {@link AvailPsiFile}.
	 *
	 * @param event
	 *        The {@link AnActionEvent}.
	 * @return The file or {@code null} if not applicable.
	 */
	protected @Nullable AvailPsiFile psiFile (final AnActionEvent event)
	{
		try
		{
			final Object o = event.getData(CommonDataKeys.PSI_FILE);
			return o instanceof AvailPsiFile ? (AvailPsiFile) o : null;
		}
		catch (final Throwable e)
		{
			return null;
		}
	}

	/**
	 * Answer a custom menu item name for this {@link AvailAction}.
	 *
	 * @param psiFile
	 *        The {@link AvailPsiFile}.
	 * @return The name if available or an empty String for no custom name.
	 */
	protected @NotNull String customMenuItem (
		final @Nullable AvailPsiFile psiFile)
	{
		return "";
	}

	/**
	 * Answer whether or not the {@link AvailAction} should be visible.
	 *
	 * @param psiFile
	 *        The {@link AvailPsiFile}.
	 * @return {@code true} if it should be visible; {@code false} otherwise.
	 */
	protected boolean customVisibilityCheck (
		final @Nullable AvailPsiFile psiFile)
	{
		return true;
	}

	@Override
	public void update (final AnActionEvent event)
	{
		final Project project = event.getProject();
		if (project != null)
		{
			final AvailPsiFile psiFile = psiFile(event);
			if (customVisibilityCheck(psiFile))
			{
				final String customMenuItem = customMenuItem(psiFile);
				if (!customMenuItem.isEmpty())
				{
					event.getPresentation().setText(customMenuItem);
				}
				final Icon icon = icon();
				if (icon != null)
				{
					final Presentation presentation = getTemplatePresentation();
					presentation.setIcon(icon);
					presentation.setDisabledIcon(icon);
					presentation.setHoveredIcon(icon);
					presentation.setSelectedIcon(icon);
				}
				event.getPresentation().setEnabledAndVisible(true);
			}
			else
			{
				event.getPresentation().setEnabledAndVisible(false);
			}

		}
	}
}
