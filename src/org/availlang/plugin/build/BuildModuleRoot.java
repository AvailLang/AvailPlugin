package org.availlang.plugin.build;
import com.avail.builder.ModuleRoot;
import com.avail.builder.ResolvedModuleName;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.progress.ProgressManager;
import org.availlang.plugin.actions.AvailAction;
import org.availlang.plugin.psi.AvailPsiFile;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

/**
 * A {@code BuildModuleRoot} is {@link AnAction} that causes a {@link
 * ModuleRoot} to build.
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class BuildModuleRoot
extends AvailAction
{
	/**
	 * The top leve {@link ModuleRoot}.
	 */
	private final @NotNull ResolvedModuleName resolvedModuleName;

	/**
	 * Construct a {@link BuildModuleRoot}.
	 *
	 * @param resolvedModuleName
	 *        The {@link ResolvedModuleName} of the {@link ModuleRoot} to build.
	 */
	public BuildModuleRoot (
		final @NotNull ResolvedModuleName resolvedModuleName)
	{
		this.resolvedModuleName = resolvedModuleName;
	}

	@Override
	public void actionPerformed (final AnActionEvent event)
	{
		BuildModule.build(
			event, ProgressManager.getInstance(), resolvedModuleName);
	}

	@Override
	protected @NotNull String customMenuItem (
		final @Nullable AvailPsiFile psiFile)
	{
		return  resolvedModuleName.localName();
	}
}
