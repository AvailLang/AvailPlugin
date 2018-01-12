/*
 * AvailRootsStep.java
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
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import com.intellij.util.ui.JBUI;
import org.availlang.plugin.ui.model.SimpleTableModel;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;

/**
 * An {@code AvailConfigurationStep} is a {@link ModuleWizardStep} that allows a user
 * to create {@link ModuleRoot}s when setting up a new Avail project.
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public abstract class AvailConfigurationStep
extends ModuleWizardStep
{
	/**
	 * The {@link Project} this {@link AvailConfigurationStep} is for.
	 */
	private final @NotNull Project project;

	/**
	 * Answer the {@link Project} this {@link AvailConfigurationStep} is for.
	 *
	 * @return A {@code Project}.
	 */
	public @NotNull Project getProject ()
	{
		return project;
	}

	/**
	 * The panel for adding the rootDirs.
	 */
	private final @NotNull JComponent rootPanel;

	@Override
	public JComponent getComponent()
	{
		return rootPanel;
	}

	/**
	 * Answer the brief title that describes this {@link
	 * AvailConfigurationStep}.
	 *
	 * @return A String.
	 */
	public abstract @NotNull String stepTitle ();

	/**
	 * Answer the {@link SimpleTableModel} for this {@link
	 * AvailConfigurationStep}'s {@link #rootPanel}.
	 *
	 * @return A {@code SimpleTableModel}.
	 */
	public abstract @NotNull SimpleTableModel tableModel ();

	/**
	 * Answer the {@link TableColumnModel} for this {@link
	 * AvailConfigurationStep}'s {@link #rootPanel}.
	 *
	 * @param jbTable
	 *        The {@link JBTable} that owns the {@link TableColumnModel}.
	 * @return A {@code TableColumnModel}.
	 */
	public abstract @NotNull TableColumnModel tableColumnModel (
		final @NotNull JBTable jbTable);

	/**
	 * Answer the {@link AbstractAction} used for adding a row to the {@link
	 * JBTable}.
	 *
	 * @param jbTable
	 *        The {@code JBTable} to add the row to.
	 * @return An {@code AbstractAction}.
	 */
	public abstract @NotNull AbstractAction addAction (
		final @NotNull JBTable jbTable);

	/**
	 * Answer the {@link AbstractAction} used for removing a row from the {@link
	 * JBTable}.
	 *
	 * @param jbTable
	 *        The {@code JBTable} to add the row to.
	 * @return An {@code AbstractAction}.
	 */
	public abstract @NotNull AbstractAction removeAction (
		final @NotNull JBTable jbTable);

	/**
	 * Answer the {@link JPanel} that contains the {@link SimpleTableModel}s
	 * used for setting {@link ModuleRoot}s and renames table?
	 */
	private @NotNull JPanel createPanel ()
	{
		final JPanel panel = new JPanel(new BorderLayout(20, 20));
		panel.setBorder(JBUI.Borders.empty());

		final JLabel panelLabel = new JLabel(stepTitle());
		panel.add(panelLabel);

		tableModel().rows().clear();
		final JBTable jbTable = new JBTable(tableModel());

		final JBScrollPane scrollPane = new JBScrollPane(jbTable);
		panel.add(scrollPane);

		final AbstractAction addAction = addAction(jbTable);
		final JButton addButton = new JButton(addAction);
		panel.add(addButton);

		final AbstractAction removeAction = removeAction(jbTable);
		final JButton removeButton = new JButton(removeAction);
		panel.add(removeButton);

		final GroupLayout layout = new GroupLayout(panel);
		panel.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setHorizontalGroup(
			layout.createParallelGroup()
				.addComponent(panelLabel)
				.addComponent(scrollPane)
				.addGroup(layout.createSequentialGroup()
					.addComponent(addButton)
					.addComponent(removeButton)));
		layout.setVerticalGroup(
			layout.createSequentialGroup()
				.addComponent(panelLabel)
				.addComponent(scrollPane)
				.addGroup(layout.createParallelGroup()
					.addComponent(addButton)
					.addComponent(removeButton)));

		return panel;
	}

	/**
	 * Construct the {@link AvailConfigurationStep}.
	 *
	 * @param project
	 *        The {@link Project} this {@link AvailConfigurationStep} is for.
	 */
	AvailConfigurationStep (final @NotNull Project project)
	{
		this.project = project;
		this.rootPanel = createPanel();
	}

	// TODO remove when stable
	//	/**
//	 * The {@link SimpleTableModel} used for setting {@link ModuleRoot}s.
//	 */
//	private static final @NotNull SimpleTableModel rootsTableModel =
//		new SimpleTableModel("root");
//
//	/**
//	 * The {@link SimpleTableModel} used for setting {@link ModuleRoot}s.
//	 */
//	private static final @NotNull SimpleTableModel sdksTableModel =
//		new SimpleTableModel("sdk", "repository", "source");

//	/**
//	 * Takes root relative paths of module names and map them to other module
//	 * names at other locations? It gives alternative short names for stuff
//	 * (aliases).
//	 *
//	 * For example if you have two implementation of Sockets, a super optimized
//	 * and another that is for super debug. Changing the renames can make it
//	 * point to the alias.
//	 */
//	private static final @NotNull SimpleTableModel renamesTableModel =
//		new SimpleTableModel("source", "target");

	/*
	 * Answer the {@link JPanel} that contains the {@link SimpleTableModel}s
	 * used for setting {@link ModuleRoot}s and renames table?
	 */
//	private static @NotNull JPanel createPPPanel ()
//	{
//		final JPanel panel = new JPanel(new BorderLayout(20, 20));
//		panel.setBorder(JBUI.Borders.empty());
//
//		// Add the module rootDirs area.
//		final JLabel rootsLabel = new JLabel("Avail module root directories");
//		panel.add(rootsLabel);
//
//		rootsTableModel.rows().clear();
//		final JBTable rootsTable = new JBTable(rootsTableModel);
//		rootsTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
//		final TableColumnModel rootsColumns = rootsTable.getColumnModel();
//		rootsColumns.getColumn(0).setMinWidth(50);
//		rootsColumns.getColumn(0).setPreferredWidth(400);
//		rootsColumns.getColumn(1).setMinWidth(50);
//		rootsColumns.getColumn(1).setPreferredWidth(400);
//		rootsTable.setGridColor(JBColor.GRAY);
//		rootsTable.setFillsViewportHeight(true);
//		final JBScrollPane rootsScrollPane = new JBScrollPane(rootsTable);
//		panel.add(rootsScrollPane);
//
//		final AbstractAction addRootAction =
//			new AbstractAction("+")
//			{
//				@Override
//				public void actionPerformed (final ActionEvent e)
//				{
//					int insertionIndex = rootsTable.getSelectedRow();
//					if (insertionIndex == -1)
//					{
//						insertionIndex = rootsTableModel.getRowCount();
//					}
//					rootsTableModel.rows().add(
//						insertionIndex, Arrays.asList("", "", ""));
//					rootsTableModel.fireTableDataChanged();
//					rootsTable.changeSelection(insertionIndex, 0, false, false);
//				}
//			};
//		final JButton addRootButton = new JButton(addRootAction);
//		panel.add(addRootButton);
//
//		final AbstractAction removeRootAction =
//			new AbstractAction("-")
//			{
//				@Override
//				public void actionPerformed (final ActionEvent e)
//				{
//					final int deletionIndex = rootsTable.getSelectedRow();
//					if (deletionIndex != -1)
//					{
//						rootsTableModel.rows().remove(deletionIndex);
//						rootsTableModel.fireTableDataChanged();
//						rootsTable.changeSelection(
//							rootsTableModel.rows().isEmpty()
//								? -1
//								: min(
//									deletionIndex,
//									rootsTableModel.getRowCount() - 1),
//							0,
//							false,
//							false);
//					}
//				}
//			};
//		final JButton removeRootButton = new JButton(removeRootAction);
//		panel.add(removeRootButton);
//
//		// Add the sdks area.
//		final JLabel sdksLabel = new JLabel("Avail SDKs");
//		panel.add(sdksLabel);
//
//		sdksTableModel.rows().clear();
//
//		final JBTable sdksTable = new JBTable(sdksTableModel);
//		sdksTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
//		final TableColumnModel sdksColumns = sdksTable.getColumnModel();
//		sdksColumns.getColumn(0).setMinWidth(30);
//		sdksColumns.getColumn(0).setPreferredWidth(60);
//		sdksColumns.getColumn(1).setMinWidth(50);
//		sdksColumns.getColumn(1).setPreferredWidth(400);
//		sdksColumns.getColumn(2).setMinWidth(50);
//		sdksColumns.getColumn(2).setPreferredWidth(400);
//		sdksTable.setGridColor(JBColor.GRAY);
//		sdksTable.setFillsViewportHeight(true);
//		final JBScrollPane sdksScrollPane = new JBScrollPane(sdksTable);
//		panel.add(sdksScrollPane);
//
//		final AbstractAction addSdkAction =
//			new AbstractAction("+")
//			{
//				@Override
//				public void actionPerformed (final ActionEvent e)
//				{
//					int insertionIndex = sdksTable.getSelectedRow();
//					if (insertionIndex == -1)
//					{
//						insertionIndex = sdksTableModel.getRowCount();
//					}
//					sdksTableModel.rows().add(
//						insertionIndex, Arrays.asList("", "", ""));
//					sdksTableModel.fireTableDataChanged();
//					sdksTable.changeSelection(insertionIndex, 0, false, false);
//				}
//			};
//		final JButton addSdkButton = new JButton(addSdkAction);
//		panel.add(addSdkButton);
//
//		final AbstractAction removeSdkAction =
//			new AbstractAction("-")
//			{
//				@Override
//				public void actionPerformed (final ActionEvent e)
//				{
//					final int deletionIndex = sdksTable.getSelectedRow();
//					if (deletionIndex != -1)
//					{
//						sdksTableModel.rows().remove(deletionIndex);
//						sdksTableModel.fireTableDataChanged();
//						sdksTable.changeSelection(
//							sdksTableModel.rows().isEmpty()
//								? -1
//								: min(
//									deletionIndex,
//									sdksTableModel.getRowCount() - 1),
//							0,
//							false,
//							false);
//					}
//				}
//			};
//		final JButton removeSdkButton = new JButton(removeSdkAction);
//		panel.add(removeSdkButton);
//
//
//		// Add the renames area.
//		final JLabel renamesLabel = new JLabel("Renames");
//		panel.add(renamesLabel);
//
//		renamesTableModel.rows().clear();
//
//		final JBTable renamesTable = new JBTable(renamesTableModel);
//		renamesTable.putClientProperty(
//			"terminateEditOnFocusLost", Boolean.TRUE);
//		final TableColumnModel renamesColumns = renamesTable.getColumnModel();
//		renamesColumns.getColumn(0).setMinWidth(50);
//		renamesColumns.getColumn(0).setPreferredWidth(400);
//		renamesColumns.getColumn(1).setMinWidth(50);
//		renamesColumns.getColumn(1).setPreferredWidth(400);
//		renamesTable.setGridColor(JBColor.GRAY);
//		renamesTable.setFillsViewportHeight(true);
//		final JBScrollPane renamesScrollPane = new JBScrollPane(renamesTable);
//		panel.add(renamesScrollPane);
//
//		final AbstractAction addRenameAction =
//			new AbstractAction("+")
//			{
//				@Override
//				public void actionPerformed (final ActionEvent e)
//				{
//					int insertionIndex = renamesTable.getSelectedRow();
//					if (insertionIndex == -1)
//					{
//						insertionIndex = renamesTableModel.getRowCount();
//					}
//					renamesTableModel.rows().add(
//						insertionIndex, Arrays.asList("", ""));
//					renamesTableModel.fireTableDataChanged();
//					renamesTable.changeSelection(
//						insertionIndex, 0, false, false);
//				}
//			};
//		final JButton addRenameButton = new JButton(addRenameAction);
//		panel.add(addRenameButton);
//
//		final AbstractAction removeRenameAction =
//			new AbstractAction("-")
//			{
//				@Override
//				public void actionPerformed (final ActionEvent e)
//				{
//					final int deletionIndex = renamesTable.getSelectedRow();
//					if (deletionIndex != -1)
//					{
//						renamesTableModel.rows().remove(deletionIndex);
//						renamesTableModel.fireTableDataChanged();
//						renamesTable.changeSelection(
//							renamesTableModel.rows().isEmpty()
//								? -1
//								: min(
//									deletionIndex,
//									renamesTableModel.getRowCount() - 1),
//							0,
//							false,
//							false);
//					}
//				}
//			};
//		final JButton removeRenameButton = new JButton(removeRenameAction);
//		panel.add(removeRenameButton);
//
//		// Add the ok/cancel buttons.
////		final AbstractAction okAction =
////			new AbstractAction(
////				UIManager.getString("OptionPane.okButtonText"))
////			{
////				@Override
////				public void actionPerformed (final ActionEvent e)
////				{
//////					savePreferences();
//////					workbench.refresh();
//////					preferencesDialog.setVisible(false);
////				}
////			};
////		final JButton okButton = new JButton(okAction);
////		panel.add(okButton);
////		final AbstractAction cancelAction =
////			new AbstractAction(
////				UIManager.getString("OptionPane.cancelButtonText"))
////			{
////				@Override
////				public void actionPerformed (final ActionEvent e)
////				{
//////					preferencesDialog.setVisible(false);
////				}
////			};
////		final JButton cancelButton = new JButton(cancelAction);
////		panel.add(cancelButton);
//
//		final GroupLayout layout = new GroupLayout(panel);
//		panel.setLayout(layout);
//		layout.setAutoCreateGaps(true);
//		layout.setHorizontalGroup(
//			layout.createParallelGroup()
//				.addComponent(rootsLabel)
//				.addComponent(rootsScrollPane)
//				.addGroup(layout.createSequentialGroup()
//					.addComponent(addRootButton)
//					.addComponent(removeRootButton))
//				.addComponent(sdksLabel)
//				.addComponent(sdksScrollPane)
//				.addGroup(layout.createSequentialGroup()
//					.addComponent(addSdkButton)
//					.addComponent(removeSdkButton))
//				.addComponent(renamesLabel)
//				.addComponent(renamesScrollPane)
//				.addGroup(layout.createSequentialGroup()
//					.addComponent(addRenameButton)
//					.addComponent(removeRenameButton)));
//		layout.setVerticalGroup(
//			layout.createSequentialGroup()
//				.addComponent(rootsLabel)
//				.addComponent(rootsScrollPane)
//				.addGroup(layout.createParallelGroup()
//					.addComponent(addRootButton)
//					.addComponent(removeRootButton))
//				.addComponent(sdksLabel)
//				.addComponent(sdksScrollPane)
//				.addGroup(layout.createParallelGroup()
//					.addComponent(addSdkButton)
//					.addComponent(removeSdkButton))
//				.addComponent(renamesLabel)
//				.addComponent(renamesScrollPane)
//				.addGroup(layout.createParallelGroup()
//					.addComponent(addRenameButton)
//					.addComponent(removeRenameButton)));
//		return panel;
//	}
}
