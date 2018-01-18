/*
 * EntryPointExecutor.java
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

package org.availlang.plugin.execution;

import com.intellij.execution.ExecutionBundle;
import com.intellij.execution.Executor;
import com.intellij.execution.ExecutorRegistry;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.wm.ToolWindowId;
import com.intellij.ui.UIBundle;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 *
 */
public class EntryPointExecutor
extends Executor
{
  public static final @NotNull String executorId = "avail.console.entrypoint";

  @Override
  @NotNull
  public String getStartActionText()
  {
    return "Run";
  }

  @Override
  public String getToolWindowId()
  {
    return executorId;
  }

  @Override
  public Icon getToolWindowIcon()
  {
    return AllIcons.Toolwindows.ToolWindowRun;
  }

  @Override
  @NotNull
  public Icon getIcon()
  {
    return AllIcons.Actions.Execute;
  }

  @Override
  public Icon getDisabledIcon()
  {
    return AllIcons.Process.DisabledRun;
  }

  @Override
  public String getDescription()
  {
    return "Run selected entry point";
  }

  @Override
  public @NotNull String getActionName()
  {
    return "Run";
  }

  @Override
  public @NotNull String getId()
  {
    return executorId;
  }

  @Override
  public String getContextActionId()
  {
    return "RunEntryPoint";
  }

  @Override
  public String getHelpId()
  {
    return "ideaInterface.run";
  }
}
