package hu.palferi.mergetool.ui;

import java.awt.Color;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.prefs.Preferences;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class ParameterPanel extends JPanel {
	private Preferences preferences;
	private JTextField programNameField;
	private JSlider strictureSlider;

	public ParameterPanel() {
		preferences = Preferences.userRoot().node(this.getClass().getName());

		setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Paraméterek"),
				BorderFactory.createEmptyBorder(15, 15, 5, 15)));

		add(new JLabel("Program név"));
		programNameField = new JTextField(16);
		programNameField.setText(preferences.get("programName", ""));
		add(programNameField);
		add(Box.createHorizontalStrut(70));

		Dictionary<Integer, JLabel> strictureLevels = new Hashtable<>();
		strictureLevels.put(1, new JLabel("laza"));
		strictureLevels.put(3, new JLabel("közepes"));
		strictureLevels.put(5, new JLabel("szigorú"));

		strictureSlider = new JSlider(SwingConstants.HORIZONTAL, 1, 5, 1);
		strictureSlider.setLabelTable(strictureLevels);
		strictureSlider.setPaintLabels(true);
		// strictureSlider.setPreferredSize(new Dimension(140, 260));
		strictureSlider.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.lightGray, 1, true),
				BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		add(new JLabel("Egyezés"));
		add(strictureSlider);
	}

	public String getProgramName() {
		preferences.put("programName", programNameField.getText());
		return programNameField.getText();
	}

	public int getStricture() {
		return strictureSlider.getValue();
	}

}
