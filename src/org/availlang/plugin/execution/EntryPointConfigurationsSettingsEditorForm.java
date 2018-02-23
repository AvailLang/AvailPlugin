/*
 * EntryPointConfigurationsSettingsEditor.java
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

package org.availlang.plugin.execution;
import com.avail.utility.Mutable;
import com.avail.utility.Nulls;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.LabeledComponent;
import org.availlang.plugin.core.AvailComponent;
import org.availlang.plugin.core.utility.ModuleEntryPoints;
import org.availlang.plugin.process.execution.EntryPointRunConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A {@code EntryPointConfigurationsSettingsEditor} is a {@link SettingsEditor}
 * for a UI interface on
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class EntryPointConfigurationsSettingsEditorForm
extends SettingsEditor<EntryPointRunConfiguration>
{
	private JPanel mainPanel;
	private LabeledComponent<JComboBox<String>> entryPoint;
	private LabeledComponent<JComboBox<String>> module;
	private LabeledComponent<JComboBox<String>> roots;
	private LabeledComponent configName;
	private JTextField textField1;
	private final @NotNull Project project;

	private ModuleEntryPoints selectedModuleEntryPoint;

	public @Nullable ModuleEntryPoints getSelectedModuleEntryPoint ()
	{
		return selectedModuleEntryPoint;
	}

	public @Nullable String getSelectedEntryPoint ()
	{
		return (String) entryPoint.getComponent().getSelectedItem();
	}

	@Override
	protected void resetEditorFrom (final @NotNull EntryPointRunConfiguration s)
	{

	}

	@Override
	protected void applyEditorTo (final @NotNull EntryPointRunConfiguration s)
	throws ConfigurationException
	{

	}

	@Override
	protected @NotNull JComponent createEditor ()
	{
		final AvailComponent component =
			Nulls.stripNull(project.getComponent(AvailComponent.class));
		component.refresh();
		List<String> rootNames =
			new ArrayList<>(component.moduleRoots().rootNames());
		Collections.sort(rootNames);
		final JComboBox<String> rootBox = roots.getComponent();
		final JComboBox<String> moduleBox = module.getComponent();
		final JComboBox<String> entryPointBox = entryPoint.getComponent();
		final List<ModuleEntryPoints> moduleEntryPoints = new ArrayList<>();
		final Mutable<Boolean> allowModuleBoxListenerToFire = new Mutable<>(true);
		rootBox.addActionListener(event ->
		{
			allowModuleBoxListenerToFire.value = false;
			moduleEntryPoints.clear();
			moduleEntryPoints.addAll(
				component.moduleEntryPoints(
					(String) rootBox.getSelectedItem()));
			moduleEntryPoints.sort(Comparator.comparing(
				a -> a.resolvedModuleName().localName()));
			moduleBox.removeAllItems();
			moduleEntryPoints.forEach(m ->
				moduleBox.addItem(m.resolvedModuleName().localName()));
			final int index = moduleBox.getSelectedIndex();
			selectedModuleEntryPoint = moduleEntryPoints.get(index);
			entryPointBox.removeAllItems();
			selectedModuleEntryPoint.entryPoints().forEach(
				entryPointBox::addItem);
			allowModuleBoxListenerToFire.value = true;
		});

		moduleBox.addActionListener(mod ->
		{
			if (allowModuleBoxListenerToFire.value)
			{
				final int index = moduleBox.getSelectedIndex();
				selectedModuleEntryPoint = moduleEntryPoints.get(index);
				entryPointBox.removeAllItems();
				selectedModuleEntryPoint.entryPoints()
					.forEach(entryPointBox::addItem);
			}
		});
		rootNames.forEach(rootBox::addItem);
		return mainPanel;
	}

	public EntryPointConfigurationsSettingsEditorForm (
		final @NotNull Project project)
	{
		this.project = project;
	}
}
