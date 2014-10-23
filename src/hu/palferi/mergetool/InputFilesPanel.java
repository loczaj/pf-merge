package hu.palferi.mergetool;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class InputFilesPanel extends JPanel {

	public FileOpenPanel transferFilePanel;
	public FileOpenPanel registerFilePanel;
	public FileOpenPanel customerFilePanel;

	public InputFilesPanel() {

		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Bemeneti fájlok"),
				BorderFactory.createEmptyBorder(15, 15, 15, 15)));

		transferFilePanel = new FileOpenPanel("Utalások");
		registerFilePanel = new FileOpenPanel("Regisztációk");
		customerFilePanel = new FileOpenPanel("Ügyféltörzs");

		add(transferFilePanel);
		add(registerFilePanel);
		add(customerFilePanel);
	}
}
