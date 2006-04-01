/*
 * WindowTitleDecoration.java
 *
 * Created on March 31, 2006, 5:43 PM
 */

package com.thinkparity.browser.platform.application.window;

import com.thinkparity.browser.platform.util.ImageIOUtil;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.event.MouseEvent;
import javax.swing.Icon;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

/**
 *
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class WindowTitleDecoration extends JPanel {

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

    private static final Color C_1 = new Color(243, 245, 247, 255);

    private static final Color C_2 = new Color(213, 219, 225, 255);

    private static final Color C_3 = new Color(246, 247, 249, 255);

    private static final Color C_4 = new Color(231, 234, 239, 255);

    private static final Color C_5 = new Color(160, 161, 163, 255);

    private static final Color C_6 = new Color(217, 218, 220, 255);

    private static final Icon CLOSE_ICON;

    private static final Icon CLOSE_ROLLOVER_ICON;

    static {
        CLOSE_ICON = ImageIOUtil.readIcon("CloseButton.png");
        CLOSE_ROLLOVER_ICON = ImageIOUtil.readIcon("CloseButtonRollover.png");
    }

    /**
     * Create a WindowTitleDecoration
     */
    public WindowTitleDecoration() {
    	super();
    	final MouseInputAdapter mouseInputAdapter = new MouseInputAdapter() {
    		int offsetX;
    		int offsetY;
                public void mouseDragged(final MouseEvent e) {
                    final java.awt.Window window = SwingUtilities.getWindowAncestor(WindowTitleDecoration.this);
                    final Point newL = window.getLocation();
                    newL.x += e.getPoint().x - offsetX;
                    newL.y += e.getPoint().y - offsetY;
                    window.setLocation(newL);
                }
                public void mousePressed(final MouseEvent e) {
                    offsetX = e.getPoint().x;
                    offsetY = e.getPoint().y;
                }
    	};
    	addMouseListener(mouseInputAdapter);
    	addMouseMotionListener(mouseInputAdapter);
        initComponents();
    }

    protected void paintComponent(final Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            Paint gradient = new GradientPaint(0, 0, C_1, 0, getHeight() / 2, C_2);
            g2.setPaint(gradient);
            g2.fillRect(0, 0, getWidth(), getHeight() / 2);

            gradient = new GradientPaint(0, getHeight() / 2, C_2, 0, getHeight(), C_3);
            g2.setPaint(gradient);
            g2.fillRect(0, getHeight() / 2, getWidth(), getHeight());
        }
        finally { g2.dispose(); }

        g2 = (Graphics2D) g.create();
        try {
            g2.setColor(C_4);
            g2.drawLine(0, getHeight() - 3, getWidth(), getHeight() - 3);

            g2.setColor(C_5);
            g2.drawLine(0, getHeight() - 2, getWidth(), getHeight() - 2);

            g2.setColor(C_6);
            g2.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
        }
        finally { g2.dispose(); }
        
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        javax.swing.JLabel fillJLabel;
        java.awt.GridBagConstraints gridBagConstraints;

        closeJLabel = new javax.swing.JLabel();
        fillJLabel = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        closeJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/CloseButton.png")));
        closeJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                closeJLabelMouseClicked(e);
            }
            public void mouseEntered(java.awt.event.MouseEvent e) {
                closeJLabelMouseEntered(e);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                closeJLabelMouseExited(e);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 6, 8);
        add(closeJLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        add(fillJLabel, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents

    private void closeJLabelMouseClicked(java.awt.event.MouseEvent e) {//GEN-FIRST:event_closeJLabelMouseClicked
        final java.awt.Window window = SwingUtilities.getWindowAncestor(this);
        closeJLabel.setIcon(CLOSE_ICON);
        window.dispose();
    }//GEN-LAST:event_closeJLabelMouseClicked

    private void closeJLabelMouseExited(java.awt.event.MouseEvent e) {//GEN-FIRST:event_closeJLabelMouseExited
        closeJLabel.setIcon(CLOSE_ICON);
    }//GEN-LAST:event_closeJLabelMouseExited

    private void closeJLabelMouseEntered(java.awt.event.MouseEvent e) {//GEN-FIRST:event_closeJLabelMouseEntered
        closeJLabel.setIcon(CLOSE_ROLLOVER_ICON);
    }//GEN-LAST:event_closeJLabelMouseEntered

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel closeJLabel;
    // End of variables declaration//GEN-END:variables
}
