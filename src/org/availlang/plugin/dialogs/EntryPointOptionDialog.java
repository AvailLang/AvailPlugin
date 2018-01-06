package org.availlang.plugin.dialogs;
import com.avail.builder.AvailBuilder;
import com.avail.builder.AvailBuilder.LoadedModule;
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
		final String item = getChosenElements().iterator().next();
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

	}

	/**
	 * Populate the entry points.
	 *
	 * @return A {@code String} {@link List} of available entry points.
	 */
	private static List<String> calculateEntryPoints ()
	{
		final List<String> entryPoints = new ArrayList<>();
		final AvailBuilder builder = AvailComponent.getInstance().getBuilder();
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
