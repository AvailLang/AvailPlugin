/*
 * AvailProjectStructureDetector.java
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
import com.intellij.ide.util.importProject.ModuleDescriptor;
import com.intellij.ide.util.importProject.ProjectDescriptor;
import com.intellij.ide.util.projectWizard.ModuleBuilder;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.ProjectWizardStepFactory;
import com.intellij.ide.util.projectWizard.importSources.DetectedProjectRoot;
import com.intellij.ide.util.projectWizard.importSources.ProjectFromSourcesBuilder;
import com.intellij.ide.util.projectWizard.importSources.ProjectStructureDetector;
import com.intellij.openapi.module.JavaModuleType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.util.io.FileUtilRt;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * A {@code AvailProjectStructureDetector} is TODO: Document this!
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class AvailProjectStructureDetector
extends ProjectStructureDetector
{
	@Override
	public @NotNull DirectoryProcessingResult detectRoots (
		final @NotNull File dir,
		final @NotNull File[] children,
		final @NotNull File base,
		final @NotNull List<DetectedProjectRoot> result)
	{
		for (File child : children)
		{
			if (FileUtilRt.extensionEquals(child.getName(), "avail"))
			{
				File root;
				for (String pattern :
					new String[]{"src/main/smalltalk", "src/test/smalltalk", "src"}) {
					if ((root = findParentLike(pattern, dir, base)) != null)
					{
						result.add(new AvailDetectedSourceRoot(root));
						return DirectoryProcessingResult.SKIP_CHILDREN;
					}
				}
			}
		}
		return DirectoryProcessingResult.PROCESS_CHILDREN;
	}

	private File findParentLike(String pattern, File dir, File limit) {
		String[] names = pattern.split("/");
		Collections.reverse(Arrays.asList(names));

		while (dir != null && dir.getPath().startsWith(limit.getPath())) {
			if (names[0].equals(dir.getName())) {
				if (checkParents(dir, names)) {
					return dir;
				}
			}

			dir = dir.getParentFile();
		}
		return null;
	}

	private boolean checkParents(File dir, String[] names) {
		for (String name : names) {
			if (dir.getName().equals(name)) {
				dir = dir.getParentFile();
			} else {
				return false;
			}
		}
		return true;
	}

//	@Override
//	public void setupProjectStructure(
//		@NotNull Collection<DetectedProjectRoot> roots,
//		@NotNull ProjectDescriptor projectDescriptor,
//		final @NotNull ProjectFromSourcesBuilder builder)
//	{
//		if (!roots.isEmpty()
//			&& !builder.hasRootsFromOtherDetectors(this))
//		{
//			List<ModuleDescriptor> modules = projectDescriptor.getModules();
//			if (modules.isEmpty())
//			{
//				modules = new ArrayList<>();
//				for (DetectedProjectRoot root : roots)
//				{
//					modules.add(new ModuleDescriptor(
//						new File(builder.getBaseProjectPath()),
//						JavaModuleType.getModuleType(), root)
//					{
//
//						@Override
//						public void updateModuleConfiguration(
//							Module module,
//							ModifiableRootModel rootModel)
//						{
//							super.updateModuleConfiguration(module, rootModel);
//							for (ModuleBuilder moduleBuilder
//								: builder.getContext().getAllBuilders())
//							{
//								if (moduleBuilder instanceof AvailModuleBuilder)
//								{
//									((AvailModuleBuilder) moduleBuilder)
//										.moduleCreated(module);
//									return;
//								}
//							}
//						}
//					});
//				}
//				projectDescriptor.setModules(modules);
//			}
//		}
//	}

	@Override
	public String getDetectorId()
	{
		return "Avail";
	}
}
