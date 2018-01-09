package org.availlang.plugin.actions;
import com.avail.builder.AvailBuilder;
import com.avail.builder.ResolvedModuleName;
import com.avail.linking.EntryPoint;
import com.avail.utility.evaluation.Continuation0;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.availlang.plugin.core.AvailComponent;
import org.availlang.plugin.ui.dialogs.EntryPointOptionDialog;
import org.availlang.plugin.ui.dialogs.MessageDialog;
import org.availlang.plugin.ui.dialogs.ResolvedModuleNameOptionDialog;
import org.availlang.plugin.ui.dialogs.TextInputDialog;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A {@code BuildAndRunModuleEntryPoint} is a {@link DisplayAndBuildModules}
 * that allows for selecting an {@link EntryPoint} to run on an Avail module,
 * building the module first if necessary.
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class BuildAndRunModuleEntryPoint
extends DisplayAndBuildModules
{
	@Override
	protected void done (
		final @NotNull Project project,
		final @NotNull ProgressManager manager,
		final @NotNull AnActionEvent event)
	{
		final AvailBuilder builder = AvailComponent.getInstance().builder();
		RunEntryPoint.
			runEntryPoint(event, manager, builder, entryPointCommand);
	}

	@Override
	protected boolean isSingleSelect ()
	{
		return true;
	}

	@Override
	protected @NotNull String moduleSelectDialogTitle ()
	{
		return "Choose Module to Run Entry Point";
	}

	/**
	 * The {@code String} entry point command to run.
	 */
	private @NotNull String entryPointCommand = "";

	@Override
	protected void firstThen (
		final @NotNull Project project,
		final @NotNull ResolvedModuleNameOptionDialog dialog,
		final @NotNull Continuation0 next)
	{
		final ResolvedModuleName name = dialog.getSelection();
		assert name != null;
		final AvailComponent component = AvailComponent.getInstance();
		final List<String> entryPoints =
			component.entryPoints(name);
		if (entryPoints.isEmpty())
		{
			new MessageDialog(
					"ERROR",
					String.format(
						"No entry points available on %s", name.localName()))
				.show();
		}
		else
		{
			final EntryPointOptionDialog epd =
				RunEntryPoint.entryPointOptionDialog(
					project,
					name,
					entryPoints);
			epd.show();
			if (epd.getExitCode() == DialogWrapper.OK_EXIT_CODE)
			{
				final String entryPoint = epd.selectedEntryPoint();
				final TextInputDialog textDialog =
					new TextInputDialog(
						project,
						"",
						"Run Entry Point",
						entryPoint);
				textDialog.show();
				if (textDialog.getExitCode() == DialogWrapper.OK_EXIT_CODE)
				{
					final String command = textDialog.getInputString();
					if (command != null && !command.isEmpty())
					{
						entryPointCommand = command;
						next.value();
					}
					else
					{
						new MessageDialog(
								"ERROR","No entry point entered")
							.show();
					}
				}
			}
		}
	}

	/**
	 * Construct a {@link BuildAndRunModuleEntryPoint}.
	 *
	 * @param menuItemText
	 *        The {@code String} to display in the initial menu.
	 * @param names
	 *        The {@link List} of top-level {@link ResolvedModuleName}s that can
	 *        be built.
	 */
	public BuildAndRunModuleEntryPoint (
		final @NotNull String menuItemText,
		final @NotNull List<ResolvedModuleName> names)
	{
		super(menuItemText, names);
	}
}
