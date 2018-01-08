package org.availlang.plugin.build;
import com.avail.builder.AvailBuilder;
import com.avail.builder.ResolvedModuleName;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task.Backgroundable;
import com.intellij.openapi.progress.util.ProgressWindow;
import org.availlang.plugin.actions.AvailAction;
import org.availlang.plugin.core.AvailComponent;
import org.availlang.plugin.language.AvailLanguage;
import org.availlang.plugin.psi.AvailPsiFile;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

/**
 * A {@code BuildModule} is {@link AnAction} that causes an {@link
 * AvailLanguage} to build.
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class BuildModule
extends AvailAction
{
	@Override
	public void actionPerformed (final AnActionEvent event)
	{
		final ProgressManager manager = ProgressManager.getInstance();
		final AvailPsiFile psiFile = psiFile(event);
		assert psiFile != null;
		final ResolvedModuleName resolvedModuleName =
			psiFile.resolvedModuleName();
		if (resolvedModuleName != null)
		{
			build(event, manager, resolvedModuleName);
		}
	}

	/**
	 * Build a {@link ResolvedModuleName}.
	 *
	 * @param event
	 *        The {@link AnActionEvent}.
	 * @param manager
	 *        The {@link ProgressManager}.
	 * @param resolvedModuleName
	 *        The {@code ResolvedModuleName} to build.
	 */
	public static void build (
		final @NotNull AnActionEvent event,
		final @NotNull ProgressManager manager,
		final @NotNull ResolvedModuleName resolvedModuleName)
	{
		final AvailBuilder builder =
			AvailComponent.getInstance().builder();
		final String label = String.format(
			"Building %s",
			resolvedModuleName.qualifiedName());
		manager.runProcessWithProgressAsynchronously(
			new Backgroundable(
				event.getProject(),
				label,
				true)
			{
				@Override
				public void run (@NotNull final ProgressIndicator progress)
				{
					final ProgressWindow window = (ProgressWindow) progress;
					window.setTitle(label);
					builder.buildTarget(
						resolvedModuleName,
						(moduleName, moduleSize, position) -> {},
						(position, globalCodeSize) ->
						{
							assert position != null;
							assert globalCodeSize != null;
							if (window.isCanceled())
							{
								builder.cancel();
							}
							else
							{
								final long perThousand =
									(position * 1000L) / globalCodeSize;
								final double percent =
									perThousand / 1000.0d;
								progress.setText(String.format(
									"%,dB (%3.1f%%)",
									position,
									percent * 100.d));
								progress.setFraction(percent);
							}
						}
					);
				}
			},
			new ProgressWindow(true, event.getProject()));
	}

	@Override
	protected @NotNull String customMenuItem (
		final @Nullable AvailPsiFile psiFile)
	{
		final AvailPsiFile file = psiFile;
		assert file != null;
		return  "Build '"
			+ file.getVirtualFile().getNameWithoutExtension() + "'";
	}

	@Override
	protected boolean customVisibilityCheck (
		final @Nullable AvailPsiFile psiFile)
	{
		// It can only be visible if the file has not yet been loaded.
		final AvailPsiFile file = psiFile;
		assert file != null;
		return !AvailComponent.getInstance().isLoaded(file);
	}
}
