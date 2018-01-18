/*
 * AvailIcon.java
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

package org.availlang.plugin.icons;
import com.intellij.openapi.util.IconLoader;
import org.availlang.plugin.exceptions.AvailPluginException;
import org.availlang.plugin.file.AvailDirectoryFileType;
import org.availlang.plugin.file.AvailFileType;
import org.availlang.plugin.file.AvailRepoFileType;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.net.URL;

/**
 * An {@link AvailIcon} is a file that statically makes icons available for
 * Avail related aspects of the Avail plugin.
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public final class AvailIcon
{
	/**
	 * Get the {@link Icon} for the provided base file name.
	 *
	 * @param fileName
	 *        The {@code String} name of the file (without path).
	 * @return An {@code Icon}.
	 */
	private static @NotNull Icon getIcon (final @NotNull String fileName)
	{
		final String path = String.format("/icons/%s", fileName);
		final URL iconUrl = AvailIcon.class.getResource(path);
		final Icon fileIcon = IconLoader.findIcon(iconUrl);
		if (fileIcon == null)
		{
			final AvailPluginException e = new AvailPluginException(
				String.format(
					"(%s): The icon, %s, is not located in the resources' icon "
						+ "folder",
					path,
					fileName));
			e.errorDialog();
			throw e;
		}
		return fileIcon;
	}

	/**
	 * The {@link Icon} used to represent an {@link AvailFileType}.
	 */
	public static final @NotNull Icon availIcon =
		getIcon("AvailBasic.png");

	/**
	 * The {@link Icon} used to represent an {@link AvailFileType}.
	 */
	public static final @NotNull Icon inTree =
		getIcon("ModuleInTree.png");

	/**
	 * The {@link Icon} used to represent an {@link AvailFileType}.
	 */
	public static final @NotNull Icon availFileIcon =
		getIcon("AvailFileType.png");

	/**
	 * The {@link Icon} used to represent an {@link AvailRepoFileType}.
	 */
	public static final @NotNull Icon availRepoFileIcon =
		getIcon("AvailRepoSDK.png");

	/**
	 * The {@link Icon} used to represent an {@link AvailRepoFileType}.
	 */
	public static final @NotNull Icon availModuleIcon =
		getIcon("AvailHammer.png");

	/**
	 * The {@link Icon} used to represent an {@link AvailRepoFileType}.
	 */
	public static final @NotNull Icon availEntryPointIcon =
		getIcon("AvailEntryPoint.png");;

	/**
	 * The {@link Icon} used to represent an {@link AvailDirectoryFileType}.
	 */
	public static final @NotNull Icon availDirectoryFileIcon =
		getIcon("PackageInTree.png");
}
