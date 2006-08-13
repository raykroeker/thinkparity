/*
 * Created on April 12, 2006, 12:37 PM
 */
package com.thinkparity.browser.application.browser.display.renderer.tab;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.image.BufferedImage;

import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.plaf.ComponentUI;

import com.thinkparity.codebase.swing.AbstractJPanel;

import com.thinkparity.browser.application.browser.component.LabelFactory;

/**
 * @author raykroeker@thinkparity.com
 * @version 1.1.2.1
 */
public class TabCellRenderer extends AbstractJPanel implements
        ListCellRenderer {

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    /** The padding label's left inset. */
    private static final int PADDING_INSET_LEFT = 12;

    /** The cell background image. */
    private BufferedImage background;

    /** The padding label's constraints. */
    private final GridBagConstraints paddingGBC;

    /** Creates new form MainCellRenderer */
    public TabCellRenderer() {
        super("MainCellRenderer");
        this.paddingGBC = new GridBagConstraints();
        this.paddingGBC.gridx = this.paddingGBC.gridy = 0;
        initComponents();
    }

    /**
     * Return a component that has been configured to display the specified
     * value. That component's <code>paint</code> method is then called to
     * "render" the cell.  If it is necessary to compute the dimensions
     * of a list because the list cells do not have a fixed size, this method
     * is called to generate a component on which <code>getPreferredSize</code>
     * can be invoked.
     * 
     * 
     * @param list The JList we're painting.
     * @param value The value returned by list.getModel().getElementAt(index).
     * @param index The cells index.
     * @param isSelected True if the specified cell was selected.
     * @param cellHasFocus True if the specified cell has the focus.
     * @return A component whose paint() method will render the specified value.
     * @see JList
     * @see ListSelectionModel
     * @see ListModel
     */
    public Component getListCellRendererComponent(final JList list, final Object value, final int index, final boolean isSelected, final boolean cellHasFocus) {
        final TabCell cell = (TabCell) value;

        remove(paddingJLabel);
        final GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, Math.round(PADDING_INSET_LEFT * cell.getTextInsetFactor()), 0, 0);
        add(paddingJLabel, gridBagConstraints);

        if(isSelected) {
            background = cell.getBackgroundSelected();
            nodeIconJLabel.setIcon(cell.getNodeIconSelected());
        }
        else {
            background = cell.getBackground();
            nodeIconJLabel.setIcon(cell.getNodeIcon());
        }
        setBorder(cell.getBorder());


        textJLabel.setFont(cell.getTextFont());
        textJLabel.setForeground(cell.getTextForeground());
        textJLabel.setText(cell.getText());

        setToolTipText(cell.getToolTip());

        infoIconJLabel.setIcon(cell.getInfoIcon());

        revalidate();
        repaint();

        return this;
    }

    /**
     * Calls the UI delegate's paint method, if the UI delegate
     * is non-<code>null</code>.  We pass the delegate a copy of the
     * <code>Graphics</code> object to protect the rest of the
     * paint code from irrevocable changes
     * (for example, <code>Graphics.translate</code>).
     * <p>
     * If you override this in a subclass you should not make permanent
     * changes to the passed in <code>Graphics</code>. For example, you
     * should not alter the clip <code>Rectangle</code> or modify the
     * transform. If you need to do these operations you may find it
     * easier to create a new <code>Graphics</code> from the passed in
     * <code>Graphics</code> and manipulate it. Further, if you do not
     * invoker super's implementation you must honor the opaque property,
     * that is
     * if this component is opaque, you must completely fill in the background
     * in a non-opaque color. If you do not honor the opaque property you
     * will likely see visual artifacts.
     * <p>
     * The passed in <code>Graphics</code> object might
     * have a transform other than the identify transform
     * installed on it.  In this case, you might get
     * unexpected results if you cumulatively apply
     * another transform.
     * 
     * 
     * @param g the <code>Graphics</code> object to protect
     * @see #paint
     * @see ComponentUI
     */
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2 = (Graphics2D) g.create();
        // we draw at y=1 because there is a single pixel white
        // border at the top of every main cell
        try { g2.drawImage(background, getInsets().left, getInsets().top, this); }
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

        paddingJLabel = LabelFactory.create();
        nodeIconJLabel = LabelFactory.create();
        textJLabel = LabelFactory.create();
        infoIconJLabel = LabelFactory.create();

        setLayout(new java.awt.GridBagLayout());

        paddingJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Invisible1x20.png")));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 0);
        add(paddingJLabel, gridBagConstraints);

        nodeIconJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/MainCellExpand.png")));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 6);
        add(nodeIconJLabel, gridBagConstraints);

        textJLabel.setText("!Document!");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        add(textJLabel, gridBagConstraints);

        infoIconJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Key.png")));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 32);
        add(infoIconJLabel, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel infoIconJLabel;
    private javax.swing.JLabel nodeIconJLabel;
    private javax.swing.JLabel paddingJLabel;
    private javax.swing.JLabel textJLabel;
    // End of variables declaration//GEN-END:variables
}
