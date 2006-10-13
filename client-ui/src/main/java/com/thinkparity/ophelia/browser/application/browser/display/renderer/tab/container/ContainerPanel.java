/*
 * Created On: October 6, 2006, 5:03 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.swing.border.BottomBorder;
import com.thinkparity.codebase.swing.border.TopBorder;

import com.thinkparity.ophelia.browser.Constants.Colors;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Colours;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.container.ContainerModel;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel;
import com.thinkparity.ophelia.model.container.ContainerDraft;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ContainerPanel extends DefaultTabPanel {
    
    /** The border for the bottom of the cell. */
    private static final Border BORDER_BOTTOM;
    
    /** The border for the bottom of the last cell. */
    private static final Border BORDER_LAST_BOTTOM;
    
    /** The border for the top of the cell. */
    private static final Border BORDER_TOP;
    
    /** The border for the top of a group of cells. */
    private static final Border BORDER_GROUP_TOP;
    
    /** The border colours. */
    private static final Color[] BORDER_COLOURS;
    
    /** The border colours for a the first cell of a group. */
    private static final Color[] BORDER_COLOURS_GROUP_TOP;
    
    /** Dimension of the cell. */
    private static final Dimension DIMENSION;
    
    static {
        BORDER_COLOURS = new Color[] {Colours.MAIN_CELL_DEFAULT_BORDER, Color.WHITE};
        BORDER_COLOURS_GROUP_TOP = new Color[] {Colours.MAIN_CELL_DEFAULT_BORDER,
                Colours.MAIN_CELL_DEFAULT_BORDER, Colours.MAIN_CELL_DEFAULT_BORDER, Color.WHITE};
        
        BORDER_TOP = new TopBorder(BORDER_COLOURS, 2, new Insets(2,0,0,0));
        BORDER_GROUP_TOP = new TopBorder(BORDER_COLOURS_GROUP_TOP, 4, new Insets(4,0,0,0));
        BORDER_BOTTOM = new BottomBorder(Color.WHITE);
        BORDER_LAST_BOTTOM = new BottomBorder(BORDER_COLOURS, 2, new Insets(0,0,2,0));
        
        DIMENSION = new Dimension(50,23);
    }

    /** A <code>Container</code>. */
    private Container container;

    /** The container panel's model. */
    private final ContainerModel model;

    /**
     * Create ContainerPanel.
     * 
     */
    public ContainerPanel(final ContainerModel model) {
        super();
        this.model = model;
        initComponents();
        installMouseOverTracker(containerNameJLabel);
    }

    /**
     * Obtain the container id.
     * 
     * @return A container id <code>Long</code>.
     */
    public Long getContainerId() {
        return container.getId();
    }

    /**
     * Obtain the id for the tab panel.  In this case it's a container id.
     *
     * @return An id <code>Object</code>.
     */
    @Override
    public Object getId() {
        return new StringBuffer(getClass().getName()).append("//")
            .append(container.getId()).toString();
    }

    /**
     * Set the container and its draft.
     * 
     * @param container
     *            A <code>Container</code>.
     * @param draft
     *            A <code>ContainerDraft</code>.
     */
    public void setContainer(final Container container,
            final ContainerDraft draft) {
        this.container = container;

        containerNameJLabel.setText(container.getName());
        if (container.isDraft()) {
            draftOwnerJLabel.setText(draft.getOwner().getName());
        } else {
            draftOwnerJLabel.setText("");
        }
    }
    
    /**
     * Get the container associated with this container panel.
     */
    public Container getContainer() {
        return container;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel#triggerSingleClick(java.awt.event.MouseEvent)
     */
    @Override
    protected void triggerSingleClick(final MouseEvent e) {
        if (isSetMouseOver()) {
            model.triggerExpand(this);
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel#triggerDoubleClick(java.awt.event.MouseEvent)
     */
    @Override
    protected void triggerDoubleClick(MouseEvent e) {
        if (!isSetMouseOver()) {
            model.triggerExpand(this);
        }
    }

    /**
     * Show the popup menu for the container.
     * 
     * @param e
     *            A <code>MouseEvent</code>.
     */
    @Override
    protected void triggerPopup(final Component invoker, final MouseEvent e) {
        new ContainerPopup(model, container).show(invoker, e);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        westPaddingJLabel = new javax.swing.JLabel();
        iconJLabel = new javax.swing.JLabel();
        containerNameJLabel = new javax.swing.JLabel();
        draftOwnerJLabel = new javax.swing.JLabel();
        eastPaddingJLabel = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(32767, 23));
        setMinimumSize(new java.awt.Dimension(120, 23));
        setPreferredSize(new java.awt.Dimension(120, 23));
        westPaddingJLabel.setFocusable(false);
        westPaddingJLabel.setMaximumSize(new java.awt.Dimension(4, 20));
        westPaddingJLabel.setMinimumSize(new java.awt.Dimension(4, 20));
        westPaddingJLabel.setPreferredSize(new java.awt.Dimension(4, 20));

        iconJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/IconContainer.png")));

        containerNameJLabel.setText("!Package!");
        containerNameJLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        containerNameJLabel.setMaximumSize(new java.awt.Dimension(500, 20));
        containerNameJLabel.setMinimumSize(new java.awt.Dimension(1, 20));
        containerNameJLabel.setPreferredSize(new java.awt.Dimension(1, 20));

        draftOwnerJLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        draftOwnerJLabel.setText("!Draft Owner!");
        draftOwnerJLabel.setMaximumSize(new java.awt.Dimension(500, 20));
        draftOwnerJLabel.setMinimumSize(new java.awt.Dimension(50, 20));
        draftOwnerJLabel.setPreferredSize(new java.awt.Dimension(50, 20));

        eastPaddingJLabel.setFocusable(false);
        eastPaddingJLabel.setMaximumSize(new java.awt.Dimension(20, 20));
        eastPaddingJLabel.setMinimumSize(new java.awt.Dimension(20, 20));
        eastPaddingJLabel.setPreferredSize(new java.awt.Dimension(20, 20));

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(westPaddingJLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(iconJLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(containerNameJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 11, Short.MAX_VALUE)
                .add(draftOwnerJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(eastPaddingJLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(westPaddingJLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(containerNameJLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(draftOwnerJLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(eastPaddingJLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(iconJLabel)))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel#setMouseOver(java.lang.Boolean)
     */
    @Override
    public void setMouseOver(final Boolean mouseOver) {
        super.setMouseOver(mouseOver);
        if (mouseOver) {
            containerNameJLabel.setText(new StringBuffer("<html><u>")
                .append(container.getName()).append("</u></html>").toString());
        } else {
            containerNameJLabel.setText(new StringBuffer("<html>")
                .append(container.getName()).append("</html>").toString());
        }
        repaint();
    }
    
    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel#formMousePressed(java.awt.event.MouseEvent)
     */
    @Override
    protected void formMousePressed(MouseEvent e) {
        super.formMousePressed(e);
        if (e.getButton()==MouseEvent.BUTTON1) {
            model.selectTabPanel(this);
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel#formMouseReleased(java.awt.event.MouseEvent)
     */
    @Override
    protected void formMouseReleased(MouseEvent e) {
        super.formMouseReleased(e);
        if (e.isPopupTrigger()) {
            model.selectTabPanel(this);
        }
    }
    
    /**
     * Adjust foreground color.  
     */
    public void adjustForegroundColor() {       
        final Color color;
        if (isSelected() && isExpanded()) {
            color = Colors.Swing.LIST_SELECTION_INACTIVE_FG; 
        } else if (isSelected()) {
            color = Colors.Swing.LIST_SELECTION_FG;
        } else {
            color = Colors.Swing.LIST_FG;
        }
        containerNameJLabel.setForeground(color);
        draftOwnerJLabel.setForeground(color);
    }
    
    /**
     * Get the background color.
     * 
     * @param index
     *          Index into the display list.
     * @return Background color.
     */
    public Color getBackground(final int index) {
        final Color color;
        if (isSelected() && isExpanded()) {
            color = Colors.Swing.LIST_SELECTION_INACTIVE_BG; 
        } else if (isSelected()) {
            color = Colors.Swing.LIST_SELECTION_BG;
        } else if (0 == index % 2) {
            color = Colors.Swing.LIST_EVEN_BG;
        } else {
            color = Colors.Swing.LIST_ODD_BG;
        }
        
        return color;
    }  

    /**
     * Get the preferred size.
     * 
     * @param last
     *          True if this is the last entity.
     * @return The preferred size <code>Dimension</code>.
     */   
    public Dimension getPreferredSize(final Boolean last) {
        final Dimension dimension;
        
        if (last || isFirstNonDraft()) {
            // Adjust height to account for border thickness
            dimension = new Dimension(DIMENSION);
            if (last) {
                dimension.height += 1;
            }
            if (isFirstNonDraft()) {
                dimension.height += 2;
            }
        } else {
            dimension = DIMENSION;
        }
        
        return dimension;
    }
    
    /**
     * Get the border for the package.
     * 
     * @param last
     *          True if this is the last entity.
     * @return A border.
     */
    public Border getBorder(final Boolean last) {
        final Border topBorder;
        final Border bottomBorder;
        if (isFirstNonDraft()) {
            topBorder = BORDER_GROUP_TOP;
        } else {
            topBorder = BORDER_TOP;
        }
        if (last) {
            bottomBorder = BORDER_LAST_BOTTOM;
        } else {
            bottomBorder = BORDER_BOTTOM;
        }
        
        return BorderFactory.createCompoundBorder(topBorder, bottomBorder);
    }
    
    /**
     * Determine if the panel is expanded.
     * 
     * @param tabPanel
     *            A <code>TabPanel</code>.
     * @return True if the panel is expanded; false otherwise.
     */
    private Boolean isExpanded() {
        return model.isExpanded(this);
    }
    
    /**
     * Determine if the panel is selected.
     * 
     * @param tabPanel
     *            A <code>TabPanel</code>.
     * @return True if the panel is selected; false otherwise.
     */
    private Boolean isSelected() {
        return model.isSelected(this);
    }
    
    /**
     * Determine if the panel is the first non-draft.
     * 
     * @param tabPanel
     *            A <code>TabPanel</code>.
     * @return True if the panel is the first non-draft; false otherwise.
     */
    private Boolean isFirstNonDraft() {
        return model.isFirstNonDraft(this);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel containerNameJLabel;
    private javax.swing.JLabel draftOwnerJLabel;
    private javax.swing.JLabel eastPaddingJLabel;
    private javax.swing.JLabel iconJLabel;
    private javax.swing.JLabel westPaddingJLabel;
    // End of variables declaration//GEN-END:variables
}
