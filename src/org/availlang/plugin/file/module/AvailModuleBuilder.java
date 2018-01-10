package org.availlang.plugin.file.module;
import com.intellij.ide.util.projectWizard.ModuleBuilder;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import org.jetbrains.annotations.NotNull;

/**
 * A {@code AvailModuleBuilder} is an {@link ModuleBuilder} for {@link
 * AvailModuleType}s.
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class AvailModuleBuilder extends ModuleBuilder
{
	@Override
	public void setupRootModel (final ModifiableRootModel modifiableRootModel)
	throws ConfigurationException
	{
		// TODO what's all this then?
	}

	@Override
	public ModuleType getModuleType ()
	{
		return AvailModuleType.getInstance();
	}

	@Override
	public ModuleWizardStep[] createWizardSteps (
		final @NotNull WizardContext wizardContext,
		final @NotNull ModulesProvider modulesProvider)
	{
		final ModuleWizardStep[] steps = new AvailRootsStep[1];
		steps[0]= new AvailRootsStep();
		return steps;
	}
}
