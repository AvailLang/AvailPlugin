package org.availlang.plugin.ui.model;
import javax.swing.*;

/**
 * A {@code SingleListSelectionModel} is a {@link DefaultListSelectionModel}
 * that restricts selections to one element.
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class SingleListSelectionModel extends DefaultListSelectionModel
{
	public SingleListSelectionModel ()
	{
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}

	@Override
	public void clearSelection ()
	{
		// no implementation
	}

	@Override
	public void removeSelectionInterval (final int index0, final int index1)
	{
		// no implementation
	}
}
