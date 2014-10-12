package hu.palferi.mergetool;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
class PreviewTableModel extends AbstractTableModel {

	private String[] columnNames = { "Partner", "Közlemény", "Résztvevő", "Összeg", "Párosít" };

	private Object[][] data = new Object[20][5];

	public PreviewTableModel() {
		for (int i = 0; i < 20; i++) {
			data[i][0] = "Gipsz Jakab";
			data[i][1] = "Tánc Gipsz Jakab";
			data[i][2] = "Gipsz Jakab";
			data[i][3] = new Integer(1500);
			data[i][4] = new Boolean(true);
		}
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return data.length;
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}

	public Object getValueAt(int row, int col) {
		return data[row][col];
	}

	// JTable uses this method to determine the default renderer/ editor for each cell.
	public Class<?> getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	public boolean isCellEditable(int row, int col) {
		return (col > 1);
	}

	// Data change
	public void setValueAt(Object value, int row, int col) {
		data[row][col] = value;
		fireTableCellUpdated(row, col);
	}

}