/*
 * ResolvedModuleNameOptionDialog.java
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
package org.availlang.plugin.ui.dialogs;
import com.avail.builder.ResolvedModuleName;
import com.intellij.ide.util.ChooseElementsDialog;
import com.intellij.ide.util.ElementsChooser;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;

/**
 * An {@code EntryPointOptionDialog} is a {@link ChooseElementsDialog}.
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class ResolvedModuleNameOptionDialog
	extends ChooseElementsDialog<ResolvedModuleName>
{
	@Override
	protected String getItemText (final ResolvedModuleName item)
	{
		return item.localName();
	}

	@Override
	protected @Nullable Icon getItemIcon (final ResolvedModuleName item)
	{
		return null;
	}

	/**
	 * Set this {@link ResolvedModuleNameOptionDialog} to only {@linkplain
	 * ElementsChooser#setSingleSelectionMode() select one} module.
	 */
	public void setSingleSelect ()
	{
		myChooser.setSingleSelectionMode();
	}

	/**
	 * Answer the selected {@link ResolvedModuleName}.
	 *
	 * @return A {@code ResolvedModuleName} or {@code null} if nothing selected.
	 */
	public @Nullable ResolvedModuleName getSelection ()
	{
		return myChooser.getSelectedElement();
	}

	/**
	 * Construct a {@link ResolvedModuleNameOptionDialog}.
	 *
	 * @param project
	 *        A {@link Project}.
	 */
	public ResolvedModuleNameOptionDialog (
		final @NotNull Project project,
		final List<ResolvedModuleName> resolvedModuleNames)
	{
		super(
			project,
			resolvedModuleNames,
			"Select Modules to build",
			"Build selected modules",
			true);

	}



	@Override
	protected JComponent createCenterPanel ()
	{
		return super.createCenterPanel();
	}
}
