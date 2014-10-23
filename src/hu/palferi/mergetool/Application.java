package hu.palferi.mergetool;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

@SuppressWarnings("serial")
public class Application extends JPanel implements ActionListener {

	private JTable previewTable;
	private FileOpenPanel transferFilePanel;
	private FileOpenPanel registerFilePanel;
	private FileOpenPanel customerFilePanel;

	public Application() {
		super();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		JPanel filesPanel = new JPanel();
		filesPanel.setLayout(new BoxLayout(filesPanel, BoxLayout.PAGE_AXIS));
		filesPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Bemeneti fájlok"),
				BorderFactory.createEmptyBorder(15, 15, 15, 15)));
		add(filesPanel);

		transferFilePanel = new FileOpenPanel("Utalások");
		registerFilePanel = new FileOpenPanel("Regisztációk");
		customerFilePanel = new FileOpenPanel("Ügyféltörzs");
		filesPanel.add(transferFilePanel);
		filesPanel.add(registerFilePanel);
		filesPanel.add(customerFilePanel);

		JPanel customerPanel = new JPanel();
		customerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		customerPanel.setLayout(new BoxLayout(customerPanel, BoxLayout.PAGE_AXIS));
		customerPanel.setAlignmentX(CENTER_ALIGNMENT);

		FileOpenPanel customerOutputFilePanel = new FileOpenPanel("Új ügyféltörzs");
		customerOutputFilePanel.setBorder(BorderFactory.createEmptyBorder(25, 15, 25, 15));
		customerPanel.add(customerOutputFilePanel);

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
		customerPanel.add(strictureSlider);

		JButton startButton = new JButton("Mehet");
		startButton.addActionListener(this);
		startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		startButton.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		customerPanel.add(Box.createVerticalStrut(10));
		customerPanel.add(startButton);

		previewTable = new JTable(new PreviewTableModel());
		previewTable.setPreferredScrollableViewportSize(new Dimension(450, 250));
		previewTable.setFillsViewportHeight(true);
		// previewTable.getSelectionModel().addListSelectionListener(new RowListener());
		// previewTable.getColumnModel().getSelectionModel().addListSelectionListener(new
		// ColumnListener());

		JPanel previewPanel = new JPanel();
		previewPanel.setLayout(new BorderLayout());
		previewPanel.add(new JScrollPane(previewTable));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addTab("Ügyféltörzs karbantartás", customerPanel);
		tabbedPane.addTab("Számla import készítés", null);
		tabbedPane.addTab("Előnézet", previewPanel);
		add(tabbedPane);
	}

	public void actionPerformed(ActionEvent e) {
	}

	// Create the GUI and show it. For thread safety, this method should be invoked from the
	// event-dispatching thread.
	private static void createAndShowGUI() throws ReflectiveOperationException,
			UnsupportedLookAndFeelException {

		// UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");

		JFrame frame = new JFrame("Párosító Progi");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Application contentPane = new Application();
		contentPane.setOpaque(true);
		frame.setContentPane(contentPane);
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					createAndShowGUI();
				} catch (Throwable e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, e.toString() + "\n" + e.getStackTrace()[0],
							"Uppsz", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
}