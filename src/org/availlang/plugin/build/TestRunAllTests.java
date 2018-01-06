package org.availlang.plugin.build;

import com.avail.builder.AvailBuilder;
import com.avail.builder.ResolvedModuleName;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task.Backgroundable;
import com.intellij.openapi.progress.util.ProgressWindow;
import com.intellij.openapi.project.Project;
import org.availlang.plugin.core.AvailComponent;
import org.availlang.plugin.psi.AvailPsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.Semaphore;

/**
 * TODO: Document this!
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class TestRunAllTests
extends AnAction
{
	private @Nullable
	AvailPsiFile psiFile (final AnActionEvent event)
	{
		final Object o = event.getData(CommonDataKeys.NAVIGATABLE);
		return o instanceof AvailPsiFile ? (AvailPsiFile) o : null;
	}

	@Override
	public void actionPerformed (final AnActionEvent event)
	{
		final ProgressManager manager = ProgressManager.getInstance();
		final AvailBuilder builder = AvailComponent.getInstance().getBuilder();
		manager.runProcessWithProgressAsynchronously(
			new Backgroundable(
				event.getProject(),
				"Running all tests",
				false)
			{
				@Override
				public void run (@NotNull final ProgressIndicator progress)
				{
					final ProgressWindow window = (ProgressWindow) progress;
					window.setTitle("Running all tests");
					window.setIndeterminate(true);
					final Semaphore done = new Semaphore(0);
					builder.attemptCommand(
						"Run all tests",
						(commands, proceed) -> proceed.value(commands.get(0)),
						(result, cleanup) -> cleanup.value(done::release),
						() -> System.err.println("[Failed] Run all tests"));
					done.acquireUninterruptibly();
				}
			},
			new ProgressWindow(true, event.getProject()));
	}

	@Override
	public void update (final AnActionEvent event)
	{
		final Project project = event.getProject();
		if (project != null)
		{
			final AvailPsiFile psiFile = psiFile(event);
			if (psiFile != null)
			{
				event.getPresentation().setText("[Test] Run all tests");
				event.getPresentation().setEnabledAndVisible(true);
			}
		}
	}
}
