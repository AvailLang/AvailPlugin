package org.availlang.plugin.file.module;

import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.ModuleTypeManager;
import org.availlang.plugin.icons.AvailIcon;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * An {@link AvailModuleType} is a {@link ModuleType} specific to Avail.
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class AvailModuleType extends ModuleType<AvailModuleBuilder>
{
	/**
	 * The identifier for the {@link AvailModuleType}.
	 */
	private static final String ID = "AVAIL_MODULE_TYPE";

	/**
	 * Answer an instance of {@link AvailModuleType} from the {@link
	 * ModuleTypeManager}.
	 *
	 * @return A {@code AvailModuleType}.
	 */
	public static AvailModuleType getInstance ()
	{
		return (AvailModuleType) ModuleTypeManager.getInstance().findByID(ID);
	}

	@Override
	public @NotNull AvailModuleBuilder createModuleBuilder ()
	{
		return new AvailModuleBuilder();
	}

	@Override
	public @NotNull String getName ()
	{
		return "Avail";
	}

	@Override
	public @NotNull String getDescription ()
	{
		return "An Avail project";
	}

	@Override
	public Icon getNodeIcon (final boolean isOpened)
	{
		return AvailIcon.availRepoFileIcon;
	}

	/**
	 * Construct a {@link AvailModuleType}.
	 */
	public AvailModuleType ()
	{
		super(ID);
	}
}
