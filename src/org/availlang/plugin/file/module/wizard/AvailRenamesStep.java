/*
 * AvailRootStep.java
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

package org.availlang.plugin.file.module.wizard;
import com.avail.builder.ModuleNameResolver;
import com.avail.builder.ModuleRoot;
import com.intellij.ui.JBColor;
import com.intellij.ui.table.JBTable;
import org.availlang.plugin.configuration.AvailPluginConfiguration;
import org.availlang.plugin.configuration.AvailPluginConfiguration.AvailRename;
import org.availlang.plugin.ui.model.SimpleTableModel;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.min;

/**
 * A {@code AvailRenamesStep} is is an {@link AvailRootStep} for optionally
 * setting up Avail {@link ModuleNameResolver} {@linkplain
 * ModuleNameResolver#addRenameRule(String, String) renames}.
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class AvailRenamesStep
extends AvailConfigurationStep
{
	/**
	 * Answer a {@link SimpleTableModel} used for setting {@link ModuleRoot}s.
	 */
	private static final @NotNull SimpleTableModel renamesTableModel ()
	{
		return new SimpleTableModel("source", "target");
	}

	/**
	 * The {@link SimpleTableModel} used for setting {@link ModuleRoot}s.
	 */
	private SimpleTableModel renamesTableModel;

	@Override
	public @NotNull SimpleTableModel tableModel ()
	{
		if (renamesTableModel == null)
		{
			renamesTableModel = renamesTableModel();
		}
		return renamesTableModel;
	}

	/**
	 * The input {@linkplain AvailRename Avail renames}.
	 */
	private final @NotNull Map<String, AvailRename> localRenamesMap =
		new HashMap<>();

	@Override
	public void updateDataModel()
	{
		configuration.renameMap.clear();
		configuration.markDirty();
		localRenamesMap.forEach(configuration.renameMap::put);
	}

	@Override
	public boolean validate () throws com.intellij.openapi.options.ConfigurationException
	{
		int i = 1;
		for (final List<String> row : tableModel().rows())
		{
			if (row.size() != 2)
			{
				throw new com.intellij.openapi.options.ConfigurationException(
					String.format(
						"Row %d input is malformed; there should be a value in "
							+ "each column",
						i));
			}
			final String source = row.get(0);
			if (source.isEmpty())
			{
				throw new com.intellij.openapi.options.ConfigurationException(
					String.format("Row %d is missing a source", i));
			}
			final String target = row.get(1);
			if (target.isEmpty())
			{
				throw new com.intellij.openapi.options.ConfigurationException(
					String.format("Row %d is missing a target", i));
			}
			final AvailRename rename =
				new AvailRename(source, target);
			localRenamesMap.put(rename.source, rename);
			i++;
		}
		return true;
	}

	@Override
	public @NotNull String stepTitle ()
	{
		return "Optionally Add Avail Module Renames";
	}

	@Override
	public final @NotNull TableColumnModel tableColumnModel (
		final @NotNull JBTable jbTable)
	{
		jbTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		final TableColumnModel renamesColumns = jbTable.getColumnModel();
		renamesColumns.getColumn(0).setMinWidth(50);
		renamesColumns.getColumn(0).setPreferredWidth(400);
		renamesColumns.getColumn(1).setMinWidth(50);
		renamesColumns.getColumn(1).setPreferredWidth(400);
		jbTable.setGridColor(JBColor.GRAY);
		jbTable.setFillsViewportHeight(true);
		return renamesColumns;
	}

	@Override
	public @NotNull AbstractAction addAction (final @NotNull JBTable jbTable)
	{
		return new AbstractAction("+")
		{
			@Override
			public void actionPerformed (final ActionEvent e)
			{
				int insertionIndex = jbTable.getSelectedRow();
				if (insertionIndex == -1)
				{
					insertionIndex = renamesTableModel.getRowCount();
				}
				renamesTableModel.rows().add(
					insertionIndex, Arrays.asList("", ""));
				renamesTableModel.fireTableDataChanged();
				jbTable.changeSelection(
					insertionIndex, 0, false, false);
			}
		};
	}

	@Override
	public @NotNull AbstractAction removeAction (final @NotNull JBTable jbTable)
	{
		return new AbstractAction("-")
		{
			@Override
			public void actionPerformed (final ActionEvent e)
			{
				final int deletionIndex = jbTable.getSelectedRow();
				if (deletionIndex != -1)
				{
					renamesTableModel.rows().remove(deletionIndex);
					renamesTableModel.fireTableDataChanged();
					jbTable.changeSelection(
						renamesTableModel.rows().isEmpty()
							? -1
							: min(
								deletionIndex,
								renamesTableModel.getRowCount() - 1),
						0,
						false,
						false);
				}
			}
		};
	}

	/**
	 * Construct a {@link AvailRenamesStep}.
	 *
	 * @param configuration
	 *        The {@link AvailPluginConfiguration} used to configure the
	 *        project.
	 */
	public AvailRenamesStep (
		final @NotNull AvailPluginConfiguration configuration)
	{
		super(configuration);
	}
}
