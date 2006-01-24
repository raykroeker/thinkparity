/*
 * Jan 17, 2006
 */
package com.thinkparity.browser.javax.swing;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;

import javax.swing.JFrame;

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
	 * @see java.awt.Container#paint(java.awt.Graphics)
	 * 
	 */
	public void paint(Graphics g) {
		super.paint(g);

		final Graphics2D g2 = (Graphics2D) g.create();
		try {
			g2.drawRoundRect(0, 200, 350, 60, 15, 15);
		}
		finally { g2.dispose(); }
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
	}
}
