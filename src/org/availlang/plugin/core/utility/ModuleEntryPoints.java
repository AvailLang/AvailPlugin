/*
 * ModuleEntryPoints.java
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
package org.availlang.plugin.core.utility;
import com.avail.builder.ResolvedModuleName;
import com.avail.linking.EntryPoint;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A {@link ModuleEntryPoints} holds on to a {@link ResolvedModuleName} and the
 * {@linkplain EntryPoint#methodName name} of any {@link EntryPoint} contained
 * in the module represented by the {@code ResolvedModuleName}.
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class ModuleEntryPoints
{
	/**
	 * The {@link List} of {@linkplain EntryPoint#methodName}s.
	 */
	private final @NotNull List<String> entryPoints;

	/**
	 * Answer the {@link List} of {@linkplain EntryPoint#methodName}s.
	 *
	 * @return A list.
	 */
	public @NotNull List<String> entryPoints ()
	{
		return entryPoints;
	}

	/**
	 * The corresponding {@link ResolvedModuleName}.
	 */
	private final @NotNull ResolvedModuleName resolvedModuleName;

	/**
	 * Answer the corresponding {@link ResolvedModuleName}.
	 *
	 * @return A {@code ResolvedModuleName}.
	 */
	public @NotNull ResolvedModuleName resolvedModuleName ()
	{
		return resolvedModuleName;
	}

	/**
	 * Construct a {@link ModuleEntryPoints}.
	 *
	 * @param entryPoints
	 *        The {@link List} of {@linkplain EntryPoint#methodName}s.
	 * @param resolvedModuleName
	 *        The corresponding {@link ResolvedModuleName}.
	 */
	public ModuleEntryPoints (
		final @NotNull List<String> entryPoints,
		final @NotNull ResolvedModuleName resolvedModuleName)
	{
		this.entryPoints = entryPoints;
		this.resolvedModuleName = resolvedModuleName;
	}
}
