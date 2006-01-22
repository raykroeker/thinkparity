/*
 * Jan 17, 2006
 */
package com.thinkparity.browser.javax.swing;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.thinkparity.browser.ui.MainWindow;

/**
 * Used as a quick-launch for testing.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class JPanelTester extends JFrame {

	/**
	 * @see java.io.Serialiable
	 */
	private static final long serialVersionUID = 1;

	/**
	 * Run the JFrameRunner.
	 * 
	 * @param args
	 *            Command line arguments.
	 */
	public static void main(String[] args) {
		try { JPanelTester.open(); }
		catch(Exception x) {
			x.printStackTrace(System.err);
			System.exit(1);
		}
	}

	/**
	 * Instantiate the jFrame and set it to visible.
	 *
	 */
	private static void doOpen() {
		BrowserUI.setParityLookAndFeel();

		final JPanelTester jFrame = new JPanelTester();
		jFrame.setVisible(true);
	}

	/**
	 * Use the swing utils to open the jFrame in a thread-safe manner.
	 *
	 */
	private static void open() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() { doOpen(); }
		});
	}

	/**
	 * Create a JFrameRunner.
	 */
	private JPanelTester() {
		super();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(MainWindow.getDefaultSize());
		setTitle("Test Runner:  Login JPanel");
		setLayout(new GridBagLayout());
		setLocation(100, 100);

		add(createTestJPanel(), createTestJPanelConstraints());
	}

	private Component createTestJPanel() {
		final File imageFile = new File("C:\\Documents and Settings\\raymond\\My Documents\\eclipse.org\\workspaces\\thinkparity.com\\com.thinkparity.parity-browser2\\target\\classes\\images\\historyHeading.png");
		System.out.println(imageFile.exists());
		return new JLabel(new ImageIcon(imageFile.getAbsolutePath()));
//		final JPanel jPanel = new LoginJPanel();
//		jPanel.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
//		final Component[] components = jPanel.getComponents();
//		final Color color = new Color(64, 192, 18, 255);
//		for(Component c : components) {
//			if(c instanceof JLabel) { ((JLabel) c).setOpaque(true); }
//			c.setBackground(color);
//		}
//		return jPanel;
	}

	private Object createTestJPanelConstraints() {
		return new GridBagConstraints(0, 0,
				1, 1,
				1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0),
				0, 0);
	}
}
