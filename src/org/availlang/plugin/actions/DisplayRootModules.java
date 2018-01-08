package org.availlang.plugin.actions;
import com.avail.builder.ModuleRoot;
import com.avail.builder.ResolvedModuleName;
import com.intellij.ide.IdeView;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.availlang.plugin.build.BuildModule;
import org.availlang.plugin.core.AvailComponent;
import org.availlang.plugin.dialogs.ResolvedModuleNameOptionDialog;
import org.availlang.plugin.psi.AvailPsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * TODO: Document this!
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class DisplayRootModules
extends AvailAction
{
	/**
	 * The {@link ModuleRoot} that contains the top level Avail {@link
	 * ResolvedModuleName}s to build.
	 */
	private final @NotNull ModuleRoot moduleRoot;

	/**
	 * The {@link List} of top-level {@link ResolvedModuleName}s that can be
	 * built.
	 */
	private final @NotNull List<ResolvedModuleName> names;

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
		final ResolvedModuleNameOptionDialog dialog =
			new ResolvedModuleNameOptionDialog(project, names);
		dialog.setTitle("Choose module(s) to build");
		dialog.show();

		if (dialog.getExitCode() == DialogWrapper.OK_EXIT_CODE)
		{
			final List<ResolvedModuleName> toBuildList =
				dialog.getChosenElements();
			for (final ResolvedModuleName name : toBuildList)
			{
				BuildModule.build(event, manager, name);
			}
		}
	}

	@Override
	protected @NotNull String customMenuItem (
		final @Nullable AvailPsiFile psiFile)
	{
		return moduleRoot.name();
	}

	/**
	 * Construct a {@link DisplayRootModules}.
	 *
	 * @param moduleRoot
	 *        The {@link ModuleRoot} this is for.
	 */
	public DisplayRootModules (final @NotNull ModuleRoot moduleRoot)
	{
		this.moduleRoot = moduleRoot;
		this.names =
			AvailComponent.getInstance().topLevelResolvedNames(moduleRoot);
	}

	@Override
	public void update (final AnActionEvent event)
	{
		super.update(event);
	}
}
