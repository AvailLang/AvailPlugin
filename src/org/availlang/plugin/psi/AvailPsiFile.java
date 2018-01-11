/*
 * AvailPsiFile.java
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
package org.availlang.plugin.psi;
import com.avail.builder.ModuleName;
import com.avail.builder.ModuleNameResolver;
import com.avail.builder.ModuleRoot;
import com.avail.builder.ModuleRoots;
import com.avail.builder.ResolvedModuleName;
import com.avail.builder.UnresolvedDependencyException;
import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.availlang.plugin.core.AvailComponent;
import org.availlang.plugin.file.AvailFileType;
import org.availlang.plugin.language.AvailLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.File;

/**
 * An {@code AvailPsiFile} is a {@link PsiFileBase}.
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class AvailPsiFile
extends PsiFileBase
{
	@Override
	public @NotNull FileType getFileType ()
	{
		return AvailFileType.soleInstance;
	}

	/**
	 * Construct a new {@link AvailPsiFile}.
	 *
	 * @param viewProvider
	 *        The {@link FileViewProvider}.
	 */
	public AvailPsiFile (final @NotNull FileViewProvider viewProvider)
	{
		super(viewProvider, AvailLanguage.soleInstance);
	}

	private @Nullable ResolvedModuleName resolvedModuleName;

	/**
	 * Answer the {@link ResolvedModuleName} that corresponds to this
	 * {@link AvailPsiFile}.
	 *
	 * @return The resolved module name, or {@code null} if module name
	 *         resolution failed.
	 */
	public @Nullable ResolvedModuleName resolvedModuleName ()
	{
		if (resolvedModuleName == null)
		{
			final String path = getVirtualFile().getPath();
			final AvailComponent component = AvailComponent.getInstance();
			final ModuleNameResolver resolver =
				component.resolver();
			final ModuleRoots roots = component.moduleRoots();
			for (final ModuleRoot root : roots.roots())
			{
				final File sourceDirectory = root.sourceDirectory();
				if (sourceDirectory != null)
				{
					final String sourceDirectoryPath =
						sourceDirectory.getPath();
					if (path.startsWith(sourceDirectoryPath))
					{
						final String fullyQualifiedName = String.format(
							"/%s/%s",
							root.name(),
							path
								.substring(sourceDirectoryPath.length() + 1)
								.replace(".avail", ""));
						final ModuleName moduleName =
							new ModuleName(fullyQualifiedName);
						try
						{
							resolvedModuleName =
								resolver.resolve(moduleName, null);
						}
						catch (final UnresolvedDependencyException e)
						{
							// Nothing.
						}
						break;
					}
				}
			}
		}
		return resolvedModuleName;
	}

	@Override
	public String toString ()
	{
		return "Avail File";
	}

	@Override
	public Icon getIcon (int flags)
	{
		return super.getIcon(flags);
	}
}
