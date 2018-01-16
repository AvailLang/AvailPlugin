/*
 * AvailVirtualFile.java
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

package org.availlang.plugin.file.vfs;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileSystem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A {@code AvailVirtualFile} is TODO: Document this!
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class AvailVirtualFile
extends VirtualFile
{
	@Override
	public @NotNull String getName ()
	{
		return null;
	}

	@Override
	public @NotNull VirtualFileSystem getFileSystem ()
	{
		return null;
	}

	@Override
	public @NotNull String getPath ()
	{
		return null;
	}

	@Override
	public boolean isWritable ()
	{
		return false;
	}

	@Override
	public boolean isDirectory ()
	{
		return false;
	}

	@Override
	public boolean isValid ()
	{
		return false;
	}

	@Override
	public VirtualFile getParent ()
	{
		return null;
	}

	@Override
	public VirtualFile[] getChildren ()
	{
		return new VirtualFile[0];
	}

	@Override
	public @NotNull OutputStream getOutputStream (
		final Object requestor,
		final long newModificationStamp,
		final long newTimeStamp)
	throws IOException
	{
		return null;
	}

	@Override
	public @NotNull byte[] contentsToByteArray () throws IOException
	{
		return new byte[0];
	}

	@Override
	public long getTimeStamp ()
	{
		return 0;
	}

	@Override
	public long getLength ()
	{
		return 0;
	}

	@Override
	public void refresh (
		final boolean asynchronous,
		final boolean recursive,
		final @Nullable Runnable postRunnable)
	{

	}

	@Override
	public InputStream getInputStream () throws IOException
	{
		return null;
	}
}
