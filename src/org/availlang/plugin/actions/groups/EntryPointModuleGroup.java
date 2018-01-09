package org.availlang.plugin.actions.groups;
import com.avail.builder.ModuleRoot;
import com.avail.builder.ModuleRoots;
import com.avail.builder.ResolvedModuleName;
import com.avail.linking.EntryPoint;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.availlang.plugin.actions.DisplayAndBuildModules;
import org.availlang.plugin.core.AvailComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@code BuildRootGroup} is an {@link ActionGroup} used to provide an array
 * of Avail modules in a given {@link ModuleRoot}, that have {@link
 * EntryPoint}s, that can be chosen to be built.
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class EntryPointModuleGroup
extends ActionGroup
{
	@Override
	public @NotNull AnAction[] getChildren (final @Nullable AnActionEvent e)
	{
		final AvailComponent component = AvailComponent.getInstance();
		component.refresh();
		final ModuleRoots roots =
			component.moduleRoots();
		final DisplayAndBuildModules[] displayAndBuildModules =
			new DisplayAndBuildModules[roots.roots().size()];
		int index = 0;
		for (final ModuleRoot root : roots.roots())
		{
			final List<ResolvedModuleName> nameList = new ArrayList<>();
			component.moduleEntryPoints(root.name())
				.forEach(mep -> nameList.add(mep.resolvedModuleName()));
			displayAndBuildModules[index++] =
				getDisplayAndBuildModules(root, nameList);
		}
		return displayAndBuildModules;
	}

	/**
	 * Answer a {@link DisplayAndBuildModules}.
	 *
	 * @param root
	 *        The {@link ModuleRoot}.
	 * @param nameList
	 *        The {@link List} of {@link ResolvedModuleName}s available from the
	 *        root that have {@link EntryPoint}s.
	 * @return A {@code DisplayAndBuildModules}.
	 */
	protected @NotNull DisplayAndBuildModules getDisplayAndBuildModules (
		final @NotNull ModuleRoot root,
		final @NotNull List<ResolvedModuleName> nameList)
	{
		return new DisplayAndBuildModules(root.name(), nameList);
	}

	@Override
	public void update (final AnActionEvent e)
	{
		e.getPresentation().setEnabledAndVisible(true);
	}
}
