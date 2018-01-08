package org.availlang.plugin.dialogs;
import com.intellij.ide.util.ChooseElementsDialog;
import com.intellij.openapi.project.Project;
import org.availlang.plugin.core.utility.ModuleEntryPoints;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;

/**
 * An {@code ModuleEntryPointsOptionDialog} is a {@link ChooseElementsDialog}
 * used to choose
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class ModuleEntryPointsOptionDialog
extends ChooseElementsDialog<ModuleEntryPoints>
{
	@Override
	protected String getItemText (final ModuleEntryPoints item)
	{
		return item.resolvedModuleName().localName();
	}

	@Override
	protected @Nullable Icon getItemIcon (final ModuleEntryPoints item)
	{
		return null;
	}

	/**
	 * Construct a {@link ModuleEntryPointsOptionDialog}.
	 *
	 * @param project
	 *        A {@link Project}.
	 */
	public ModuleEntryPointsOptionDialog (
		final @NotNull Project project,
		final List<ModuleEntryPoints> resolvedModuleNames)
	{
		super(
			project,
			resolvedModuleNames,
			"Select Modules to build",
			"Build selected modules",
			true);

	}
}
