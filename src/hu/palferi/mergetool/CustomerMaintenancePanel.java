package hu.palferi.mergetool;

import java.awt.Component;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

@SuppressWarnings("serial")
public class CustomerMaintenancePanel extends JPanel {

	public JButton startButton;

	public CustomerMaintenancePanel() {
		// TODO Auto-generated constructor stub

		setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setAlignmentX(CENTER_ALIGNMENT);

		FileOpenPanel customerOutputFilePanel = new FileOpenPanel("Új ügyféltörzs");
		customerOutputFilePanel.setBorder(BorderFactory.createEmptyBorder(25, 15, 25, 15));
		add(customerOutputFilePanel);

		Dictionary<Integer, JLabel> strictureLevels = new Hashtable<>();
		strictureLevels.put(1, new JLabel("laza"));
		strictureLevels.put(3, new JLabel("közepes"));
		strictureLevels.put(5, new JLabel("szigorú"));

		JSlider strictureSlider = new JSlider(0, 1, 5, 1);
		strictureSlider.setLabelTable(strictureLevels);
		strictureSlider.setPaintLabels(true);
		strictureSlider.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Egyezés mértéke"),
				BorderFactory.createEmptyBorder(15, 15, 25, 15)));
		add(strictureSlider);

		startButton = new JButton("Mehet");
		startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		startButton.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		add(Box.createVerticalStrut(10));
		add(startButton);
	}
}
