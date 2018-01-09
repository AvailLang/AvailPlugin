package org.availlang.plugin.ui.dialogs;
import com.avail.builder.ResolvedModuleName;
import com.intellij.ide.util.ChooseElementsDialog;
import com.intellij.ide.util.ElementsChooser;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;

/**
 * An {@code EntryPointOptionDialog} is a {@link ChooseElementsDialog}.
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class ResolvedModuleNameOptionDialog
	extends ChooseElementsDialog<ResolvedModuleName>
{
	@Override
	protected String getItemText (final ResolvedModuleName item)
	{
		return item.localName();
	}

	@Override
	protected @Nullable Icon getItemIcon (final ResolvedModuleName item)
	{
		return null;
	}

	/**
	 * Set this {@link ResolvedModuleNameOptionDialog} to only {@linkplain
	 * ElementsChooser#setSingleSelectionMode() select one} module.
	 */
	public void setSingleSelect ()
	{
		myChooser.setSingleSelectionMode();
	}

	/**
	 * Answer the selected {@link ResolvedModuleName}.
	 *
	 * @return A {@code ResolvedModuleName} or {@code null} if nothing selected.
	 */
	public @Nullable ResolvedModuleName getSelection ()
	{
		return myChooser.getSelectedElement();
	}

	/**
	 * Construct a {@link ResolvedModuleNameOptionDialog}.
	 *
	 * @param project
	 *        A {@link Project}.
	 */
	public ResolvedModuleNameOptionDialog (
		final @NotNull Project project,
		final List<ResolvedModuleName> resolvedModuleNames)
	{
		super(
			project,
			resolvedModuleNames,
			"Select Modules to build",
			"Build selected modules",
			true);

	}



	@Override
	protected JComponent createCenterPanel ()
	{
		return super.createCenterPanel();
	}
}
