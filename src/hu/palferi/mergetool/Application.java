package hu.palferi.mergetool;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

@SuppressWarnings("serial")
public class Application extends JPanel implements ActionListener {

	private InputFilesPanel inputFilesPanel;

	public Application() {
		super();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		inputFilesPanel = new InputFilesPanel();
		add(inputFilesPanel);

		CustomerMaintenancePanel customerPanel = new CustomerMaintenancePanel();
		customerPanel.startButton.addActionListener(this);

		PreviewPanel previewPanel = new PreviewPanel();

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addTab("Ügyféltörzs karbantartás", customerPanel);
		tabbedPane.addTab("Számla import készítés", null);
		tabbedPane.addTab("Előnézet", previewPanel);
		add(tabbedPane);
	}

	public void actionPerformed(ActionEvent e) {
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