/*
 * AvailDirectoryFileType.java
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
package org.availlang.plugin.file;
import com.intellij.openapi.fileTypes.DirectoryFileType;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.vfs.VirtualFile;
import org.availlang.plugin.icons.AvailIcon;
import org.availlang.plugin.language.AvailLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * An {@code AvailDirectoryFileType} is a {@link DirectoryFileType} specific to
 * Avail directories.
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class AvailDirectoryFileType
implements DirectoryFileType
{
	/**
	 * The sole {@link AvailDirectoryFileType}.
	 */
	public static final @NotNull AvailDirectoryFileType soleInstance =
		new AvailDirectoryFileType();


	/**
	 * Construct an {@link AvailDirectoryFileType}.
	 */
	private AvailDirectoryFileType () {}

	@Override
	public @NotNull String getName ()
	{
		return "Avail directory";
	}

	@Override
	public @NotNull String getDescription ()
	{
		return "Avail directory";
	}

	@Override
	public @NotNull String getDefaultExtension ()
	{
		return "avail";
	}

	@Override
	public @Nullable Icon getIcon ()
	{
		return AvailIcon.availDirectoryFileIcon;
	}

	@Override
	public boolean isReadOnly ()
	{
		return false;
	}

	@Override
	public boolean isBinary ()
	{
		return false;
	}

	@Override
	public @Nullable String getCharset (
		final @NotNull VirtualFile file,
		final @NotNull byte[] content)
	{
		return null;
	}
}
