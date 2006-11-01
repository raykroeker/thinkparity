/*
 * WindowTitle.java
 *
 * Created on October 29, 2006, 4:11 PM
 */

package com.thinkparity.ophelia.browser.platform.application.window;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import com.thinkparity.ophelia.browser.Constants.Colors;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.Resizer;
import com.thinkparity.ophelia.browser.util.ImageIOUtil;

/**
 *
 * @author  Administrator
 */
public class WindowTitle extends javax.swing.JPanel {
    
    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    /** The start colour of the gradient. */
    private static final Color GRADIENT_PAINT_COLOUR_1 = Colors.Browser.Window.BG_GRAD_START;

    /** The finish colour of the gradient. */
    private static final Color GRADIENT_PAINT_COLOUR_2 = Colors.Browser.Window.BG_GRAD_FINISH;

    /** The colour of the single line below the title deocration. */
    private static final Color TITLE_BOTTOM_BORDER_COLOUR = Colors.Browser.Window.TITLE_BOTTOM_BORDER;
    
    /** Close label icon. */
    private static final Icon CLOSE_ICON = ImageIOUtil.readIcon("BrowserTitle_Close.png");
    
    /** Close label rollover icon. */
    private static final Icon CLOSE_ROLLOVER_ICON = ImageIOUtil.readIcon("BrowserTitle_CloseRollover.png");

    /** Creates new form WindowTitle */
    public WindowTitle(final String title) {
        initComponents();
        new Resizer(null, this, Boolean.TRUE, Resizer.ResizeEdges.TOP);
        titleJLabel.setText(title);
        closeJLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                SwingUtilities.getWindowAncestor(WindowTitle.this).dispose();
            }
            @Override
            public void mouseEntered(final MouseEvent e) {
                ((JLabel) e.getSource()).setIcon(CLOSE_ROLLOVER_ICON);
            }
            @Override
            public void mouseExited(final MouseEvent e) {
                ((JLabel) e.getSource()).setIcon(CLOSE_ICON);
            }            
        });
    }
    
    /** @see javax.swing.JComponent#paintComponent(java.awt.Graphics) */
    protected void paintComponent(final Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            // vertical gradients
            final Paint gPaint =
                new GradientPaint(0, 0, GRADIENT_PAINT_COLOUR_1, 0, getHeight(), GRADIENT_PAINT_COLOUR_2);
            g2.setPaint(gPaint);
            g2.fillRect(0, 0, getWidth(), getHeight());            
        }
        finally { g2.dispose(); }

        g2 = (Graphics2D) g.create();
        try {
          g2.setColor(TITLE_BOTTOM_BORDER_COLOUR);
          g2.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
        }
        finally { g2.dispose(); }        
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        titleJLabel = new javax.swing.JLabel();
        closeJLabel = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        titleJLabel.setFont(new java.awt.Font("Times New Roman", 1, 14));
        titleJLabel.setText("!Title!");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 0);
        add(titleJLabel, gridBagConstraints);

        closeJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/BrowserTitle_Close.png")));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 1);
        add(closeJLabel, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel closeJLabel;
    private javax.swing.JLabel titleJLabel;
    // End of variables declaration//GEN-END:variables

}
