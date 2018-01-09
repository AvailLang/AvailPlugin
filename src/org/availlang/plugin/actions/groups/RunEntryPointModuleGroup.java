package org.availlang.plugin.actions.groups;
import com.avail.builder.ModuleRoot;
import com.avail.builder.ResolvedModuleName;
import com.avail.linking.EntryPoint;
import org.availlang.plugin.actions.BuildAndRunModuleEntryPoint;
import org.availlang.plugin.actions.DisplayAndBuildModules;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A {@code BuildRootGroup} is an {@link EntryPointModuleGroup} that can run an
 * {@link EntryPoint} for a given module.
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class RunEntryPointModuleGroup
extends EntryPointModuleGroup
{
	@Override
	protected @NotNull DisplayAndBuildModules getDisplayAndBuildModules (
		final @NotNull ModuleRoot root,
		final @NotNull List<ResolvedModuleName> nameList)
	{
		return new BuildAndRunModuleEntryPoint(root.name(), nameList);
	}
}
