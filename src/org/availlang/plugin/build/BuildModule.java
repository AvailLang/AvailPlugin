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
import org.availlang.plugin.language.AvailLanguage;
import org.availlang.plugin.psi.AvailPsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A {@code BuildModule} is {@link AnAction} that causes an {@link
 * AvailLanguage} to build.
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class BuildModule
extends AnAction
{
	private @Nullable AvailPsiFile psiFile (final AnActionEvent event)
	{
		final Object o = event.getData(CommonDataKeys.NAVIGATABLE);
		return o instanceof AvailPsiFile ? (AvailPsiFile) o : null;
	}

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
			final AvailBuilder builder =
				AvailComponent.getInstance().getBuilder();
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
				final String option = "Build '"
					+ psiFile.getVirtualFile().getNameWithoutExtension() + "'";
				event.getPresentation().setText(option);
				event.getPresentation().setEnabledAndVisible(true);
			}
		}
	}
}
