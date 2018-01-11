/*
 * AvailModuleType.java
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

import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.ModuleTypeManager;
import org.availlang.plugin.icons.AvailIcon;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * An {@link AvailModuleType} is a {@link ModuleType} specific to Avail.
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class AvailModuleType extends ModuleType<AvailModuleBuilder>
{
	/**
	 * The identifier for the {@link AvailModuleType}.
	 */
	private static final String ID = "AVAIL_MODULE_TYPE";

	/**
	 * Answer an instance of {@link AvailModuleType} from the {@link
	 * ModuleTypeManager}.
	 *
	 * @return A {@code AvailModuleType}.
	 */
	public static AvailModuleType getInstance ()
	{
		return (AvailModuleType) ModuleTypeManager.getInstance().findByID(ID);
	}

	@Override
	public @NotNull AvailModuleBuilder createModuleBuilder ()
	{
		return new AvailModuleBuilder();
	}

	@Override
	public @NotNull String getName ()
	{
		return "Avail";
	}

	@Override
	public @NotNull String getDescription ()
	{
		return "An Avail project";
	}

	@Override
	public Icon getNodeIcon (final boolean isOpened)
	{
		return AvailIcon.availRepoFileIcon;
	}

	/**
	 * Construct a {@link AvailModuleType}.
	 */
	public AvailModuleType ()
	{
		super(ID);
	}
}
