/*
 * Created On: October 6, 2006, 1:42 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.TransferHandler;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.Resizer;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.Resizer.ResizeEdges;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel;

/**
 * <b>Title:</b>thinkParity Tab Panel Avatar<br>
 * <b>Description:</b>An abstraction of a tab avatar. A tab avatar is the
 * visual representation of the avatar. It requires a model of a specific type
 * to feed it data and generally maintain it via a DefaultList model
 * intermediary.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 * @param <T> The model type.
 */
public abstract class TabPanelAvatar<T extends TabModel> extends TabAvatar<T> {

    /**
     * A client property key <code>String</code> mapping to the panels'
     * transfer handlers.
     */
    private static final String CPK_PANEL_TRANSFER_HANDLER;

    static {
        CPK_PANEL_TRANSFER_HANDLER = "TabPanelAvatar#panelTransferHandler";
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private final javax.swing.JLabel fillJLabel = new javax.swing.JLabel();
    private final javax.swing.JPanel tabJPanel = new javax.swing.JPanel();
    // End of variables declaration//GEN-END:variables

    /**
     * The set of <code>GridBagConstraints</code> used when adding a fill
     * component.
     */
    private final GridBagConstraints fillConstraints;

    /** The set of <code>GridBagConstraints</code> used when adding a panel. */
    private final GridBagConstraints panelConstraints;

    /**
     * A <code>TransferHandler</code> which dispatches all panel import export
     * requests to the model.
     */
    private final TransferHandler panelTransferHandler;

    /** A popup delegate for the tab. */
    private TabAvatarPopupDelegate popupDelegate;

    /**
     * Creates TabPanelAvatar.
     * 
     */
    public TabPanelAvatar(final AvatarId id, final T model) {
    	super(id, model);
        this.fillConstraints = new GridBagConstraints();
        this.fillConstraints.fill = GridBagConstraints.HORIZONTAL;
        this.fillConstraints.weightx = 1.0F;
        this.fillConstraints.weighty = 1.0F;
        this.fillConstraints.gridx = 0;
        this.fillConstraints.gridy = GridBagConstraints.RELATIVE;
        this.panelConstraints = new GridBagConstraints();
        this.panelConstraints.fill = GridBagConstraints.BOTH;
        this.panelConstraints.gridx = 0;
        this.panelTransferHandler = new TransferHandler() {
            @Override
            public boolean canImport(final JComponent comp,
                    final DataFlavor[] transferFlavors) {
                logger.logApiId();
                logger.logVariable("comp", comp);
                logger.logVariable("transferFlavors", transferFlavors);
                return model.canImportData((TabPanel) comp, transferFlavors);
            }
            @Override
            public boolean importData(final JComponent comp,
                    final Transferable transferable) {
                logger.logApiId();
                logger.logVariable("comp", comp);
                logger.logVariable("transferable", transferable);
                try {
                    model.importData((TabPanel) comp, transferable);
                    return true;
                } catch (final Throwable t) {
                    logger.logError(t, "Could not import data {0} for {1}.",
                            transferable, comp);
                    return false;
                }
            }
        };
    	installDataListener();
        initComponents();
        installResizer();
        setTransferHandler(new TransferHandler() {
            @Override
            public boolean canImport(final JComponent comp,
                    final DataFlavor[] transferFlavors) {
                logger.logApiId();
                logger.logVariable("comp", comp);
                logger.logVariable("transferFlavors", transferFlavors);
                return model.canImportData(transferFlavors);
            }
            @Override
            public boolean importData(final JComponent comp,
                    final Transferable transferable) {
                logger.logApiId();
                logger.logVariable("comp", comp);
                logger.logVariable("transferable", transferable);
                try {
                    model.importData(transferable);
                    return true;
                } catch (final Throwable t) {
                    logger.logError(t, "Could not import data {0}.",
                            transferable);
                    return false;
                }
            }
        });
    }

    /**
     * @see com.thinkparity.codebase.swing.AbstractJPanel#debug()
     * 
     */
    @Override
    public void debug() {
        final Component[] components = tabJPanel.getComponents();
        logger.logDebug("{0} components.", components.length);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabAvatar#reload()
     * 
     */
    @Override
	public void reload() {
    	super.reload();
    }

    /**
     * Obtain the popup delegate.
     * 
     * @return A <code>TabAvatarPopupDelegate</code>.
     */
    protected TabAvatarPopupDelegate getPopupDelegate() {
        return popupDelegate;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#getResizeEdges()
     * 
     */
    @Override
    protected ResizeEdges getResizeEdges() {
        return Resizer.ResizeEdges.MIDDLE;
    }

    /**
     * Set the popup delegate.
     * 
     * @param popupDelegate
     *            A <code>TabAvatarPopupDelegate</code>.
     */
    protected void setPopupDelegate(final TabAvatarPopupDelegate popupDelegate) {
        this.popupDelegate = popupDelegate;
    }

    /**
     * Add the fill component.
     * 
     * @param listSize
     *            The <code>int</code> size of the list.
     */
    private void addFill(final int listSize) {
        tabJPanel.add(fillJLabel, fillConstraints, listSize);
    }
    
    /**
     * Add a panel from a model at an index.
     * 
     * @param index
     *            An <code>int</code> index.
     * @param panel
     *            A <code>TabPanel</code>.
     */
    private void addPanel(final int index, final TabPanel panel) {
        final JComponent jComponent = (JComponent) panel;
        // property to protect against adding it twice
        final TransferHandler transferHandler =
            (TransferHandler) jComponent.getClientProperty(CPK_PANEL_TRANSFER_HANDLER);
        if (null == transferHandler) {
            // setup a transfer handler for each panel added
            jComponent.setTransferHandler(panelTransferHandler);
            jComponent.putClientProperty(CPK_PANEL_TRANSFER_HANDLER, panelTransferHandler);
        }
        panelConstraints.gridy = index;
        tabJPanel.add((Component) panel, panelConstraints.clone(), index);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        final javax.swing.JPanel headerJPanel = new javax.swing.JPanel();
        final javax.swing.JLabel paddingJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel orderByJLabel = new javax.swing.JLabel();
        final javax.swing.JScrollPane tabJScrollPane = new javax.swing.JScrollPane();

        setLayout(new java.awt.GridBagLayout());

        headerJPanel.setLayout(new java.awt.GridBagLayout());

        headerJPanel.setMaximumSize(new java.awt.Dimension(5000, 19));
        headerJPanel.setMinimumSize(new java.awt.Dimension(128, 19));
        headerJPanel.setOpaque(false);
        headerJPanel.setPreferredSize(new java.awt.Dimension(128, 19));
        paddingJLabel.setFocusable(false);
        paddingJLabel.setMaximumSize(new java.awt.Dimension(4, 20));
        paddingJLabel.setMinimumSize(new java.awt.Dimension(4, 20));
        paddingJLabel.setPreferredSize(new java.awt.Dimension(4, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        headerJPanel.add(paddingJLabel, gridBagConstraints);

        orderByJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/SortButton.png")));
        orderByJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                orderByJLabelMouseClicked(e);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        headerJPanel.add(orderByJLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 4);
        add(headerJPanel, gridBagConstraints);

        tabJScrollPane.setBorder(null);
        tabJScrollPane.setPreferredSize(new java.awt.Dimension(256, 128));
        tabJPanel.setLayout(new java.awt.GridBagLayout());

        tabJPanel.setBackground(new java.awt.Color(255, 255, 255));
        tabJPanel.add(fillJLabel, new java.awt.GridBagConstraints());

        tabJScrollPane.setViewportView(tabJPanel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(tabJScrollPane, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents

    /**
     * Install the a data listener on the list model. This will translate the
     * model's data events into UI component events.
     * 
     */
    private void installDataListener() {
        final DefaultListModel listModel = model.getListModel();
        listModel.addListDataListener(new ListDataListener() {
            /**
             * @see javax.swing.event.ListDataListener#contentsChanged(javax.swing.event.ListDataEvent)
             * 
             */
            public void contentsChanged(final ListDataEvent e) {
                debug();
                removeFill();
                for (int i = e.getIndex0(); i <= e.getIndex1(); i++) {
                    removePanel(i);
                    addPanel(i, (TabPanel) listModel.get(i));
                }
                addFill(listModel.size());
                reloadPanels();
            }
            /**
             * @see javax.swing.event.ListDataListener#intervalAdded(javax.swing.event.ListDataEvent)
             * 
             */
            public void intervalAdded(final ListDataEvent e) {
                debug();
                removeFill();
                for (int i = e.getIndex0(); i <= e.getIndex1(); i++) {
                    addPanel(i, (TabPanel) listModel.get(i));
                }
                addFill(listModel.size());
                reloadPanels();
            }
            /**
             * @see javax.swing.event.ListDataListener#intervalRemoved(javax.swing.event.ListDataEvent)
             * 
             */
            public void intervalRemoved(final ListDataEvent e) {
                debug();
                removeFill();
                for (int i = e.getIndex0(); i <= e.getIndex1(); i++) {
                    removePanel(i);
                }
                addFill(listModel.size());
                reloadPanels();
            } 
        });
    }

    private void orderByJLabelMouseClicked(java.awt.event.MouseEvent e) {//GEN-FIRST:event_orderByJLabelMouseClicked
        final javax.swing.JLabel jLabel = (javax.swing.JLabel) e.getSource();
        popupDelegate.initialize(this, jLabel.getX(), jLabel.getY()
                + jLabel.getHeight(), jLabel.getWidth());
        popupDelegate.showForSort();
    }//GEN-LAST:event_orderByJLabelMouseClicked

    /**
     * Reload the panels display. The jpanel is revalidated.
     * 
     */
    private void reloadPanels() {
        tabJPanel.revalidate();
    }
    /**
     * Remove the fill component.
     *
     */
    private void removeFill() {
        tabJPanel.remove(fillJLabel);
    }

    /**
     * Remove a panel at an index.
     * 
     * @param index
     *            An <code>int</code> index.
     */
    private void removePanel(final int index) {
        tabJPanel.remove(index);
    }
}
