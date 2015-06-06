package hu.palferi.mergetool.ui;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class IOFilesPanel extends JPanel {

	public FileOpenPanel upperFilePanel;
	public FileOpenPanel lowerFilePanel;

	public IOFilesPanel(String borderTitle, String upperTitle, String lowerTitle, boolean write) {
		this(borderTitle, upperTitle, lowerTitle, write, write);
	}

	public IOFilesPanel(String borderTitle, String upperTitle, String lowerTitle, boolean upperWrite,
			boolean lowerWrite) {

		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(borderTitle),
				BorderFactory.createEmptyBorder(15, 15, 15, 15)));

		upperFilePanel = new FileOpenPanel(upperTitle, upperWrite);
		lowerFilePanel = new FileOpenPanel(lowerTitle, lowerWrite);

		add(upperFilePanel);
		add(lowerFilePanel);
	}
}
