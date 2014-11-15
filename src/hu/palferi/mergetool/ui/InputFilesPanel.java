package hu.palferi.mergetool.ui;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class InputFilesPanel extends JPanel {

	public FileOpenPanel transferFilePanel;
	public FileOpenPanel registerFilePanel;

	public InputFilesPanel() {

		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Bemeneti fájlok"),
				BorderFactory.createEmptyBorder(15, 15, 15, 15)));

		transferFilePanel = new FileOpenPanel("Utalások", false);
		registerFilePanel = new FileOpenPanel("Regisztációk", false);

		add(transferFilePanel);
		add(registerFilePanel);
	}
}
