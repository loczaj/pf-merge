package hu.palferi.mergetool.ui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.NumberFormat;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.prefs.Preferences;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.PlainDocument;

@SuppressWarnings("serial")
public class BillingPanel extends JPanel {

	public JButton startButton;
	public IOFilesPanel outputFilesPanel;

	private JSlider strictureSlider;
	private Preferences preferences;
	private JFormattedTextField priceField;
	private JTextField customerIdPrefixField;

	public BillingPanel() {

		this.preferences = Preferences.userRoot().node(this.getClass().getName());

		setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		setLayout(new GridBagLayout());
		setAlignmentX(CENTER_ALIGNMENT);
		GridBagConstraints gbc = new GridBagConstraints();

		outputFilesPanel = new IOFilesPanel("Kimeneti fájlok", "Ügyféltörzs", "Számla import", true);
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(outputFilesPanel, gbc);

		JPanel parameterPanel = new JPanel();
		parameterPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Paraméterek"),
				BorderFactory.createEmptyBorder(15, 15, 15, 15)));
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.ipadx = 73;
		add(parameterPanel, gbc);

		parameterPanel.add(new JLabel("Ügyfél kód prefix"));
		customerIdPrefixField = new JTextField(4);
		customerIdPrefixField.setText(preferences.get("customerIdPrefix", ""));
		PlainDocument prefixDocument = (PlainDocument) customerIdPrefixField.getDocument();
		prefixDocument.setDocumentFilter(new DocumentSizeFilter(3));
		parameterPanel.add(customerIdPrefixField);
		parameterPanel.add(Box.createHorizontalStrut(70));

		parameterPanel.add(new JLabel("Egységár"));
		NumberFormat priceFormat = NumberFormat.getNumberInstance();
		priceFormat.setMaximumIntegerDigits(5);
		priceField = new JFormattedTextField(priceFormat);
		priceField.setValue(preferences.getInt("price", 1000));
		priceField.setColumns(4);
		parameterPanel.add(priceField);

		Dictionary<Integer, JLabel> strictureLevels = new Hashtable<>();
		strictureLevels.put(1, new JLabel("laza"));
		strictureLevels.put(3, new JLabel("közepes"));
		strictureLevels.put(5, new JLabel("szigorú"));

		strictureSlider = new JSlider(SwingConstants.VERTICAL, 1, 5, 1);
		strictureSlider.setLabelTable(strictureLevels);
		strictureSlider.setPaintLabels(true);
		// strictureSlider.setPreferredSize(new Dimension(140, 260));
		strictureSlider.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Egyezés mértéke"),
				BorderFactory.createEmptyBorder(15, 15, 15, 15)));
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridheight = 2;
		gbc.ipadx = 0;
		add(strictureSlider, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridheight = 1;
		gbc.gridwidth = 2;
		add(Box.createVerticalStrut(10), gbc);

		startButton = new JButton("Mehet");
		startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		startButton.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		gbc.gridx = 0;
		gbc.gridy = 3;
		add(startButton, gbc);
	}

	public String getCustomerIdPrefix() {
		preferences.put("customerIdPrefix", customerIdPrefixField.getText());
		return customerIdPrefixField.getText();
	}

	public int getPrice() {
		int price = ((Number) priceField.getValue()).intValue();
		preferences.putInt("price", price);
		return price;
	}

	public int getStricture() {
		return strictureSlider.getValue();
	}
}
