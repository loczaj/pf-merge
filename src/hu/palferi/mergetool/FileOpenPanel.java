package hu.palferi.mergetool;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.prefs.Preferences;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

@SuppressWarnings("serial")
public class FileOpenPanel extends JPanel implements ActionListener, DocumentListener {

	static private JFileChooser fileDialog = new JFileChooser();

	private Preferences preferences;

	private JFormattedTextField nameField;
	private JButton openButton;

	private String title;
	private boolean save;
	private File selectedFile;

	FileOpenPanel(String title, boolean save) {

		this.preferences = Preferences.userRoot().node(this.getClass().getName());
		this.save = save;
		this.title = title;

		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(),
				BorderFactory.createEmptyBorder(7, 7, 7, 7)));

		nameField = new JFormattedTextField();
		nameField.setPreferredSize(new Dimension(250, 10));
		nameField.setEditable(true);
		nameField.getDocument().addDocumentListener(this);
		nameField.setText(preferences.get(title, ""));

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

	public boolean canUseSelectedFile() {
		if (save) {
			return !(selectedFile == null || selectedFile.isDirectory()
					|| (selectedFile.isFile() && !selectedFile.canWrite())
					|| selectedFile.getParentFile() == null || !selectedFile.getParentFile()
					.isDirectory());
		} else {
			return (selectedFile != null && selectedFile.isFile() && selectedFile.canRead());
		}
	}

	public File getSelectedFile() {
		preferences.put(title, nameField.getText());
		return selectedFile;
	}

	public void showFileError() {
		nameField.setBackground(Color.orange);
	}

	public void showFileOkay() {
		nameField.setBackground(null);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// Handle open button action.
		int state;
		if (e.getSource() == openButton) {

			if (save)
				state = fileDialog.showSaveDialog(null);
			else
				state = fileDialog.showOpenDialog(null);

			if (state == JFileChooser.APPROVE_OPTION) {
				selectedFile = fileDialog.getSelectedFile();
				nameField.setText(selectedFile.getPath());
			}
		}
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		this.changedUpdate(e);
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		this.changedUpdate(e);
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		selectedFile = new File(nameField.getText());
		if (canUseSelectedFile())
			this.showFileOkay();
		else
			this.showFileError();
	}
}
