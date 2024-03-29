/*
 * BuildModuleRoot.java
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
package org.availlang.plugin.build;
import com.avail.builder.ModuleRoot;
import com.avail.builder.ResolvedModuleName;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.progress.ProgressManager;
import org.availlang.plugin.actions.AvailAction;
import org.availlang.plugin.file.psi.AvailPsiFile;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

/**
 * A {@code BuildModuleRoot} is {@link AnAction} that causes a {@link
 * ModuleRoot} to build.
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class BuildModuleRoot
extends AvailAction
{
	/**
	 * The top leve {@link ModuleRoot}.
	 */
	private final @NotNull ResolvedModuleName resolvedModuleName;

	/**
	 * Construct a {@link BuildModuleRoot}.
	 *
	 * @param resolvedModuleName
	 *        The {@link ResolvedModuleName} of the {@link ModuleRoot} to build.
	 */
	public BuildModuleRoot (
		final @NotNull ResolvedModuleName resolvedModuleName)
	{
		this.resolvedModuleName = resolvedModuleName;
	}

	@Override
	public void actionPerformed (final AnActionEvent event)
	{
		BuildModule.build(
			false, false,
			getAvailComponent(event),
			ProgressManager.getInstance(),
			resolvedModuleName);
	}

	@Override
	protected @NotNull String customMenuItem (
		final @Nullable AvailPsiFile psiFile)
	{
		return  resolvedModuleName.localName();
	}
}
