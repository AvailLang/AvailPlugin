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
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import com.intellij.util.ui.JBUI;
import org.availlang.plugin.configuration.AvailPluginConfiguration;
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
	 * The {@link AvailPluginConfiguration} used to configure the project.
	 */
	protected final @NotNull AvailPluginConfiguration configuration;

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
		tableColumnModel(jbTable);

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
	 * @param configuration
	 *        The {@link AvailPluginConfiguration} used to configure the
	 *        project.
	 */
	AvailConfigurationStep (
		final @NotNull AvailPluginConfiguration configuration)
	{
		this.rootPanel = createPanel();
		this.configuration = configuration;
	}
}
