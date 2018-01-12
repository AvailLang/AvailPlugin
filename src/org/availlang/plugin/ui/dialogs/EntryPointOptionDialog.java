/*
 * EntryPointOptionDialog.java
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
import com.avail.builder.AvailBuilder;
import com.avail.builder.AvailBuilder.LoadedModule;
import com.avail.linking.EntryPoint;
import com.intellij.ide.util.ChooseElementsDialog;
import com.intellij.openapi.project.Project;
import org.availlang.plugin.core.AvailComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * An {@code EntryPointOptionDialog} is a {@link ChooseElementsDialog}.
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class EntryPointOptionDialog
extends ChooseElementsDialog<String>
{
	@Override
	protected String getItemText (final String item)
	{
		return item;
	}

	@Override
	protected @Nullable Icon getItemIcon (final String item)
	{
		return null;
	}

	/**
	 * Answer the selected entry point.
	 *
	 * @return A {@code String}.
	 */
	public @NotNull String selectedEntryPoint ()
	{
		final String item = myChooser.getSelectedElement();
		assert item != null;
		return item;
	}

	/**
	 * Construct a {@link EntryPointOptionDialog}.
	 *
	 * @param project
	 *        A {@link Project}.
	 */
	public EntryPointOptionDialog (final @NotNull Project project)
	{
		super(
			project,
			calculateEntryPoints(project),
			"Run Entry Point",
			"Run Avail Entry Point",
			true);
		myChooser.setSingleSelectionMode();
	}

	/**
	 * Construct a {@link EntryPointOptionDialog}.
	 *
	 * @param project
	 *        A {@link Project}.
	 * @param entryPoints
	 *        The {@link List} of {@code String} {@link EntryPoint}s to run.
	 */
	public EntryPointOptionDialog (
		final @NotNull Project project,
		final @NotNull List<String> entryPoints)
	{
		super(
			project,
			entryPoints,
			"Run Entry Point",
			"Run Avail Entry Point",
			true);
		myChooser.setSingleSelectionMode();
	}

	/**
	 * Populate the entry points.
	 *
	 * @return A {@code String} {@link List} of available entry points.
	 */
	private static List<String> calculateEntryPoints (
		final @NotNull Project project)
	{
		final List<String> entryPoints = new ArrayList<>();
		final AvailBuilder builder =
			AvailComponent.getInstance(project).builder();
		for (final LoadedModule loadedModule : builder.loadedModulesCopy())
		{
			if (!loadedModule.entryPoints().isEmpty())
			{
				entryPoints.addAll(loadedModule.entryPoints());
			}
		}
		return entryPoints;
	}
}
