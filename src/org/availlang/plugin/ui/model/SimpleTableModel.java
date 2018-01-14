/*
 * SimpleTableModel.java
 * Copyright © 1993-2018, The Avail Foundation, LLC.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * * Neither the name of the copyright holder nor the names of the contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package org.availlang.plugin.ui.model;
import org.jetbrains.annotations.NotNull;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code SimpleTableModel} interface is TODO: Document this!
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class SimpleTableModel
extends AbstractTableModel
{
	/**
	 * The names of the columns. The size of this array will translate into
	 * the number of columns on the table.
	 */
	private final @NotNull String[] columnNames;

	/**
	 * A {@link List} of {@code List}s of Strings that represent rows and
	 * their cell values in the table.
	 */
	private final @NotNull List<List<String>> rows = new ArrayList<>();

	@Override
	public String getColumnName (final int column) {
		return columnNames[column];
	}

	@Override
	public int getRowCount ()
	{
		return rows.size();
	}

	@Override
	public int getColumnCount ()
	{
		return columnNames.length;
	}

	@Override
	public Object getValueAt (
		final int row,
		final int column)
	{
		assert row < getRowCount() : String.format(
			"requested value at (%d,%d), but only has %d rows",
			column,
			row,
			getRowCount());
		assert column < getColumnCount() : String.format(
			"requested value at (%d,%d), but only has %d columns",
			column,
			row,
			getColumnCount());
		return rows.get(row).get(column);
	}

	@Override
	public boolean isCellEditable (final int row, final int column)
	{
		return true;
	}

	@Override
	public void setValueAt (
		final Object value,
		final int row,
		final int column)
	{
		rows.get(row).set(column, (String) value);
		fireTableCellUpdated(row, column);
	}

	/**
	 * Answer the {@link #rows}.
	 *
	 * @return A {@link List}.
	 */
	@NotNull
	public List<List<String>> rows ()
	{
		return rows;
	}

	/**
	 * Construct a {@link SimpleTableModel}.
	 *
	 * @param columnNames
	 *        The {@link List} of {@code List}s of Strings that represent
	 *        rows and their cell values in the table.
	 */
	public SimpleTableModel (final @NotNull String... columnNames)
	{
		this.columnNames = columnNames;
	}
}
