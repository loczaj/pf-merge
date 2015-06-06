package hu.palferi.mergetool.ui;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class CustomerPanel extends JPanel {

	public JButton startButton;
	public IOFilesPanel filesPanel;

	public CustomerPanel() {

		setBorder(BorderFactory.createEmptyBorder(25, 15, 15, 15));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setAlignmentX(CENTER_ALIGNMENT);

		filesPanel = new IOFilesPanel("Ügyfél fájlok", "KS ügyféltörzs", "Ügyfél import", false, true);
		add(filesPanel);

		add(Box.createVerticalStrut(10));

		startButton = new JButton("Mehet");
		startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		startButton.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		add(startButton);
	}
}
