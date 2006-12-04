/*
 * Created On: October 7, 2006, 10:25 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JList;

import com.thinkparity.codebase.FuzzyDateFormat;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta.Delta;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.container.ContainerDraft;

import com.thinkparity.ophelia.browser.Constants.Images;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.FileIconReader;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.MainPanelImageCache;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.MainPanelImageCache.TabPanelIcon;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanelPopupDelegate;
import com.thinkparity.ophelia.browser.util.localization.MainCellL18n;

/**
 * <b>Title:</b>thinkParity Container Versions Panel<br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ContainerVersionPanel extends DefaultTabPanel {

    /**
     * A client property key <code>String</code> for the is popup trigger
     * property.
     */
    private static final String CPK_IS_POPUP_TRIGGER;

    /** A <code>FuzzyDateFormat</code>. */
    private static final FuzzyDateFormat FUZZY_DATE_FORMAT;

    /** Number of rows visible in the expanded package lists. */
    private static final Integer NUMBER_VISIBLE_ROWS;

    static {
        CPK_IS_POPUP_TRIGGER = ContainerVersionPanel.class.getName() + "#isPopupTrigger";
        FUZZY_DATE_FORMAT = new FuzzyDateFormat();
        NUMBER_VISIBLE_ROWS = 5;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private final javax.swing.JList eastJList = new javax.swing.JList();
    private final javax.swing.JList westJList = new javax.swing.JList();
    // End of variables declaration//GEN-END:variables

    /** The <code>Container</code>. */
    protected Container container;

    /** A container tab's <code>DefaultActionDelegate</code>. */
    private ActionDelegate actionDelegate;

    /** The clipped down background images on the left. */
    private BufferedImage[] clippedContainerBackgroundLeft;

    /** The clipped down background image on the right. */
    private BufferedImage clippedContainerBackgroundRight;

    /** A file icon reader. */
    private final FileIconReader fileIconReader;

    /** An image cache. */
    private final MainPanelImageCache imageCache;

    /** The panel localization. */
    private final MainCellL18n localization;

    /** An apache logger. */
    private final Log4JWrapper logger;

    /** A container tab's <code>PopupDelegate</code>. */
    private PopupDelegate popupDelegate;

    /** The scaled up background images on the left. */
    private BufferedImage[] scaledContainerBackgroundLeft;
    
    /** The scaled up background image on the right. */
    private BufferedImage scaledContainerBackgroundRight;

    /** The version's content list model. */
    private final DefaultListModel versionsContentModel;

    /** The version's list model. */
    private final DefaultListModel versionsModel;

    /**
     * Create ContainerVersionsPanel
     * 
     */
    public ContainerVersionPanel() {
        super();
        this.versionsModel = new DefaultListModel();
        this.versionsContentModel = new DefaultListModel();
        this.imageCache = new MainPanelImageCache();
        this.fileIconReader = new FileIconReader();
        this.logger = new Log4JWrapper();
        this.localization = new MainCellL18n("ContainerVersionsPanel");
        initBackgroundImages();
        initComponents();
    }

    /**
     * Obtain actionDelegate.
     *
     * @return A ContainerTabActionDelegate.
     */
    public ActionDelegate getActionDelegate() {
        return actionDelegate;
    }

    /**
     * Obtain the container.
     * 
     * @return A <code>Container</code>.
     */
    public Container getContainer() {
        return container;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel#getId()
     */
    @Override
    public Object getId() {
        return new StringBuffer(getClass().getName()).append("//")
                .append(container.getId()).toString();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel#getPanelPopupDelegate()
     *
     */
    public TabPanelPopupDelegate getPanelPopupDelegate() {
        return popupDelegate;
    }

    /**
     * Set actionDelegate.
     *
     * @param actionDelegate
     *		A ContainerTabActionDelegate.
     */
    public void setActionDelegate(final ActionDelegate actionDelegate) {
        this.actionDelegate = actionDelegate;
    }

    /**
     * Set the container and its draft.
     * 
     * @param container
     *            The <code>Container</code>.
     * @param draft
     *            The <code>ContainerDraft</code>.
     */
    public void setPanelData(
            final Container container,
            final ContainerDraft draft,
            final List<ContainerVersion> versions,
            final Map<ContainerVersion, Map<DocumentVersion, Delta>> documentVersions,
            final Map<ContainerVersion, Map<User, ArtifactReceipt>> publishedTo,
            final Map<ContainerVersion, User> publishedBy) {
        this.container = container;
        // if a draft exists and the user is the creator of the draft or the
        // user has the latest version of the container display a draft element
        if (null != draft && container.isLocalDraft()
                || (container.isDraft() && container.isLatest())) {
            versionsModel.addElement(new DraftCell(draft));
        }
        // display all version elements
        for (final ContainerVersion version : versions) {
            versionsModel.addElement(new VersionCell(version, documentVersions
                    .get(version), publishedTo.get(version), publishedBy
                    .get(version)));
        }
        selectFirstVersion();
    }

    /**
     * Set popupDelegate.
     *
     * @param popupDelegate
     *		A ContainerTabPopupDelegate.
     */
    public void setPopupDelegate(final PopupDelegate popupDelegate) {
        this.popupDelegate = popupDelegate;
    }

    /**
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     *
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        final int rightWidth = getWidth() / 2;
        final int leftWidth = getWidth() - rightWidth - 5;
        final int selectedIndex = westJList.getSelectedIndex() - westJList.getFirstVisibleIndex();        
        final int imageIndex;
        if (westJList.getSelectedIndex() < 0 ) {
            imageIndex = 0;
        } else if ((selectedIndex < 0 ) || (selectedIndex >= NUMBER_VISIBLE_ROWS)) {
            imageIndex = 0;
        } else {
            imageIndex = selectedIndex + 1;
        }
        
        // Prepare the right side.        
        //  - Resize the scaled background image if it is not large enough. This operation is a bit slow
        // so we only use it to make the image larger. This covers the unlikely scenario that the width
        // becomes larger than the screen size.
        //  - Then clip the background image if it is too large. This operation is quicker than scaling.
        if ((null == scaledContainerBackgroundRight) || scaledContainerBackgroundRight.getWidth() < rightWidth) {
            initBackgroundImages(rightWidth);
        }
        if ((null == clippedContainerBackgroundRight) || (clippedContainerBackgroundRight.getWidth() != rightWidth)) {
            clippedContainerBackgroundRight = scaledContainerBackgroundRight.getSubimage(
                    0, 0, rightWidth, getHeight());
        }
        
        // Prepare the left side.
        if ((null == scaledContainerBackgroundLeft[imageIndex]) || scaledContainerBackgroundLeft[imageIndex].getWidth() < leftWidth) {
            initBackgroundImages(getWidth());
        }
        if ((null == clippedContainerBackgroundLeft[imageIndex]) || (clippedContainerBackgroundLeft[imageIndex].getWidth() != leftWidth)) {
            clippedContainerBackgroundLeft[imageIndex] = scaledContainerBackgroundLeft[imageIndex].getSubimage(
                    0, 0, leftWidth, getHeight());
        }
                
        // Paint
        final Graphics g2 = g.create();
        try {
            g2.drawImage(clippedContainerBackgroundLeft[imageIndex], 0, 0, leftWidth, getHeight(), null);
            g2.drawImage(Images.BrowserTitle.CONTAINER_BACKGROUND_MID[imageIndex], leftWidth, 0, 5, getHeight(), null);
            g2.drawImage(clippedContainerBackgroundRight, getWidth()-rightWidth, 0, rightWidth, getHeight(), null);
        }
        finally { g2.dispose(); }
    }

    private void eastJListFocusGained(java.awt.event.FocusEvent e) {//GEN-FIRST:event_eastJListFocusGained
        jListFocusGained((JList) e.getSource());
    }//GEN-LAST:event_eastJListFocusGained

    private void eastJListFocusLost(java.awt.event.FocusEvent e) {//GEN-FIRST:event_eastJListFocusLost
        jListFocusLost((JList) e.getSource());
    }//GEN-LAST:event_eastJListFocusLost

    private void eastJListMouseClicked(final java.awt.event.MouseEvent e) {// GEN-FIRST:event_eastJListMouseClicked
        jListMouseClicked((JList) e.getSource(), e);
    }// GEN-LAST:event_eastJListMouseClicked

    private void eastJListMousePressed(final java.awt.event.MouseEvent e) {// GEN-FIRST:event_eastJListMousePressed
        jListMousePressed((JList) e.getSource(), e);
    }// GEN-LAST:event_eastJListMousePressed

    private void eastJListMouseReleased(final java.awt.event.MouseEvent e) {// GEN-FIRST:event_eastJListMouseReleased
        jListMouseReleased((JList) e.getSource(), e);
    }

    /**
     * Get the document icon, ie. associated with the file extension.
     * 
     * @return The document icon.
     */
    private ImageIcon getDocumentIcon(final Document document) {
        return fileIconReader.getIcon(document);
    }

    /**
     * Get the document icon, ie. associated with the file extension.
     * 
     * @return The document icon.
     */
    private ImageIcon getDocumentIcon(final DocumentVersion documentVersion) {
        return fileIconReader.getIcon(documentVersion);
    }

    /**
     * Initialize the background scaling and clipping images.
     * Note that index 0 in the arrays corresponds to "none selected".
     *
     */
    private void initBackgroundImages() {
        if (null == scaledContainerBackgroundLeft) {
            scaledContainerBackgroundLeft = new BufferedImage[NUMBER_VISIBLE_ROWS + 1];
        }
        if (null == clippedContainerBackgroundLeft) {
            clippedContainerBackgroundLeft = new BufferedImage[NUMBER_VISIBLE_ROWS + 1];
        }
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        initBackgroundImages((int)screenSize.getWidth());
    }
    
    /**
     * Initialize background images to the specified size (or larger).
     * 
     * @param newDimension
     *      New dimension.
     */
    private void initBackgroundImages(final int newWidth) {
        // Set up and if necessary scale the left background images.
        // Note that index 0 corresponds to "none selected".
        for (int index = 0; index <= NUMBER_VISIBLE_ROWS; index++) {
            if (newWidth <= Images.BrowserTitle.CONTAINER_BACKGROUND_LEFT[index].getWidth()) {
                scaledContainerBackgroundLeft[index] = Images.BrowserTitle.CONTAINER_BACKGROUND_LEFT[index];
            } else {
                final Image image = Images.BrowserTitle.CONTAINER_BACKGROUND_LEFT[index].getScaledInstance(
                        newWidth, Images.BrowserTitle.CONTAINER_BACKGROUND_LEFT[index].getHeight(), Image.SCALE_FAST);
                scaledContainerBackgroundLeft[index] = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
                final Graphics g2 = scaledContainerBackgroundLeft[index].createGraphics();
                try {
                    g2.drawImage(image, 0, 0, null);
                }
                finally { g2.dispose(); }
            }
        }

        // Set up and if necessary scale the right background image
        if (newWidth <= Images.BrowserTitle.CONTAINER_BACKGROUND_RIGHT.getWidth()) {
            scaledContainerBackgroundRight = Images.BrowserTitle.CONTAINER_BACKGROUND_RIGHT;
        } else {
            final Image image = Images.BrowserTitle.CONTAINER_BACKGROUND_RIGHT.getScaledInstance(
                    newWidth, Images.BrowserTitle.CONTAINER_BACKGROUND_RIGHT.getHeight(), Image.SCALE_FAST);
            scaledContainerBackgroundRight = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
            final Graphics g2 = scaledContainerBackgroundRight.createGraphics();
            try {
                g2.drawImage(image, 0, 0, null);
            }
            finally { g2.dispose(); }
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        final javax.swing.JSplitPane jSplitPane = new javax.swing.JSplitPane();
        final javax.swing.JPanel westJPanel = new javax.swing.JPanel();
        final javax.swing.JScrollPane westJScrollPane = new javax.swing.JScrollPane();
        westJScrollPane.getViewport().setOpaque(false);
        final javax.swing.JPanel eastJPanel = new javax.swing.JPanel();
        final javax.swing.JScrollPane eastJScrollPane = new javax.swing.JScrollPane();
        eastJScrollPane.getViewport().setOpaque(false);

        setLayout(new java.awt.GridBagLayout());

        jSplitPane.setBorder(null);
        jSplitPane.setDividerSize(0);
        jSplitPane.setResizeWeight(0.5);
        jSplitPane.setMinimumSize(new java.awt.Dimension(55, 75));
        jSplitPane.setOpaque(false);
        westJPanel.setLayout(new java.awt.GridBagLayout());

        westJPanel.setOpaque(false);
        westJScrollPane.setBorder(null);
        westJScrollPane.setOpaque(false);
        westJList.setModel(versionsModel);
        westJList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        westJList.setCellRenderer(new WestVersionCellRenderer());
        westJList.setOpaque(false);
        westJList.setVisibleRowCount(NUMBER_VISIBLE_ROWS);
        westJList.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                westJListFocusGained(e);
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                westJListFocusLost(e);
            }
        });
        westJList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent e) {
                westJListValueChanged(e);
            }
        });
        westJList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                westJListMouseClicked(e);
            }
            public void mousePressed(java.awt.event.MouseEvent e) {
                westJListMousePressed(e);
            }
            public void mouseReleased(java.awt.event.MouseEvent e) {
                westJListMouseReleased(e);
            }
        });

        westJScrollPane.setViewportView(westJList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        westJPanel.add(westJScrollPane, gridBagConstraints);

        jSplitPane.setLeftComponent(westJPanel);

        eastJPanel.setLayout(new java.awt.GridBagLayout());

        eastJPanel.setOpaque(false);
        eastJScrollPane.setBorder(null);
        eastJScrollPane.setOpaque(false);
        eastJList.setModel(versionsContentModel);
        eastJList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        eastJList.setCellRenderer(new EastVersionCellRenderer());
        eastJList.setOpaque(false);
        eastJList.setVisibleRowCount(NUMBER_VISIBLE_ROWS);
        eastJList.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                eastJListFocusGained(e);
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                eastJListFocusLost(e);
            }
        });
        eastJList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                eastJListMouseClicked(e);
            }
            public void mousePressed(java.awt.event.MouseEvent e) {
                eastJListMousePressed(e);
            }
            public void mouseReleased(java.awt.event.MouseEvent e) {
                eastJListMouseReleased(e);
            }
        });

        eastJScrollPane.setViewportView(eastJList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        eastJPanel.add(eastJScrollPane, gridBagConstraints);

        jSplitPane.setRightComponent(eastJPanel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 34, 0, 0);
        add(jSplitPane, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents

    /**
     * Handle the focus gained event on the list.
     * 
     * @param jList
     *            A <code>JList</code>.
     */
    private void jListFocusGained(final JList jList) {
        eastJList.repaint();
        westJList.repaint();
    }
    
    /**
     * Handle the focus lost event on the list.
     * 
     * @param jList
     *            A <code>JList</code>.
     */
    private void jListFocusLost(final JList jList) {
        eastJList.repaint();
        westJList.repaint();
    }
    
    /**
     * Handle the click event for the given list. All we do is check for a
     * double-click event to run a specific action.
     * 
     * @param jList
     *            A <code>JList</code>.
     * @param e
     *            A <code>MouseEvent</code>.
     */
    private void jListMouseClicked(final JList jList,
            final java.awt.event.MouseEvent e) {
        logger.logApiId();
        logger.logVariable("e", e);
        // a click count of 2, 4, 6, etc. triggers double click event
        if (e.getClickCount() % 2 == 0) {
            ((DefaultVersionCell) jList.getSelectedValue()).invokeAction();
        }
    }

    /**
     * Handle the mouse pressed event for the given list. The isPopupTrigger
     * property of the mouse event is only set within the pressed event so here
     * we save that value within the list; and check it within
     * {@link #jListMouseRelease(JList, MouseEvent)}.
     * 
     * @param jList
     *            A <code>JList</code>.
     * @param e
     *            A <code>MouseEvent</code>.
     */
    private void jListMousePressed(final JList jList,
            final java.awt.event.MouseEvent e) {
        logger.logApiId();
        logger.logVariable("e", e);
        if (e.isPopupTrigger())
            jList.putClientProperty(CPK_IS_POPUP_TRIGGER, Boolean.TRUE);
        else
            jList.putClientProperty(CPK_IS_POPUP_TRIGGER, Boolean.FALSE);
    }
    
    /**
     * Handle the mouse released event for the given list. Check the list's
     * client property for the is popup trigger boolean property set within
     * {@link #jListMousePressed(JList, MouseEvent)} and if true; first select
     * the underlying cell; then display its popup.
     * 
     * @param jList
     *            A <code>JList</code>.
     * @param e
     *            A <code>MouseEvent</code>.
     */
    private void jListMouseReleased(final JList jList,
            final java.awt.event.MouseEvent e) {
        logger.logApiId();
        logger.logVariable("e", e);
        final Boolean isPopupTrigger = (Boolean) jList.getClientProperty(CPK_IS_POPUP_TRIGGER);
        if (null != isPopupTrigger && isPopupTrigger.booleanValue()) {
            jList.setSelectedIndex(jList.locationToIndex(e.getPoint()));
            popupDelegate.initialize((Component) e.getSource(), e.getX(), e.getY());
            ((DefaultVersionCell) jList.getSelectedValue()).showPopup();
        }
    }

    /**
     * Select the first version cell.
     *
     */
    private void selectFirstVersion() {
        if (0 < versionsModel.size()) {
            westJList.setSelectedIndex(0);
        }
    }
    
    private void westJListFocusGained(java.awt.event.FocusEvent e) {//GEN-FIRST:event_westJListFocusGained
        jListFocusGained((JList) e.getSource());
    }//GEN-LAST:event_westJListFocusGained

    private void westJListFocusLost(java.awt.event.FocusEvent e) {//GEN-FIRST:event_westJListFocusLost
        jListFocusLost((JList) e.getSource());
    }//GEN-LAST:event_westJListFocusLost
    
    private void westJListMouseClicked(final java.awt.event.MouseEvent e) {//GEN-FIRST:event_westJListMouseClicked
        jListMouseClicked((JList) e.getSource(), e);
    }//GEN-LAST:event_westJListMouseClicked
    
    private void westJListMousePressed(final java.awt.event.MouseEvent e) {//GEN-FIRST:event_westJListMousePressed
        jListMousePressed((JList) e.getSource(), e);
    }//GEN-LAST:event_westJListMousePressed
    
    private void westJListMouseReleased(final java.awt.event.MouseEvent e) {//GEN-FIRST:event_westJListMouseReleased
        jListMouseReleased((JList) e.getSource(), e);        
    }//GEN-LAST:event_westJListMouseReleased

    private void westJListValueChanged(final javax.swing.event.ListSelectionEvent e) {//GEN-FIRST:event_westJListValueChanged
        if (e.getValueIsAdjusting() || westJList.isSelectionEmpty()) {
            repaint();
            return;
        }

        versionsContentModel.clear();
        for (final Object selectedValue : westJList.getSelectedValues()) {
            for (final DefaultVersionCell contentCell :
                ((AbstractVersionCell) selectedValue).contentCells) {
                versionsContentModel.addElement(contentCell);
            }
        }
    }//GEN-LAST:event_westJListValueChanged

    /** The version's list cell. */
    abstract class AbstractVersionCell extends DefaultVersionCell {

        /** The list of cells. */
        private final List<DefaultVersionCell> contentCells;
        
        protected AbstractVersionCell() {
            super();
            this.contentCells = new ArrayList<DefaultVersionCell>();
        }
        protected void addContentCell(final DefaultVersionCell contentCell) {
            this.contentCells.add(contentCell);
        }
    }

    /** A version document cell. */
    private final class DocumentVersionCell extends DefaultVersionCell {
        private final Delta delta;
        private final DocumentVersion version;
        private DocumentVersionCell(final DocumentVersion version, final Delta delta) {
            super();
            this.delta = delta;
            this.version = version;
            initText(version, delta);   
            setIcon(getDocumentIcon(version));
        }
        @Override
        public void invokeAction() {
            actionDelegate.invokeForDocument(version, delta);
        }
        @Override
        public void showPopup() {
            popupDelegate.showForDocument(version, delta);
        }
        private void initText(final DocumentVersion documentVersion, final Delta delta) {
            final String formatPattern;
            Delta finalDelta = delta;
            if (null == finalDelta) {
                finalDelta = Delta.ADDED;
            }            
            switch (delta) {
            case ADDED:
            case MODIFIED:
            case REMOVED:
                // TODO remove text colour from within html
                formatPattern = "<html>{0} <font color=\"#646464\">({1})</font></html>";
                break;
            case NONE:
                formatPattern = "{0}";
                break;
            default:
                throw Assert.createUnreachable("UNKNOWN DOCUMENT STATE");
            }
            setText(MessageFormat.format(formatPattern,
                    documentVersion.getName(), delta.toString().toLowerCase()));
        }
    }

    /** A draft cell. */
    private final class DraftCell extends AbstractVersionCell {
        private final ContainerDraft draft;
        private DraftCell(final ContainerDraft draft) {
            super();
            this.draft = draft;
            initText(draft);
            setIcon(imageCache.read(TabPanelIcon.DRAFT));
            int countCells = 0;
            if (container.isLocalDraft()) {
                for (final Document document : draft.getDocuments()) {
                    addContentCell(new DraftDocumentCell(draft, document));
                    countCells++;
                }
            }
        }
        @Override
        public void showPopup() {
            popupDelegate.showForDraft(container, draft);
        }
        private void initText(final ContainerDraft draft) {
            if (container.isLocalDraft()) {
                setText(localization.getString("Draft"));
            } else {
                setText(localization.getString("DraftNotLocal", draft.getOwner().getName()));
            }
        }
    }

    /** A draft document cell. */
    private final class DraftDocumentCell extends DefaultVersionCell {
        private final Document document;
        private final ContainerDraft draft;
        private DraftDocumentCell(final ContainerDraft draft,
                final Document document) {
            super();
            this.document = document;
            this.draft = draft;
            initText(draft, document);            
            setIcon(getDocumentIcon(document));
        }
        @Override
        public void invokeAction() {
            actionDelegate.invokeForDocument(draft, document);
        }
        @Override
        public void showPopup() {
            popupDelegate.showForDocument(draft, document);
        }
        Long getContainerId() {
            return draft.getContainerId();
        }
        Long getId() {
            return document.getId();
        }
        
        ContainerDraft.ArtifactState getState() {
            return draft.getState(document);
        }
        private void initText(final ContainerDraft draft, final Document document) {
            final String formatPattern;
            switch (draft.getState(document)) {
            case ADDED:
            case MODIFIED:
            case REMOVED:
                // TODO remove text colour from within html
                formatPattern = "<html>{0} <font color=\"#646464\">({1})</font></html>";
                break;
            case NONE:
                formatPattern = "{0}";
                break;
            default:
                throw Assert.createUnreachable("UNKNOWN DOCUMENT STATE");
            }
            setText(MessageFormat.format(formatPattern,
                    document.getName(), draft.getState(document).toString().toLowerCase()));
        }
    }

    /** A user cell. */
    private final class UserCell extends DefaultVersionCell {
        private final User user;
        private UserCell(final User user, final Boolean publisher,
                final ArtifactReceipt receipt) {
            this.user = user;
            initText(user, publisher, receipt);
            initIcon(publisher, receipt);
        }
        @Override
        public void invokeAction() {
            actionDelegate.invokeForUser(user);
        }
        @Override
        public void showPopup() {
            popupDelegate.showForUser(user);
        }
        private void initIcon(final Boolean isPublisher, final ArtifactReceipt receipt) {
            if ((isPublisher) || ((null != receipt) && (receipt.isSetReceivedOn()))) {
                setIcon(imageCache.read(TabPanelIcon.USER));
            } else {
                setIcon(imageCache.read(TabPanelIcon.USER_NOT_RECEIVED));
            }
        }
        private void initText(final User user, final Boolean isPublisher, final ArtifactReceipt receipt) {
            final StringBuffer text = new StringBuffer("<html>").append(user.getName()).append(" ");
            // TODO remove text colour from within html
            text.append("<font color=\"#646464\">");
            if (isPublisher) {
                text.append(localization.getString("UserPublished"));
            } else if (null == receipt || !receipt.isSetReceivedOn()) {
                text.append(localization.getString("UserDidNotReceive"));
            } else {
                text.append(localization.getString("UserReceived",
                        FUZZY_DATE_FORMAT.format(receipt.getReceivedOn())));
            }
            text.append("</font></html>");
            setText(text.toString());
        }
    }

    /** A version cell. */
    private final class VersionCell extends AbstractVersionCell {
        private final ContainerVersion version;
        private VersionCell(final ContainerVersion version,
                final Map<DocumentVersion, Delta> documentVersions,
                final Map<User, ArtifactReceipt> users, final User publishedBy) {
            super();
            this.version = version;
            initText(version, publishedBy);
            if (isComment()) {
                setIcon(imageCache.read(TabPanelIcon.VERSION_WITH_COMMENT));
            } else {
                setIcon(imageCache.read(TabPanelIcon.VERSION)); 
            }
            int countCells = 0;
            for (final Entry<DocumentVersion, Delta> entry : documentVersions.entrySet()) {
                addContentCell(new DocumentVersionCell(entry.getKey(), entry.getValue()));
                countCells++;
            }
            addContentCell(new UserCell(publishedBy, Boolean.TRUE, null));
            countCells++;
            for (final Entry<User, ArtifactReceipt> entry : users.entrySet()) {
                addContentCell(new UserCell(entry.getKey(), Boolean.FALSE, entry.getValue()));
                countCells++;
            }
        }
        @Override
        public void invokeAction() {
            actionDelegate.invokeForVersion(version);
        }
        @Override
        public void showPopup() {
            popupDelegate.showForVersion(version);
        }
        Long getArtifactId() {
            return version.getArtifactId();
        }
        Long getVersionId() {
            return version.getVersionId();
        }
        Boolean isComment() {
            return ((version.isSetComment()) && (version.getComment().length()>0));
        }
        private void initText(final ContainerVersion version, final User publishedBy) {
            setText(localization.getString("Version", FUZZY_DATE_FORMAT
                    .format(version.getCreatedOn()), publishedBy.getName()));
        }
    }
}
