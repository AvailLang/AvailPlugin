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
import java.util.function.Function;

import static java.lang.Math.min;

/**
 * A {@code AvailRootStep} is is an {@link AvailConfigurationStep} for
 * optionally adding pre-existing Avail {@link ModuleRoot}s to act as the Avail
 * SDK.
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class AvailSdkStep
extends AvailConfigurationStep
{
	/**
	 * Answer a {@link SimpleTableModel} used for setting {@link ModuleRoot}s.
	 */
	private static @NotNull SimpleTableModel sdksTableModel ()
	{
		return new SimpleTableModel("repository file", "source");
	}

	/**
	 * The {@link SimpleTableModel} used for setting {@link ModuleRoot}s.
	 */
	private SimpleTableModel sdksTableModel;

	@Override
	public @NotNull SimpleTableModel tableModel ()
	{
		if (sdksTableModel == null)
		{
			sdksTableModel = sdksTableModel();
		}
		return sdksTableModel;
	}

	@Override
	public @NotNull String stepTitle ()
	{
		return "Optionally Add Avail SDKs";
	}

	/**
	 * The input {@linkplain AvailRoot Avail SDKs}.
	 */
	private final @NotNull
	Map<String, Function<AvailPluginConfiguration, AvailRoot>> localSdkMap =
		new HashMap<>();

	@Override
	public void updateDataModel()
	{
		configuration.sdkMap.clear();
		configuration.markDirty();
		localSdkMap.forEach(
			(k,v) -> configuration.sdkMap.put(k, v.apply(configuration)));
	}

	@Override
	public boolean validate () throws ConfigurationException
	{
		if (tableModel().rows().isEmpty())
		{
			return true;
		}
		int i = 1;
		for (final List<String> row : tableModel().rows())
		{
			if (row.size() != 2)
			{
				throw new ConfigurationException(
					String.format(
						"Row %d input is malformed; there should be a value in "
							+ "each column",
						i));
			}
			final String filePath = row.get(0);
			final String[] directoryPath =
				filePath.contains("/")
					? filePath.split("/")
					: filePath.split("\\\\");
			final int size = directoryPath.length;
			if (filePath.isEmpty())
			{
				throw new ConfigurationException(
					String.format("Row %d is missing an SDK file", i));
			}
			final String[] splitName =
				directoryPath[size - 1].split("\\.");
			if (splitName.length != 2 && !splitName[1].equals("repo"))
			{
				throw new ConfigurationException(
					String.format(
						"Row %d has a malformed SDK name; missing \".repo\" "
							+ "file extension",
						i));
			}
			final String source = row.get(1);
			if (source.isEmpty())
			{
				throw new ConfigurationException(
					String.format(
						"Row %d is missing an SDK source directory", i));
			}

			localSdkMap.put(
				splitName[0],
				configuration -> configuration.availRoot(
					configuration,
					true,
					splitName[0],
					directoryPath[size - 1],
					row.get(1)));
			i++;
		}
		return true;
	}

	@Override
	public final @NotNull TableColumnModel tableColumnModel (
		final @NotNull JBTable jbTable)
	{
		jbTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		final TableColumnModel sdksColumns = jbTable.getColumnModel();
		sdksColumns.getColumn(0).setMinWidth(50);
		sdksColumns.getColumn(0).setPreferredWidth(100);
		sdksColumns.getColumn(1).setMinWidth(50);
		sdksColumns.getColumn(1).setPreferredWidth(100);
		jbTable.setGridColor(JBColor.GRAY);
		jbTable.setFillsViewportHeight(true);
		return sdksColumns;
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
					insertionIndex = sdksTableModel.getRowCount();
					sdksTableModel.rows().add(Arrays.asList("", ""));
				}
				else
				{
					insertionIndex = insertionIndex + 1;
					sdksTableModel.rows().add(
						insertionIndex, Arrays.asList("", ""));
				}
				sdksTableModel.fireTableDataChanged();
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
					sdksTableModel.rows().remove(deletionIndex);
					sdksTableModel.fireTableDataChanged();
					jbTable.changeSelection(
						sdksTableModel.rows().isEmpty()
							? -1
							: min(
								deletionIndex,
								sdksTableModel.getRowCount() - 1),
						0,
						false,
						false);
				}
			}
		};
	}

	/**
	 * Construct a {@link AvailSdkStep}.
	 *
	 * @param configuration
	 *        The {@link AvailPluginConfiguration} used to configure the
	 *        project.
	 */
	public AvailSdkStep (final @NotNull AvailPluginConfiguration configuration)
	{
		super(configuration);
	}
}
