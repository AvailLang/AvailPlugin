package org.availlang.plugin.build;

import com.avail.builder.AvailBuilder;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task.Backgroundable;
import com.intellij.openapi.progress.util.ProgressWindow;
import org.availlang.plugin.actions.AvailAction;
import org.availlang.plugin.core.AvailComponent;
import org.availlang.plugin.icons.AvailIcon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.concurrent.Semaphore;

/**
 * TODO: Document this!
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class TestRunAllTests
extends AvailAction
{
	public TestRunAllTests () {}

	public TestRunAllTests (
		final @Nullable  String text,
		final @Nullable  String description,
		final @Nullable  Icon icon)
	{
		super("Test action", "test action", AvailIcon.availDirectoryFileIcon);
	}

	@Override
	public void actionPerformed (final AnActionEvent event)
	{
		final ProgressManager manager = ProgressManager.getInstance();
		final AvailBuilder builder = AvailComponent.getInstance().builder();
		manager.runProcessWithProgressAsynchronously(
			new Backgroundable(
				event.getProject(),
				"Running all tests",
				false)
			{
				@Override
				public void run (final @NotNull ProgressIndicator progress)
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
		super.update(event);
	}

	@Override
	protected @Nullable Icon icon ()
	{
		return AvailIcon.availDirectoryFileIcon;
	}
}
