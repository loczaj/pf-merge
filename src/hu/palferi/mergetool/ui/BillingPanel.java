package hu.palferi.mergetool.ui;

import java.awt.Component;
import java.text.NumberFormat;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.prefs.Preferences;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
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
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setAlignmentX(CENTER_ALIGNMENT);

		outputFilesPanel = new IOFilesPanel("Kimeneti fájlok", "Ügyféltörzs", "Számla import", true);
		add(outputFilesPanel);

		JPanel parameterPanel = new JPanel();
		parameterPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Paraméterek"),
				BorderFactory.createEmptyBorder(15, 15, 15, 15)));
		add(parameterPanel);

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

		strictureSlider = new JSlider(0, 1, 5, 1);
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
