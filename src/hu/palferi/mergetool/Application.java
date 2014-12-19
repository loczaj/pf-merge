package hu.palferi.mergetool;

import hu.palferi.mergetool.ui.BillingPanel;
import hu.palferi.mergetool.ui.IOFilesPanel;
import hu.palferi.mergetool.ui.PreviewPanel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

@SuppressWarnings("serial")
public class Application extends JPanel implements ActionListener {

	private IOFilesPanel inputFilesPanel;
	private BillingPanel billingPanel;

	public Application() {
		super();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		inputFilesPanel = new IOFilesPanel("Bemeneti fájlok", "Utalások", "Regisztációk", false);
		add(inputFilesPanel);

		billingPanel = new BillingPanel();
		billingPanel.startButton.addActionListener(this);

		PreviewPanel previewPanel = new PreviewPanel();

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addTab("Számla import készítés", billingPanel);
		tabbedPane.addTab("Előnézet", previewPanel);
		add(tabbedPane);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (inputFilesPanel.upperFilePanel.canUseSelectedFile()
				&& inputFilesPanel.lowerFilePanel.canUseSelectedFile()) {
			if (event.getSource() == billingPanel.startButton) {
				// Customer Maintenance
				if (billingPanel.outputFilesPanel.upperFilePanel.canUseSelectedFile()) {
					CustomerMaintenance maintenance = new CustomerMaintenance(
							inputFilesPanel.upperFilePanel.getSelectedFile(),
							inputFilesPanel.lowerFilePanel.getSelectedFile());

					try {
						maintenance.run(billingPanel.outputFilesPanel.upperFilePanel.getSelectedFile(),
								billingPanel.getStricture());

					} catch (InvalidFormatException | IOException ex) {
						ex.printStackTrace();
						JOptionPane.showMessageDialog(null, ex.toString(), "Uppsz",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		}
	}

	public static void main(String[] args) {

		// Schedule a job for the event-dispatching thread
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {

					// UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");

					Application contentPane = new Application();
					contentPane.setOpaque(true);

					JFrame frame = new JFrame("Párosító Progi");
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.setContentPane(contentPane);
					frame.pack();
					frame.setLocationRelativeTo(null);
					frame.setMinimumSize(new Dimension(725, 565));
					frame.setVisible(true);

				} catch (Throwable e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, e.toString(), "Uppsz", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
}