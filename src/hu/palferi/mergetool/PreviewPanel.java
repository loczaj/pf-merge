package hu.palferi.mergetool;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

@SuppressWarnings("serial")
public class PreviewPanel extends JPanel {

	private JTable previewTable;

	PreviewPanel() {

		previewTable = new JTable(new PreviewTableModel());
		previewTable.setPreferredScrollableViewportSize(new Dimension(450, 250));
		previewTable.setFillsViewportHeight(true);
		// previewTable.getSelectionModel().addListSelectionListener(new RowListener());
		// previewTable.getColumnModel().getSelectionModel().addListSelectionListener(new
		// ColumnListener());

		this.setLayout(new BorderLayout());
		this.add(new JScrollPane(previewTable));
	}
}
