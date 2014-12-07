package hu.palferi.mergetool.ui;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class IOFilesPanel extends JPanel {

	public FileOpenPanel upperFilePanel;
	public FileOpenPanel lowerFilePanel;

	public IOFilesPanel(String borderTitle, String upperTitle, String lowerTitle, boolean write) {

		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(borderTitle),
				BorderFactory.createEmptyBorder(15, 15, 15, 15)));

		upperFilePanel = new FileOpenPanel(upperTitle, write);
		lowerFilePanel = new FileOpenPanel(lowerTitle, write);

		add(upperFilePanel);
		add(lowerFilePanel);
	}
}
