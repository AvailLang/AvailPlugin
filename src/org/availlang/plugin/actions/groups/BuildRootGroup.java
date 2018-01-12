/*
 * BuildRootGroup.java
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
package org.availlang.plugin.actions.groups;
import com.avail.builder.ModuleRoot;
import com.avail.builder.ModuleRoots;
import com.avail.utility.Nulls;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.availlang.plugin.actions.DisplayAndBuildModules;
import org.availlang.plugin.core.AvailComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A {@code BuildRootGroup} is an {@link ActionGroup} used to provide an array
 * of {@link ModuleRoot}s.
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class BuildRootGroup
extends ActionGroup
{
	@Override
	public @NotNull AnAction[] getChildren (final @Nullable AnActionEvent e)
	{
		final AvailComponent component =
			AvailComponent.getInstance(Nulls.stripNull(e.getProject()));
		component.refresh();
		final ModuleRoots roots = component.moduleRoots();
		final DisplayAndBuildModules[] displayAndBuildModules =
			new DisplayAndBuildModules[roots.roots().size()];
		int index = 0;
		for (final ModuleRoot root : roots.roots())
		{
			displayAndBuildModules[index++] =
				new DisplayAndBuildModules(
					root.name(), component.topLevelResolvedNames(root));
		}
		return displayAndBuildModules;
	}

	@Override
	public void update (final AnActionEvent e)
	{
		e.getPresentation().setEnabledAndVisible(true);
	}
}
