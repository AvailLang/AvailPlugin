package org.availlang.plugin.file;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.util.IconLoader;
import org.availlang.plugin.icons.AvailIcon;
import org.availlang.plugin.language.AvailLanguage;
import org.availlang.plugin.language.AvailRepoLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * An {@code AvailFileType} is a {@link LanguageFileType} specific to Avail.
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class AvailRepoFileType
extends LanguageFileType
{
	/**
	 * The sole {@link AvailRepoFileType}.
	 */
	public static final @NotNull AvailRepoFileType soleInstance =
		new AvailRepoFileType();

	/**
	 * Construct an {@link AvailRepoFileType}.
	 */
	private AvailRepoFileType ()
	{
		super(AvailRepoLanguage.soleInstance);
	}

	@Override
	public @NotNull String getName ()
	{
		return "Avail repository file";
	}

	@Override
	public @NotNull String getDescription ()
	{
		return "Avail repository file";
	}

	@Override
	public @NotNull String getDefaultExtension ()
	{
		return "repo";
	}

	@Override
	public @Nullable Icon getIcon ()
	{
		return AvailIcon.availRepoFileIcon;
	}
}
