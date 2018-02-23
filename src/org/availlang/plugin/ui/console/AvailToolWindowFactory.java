/*
 * AvailToolWindowFactory.java
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

package org.availlang.plugin.ui.console;
import com.avail.utility.Nulls;
import com.intellij.execution.impl.ConsoleViewImpl;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.execution.ui.RunnerLayoutUi;
import com.intellij.execution.ui.layout.PlaceInGrid;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.content.ContentManager;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.availlang.plugin.core.AvailComponent;
import org.availlang.plugin.process.AvailProcessHandler;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

/**
 * A {@code AvailToolWindowFactory} is a {@link ToolWindowFactory} used to
 * create a {@link ConsoleView} for the running Avail plugin.
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class AvailToolWindowFactory
implements ToolWindowFactory
{
	/**
	 * The unique String id of an {@link AvailToolWindowFactory}.
	 */
	public static final @NotNull String consoleId = "Avail Console";

	/**
	 * The actual {@link ToolWindow} created by this {@link
	 * AvailToolWindowFactory}.
	 */
	private ToolWindow toolWindow;

	/**
	 * The {@link Project} holding on to the {@link ToolWindow}.
	 */
	private Project myProject;

	/**
	 * The {@link Content} pane that is the console.
	 */
	private Content content;

	/**
	 * The {@link ConsoleViewImpl} that is the {@link JPanel} of the console.
	 */
	private AvailConsole consoleView;

	@Override
	public void createToolWindowContent (
		final @NotNull Project project, 
		final @NotNull ToolWindow toolWindow)
	{
		final AvailComponent component =
			project.getComponent(AvailComponent.class);
		this.toolWindow = toolWindow;
		this.myProject = project;
		final AvailConsoleState state = new AvailConsoleState(component);
		this.consoleView = new AvailConsole(project, false, state, true);
		ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
		this.content = contentFactory.createContent(consoleView, "", false);
		toolWindow.getContentManager().addContent(content);
		consoleView.getComponent();
		Nulls.stripNull(component).setConsoleView(consoleView);
	}
}
