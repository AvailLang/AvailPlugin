package org.availlang.plugin.file;
import com.intellij.openapi.fileTypes.DirectoryFileType;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.vfs.VirtualFile;
import org.availlang.plugin.icons.AvailIcon;
import org.availlang.plugin.language.AvailLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * An {@code AvailDirectoryFileType} is a {@link DirectoryFileType} specific to
 * Avail directories.
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class AvailDirectoryFileType
implements DirectoryFileType
{
	/**
	 * The sole {@link AvailDirectoryFileType}.
	 */
	public static final @NotNull AvailDirectoryFileType soleInstance =
		new AvailDirectoryFileType();


	/**
	 * Construct an {@link AvailDirectoryFileType}.
	 */
	private AvailDirectoryFileType () {}

	@Override
	public @NotNull String getName ()
	{
		return "Avail directory";
	}

	@Override
	public @NotNull String getDescription ()
	{
		return "Avail directory";
	}

	@Override
	public @NotNull String getDefaultExtension ()
	{
		return "avail";
	}

	@Override
	public @Nullable Icon getIcon ()
	{
		return AvailIcon.availDirectoryFileIcon;
	}

	@Override
	public boolean isReadOnly ()
	{
		return false;
	}

	@Override
	public boolean isBinary ()
	{
		return false;
	}

	@Override
	public @Nullable String getCharset (
		final @NotNull VirtualFile file,
		final @NotNull byte[] content)
	{
		return null;
	}
}
