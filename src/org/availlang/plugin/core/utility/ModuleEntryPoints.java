package org.availlang.plugin.core.utility;
import com.avail.builder.ResolvedModuleName;
import com.avail.linking.EntryPoint;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A {@link ModuleEntryPoints} holds on to a {@link ResolvedModuleName} and the
 * {@linkplain EntryPoint#methodName name} of any {@link EntryPoint} contained
 * in the module represented by the {@code ResolvedModuleName}.
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class ModuleEntryPoints
{
	/**
	 * The {@link List} of {@linkplain EntryPoint#methodName}s.
	 */
	private final @NotNull List<String> entryPoints;

	/**
	 * Answer the {@link List} of {@linkplain EntryPoint#methodName}s.
	 *
	 * @return A list.
	 */
	public @NotNull List<String> entryPoints ()
	{
		return entryPoints;
	}

	/**
	 * The corresponding {@link ResolvedModuleName}.
	 */
	private final @NotNull ResolvedModuleName resolvedModuleName;

	/**
	 * Answer the corresponding {@link ResolvedModuleName}.
	 *
	 * @return A {@code ResolvedModuleName}.
	 */
	public @NotNull ResolvedModuleName resolvedModuleName ()
	{
		return resolvedModuleName;
	}

	/**
	 * Construct a {@link ModuleEntryPoints}.
	 *
	 * @param entryPoints
	 *        The {@link List} of {@linkplain EntryPoint#methodName}s.
	 * @param resolvedModuleName
	 *        The corresponding {@link ResolvedModuleName}.
	 */
	public ModuleEntryPoints (
		final @NotNull List<String> entryPoints,
		final @NotNull ResolvedModuleName resolvedModuleName)
	{
		this.entryPoints = entryPoints;
		this.resolvedModuleName = resolvedModuleName;
	}
}
