package org.availlang.plugin.actions;
import com.avail.builder.ResolvedModuleName;
import com.avail.utility.evaluation.Continuation0;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.availlang.plugin.build.BuildModule;
import org.availlang.plugin.core.AvailComponent;
import org.availlang.plugin.psi.AvailPsiFile;
import org.availlang.plugin.ui.dialogs.ResolvedModuleNameOptionDialog;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;

/**
 * A {@code DisplayAndBuildModules} is an {@link AvailAction} that is menu
 * driven, displaying an option, when chosen, provides a list of modules that
 * can be built.
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class DisplayAndBuildModules
extends AvailAction
{
	/**
	 * The {@code String} to display in the initial menu.
	 */
	private final @NotNull String menuItemText;

	/**
	 * The {@link List} of top-level {@link ResolvedModuleName}s that can be
	 * built.
	 */
	private final @NotNull List<ResolvedModuleName> names;

	/**
	 * Is the module selection restricted to single select?
	 *
	 * @return  {@code true} indicates it is; {@code false} otherwise.
	 */
	protected boolean isSingleSelect ()
	{
		return false;
	}

	/**
	 * Answer the title for the {@linkplain ResolvedModuleNameOptionDialog}.
	 *
	 * @return A {@code String}.
	 */
	protected @NotNull String moduleSelectDialogTitle ()
	{
		return "Choose Modules to Build";
	}

	@Override
	public void actionPerformed (final AnActionEvent event)
	{
		final Project project = getProject(event);
		final ProgressManager manager = ProgressManager.getInstance();
		final ResolvedModuleNameOptionDialog dialog =
			resolvedModuleNameOptionDialog(project);
		dialog.show();

		if (dialog.getExitCode() == DialogWrapper.OK_EXIT_CODE)
		{
			firstThen(
				project,
				dialog,
				() ->
				{
					final Iterator<ResolvedModuleName> toBuildList =
						dialog.getChosenElements().iterator();
					onSuccess(toBuildList, project, manager, event);
				});
		}
	}

	/**
	 * Run the body of this method then run the {@link Continuation0}.
	 *
	 * @param project
	 *        The current {@link Project}.
	 * @param dialog
	 *        The ResolvedModuleNameOptionDialog with the selected modules
	 *        to build.
	 * @param next
	 *        The {@code Continuation0} to run at the conclusion of this
	 *        method.
	 */
	protected void firstThen (
		final @NotNull Project project,
		final @NotNull ResolvedModuleNameOptionDialog dialog,
		final @NotNull Continuation0 next)
	{
		next.value();
	}

	private @NotNull
	ResolvedModuleNameOptionDialog resolvedModuleNameOptionDialog (
		final @NotNull Project project)
	{
		final ResolvedModuleNameOptionDialog dialog =
			new ResolvedModuleNameOptionDialog(project, names);
		dialog.setTitle(moduleSelectDialogTitle());
		if (isSingleSelect())
		{
			dialog.setSingleSelect();
		}
		return dialog;
	}

	/**
	 * The action to perform upon successful completion of {@linkplain
	 * BuildModule#build(AnActionEvent, ProgressManager, ResolvedModuleName)
	 * building a module}.
	 *
	 * @param toBuildList
	 *        The {@link Iterator} of {@link ResolvedModuleName}s to build.
	 * @param project
	 *        The {@linkplain Project}.
	 * @param manager
	 *        The {@link ProgressManager}.
	 * @param event
	 *        The {@link AnActionEvent} that triggered the {@link
	 *        DisplayAndBuildModules}.
	 */
	private void onSuccess (
		final @NotNull Iterator<ResolvedModuleName> toBuildList,
		final @NotNull Project project,
		final @NotNull ProgressManager manager,
		final @NotNull AnActionEvent event)
	{
		if (toBuildList.hasNext())
		{
			final ResolvedModuleName name = toBuildList.next();
			final AvailComponent component = AvailComponent.getInstance();
			if (component.builder().getLoadedModule(name) == null)
			{
				BuildModule.build(
					event,
					manager,
					name,
					() -> onSuccess(
						toBuildList, project, manager, event));
			}
			else
			{
				onSuccess(toBuildList, project, manager, event);
			}
		}
		else
		{
			done(project, manager, event);
		}
	}

	/**
	 * Action to perform when all selected Avail modules loaded.
	 *
	 * @param project
	 *        The {@linkplain Project}.
	 * @param manager
	 *        The {@link ProgressManager}.
	 * @param event
	 *        The {@link AnActionEvent} that triggered the {@link
	 *        DisplayAndBuildModules}.
	 */
	protected void done (
		final @NotNull Project project,
		final @NotNull ProgressManager manager,
		final @NotNull AnActionEvent event)
	{
		// No implementation
	}

	@Override
	protected @NotNull String customMenuItem (
		final @Nullable AvailPsiFile psiFile)
	{
		return menuItemText;
	}

	/**
	 * Construct a {@link DisplayAndBuildModules}.
	 *
	 * @param menuItemText
	 *        The {@code String} to display in the initial menu.
	 * @param names
	 *        The {@link List} of top-level {@link ResolvedModuleName}s that can
	 *        be built.
	 */
	public DisplayAndBuildModules (
		final @NotNull String menuItemText,
		final @NotNull List<ResolvedModuleName> names)
	{
		this.menuItemText = menuItemText;
		this.names = names;
	}
}
