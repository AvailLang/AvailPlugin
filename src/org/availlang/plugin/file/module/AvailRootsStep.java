package org.availlang.plugin.file.module;
import com.avail.builder.ModuleRoot;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.min;

/**
 * An {@code AvailRootsStep} is a {@link ModuleWizardStep} that allows a user
 * to create {@link ModuleRoot}s when setting up a new Avail project.
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class AvailRootsStep
extends ModuleWizardStep
{
	/**
	 * The panel for adding the roots.
	 */
	private final @NotNull JComponent rootPanel = createPanel();

	@Override
	public JComponent getComponent()
	{
		return rootPanel;
	}

	@Override
	public void updateDataModel()
	{
		System.out.println("I'm updated!");
		// TODO capture all model information
	}

	/**
	 * The {@link SimpleTableModel} used for setting {@link ModuleRoot}s.
	 */
	private static final @NotNull SimpleTableModel rootsTableModel =
		new SimpleTableModel("root", "repository", "source");

	/**
	 * // TODO not really sure what this bit is for.
	 */
	private static final @NotNull  SimpleTableModel renamesTableModel =
		new SimpleTableModel("module", "replacement path");

	/**
	 * Answer the {@link JPanel} that contains the {@link SimpleTableModel}s
	 * used for setting {@link ModuleRoot}s and renames table?
	 */
	private static @NotNull JPanel createPanel ()
	{
		final JPanel panel = new JPanel(new BorderLayout(20, 20));
		panel.setBorder(JBUI.Borders.empty());

		// Add the module roots area.
		final JLabel rootsLabel = new JLabel("Avail module roots");
		panel.add(rootsLabel);

		rootsTableModel.rows().clear();
//		for (final ModuleRoot root : workbench.resolver.moduleRoots().roots())
//		{
//			final java.util.List<String> triple = new ArrayList<>(3);
//			triple.add(root.name());
//			triple.add(root.repository().fileName().getPath());
//			final @Nullable File source = root.sourceDirectory();
//			triple.add(source == null ? "" : source.getPath());
//			rootsTableModel.rows().add(triple);
//		}
		final JBTable rootsTable = new JBTable(rootsTableModel);
		rootsTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		final TableColumnModel rootsColumns = rootsTable.getColumnModel();
		rootsColumns.getColumn(0).setMinWidth(30);
		rootsColumns.getColumn(0).setPreferredWidth(60);
		rootsColumns.getColumn(1).setMinWidth(50);
		rootsColumns.getColumn(1).setPreferredWidth(400);
		rootsColumns.getColumn(2).setMinWidth(50);
		rootsColumns.getColumn(2).setPreferredWidth(400);
		rootsTable.setGridColor(JBColor.GRAY);
		rootsTable.setFillsViewportHeight(true);
		final JBScrollPane rootsScrollPane = new JBScrollPane(rootsTable);
		panel.add(rootsScrollPane);

		final AbstractAction addRootAction =
			new AbstractAction("+")
			{
				@Override
				public void actionPerformed (final ActionEvent e)
				{
					int insertionIndex = rootsTable.getSelectedRow();
					if (insertionIndex == -1)
					{
						insertionIndex = rootsTableModel.getRowCount();
					}
					rootsTableModel.rows().add(
						insertionIndex, Arrays.asList("", "", ""));
					rootsTableModel.fireTableDataChanged();
					rootsTable.changeSelection(insertionIndex, 0, false, false);
				}
			};
		final JButton addRootButton = new JButton(addRootAction);
		panel.add(addRootButton);

		final AbstractAction removeRootAction =
			new AbstractAction("-")
			{
				@Override
				public void actionPerformed (final ActionEvent e)
				{
					final int deletionIndex = rootsTable.getSelectedRow();
					if (deletionIndex != -1)
					{
						rootsTableModel.rows().remove(deletionIndex);
						rootsTableModel.fireTableDataChanged();
						rootsTable.changeSelection(
							rootsTableModel.rows().isEmpty()
								? -1
								: min(
									deletionIndex,
									rootsTableModel.getRowCount() - 1),
							0,
							false,
							false);
					}
				}
			};
		final JButton removeRootButton = new JButton(removeRootAction);
		panel.add(removeRootButton);


		// Add the renames area.
		final JLabel renamesLabel = new JLabel("Renames");
		panel.add(renamesLabel);

		renamesTableModel.rows().clear();
//		for (final Entry<String, String> rename
//			: workbench.resolver.renameRules().entrySet())
//		{
//			final List<String> pair = new ArrayList<>(2);
//			pair.add(rename.getKey());
//			pair.add(rename.getValue());
//			renamesTableModel.rows().add(pair);
//		}

		final JBTable renamesTable = new JBTable(renamesTableModel);
		renamesTable.putClientProperty(
			"terminateEditOnFocusLost", Boolean.TRUE);
		final TableColumnModel renamesColumns = renamesTable.getColumnModel();
		renamesColumns.getColumn(0).setMinWidth(50);
		renamesColumns.getColumn(0).setPreferredWidth(400);
		renamesColumns.getColumn(1).setMinWidth(50);
		renamesColumns.getColumn(1).setPreferredWidth(400);
		renamesTable.setGridColor(JBColor.GRAY);
		renamesTable.setFillsViewportHeight(true);
		final JBScrollPane renamesScrollPane = new JBScrollPane(renamesTable);
		panel.add(renamesScrollPane);

		final AbstractAction addRenameAction =
			new AbstractAction("+")
			{
				@Override
				public void actionPerformed (final ActionEvent e)
				{
					int insertionIndex = renamesTable.getSelectedRow();
					if (insertionIndex == -1)
					{
						insertionIndex = renamesTableModel.getRowCount();
					}
					renamesTableModel.rows().add(
						insertionIndex, Arrays.asList("", ""));
					renamesTableModel.fireTableDataChanged();
					renamesTable.changeSelection(
						insertionIndex, 0, false, false);
				}
			};
		final JButton addRenameButton = new JButton(addRenameAction);
		panel.add(addRenameButton);

		final AbstractAction removeRenameAction =
			new AbstractAction("-")
			{
				@Override
				public void actionPerformed (final ActionEvent e)
				{
					final int deletionIndex = renamesTable.getSelectedRow();
					if (deletionIndex != -1)
					{
						renamesTableModel.rows().remove(deletionIndex);
						renamesTableModel.fireTableDataChanged();
						renamesTable.changeSelection(
							renamesTableModel.rows().isEmpty()
								? -1
								: min(
									deletionIndex,
									renamesTableModel.getRowCount() - 1),
							0,
							false,
							false);
					}
				}
			};
		final JButton removeRenameButton = new JButton(removeRenameAction);
		panel.add(removeRenameButton);


		// Add the ok/cancel buttons.
//		final AbstractAction okAction =
//			new AbstractAction(
//				UIManager.getString("OptionPane.okButtonText"))
//			{
//				@Override
//				public void actionPerformed (final ActionEvent e)
//				{
////					savePreferences();
////					workbench.refresh();
////					preferencesDialog.setVisible(false);
//				}
//			};
//		final JButton okButton = new JButton(okAction);
//		panel.add(okButton);
//		final AbstractAction cancelAction =
//			new AbstractAction(
//				UIManager.getString("OptionPane.cancelButtonText"))
//			{
//				@Override
//				public void actionPerformed (final ActionEvent e)
//				{
////					preferencesDialog.setVisible(false);
//				}
//			};
//		final JButton cancelButton = new JButton(cancelAction);
//		panel.add(cancelButton);

		final GroupLayout layout = new GroupLayout(panel);
		panel.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setHorizontalGroup(
			layout.createParallelGroup()
				.addComponent(rootsLabel)
				.addComponent(rootsScrollPane)
				.addGroup(layout.createSequentialGroup()
					.addComponent(addRootButton)
					.addComponent(removeRootButton))
				.addComponent(renamesLabel)
				.addComponent(renamesScrollPane)
				.addGroup(layout.createSequentialGroup()
					.addComponent(addRenameButton)
					.addComponent(removeRenameButton))
				.addGroup(Alignment.TRAILING, layout.createSequentialGroup()));
//					.addComponent(okButton)
//					.addComponent(cancelButton)));
		layout.setVerticalGroup(
			layout.createSequentialGroup()
				.addComponent(rootsLabel)
				.addComponent(rootsScrollPane)
				.addGroup(layout.createParallelGroup()
					.addComponent(addRootButton)
					.addComponent(removeRootButton))
				.addComponent(renamesLabel)
				.addComponent(renamesScrollPane)
				.addGroup(layout.createParallelGroup()
					.addComponent(addRenameButton)
					.addComponent(removeRenameButton))
				.addGroup(layout.createParallelGroup()));
//					.addComponent(okButton)
//					.addComponent(cancelButton)));
//		layout.linkSize(SwingConstants.HORIZONTAL, okButton, cancelButton);
		return panel;
	}

	/**
	 * A {@code SimpleTableModel} is an {@link AbstractTableModel} used for
	 * creating addable rows to custom tables.
	 */
	private static final class SimpleTableModel extends AbstractTableModel
	{
		/**
		 * The names of the columns. The size of this array will translate into
		 * the number of columns on the table.
		 */
		private final String[] columnNames;

		/**
		 * A {@link List} of {@code List}s of Strings that represent rows and
		 * their cell values in the table.
		 */
		private final List<List<String>> rows = new ArrayList<>();

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
		public Object getValueAt (final int row, final int column) {
			return rows.get(row).get(column);
		}

		@Override
		public boolean isCellEditable (final int row, final int column)
		{
			return true;
		}

		@Override
		public void setValueAt (final Object value, final int row, final int column) {
			rows.get(row).set(column, (String) value);
			fireTableCellUpdated(row, column);
		}

		/**
		 * Answer the {@link #rows}.
		 *
		 * @return A {@link List}.
		 */
		@NotNull List<List<String>> rows ()
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
		private SimpleTableModel (final String... columnNames)
		{
			this.columnNames = columnNames;
		}
	}
}
