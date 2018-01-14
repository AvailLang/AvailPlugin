/*
 * AvailModuleBuilder.java
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
package org.availlang.plugin.file.module;
import com.avail.utility.Nulls;
import com.intellij.ide.util.projectWizard.ModuleBuilder;
import com.intellij.ide.util.projectWizard.ModuleBuilderListener;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.module.ModifiableModuleModel;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import org.availlang.plugin.configuration.AvailPluginConfiguration;
import org.availlang.plugin.file.module.wizard.AvailConfigurationStep;
import org.availlang.plugin.file.module.wizard.AvailRenamesStep;
import org.availlang.plugin.file.module.wizard.AvailRootStep;
import org.availlang.plugin.file.module.wizard.AvailSdkStep;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jps.model.java.JavaResourceRootType;
import org.jetbrains.jps.model.java.JavaSourceRootType;

import java.util.List;

/**
 * A {@code AvailModuleBuilder} is an {@link ModuleBuilder} for {@link
 * AvailModuleType}s.
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class AvailModuleBuilder
extends ModuleBuilder
implements ModuleBuilderListener
{
	/**
	 * The {@link AvailPluginConfiguration} used to configure the project.
	 */
	private final @NotNull AvailPluginConfiguration configuration =
		new AvailPluginConfiguration();

	/**
	 * The {@link WizardContext} setting up this project.
	 */
	private WizardContext wizardContext;

	@Override
	public void setupRootModel (final ModifiableRootModel modifiableRootModel)
	{
		configuration.initializePaths(
			Nulls.stripNull(wizardContext).getProjectFileDirectory());
		configuration.initializeEnvironmentStructure();
		configuration.writeConfigFile();
		final LocalFileSystem localFileSystem = LocalFileSystem.getInstance();
		final VirtualFile base =
			localFileSystem
				.refreshAndFindFileByPath(
					FileUtil.toSystemIndependentName(
						configuration.basePath));
		final ContentEntry contentEntry =
			modifiableRootModel.addContentEntry(base);
		final VirtualFile roots =
			localFileSystem
				.refreshAndFindFileByPath(
					FileUtil.toSystemIndependentName(
						configuration.rootsDirectory));
		contentEntry.addSourceFolder(
			roots, JavaSourceRootType.SOURCE);
		final VirtualFile repos =
			localFileSystem
				.refreshAndFindFileByPath(
					FileUtil.toSystemIndependentName(
						configuration.repositoryDirectory));
		contentEntry.addSourceFolder(
			repos, JavaResourceRootType.RESOURCE);
	}

	@Override
	public @Nullable List<Module> commit (
		final @NotNull Project project,
		final ModifiableModuleModel model,
		final ModulesProvider modulesProvider)
	{
		return super.commit(project, model, modulesProvider);
	}

	@Override
	public ModuleType getModuleType ()
	{
		return AvailModuleType.getInstance();
	}

	@Override
	public ModuleWizardStep[] createWizardSteps (
		final @NotNull WizardContext wizardContext,
		final @NotNull ModulesProvider modulesProvider)
	{
		if (this.wizardContext == null)
		{
			this.wizardContext = wizardContext;
		}
		final AvailRootStep step1 = new AvailRootStep(configuration);
		final AvailSdkStep step2 = new AvailSdkStep(configuration);
		final AvailRenamesStep step3 = new AvailRenamesStep(configuration);
		final ModuleWizardStep[] steps = new AvailConfigurationStep[3];
		steps[0] = step1;
		steps[1] = step2;
		steps[2] = step3;
		return steps;
	}

	@Override
	public void moduleCreated (final @NotNull Module module)
	{
		// Todo may be needed later...
	}

	/**
	 * Construct an {@link AvailModuleBuilder}.
	 */
	AvailModuleBuilder ()
	{
		addListener(this);
	}
}
