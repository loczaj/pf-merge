package hu.palferi.mergetool;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener; //property change stuff

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class FileOpenPanel extends JPanel implements ActionListener, PropertyChangeListener {

	static private JFileChooser fileDialog = new JFileChooser();

	private JFormattedTextField nameField;
	private JButton openButton;

	FileOpenPanel(String title) {

		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(),
				BorderFactory.createEmptyBorder(7, 7, 7, 7)));

		nameField = new JFormattedTextField();
		nameField.setPreferredSize(new Dimension(250, 10));
		nameField.setEditable(false);
		// nameField.addPropertyChangeListener(this);

		openButton = new JButton("Megnyitás");
		openButton.addActionListener(this);

		// Make the label a fixed size
		JLabel label = new JLabel(title) {
			public Dimension getMinimumSize() {
				return getPreferredSize();
			}

			public Dimension getPreferredSize() {
				return new Dimension(90, super.getPreferredSize().height);
			}

			public Dimension getMaximumSize() {
				return getPreferredSize();
			}
		};

		// Put everything together.
		add(label);
		add(nameField);
		add(openButton);
		// add(Box.createHorizontalStrut(100));
	}

	// Don't allow this panel to get taller than its preferred size.
	// BoxLayout pays attention to maximum size, though most layout managers don't.
	public Dimension getMaximumSize() {
		return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
	}

	// ActionListener
	public void actionPerformed(ActionEvent e) {
		// Handle open button action.
		if (e.getSource() == openButton) {
			int state = fileDialog.showOpenDialog(null);
			if (state == JFileChooser.APPROVE_OPTION) {
				nameField.setText(fileDialog.getSelectedFile().getName());
			}
		}
	}

	// PropertyChangeListener
	public void propertyChange(PropertyChangeEvent e) {
		if (e.getSource() == nameField) {
		}
	}
}
