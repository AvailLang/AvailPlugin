package org.availlang.plugin.file;
import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;
import org.jetbrains.annotations.NotNull;

/**
 * An {@code AvailFileTypeFactory} is a {@link FileTypeFactory} used for {@link
 * AvailFileType}s.
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class AvailFileTypeFactory
extends FileTypeFactory
{
	@Override
	public void createFileTypes (
		final @NotNull FileTypeConsumer fileTypeConsumer)
	{
		fileTypeConsumer.consume(AvailFileType.soleInstance, "avail");
	}
}
