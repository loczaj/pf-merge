package hu.palferi.mergetool;

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

	private InputFilesPanel inputFilesPanel;
	private CustomerMaintenancePanel customerPanel;

	public Application() {
		super();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		inputFilesPanel = new InputFilesPanel();
		add(inputFilesPanel);

		customerPanel = new CustomerMaintenancePanel();
		customerPanel.startButton.addActionListener(this);

		PreviewPanel previewPanel = new PreviewPanel();

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addTab("Ügyféltörzs karbantartás", customerPanel);
		tabbedPane.addTab("Számla import készítés", null);
		tabbedPane.addTab("Előnézet", previewPanel);
		add(tabbedPane);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (inputFilesPanel.transferFilePanel.canUseSelectedFile()
				&& inputFilesPanel.registerFilePanel.canUseSelectedFile()
				&& inputFilesPanel.customerFilePanel.canUseSelectedFile()) {
			if (e.getSource() == customerPanel.startButton) {
				// Customer Maintenance
				if (customerPanel.outputFilePanel.canUseSelectedFile()) {
					CustomerMaintenance maintenance = new CustomerMaintenance(
							inputFilesPanel.transferFilePanel.getSelectedFile(),
							inputFilesPanel.registerFilePanel.getSelectedFile(),
							inputFilesPanel.customerFilePanel.getSelectedFile());

					try {
						maintenance.run(customerPanel.outputFilePanel.getSelectedFile(),
								customerPanel.getStricture());

					} catch (InvalidFormatException | IOException e1) {
						e1.printStackTrace();
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
					frame.setVisible(true);

				} catch (Throwable e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, e.toString() + "\n" + e.getStackTrace()[0],
							"Uppsz", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
}