package org.availlang.plugin.file;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.util.IconLoader;
import org.availlang.plugin.language.AvailLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * An {@code AvailFileType} is a {@link LanguageFileType} specific to Avail.
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class AvailFileType
extends LanguageFileType
{
	/**
	 * The sole {@link AvailFileType}.
	 */
	public static final @NotNull AvailFileType soleInstance =
		new AvailFileType();

	/**
	 * The {@link Icon} used to represent an {@link AvailFileType}.
	 */
	private static final @NotNull Icon fileIcon =
		IconLoader.getIcon("/org/availlang/plugin/icons/AvailHammer.png");

	/**
	 * Construct an {@link AvailFileType}.
	 */
	private AvailFileType ()
	{
		super(AvailLanguage.soleInstance);
	}

	@Override
	public @NotNull String getName ()
	{
		return "Avail file";
	}

	@Override
	public @NotNull String getDescription ()
	{
		return "Avail language file";
	}

	@Override
	public @NotNull String getDefaultExtension ()
	{
		return "avail";
	}

	@Override
	public @Nullable Icon getIcon ()
	{
		return fileIcon;
	}
}
