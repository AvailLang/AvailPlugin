package org.availlang.plugin.actions.groups;
import com.avail.builder.ModuleRoot;
import com.avail.builder.ModuleRoots;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.availlang.plugin.actions.DisplayRootModules;
import org.availlang.plugin.core.AvailComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A {@code BuildRootGroup} is an {@link ActionGroup} used to provide an array
 * of {@link RootModulesGroup}s.
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class BuildRootGroup
extends ActionGroup
{
	@Override
	public @NotNull AnAction[] getChildren (final @Nullable AnActionEvent e)
	{
		final AvailComponent component = AvailComponent.getInstance();
		component.refresh();
		final ModuleRoots roots =
			component.moduleRoots();
		final DisplayRootModules[] displayRootModules =
			new DisplayRootModules[roots.roots().size()];
		int index = 0;
		for (final ModuleRoot root : roots.roots())
		{
			displayRootModules[index++] = new DisplayRootModules(root);
		}
		return displayRootModules;
	}

	@Override
	public void update (final AnActionEvent e)
	{
		e.getPresentation().setEnabledAndVisible(true);
	}
}
