/*
 * Jan 27, 2006
 */
package com.thinkparity.browser.application.browser.display.avatar;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.Timer;
import javax.swing.event.MouseInputAdapter;

import com.thinkparity.browser.application.browser.UIConstants;
import com.thinkparity.browser.application.browser.component.ButtonFactory;
import com.thinkparity.browser.application.browser.component.LabelFactory;
import com.thinkparity.browser.model.util.ModelUtil;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.browser.platform.util.State;

import com.thinkparity.codebase.ResourceUtil;

import com.thinkparity.model.parity.model.workspace.Workspace;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class BrowserTitleAvatar extends Avatar {

	/**
	 * The parity workspace.
	 * 
	 */
	private static final Workspace workspace;

	static { workspace = ModelUtil.getWorkspace(); }

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	/**
	 * The paint used to draw the gradient.
	 * 
	 * @see #paintComponent(Graphics)
	 */
	private Paint gradientPaint;

	/**
	 * The mouse input adapter.  Used to drag the window by this avatar.
	 * 
	 */
	private final MouseInputAdapter mouseInputAdapter;

	/**
	 * Create a BrowserTitleAvatar.
	 * 
	 */
	BrowserTitleAvatar() {
		super("BrowserTitleAvatar");
		this.mouseInputAdapter = new MouseInputAdapter() {
			int offsetX;
			int offsetY;
			public void mouseClicked(final MouseEvent e) {
				if(2 == e.getClickCount()) {
					if(e.isShiftDown()) {
						final String info = new StringBuffer()
							.append(workspace.getWorkspaceURL())
							.toString();
						if(info.length() > 68) {
							infoJLabel.setText(info.substring(68));
						}
						new Timer(1500, new ActionListener() {
							public void actionPerformed(final ActionEvent e) {
								infoJLabel.setText("");
							}
						}).start();
					}
				}
			}
			public void mouseDragged(final MouseEvent e) {
				getController().moveMainWindow(
						new Point(e.getPoint().x - offsetX,
								e.getPoint().y - offsetY));
			}
			public void mousePressed(MouseEvent e) {
				offsetX = e.getPoint().x;
				offsetY = e.getPoint().y;
			}
		};
		addMouseListener(mouseInputAdapter);
		addMouseMotionListener(mouseInputAdapter);
		setLayout(new GridBagLayout());
		initComponents();
	}

	private JLabel infoJLabel;

	/**
	 * Initialize the browser's title components.
	 *
	 */
	private void initComponents() {
		//	 _______________________________________
		//	/										\
		//	|	                                 X  |
		//	|										|
		//	-----------------------------------------
		final GridBagConstraints c = new GridBagConstraints();

		infoJLabel = LabelFactory.create();
		c.anchor = GridBagConstraints.WEST;
		c.insets.left = 7;
		add(infoJLabel, c.clone());

		final ImageIcon closeJButtonIcon =
			new ImageIcon(ResourceUtil.getURL("images/closeButton.png"));
		final JButton closeJButton = ButtonFactory.create(null);
		closeJButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				getController().end();
			}
		});
		closeJButton.setIcon(closeJButtonIcon);
		final Dimension iconSize = new Dimension(
				closeJButtonIcon.getIconWidth(), closeJButtonIcon.getIconHeight());
		closeJButton.setPreferredSize(iconSize);

		c.anchor = GridBagConstraints.EAST;
		c.insets.left = 0;
		c.insets.right = 7;
		c.weightx = 1;
		add(closeJButton, c.clone());
	}

	/**
	 * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#getId()
	 * 
	 */
	public AvatarId getId() { return AvatarId.BROWSER_TITLE; }

	/**
	 * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#getState()
	 * 
	 */
	public State getState() { return null; }

	/**
	 * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#setState(com.thinkparity.browser.platform.util.State)
	 * 
	 */
	public void setState(final State state) {}

	/**
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 * 
	 */
	protected void paintComponent(final Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g.create();
		try {
			g2.setPaint(getGradientPaint());
			g2.fillRect(0, 0, getWidth(), getHeight());
		}
		finally { g2.dispose(); }
		g2 = (Graphics2D) g.create();
		try {
			// overlay the border so that the cornered edges get a border as well
			// COLOR BLACK
			g2.setColor(Color.BLACK);
			g2.drawRoundRect(-1, -1, getWidth() + 1, getHeight() * 2,
					UIConstants.TitlePaneCurvature, UIConstants.TitlePaneCurvature);
		}
		finally { g2.dispose(); }
	}

	/**
	 * Obtain the paint for painting the avatar's gradient.
	 * 
	 * @return The gradient paint.
	 */
	private Paint getGradientPaint() {
		if(null == gradientPaint) {
			// COLOR 236, 243, 239, 255
			// COLOR 194, 208, 219, 255
			gradientPaint = new GradientPaint(new Point(0, 0),
					new Color(236, 243, 239, 255),
					new Point(0, getHeight()), new Color(194, 208, 219, 255));
		}
		return gradientPaint;
	}
}
