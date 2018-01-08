package org.availlang.plugin.dialogs;
import com.avail.builder.ResolvedModuleName;
import com.intellij.ide.util.ChooseElementsDialog;
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
