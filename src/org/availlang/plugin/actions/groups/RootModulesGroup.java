package org.availlang.plugin.actions.groups;
import com.avail.builder.ModuleRoot;
import com.avail.builder.ResolvedModuleName;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.availlang.plugin.build.BuildModuleRoot;
import org.availlang.plugin.core.AvailComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * A {@code RootModulesGroup} is an {@link ActionGroup} that provides {@link
 * BuildRootGroup}s for the top level Avail {@link ResolvedModuleName}s of a
 * {@link ModuleRoot}.
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class RootModulesGroup
extends ActionGroup
{
	/**
	 * The {@link ModuleRoot} that contains the top level Avail {@link
	 * ResolvedModuleName}s to build.
	 */
	private final @NotNull ModuleRoot moduleRoot;

	/**
	 * The array of {@link BuildModuleRoot}.
	 */
	private final @NotNull BuildModuleRoot[] buildModuleRoots;

	@Override
	public @NotNull AnAction[] getChildren (@Nullable final AnActionEvent e)
	{
		return buildModuleRoots;
	}

	@Override
	public void update (final AnActionEvent e)
	{
		e.getPresentation().setText(moduleRoot.name());
		e.getPresentation().setEnabledAndVisible(true);
	}

	/**
	 * Construct a {@link RootModulesGroup}.
	 *
	 * @param moduleRoot
	 *        The {@link ModuleRoot} that contains the top level Avail {@link
	 *        ResolvedModuleName}s to build.
	 */
	public RootModulesGroup (final @NotNull ModuleRoot moduleRoot)
	{
		this.moduleRoot = moduleRoot;
		final List<ResolvedModuleName> names =
			AvailComponent.getInstance().topLevelResolvedNames(moduleRoot);
		final int count = names.size();
		this.buildModuleRoots = new BuildModuleRoot[count];
		for (int i = 0; i < count; i++)
		{
			buildModuleRoots[i] = new BuildModuleRoot(names.get(i));
		}
	}
}
