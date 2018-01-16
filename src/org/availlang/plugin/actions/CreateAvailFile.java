/*
 * CreateAvailFile.java
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
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import org.availlang.plugin.icons.AvailIcon;
import org.availlang.plugin.file.psi.AvailPsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

/**
 * A {@code CreateAvailFile} is an {@link AvailAction} used to create a new
 * Avail file.
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class CreateAvailFile
extends AvailAction
{
	@Override
	public void actionPerformed (final AnActionEvent event)
	{
		final AvailPsiFile psiFile = psiFile(event);
		if (psiFile != null)
		{
			final PsiDirectory psiDirectory = psiFile.getParent();
			final String dirPath = psiFile.getVirtualFile().getPath();
			// TODO create a dialog to get file name
			final File newFile = new File(dirPath
				+ File.separator
				+ "new name.avail");
			if (newFile.exists())
			{
				// TODO throw error dialog
			}
			else
			{
				try
				{
					boolean created = newFile.createNewFile();
					if (!created)
					{
						// TODO say something!
					}
					else
					{
//						final VirtualFile vf =
					}
				}
				catch (final @NotNull IOException e)
				{

				}
			}

		}
		else
		{
			final Object o = event.getData(CommonDataKeys.NAVIGATABLE);
			// TODO make an Avail PSIDirectory, then get the location
		}
	}

	@Override
	protected @Nullable Icon icon ()
	{
		return AvailIcon.availFileIcon;
	}

	@Override
	protected boolean customVisibilityCheck (
		final @NotNull AnActionEvent event,
		final @Nullable AvailPsiFile psiFile)
	{
		if (psiFile == null)
		{
			final Object o = event.getData(CommonDataKeys.NAVIGATABLE);
			// TODO check if Avail PSI Directory
			return true;
		}
		else
		{
			return true;
		}
	}
}
