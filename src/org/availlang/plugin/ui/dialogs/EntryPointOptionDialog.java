package org.availlang.plugin.ui.dialogs;
import com.avail.builder.AvailBuilder;
import com.avail.builder.AvailBuilder.LoadedModule;
import com.avail.linking.EntryPoint;
import com.intellij.ide.util.ChooseElementsDialog;
import com.intellij.openapi.project.Project;
import org.availlang.plugin.core.AvailComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * An {@code EntryPointOptionDialog} is a {@link ChooseElementsDialog}.
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class EntryPointOptionDialog
extends ChooseElementsDialog<String>
{
	@Override
	protected String getItemText (final String item)
	{
		return item;
	}

	@Override
	protected @Nullable Icon getItemIcon (final String item)
	{
		return null;
	}

	/**
	 * Answer the selected entry point.
	 *
	 * @return A {@code String}.
	 */
	public @NotNull String selectedEntryPoint ()
	{
		final String item = myChooser.getSelectedElement();
		assert item != null;
		return item;
	}

	/**
	 * Construct a {@link EntryPointOptionDialog}.
	 *
	 * @param project
	 *        A {@link Project}.
	 */
	public EntryPointOptionDialog (final @NotNull Project project)
	{
		super(
			project,
			calculateEntryPoints(),
			"Run Entry Point",
			"Run Avail Entry Point",
			true);
		myChooser.setSingleSelectionMode();
	}

	/**
	 * Construct a {@link EntryPointOptionDialog}.
	 *
	 * @param project
	 *        A {@link Project}.
	 * @param entryPoints
	 *        The {@link List} of {@code String} {@link EntryPoint}s to run.
	 */
	public EntryPointOptionDialog (
		final @NotNull Project project,
		final @NotNull List<String> entryPoints)
	{
		super(
			project,
			entryPoints,
			"Run Entry Point",
			"Run Avail Entry Point",
			true);
		myChooser.setSingleSelectionMode();
	}

	/**
	 * Populate the entry points.
	 *
	 * @return A {@code String} {@link List} of available entry points.
	 */
	private static List<String> calculateEntryPoints ()
	{
		final List<String> entryPoints = new ArrayList<>();
		final AvailBuilder builder = AvailComponent.getInstance().builder();
		for (final LoadedModule loadedModule : builder.loadedModulesCopy())
		{
			if (!loadedModule.entryPoints().isEmpty())
			{
				entryPoints.addAll(loadedModule.entryPoints());
			}
		}
		return entryPoints;
	}
}
