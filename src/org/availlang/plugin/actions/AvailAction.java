package org.availlang.plugin.actions;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import org.availlang.plugin.psi.AvailPsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An {@code AvailAction} is an {@link AnAction} specific to {@link
 * AvailPsiFile}s.
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public abstract class AvailAction
extends AnAction
{
	/**
	 * Answer a {@link AvailPsiFile}.
	 *
	 * @param event
	 *        The {@link AnActionEvent}.
	 * @return The file or {@code null} if not applicable.
	 */
	protected @Nullable AvailPsiFile psiFile (final AnActionEvent event)
	{
		final Object o = event.getData(CommonDataKeys.NAVIGATABLE);
		return o instanceof AvailPsiFile ? (AvailPsiFile) o : null;
	}

	/**
	 * Answer a custom menu item name for this {@link AvailAction}.
	 *
	 * @param psiFile
	 *        The {@link AvailPsiFile}.
	 * @return The name if available or an empty String for no custom name.
	 */
	protected @NotNull String customMenuItem (
		final @NotNull AvailPsiFile psiFile)
	{
		return "";
	}

	/**
	 * Answer whether or not the {@link AvailAction} should be visible.
	 *
	 * @param psiFile
	 *        The {@link AvailPsiFile}.
	 * @return {@code true} if it should be visible; {@code false} otherwise.
	 */
	protected boolean customVisibilityCheck (
		final @NotNull AvailPsiFile psiFile)
	{
		return true;
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
				if (customVisibilityCheck(psiFile))
				{
					final String customMenuItem = customMenuItem(psiFile);
					if (!customMenuItem.isEmpty())
					{
						event.getPresentation().setText(customMenuItem);
					}
					event.getPresentation().setEnabledAndVisible(true);
				}
				else
				{
					event.getPresentation().setEnabledAndVisible(false);
				}
			}
		}
	}
}
