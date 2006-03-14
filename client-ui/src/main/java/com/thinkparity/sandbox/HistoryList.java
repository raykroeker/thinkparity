/*
 * Mar 13, 2006
 */
package com.thinkparity.sandbox;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.text.MessageFormat;

import javax.swing.*;

import com.thinkparity.browser.application.browser.component.LabelFactory;
import com.thinkparity.browser.javax.swing.AbstractJFrame;
import com.thinkparity.browser.javax.swing.AbstractJPanel;
import com.thinkparity.browser.javax.swing.border.TopBorder;
import com.thinkparity.browser.platform.util.ImageIOUtil;

import com.thinkparity.codebase.DateUtil;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class HistoryList extends AbstractJFrame {

	private static Integer version = 0;

	private static final BufferedImage WINDOW_IMAGE;

	private static final Dimension WINDOW_SIZE;

	static {
		WINDOW_IMAGE = ImageIOUtil.read("HistoryWindow.png");
		WINDOW_SIZE =
			new Dimension(WINDOW_IMAGE.getWidth(), WINDOW_IMAGE.getHeight());
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try { HistoryList.open(); }
		catch(final Throwable t) {
			t.printStackTrace(System.err);
			System.exit(1);
		}
	}

	private static void doOpen() {
		final HistoryList jFrame = new HistoryList();
		
		for(int i = 0; i < 500; i++) { jFrame.addListItem(); }

		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.setLocation(500, 200);
		jFrame.setSize(WINDOW_SIZE);
		jFrame.setUndecorated(true);

		jFrame.setVisible(true);
	}

	private static void open() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() { HistoryList.doOpen(); }
		});
	}

	private DefaultListModel listModel;

	/**
	 * Create a HistoryList.
	 */
	public HistoryList() {
		super("");
		initComponents();
	}

	private void addListItem() {
		listModel.addElement("New Element");
	}

	private void initComponents() {
		listModel = new DefaultListModel();
		final JList jList = new JList();
		jList.setCellRenderer(new CellRenderer());
		jList.setFixedCellHeight(90);
		jList.setLayoutOrientation(JList.VERTICAL);
		jList.setModel(listModel);

		final JScrollPane jScrollPane = new JScrollPane(jList);
		jScrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		final GridBagLayout gbl = new GridBagLayout();
		setLayout(gbl);

		final GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1;
		gbc.weighty = 1;
		add(jScrollPane, gbc.clone());
	}

	private class CellRenderer extends AbstractJPanel implements ListCellRenderer {

		private JLabel dateJLabel;

		private JLabel versionJLabel;
	
		private JTextArea eventJTextArea;

		private CellRenderer() {
			super("");
			initComponents();
		}
	
		/**
		 * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList,
		 *      java.lang.Object, int, boolean, boolean)
		 */
		public Component getListCellRendererComponent(final JList list,
				final Object value, final int index, final boolean isSelected,
				final boolean cellHasFocus) {
			if(0 < index) { setBorder(new TopBorder(Color.BLACK)); }
			else { setBorder(null); }
			dateJLabel.setText(MessageFormat.format("{0,date,MMM dd, yyyy}", new Object[] {DateUtil.getInstance().getTime()}));
			eventJTextArea.setText(getText(index));
			versionJLabel.setText(MessageFormat.format("Version {0,number,0}", new Object[] { version++ }));
			return this;
		}

		private String getText(final Integer index) {
			return TEXT[index % TEXT.length];
		}

		private void initComponents() {
			dateJLabel = LabelFactory.create();
			versionJLabel = LabelFactory.create();
			eventJTextArea = new JTextArea();
			eventJTextArea.setLineWrap(true);
			eventJTextArea.setWrapStyleWord(true);

			final GridBagLayout gbl = new GridBagLayout();
			setLayout(gbl);

			final GridBagConstraints gbc = new GridBagConstraints();

			gbc.insets.top = 3;
			gbc.anchor = GridBagConstraints.WEST;
			add(dateJLabel, gbc.clone());

			gbc.weightx = 1;
			add(versionJLabel, gbc.clone());

			gbc.insets.top = 0;
			gbc.insets.bottom = 3;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.gridwidth = 2;
			gbc.gridy = 1;
			gbc.weighty = 1;
			add(eventJTextArea, gbc.clone());
		}
	}
	private static final String[] TEXT = {
		"Short text.",
		"Longer text.",
		"Medium text.  Medium text.  Medium text.  Medium text.  Medium text.Medium text.",
		"I am the very model of a modern major general.  I have information animal vegetable and mineral.  I am the very model of a modern major general.  I have information animal vegetable and mineral.  I am the very model of a modern major general.  I have information animal vegetable and mineral."
	};
}
