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
import com.avail.builder.ModuleRoot;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.ui.JBColor;
import com.intellij.ui.table.JBTable;
import org.availlang.plugin.configuration.AvailPluginConfiguration;
import org.availlang.plugin.configuration.AvailPluginConfiguration.AvailRoot;
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
 * A {@code AvailRootStep} is an {@link AvailConfigurationStep} for adding
 * development {@link ModuleRoot}s to the {@link Project}.
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class AvailRootStep
extends AvailConfigurationStep
{
	/**
	 * The {@link SimpleTableModel} used for setting {@link ModuleRoot}s.
	 */
	private final @NotNull SimpleTableModel rootsTableModel =
		new SimpleTableModel("root name");

	@Override
	public @NotNull SimpleTableModel tableModel ()
	{
		return rootsTableModel;
	}

	@Override
	public @NotNull String stepTitle ()
	{
		return "Add Avail Module Roots";
	}

	/**
	 * The input {@linkplain AvailRoot Avail roots}.
	 */
	private final @NotNull Map<String, AvailRoot> localRootMap =
		new HashMap<>();

	@Override
	public void updateDataModel()
	{
		final AvailPluginConfiguration configuration =
			getProject().getComponent(AvailPluginConfiguration.class);
		configuration.rootMap.clear();
		configuration.markDirty();
		localRootMap.forEach(configuration.rootMap::put);
	}

	@Override
	public boolean validate () throws ConfigurationException
	{
		final AvailPluginConfiguration configuration =
			getProject().getComponent(AvailPluginConfiguration.class);
		int i = 1;
		for (final List<String> row : rootsTableModel.rows())
		{
			if (row.size() != 1)
			{
				throw new ConfigurationException(
					String.format("Row %d input is malformed;", i));
			}
			final String name = row.get(0);
			if (name.isEmpty())
			{
				throw new ConfigurationException(
					String.format("Row %d is missing a root name", i));
			}
			if (name.contains("\\") || name.contains("/"))
			{
				throw new ConfigurationException(
					String.format(
						"Row %d root name contains an illegal character; "
							+ "\"\\\" nor \"/\" are permitted in the root name",
						i));
			}
			final AvailRoot root =
				new AvailRoot(
					configuration,
					false,
					name,
					"",
					"");
			localRootMap.put(root.name, root);
			i++;
		}
		return true;
	}

	@Override
	public final @NotNull TableColumnModel tableColumnModel (
		final @NotNull JBTable jbTable)
	{
		jbTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		final TableColumnModel rootColumns = jbTable.getColumnModel();
		rootColumns.getColumn(0).setMinWidth(50);
		rootColumns.getColumn(0).setPreferredWidth(450);
		jbTable.setGridColor(JBColor.GRAY);
		jbTable.setFillsViewportHeight(true);
		return rootColumns;
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
					insertionIndex = rootsTableModel.getRowCount();
				}
				rootsTableModel.rows().add(
					insertionIndex, Arrays.asList("", "", ""));
				rootsTableModel.fireTableDataChanged();
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
					rootsTableModel.rows().remove(deletionIndex);
					rootsTableModel.fireTableDataChanged();
					jbTable.changeSelection(
						rootsTableModel.rows().isEmpty()
							? -1
							: min(
								deletionIndex,
								rootsTableModel.getRowCount() - 1),
						0,
						false,
						false);
				}
			}
		};
	}

	/**
	 * Construct a {@link AvailRootStep}.
	 *
	 * @param project
	 *        The {@link Project} this {@code AvailSdkStep} is for.
	 */
	public AvailRootStep (final @NotNull Project project)
	{
		super(project);
	}
}
