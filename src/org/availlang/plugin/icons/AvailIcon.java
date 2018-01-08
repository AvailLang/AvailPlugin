package org.availlang.plugin.icons;
import com.intellij.openapi.util.IconLoader;
import org.availlang.plugin.file.AvailDirectoryFileType;
import org.availlang.plugin.file.AvailFileType;
import org.availlang.plugin.file.AvailRepoFileType;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.net.URL;

/**
 * An {@link AvailIcon} is a file that statically makes icons available for
 * Avail related aspects of the Avail plugin.
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public final class AvailIcon
{
	/**
	 * Get the {@link Icon} for the provided base file name.
	 *
	 * @param fileName
	 *        The {@code String} name of the file (without path).
	 * @return An {@code Icon}.
	 */
	public static @NotNull Icon getIcon (final @NotNull String fileName)
	{
		final String path = String.format("/icons/%s", fileName);
		final URL iconUrl = AvailIcon.class.getResource(path);
		final Icon fileIcon = IconLoader.findIcon(iconUrl);
		assert fileIcon != null : String.format(
			"(%s): The icon, %s, is not located in the resources' icon folder",
			path,
			fileName);
		return fileIcon;
	}

	/**
	 * The {@link Icon} used to represent an {@link AvailFileType}.
	 */
	public static final @NotNull Icon availFileIcon =
		getIcon("ModuleInTree.png");

	/**
	 * The {@link Icon} used to represent an {@link AvailRepoFileType}.
	 */
	public static final @NotNull Icon availRepoFileIcon =
		getIcon("AvailHammer.png");

	/**
	 * The {@link Icon} used to represent an {@link AvailDirectoryFileType}.
	 */
	public static final @NotNull Icon availDirectoryFileIcon =
		getIcon("PackageInTree.png");
}
