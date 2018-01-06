package org.availlang.plugin.actions;

import com.avail.builder.AvailBuilder;
import com.avail.builder.AvailBuilder.LoadedModule;
import com.intellij.ide.IdeView;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task.Backgroundable;
import com.intellij.openapi.progress.util.ProgressWindow;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.availlang.plugin.core.AvailComponent;
import org.availlang.plugin.dialogs.TextInputDialog;
import org.availlang.plugin.psi.AvailPsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.Semaphore;

/**
 * A {@code RunEntryPoint} is a {@link AnAction} that allows you to run an
 * entry
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class RunEntryPoint
extends AvailAction
{
	private List<String> entryPoints = Collections.emptyList();

	@Override
	public void actionPerformed (final AnActionEvent event)
	{
		DataContext dataContext = event.getDataContext();

		IdeView view = LangDataKeys.IDE_VIEW.getData(dataContext);
		if (view == null)
		{
			return;
		}
		final Project project = event.getProject();
		final ProgressManager manager = ProgressManager.getInstance();
		final AvailBuilder builder = AvailComponent.getInstance().getBuilder();
		final StringJoiner sj = new StringJoiner("\n");
		sj.add("Enter one of the following entry points:");
		entryPoints.forEach(sj::add);
		final TextInputDialog dialog =
			new TextInputDialog(
				project,
				sj.toString(),
				"Entry point to run",
				null);
		dialog.show();

		if (dialog.getExitCode() == DialogWrapper.OK_EXIT_CODE)
		{
			final String command = dialog.getInputString();
			if (command != null)
			{
				manager.runProcessWithProgressAsynchronously(
					new Backgroundable(
						event.getProject(),
						"Running: " + command,
						false)
					{
						@Override
						public void run (
							final @NotNull ProgressIndicator progress)
						{
							final ProgressWindow window =
								(ProgressWindow) progress;
							window.setTitle("Running: " + command);
							window.setIndeterminate(true);
							final Semaphore done = new Semaphore(0);
							builder.attemptCommand(
								command,
								(commands, proceed) ->
									proceed.value(commands.get(
									0)),
								(result, cleanup) ->
									cleanup.value(done::release),
								() -> System.err.println("[Failed] Run: "
									+ command));
							done.acquireUninterruptibly();
						}
					},
					new ProgressWindow(true, event.getProject()));
			}
		}
	}

	@Override
	protected boolean customVisibilityCheck (
		final @NotNull AvailPsiFile psiFile)
	{
		final LoadedModule loadedModule =
			AvailComponent.getInstance().loadedModule(psiFile);
		if (loadedModule == null)
		{
			return false;
		}
		this.entryPoints = loadedModule.entryPoints();
		return !entryPoints.isEmpty();
	}
}
