/*
 * Created On: October 7, 2006, 10:25 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

import com.thinkparity.codebase.FuzzyDateFormat;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.log4j.Log4JWrapper;
import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta.Delta;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.container.ContainerDraft;

import com.thinkparity.ophelia.browser.Constants.Colors;
import com.thinkparity.ophelia.browser.Constants.Images;
import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.application.browser.component.MenuFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.FileIconReader;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.MainPanelImageCache;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.MainPanelImageCache.TabPanelIcon;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.container.ContainerModel;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel;
import com.thinkparity.ophelia.browser.application.browser.dnd.ImportTxHandler;
import com.thinkparity.ophelia.browser.platform.application.ApplicationId;
import com.thinkparity.ophelia.browser.platform.application.ApplicationRegistry;
import com.thinkparity.ophelia.browser.util.localization.MainCellL18n;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ContainerVersionsPanel extends DefaultTabPanel {

    /** A <code>FuzzyDateFormat</code>. */
    private static final FuzzyDateFormat FUZZY_DATE_FORMAT;

    /** Number of rows visible in the expanded package lists. */
    private static final Integer NUMBER_VISIBLE_ROWS;

    static {        
        NUMBER_VISIBLE_ROWS = 4;
        FUZZY_DATE_FORMAT = new FuzzyDateFormat();
    }

    /** The browser application. */
    private final Browser browser;

    /** The clipped down background images on the left. */
    private BufferedImage[] clippedContainerBackgroundLeft = null;

    /** The clipped down background image on the right. */
    private BufferedImage clippedContainerBackgroundRight = null;

    /** The <code>Container</code>. */
    private Container container;

    /** A file icon reader. */
    private final FileIconReader fileIconReader;

    /** The focus manager. */
    private final FocusManager focusManager;

    /** An image cache. */
    private final MainPanelImageCache imageCache;

    /** The panel localization. */
    private final MainCellL18n localization;

    /** An apache logger. */
    private final Log4JWrapper logger;

    /** The <code>ContainerModel</code>. */
    private final ContainerModel model;

    /** The previous selection index of the Content list. */
    private int previousContentSelectionIndex = -1;

    /** The previous selection index of the Versions list. */
    private int previousVersionSelectionIndex = -1;

    /** The scaled up background images on the left. */
    private BufferedImage[] scaledContainerBackgroundLeft;
    
    /** The scaled up background image on the right. */
    private BufferedImage scaledContainerBackgroundRight;
    
    /** A transparent JPanel to assist with drag and drop. */
    private final TransparentJPanel transparentJPanel;

    /** The version's content list model. */
    private final DefaultListModel versionsContentModel;

    /** The version's list model. */
    private final DefaultListModel versionsModel;

    /**
     * Create ContainerVersionsPanel
     *
     * @param model
     *      The <code>ContainerModel</code>.
     */
    public ContainerVersionsPanel(final ContainerModel model) {
        super();
        this.model = model;
        this.browser = ((Browser) new ApplicationRegistry().get(ApplicationId.BROWSER));
        this.versionsModel = new DefaultListModel();
        this.versionsContentModel = new DefaultListModel();
        this.imageCache = new MainPanelImageCache();
        this.fileIconReader = new FileIconReader();
        this.logger = new Log4JWrapper();
        this.localization = new MainCellL18n("ContainerVersionsPanel");
        this.focusManager = new FocusManager();
        this.transparentJPanel = new TransparentJPanel(this);
        initComponents();
        initMouseOverTrackers();
        initBackgroundImages();
        focusManager.addFocusListener(this, model, versionsJList, FocusManager.FocusList.VERSION);
        focusManager.addFocusListener(this, model, versionsContentJList, FocusManager.FocusList.CONTENT);
    }

    /**
     * Called if the focus list changes.
     * 
     * @param focusList
     *      The list with focus.
     */
    public void focusChanged(final FocusManager.FocusList focusList) {
        repaint();
    }

    /**
     * Get the container associated with this panel.
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
            versionsModel.addElement(new DraftCell(draft, container.isLocalDraft()));
        }
        // display all version elements
        for (final ContainerVersion version : versions) {
            versionsModel.addElement(new VersionCell(version, documentVersions
                    .get(version), publishedTo.get(version), publishedBy
                    .get(version)));
        }
        // set up drag and drop
        transparentJPanel.setTransferHandler(new ImportTxHandler(browser,
                model, container));
        // If necessary, add filler rows to the versions list
        // so the backgrounds will paint in alternating colours
        if (versionsJList.getModel().getSize() <
                versionsJList.getVisibleRowCount()) {
            for (int i = versionsJList.getModel().getSize();
                    i < versionsJList.getVisibleRowCount(); i++) {
                versionsModel.addElement(new VersionFillerCell());
            }
        }
        selectFirstVersion();
        // initialize the transparentJPanel
        transparentJPanel.initialize(this);
    }

    /**
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final int rightWidth = getWidth() / 2;
        final int leftWidth = getWidth() - rightWidth - 5;
        final int selectedIndex = versionsJList.getSelectedIndex() - versionsJList.getFirstVisibleIndex();        
        final int imageIndex;
        if (versionsJList.getSelectedIndex() < 0 ) {
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

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel#triggerDoubleClick(java.awt.event.MouseEvent)
     */
    @Override
    protected void triggerDoubleClick(MouseEvent e) {
        model.triggerExpand(container);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel#triggerPopup(java.awt.Component,
     *      java.awt.event.MouseEvent)
     */
    @Override
    protected void triggerPopup(final Component invoker, final MouseEvent e) {
        new ContainerPopup(model, container).show(invoker, e);
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
     * Get the focus list.
     * 
     * @return The focus list.
     */
    private FocusManager.FocusList getFocusList() {
        return focusManager.getFocusList();
    }

    /**
     * Get the index of the cell the event is over.
     * 
     * @param e
     *          Mouse event.
     * @param jList
     *          JList.       
     * @return  index of the cell the mouse is over, or -1.
     */
    private Integer getMouseOverIndex(java.awt.event.MouseEvent e, final JList jList) {
        Integer tabCellIndex = jList.locationToIndex(e.getPoint());
        
        // Handle the case that the mouse is below the last entry
        if ((tabCellIndex != -1) && (tabCellIndex == jList.getModel().getSize() - 1)) {
            final Rectangle tabCellBounds = jList.getCellBounds(tabCellIndex, tabCellIndex);
            if (!SwingUtil.regionContains(tabCellBounds, e.getPoint())) {
                tabCellIndex = -1;
            }
        }
        
        return tabCellIndex;
    }

    /**
     * Process a JList mouse clicked event.
     */
    private void handleJListMouseClicked(final JList jList, final ListType listType, final java.awt.event.MouseEvent e) {
        logger.logApiId();
        logger.logVariable("e", e);
        if (!MenuFactory.isPopupMenu()) {
            if (e.getButton()==MouseEvent.BUTTON1) {
                final Boolean isInCell = isMouseEventWithinCell(jList, e);
                if (isInCell) { 
                    triggerJListSingleClick(jList, listType, e);
                } else {
                    triggerJListSingleClick();
                }
                // A click count of 2, 4, 6, etc. triggers double click event
                if (e.getClickCount() % 2 == 0) {
                    if (isInCell) {  
                        triggerJListDoubleClick(jList, listType, e);
                    } else {
                        triggerJListDoubleClick();
                    }
                }
            }
        }
    }

    /**
     * Process a JList popup trigger event.
     */
    private void handleJListMousePopupTrigger(final JList jList, final ListType listType, final java.awt.event.MouseEvent e) {
        if (isMouseEventWithinCell(jList, e)) {
            final int index = jList.locationToIndex(e.getPoint());
            if (!isFillerCell(jList, index)) {                              
                setSelectedIndex(jList, index);
                if (listType == ListType.VERSION) {
                    triggerJListPopup((AbstractVersionCell) jList.getSelectedValue(), e);
                } else if (listType == ListType.CONTENT) {
                    triggerJListPopup((AbstractContentCell) jList.getSelectedValue(), e);
                }
                e.consume();
            }
        }
    }

    /**
     * Process a JList mouse pressed event.
     */
    private void handleJListMousePressed(final JList jList, final ListType listType, final java.awt.event.MouseEvent e) {
        logger.logApiId();
        logger.logVariable("e", e);
        if (e.isPopupTrigger()) { 
            handleJListMousePopupTrigger(jList, listType, e);
        } else if (e.getButton()==MouseEvent.BUTTON1) {
            if (isMouseEventWithinCell(jList, e)) {
                final int index = jList.locationToIndex(e.getPoint());
                if (!isFillerCell(jList, index)) {                             
                    setSelectedIndex(jList, index);
                    e.consume();
                }
            }            
        }
    }

    /**
     * Process a JList mouse released event.
     */
    private void handleJListMouseReleased(final JList jList, final ListType listType, final java.awt.event.MouseEvent e) {
        logger.logApiId();
        logger.logVariable("e", e);
        if (e.isPopupTrigger()) { 
            handleJListMousePopupTrigger(jList, listType, e);
        }
    }

    /**
     * Initialize background images so they start out at screen width.
     */
    private void initBackgroundImages() {
        if (null == scaledContainerBackgroundLeft) {
            scaledContainerBackgroundLeft = new BufferedImage[NUMBER_VISIBLE_ROWS+1];
        }
        if (null == clippedContainerBackgroundLeft) {
            clippedContainerBackgroundLeft = new BufferedImage[NUMBER_VISIBLE_ROWS+1];
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
        // Set up and if necessary scale the left background images
        for (int index = 0; index < NUMBER_VISIBLE_ROWS+1; index++) {
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

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        javax.swing.JPanel leftJPanel;
        javax.swing.JPanel rightJPanel;
        javax.swing.JScrollPane versionsContentJScrollPane;
        javax.swing.JScrollPane versionsJScrollPane;

        final javax.swing.JSplitPane versionsJSplitPane = new javax.swing.JSplitPane();
        leftJPanel = new javax.swing.JPanel();
        versionsJScrollPane = new javax.swing.JScrollPane();
        versionsJList = new javax.swing.JList();
        rightJPanel = new javax.swing.JPanel();
        versionsContentJScrollPane = new javax.swing.JScrollPane();
        versionsContentJList = new javax.swing.JList();

        setLayout(new java.awt.GridBagLayout());

        setBackground(Colors.Browser.List.LIST_EXPANDED_BG);
        versionsJSplitPane.setBorder(null);
        versionsJSplitPane.setDividerLocation(0.5);
        versionsJSplitPane.setDividerSize(0);
        versionsJSplitPane.setResizeWeight(0.5);
        versionsJSplitPane.setMinimumSize(new java.awt.Dimension(52, 75));
        versionsJSplitPane.setOpaque(false);
        versionsJSplitPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                versionsJSplitPane.setDividerLocation(0.5);
            }
        });
        leftJPanel.setLayout(new java.awt.GridBagLayout());

        leftJPanel.setOpaque(false);
        leftJPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                leftJPanelMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                leftJPanelMousePressed(evt);
            }
        });

        versionsJScrollPane.setBorder(null);
        versionsJScrollPane.setOpaque(false);
        versionsJScrollPane.getViewport().setOpaque(false);
        versionsJScrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            public void adjustmentValueChanged(final AdjustmentEvent e) {
                repaint();
            }
        });
        versionsJList.setModel(versionsModel);
        versionsJList.setCellRenderer(new VersionCellRenderer());
        versionsJList.setOpaque(false);
        versionsJList.setVisibleRowCount(4);
        versionsJList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                versionsJListValueChanged(evt);
            }
        });
        versionsJList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                versionsJListMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                versionsJListMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                versionsJListMouseReleased(evt);
            }
        });

        versionsJScrollPane.setViewportView(versionsJList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        leftJPanel.add(versionsJScrollPane, gridBagConstraints);

        versionsJSplitPane.setLeftComponent(leftJPanel);

        rightJPanel.setLayout(new java.awt.GridBagLayout());

        rightJPanel.setOpaque(false);
        rightJPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rightJPanelMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                rightJPanelMousePressed(evt);
            }
        });

        versionsContentJScrollPane.setBorder(null);
        versionsContentJScrollPane.setOpaque(false);
        versionsContentJScrollPane.getViewport().setOpaque(false);
        versionsContentJList.setModel(versionsContentModel);
        versionsContentJList.setCellRenderer(new VersionContentCellRenderer());
        versionsContentJList.setOpaque(false);
        versionsContentJList.setVisibleRowCount(4);
        versionsContentJList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                versionsContentJListValueChanged(evt);
            }
        });
        versionsContentJList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                versionsContentJListMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                versionsContentJListMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                versionsContentJListMouseReleased(evt);
            }
        });

        versionsContentJScrollPane.setViewportView(versionsContentJList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        rightJPanel.add(versionsContentJScrollPane, gridBagConstraints);

        versionsJSplitPane.setRightComponent(rightJPanel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 34, 0, 0);
        add(versionsJSplitPane, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents

    /**
     * Install a mouse over tracker which tracks the list cell that the
     * mouse is over.
     */
    private final void initMouseOverTrackers() {
        class MouseOverTracker extends MouseInputAdapter {
            private int mouseOverIndex = -1;
            
            public MouseOverTracker() {
                super();
            }
            
            @Override
            public void mouseEntered(final MouseEvent e) {
                mouseMoved(e);
            }
            @Override
            public void mouseExited(final MouseEvent e) {
                if (-1 != mouseOverIndex) {
                    final JList jList = (JList) e.getSource();
                    updateCellMouseOver(jList, mouseOverIndex, Boolean.FALSE);
                    mouseOverIndex = -1;
                }
            }
            @Override
            public void mouseMoved(MouseEvent e) {
                final JList jList = (JList) e.getSource();
                final int mouseOverIndex = getMouseOverIndex(e, jList);
                if (mouseOverIndex != this.mouseOverIndex) {                   
                    if (-1 != this.mouseOverIndex) {
                        updateCellMouseOver(jList, this.mouseOverIndex, Boolean.FALSE);
                    }
                    if (-1 != mouseOverIndex) {
                        updateCellMouseOver(jList, mouseOverIndex, Boolean.TRUE);
                    }
                    this.mouseOverIndex = mouseOverIndex;
                }
            }
        }
        
        final MouseOverTracker versionsJListListener = new MouseOverTracker();        
        versionsJList.addMouseListener(versionsJListListener);
        versionsJList.addMouseMotionListener(versionsJListListener);
        
        final MouseOverTracker versionsContentJListListener = new MouseOverTracker();
        versionsContentJList.addMouseListener(versionsContentJListListener);
        versionsContentJList.addMouseMotionListener(versionsContentJListListener);
    }

    /**
     * Determine if the cell at this index in the list is a filler cell.
     * @param jList
     *              The JList.
     * @param index
     *              The index.
     */
    private Boolean isFillerCell(final JList jList, final int index) {
        final AbstractCell cell = (AbstractCell) jList.getModel().getElementAt(index);
        if ((cell instanceof VersionFillerCell) || (cell instanceof ContentFillerCell)) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }                                                  

    /**
     * Determine if the mouse event occurred on or below the cell of a JList in an x-y plane.
     * @param jList
     *              The JList.
     * @param e
     *              The MouseEvent.
     * @return True if the mouse event occurred in the cell.
     */
    private Boolean isMouseEventWithinCell(final JList jList, final java.awt.event.MouseEvent e) {
        final Integer tabCellIndex = jList.locationToIndex(e.getPoint());
        if (tabCellIndex == -1) {  // Will be -1 if the model is empty
            return Boolean.FALSE;
        } else {
            final Rectangle tabCellBounds = jList.getCellBounds(tabCellIndex, tabCellIndex);
            return SwingUtil.regionContains(tabCellBounds, e.getPoint());
        }
    }

    /**
     * Determine if the container is selected.
     * 
     * @return True if the container is selected; false otherwise.
     */
    private Boolean isSelectedContainer() {
        return model.isSelectedContainer(container);
    }

    private void leftJPanelMouseClicked(final java.awt.event.MouseEvent e) {// GEN-FIRST:event_leftJPanelMouseClicked
        formMouseClicked(e);
    }// GEN-LAST:event_leftJPanelMouseClicked

    private void leftJPanelMousePressed(final java.awt.event.MouseEvent e) {// GEN-FIRST:event_leftJPanelMousePressed
        formMousePressed(e);
    }// GEN-LAST:event_leftJPanelMousePressed
    
    /**
     * Restore the saved selection.
     *
     */
    private void restoreSelection(final JList jList) {
        final Integer selection =
            (Integer) getClientProperty(ClientProperty.SAVE_SELECTION);
        jList.setSelectedIndex(selection);
    }

    private void rightJPanelMouseClicked(final java.awt.event.MouseEvent e) {// GEN-FIRST:event_rightJPanelMouseClicked
        formMouseClicked(e);
    }// GEN-LAST:event_rightJPanelMouseClicked
    
    private void rightJPanelMousePressed(final java.awt.event.MouseEvent e) {// GEN-FIRST:event_rightJPanelMousePressed
        formMousePressed(e);
    }// GEN-LAST:event_rightJPanelMousePressed
    
    /**
     * Save the current selection cell.
     *
     */
    private void saveSelection(final JList jList) {
        putClientProperty(ClientProperty.SAVE_SELECTION, jList.getSelectedIndex());
    }

    /**
     * Select the first version cell.
     *
     */
    private void selectFirstVersion() {
        if (0 < versionsModel.size()) {
            versionsJList.setSelectedIndex(0);
        }
    }
    
    /**
     * Select an entry in the JList.
     * 
     * @param jList
     *              The JList.
     * @param index
     *              The JList index to select.
     */
    private void setSelectedIndex(final JList jList, final Integer index) {
        if (index != jList.getSelectedIndex()) {
            jList.setSelectedIndex(index);
        }
        if (!jList.isFocusOwner()) {
            jList.requestFocusInWindow();
        } 
    }
        
    /**
     * Handle double click below the last entry in a JList.
     */
    private void triggerJListDoubleClick() {
    }
    
    /**
     * Handle double click in a JList.
     */
    private void triggerJListDoubleClick(final JList jList, final ListType listType, final java.awt.event.MouseEvent e) {        
        if (listType == ListType.VERSION) {
            ((AbstractVersionCell)jList.getSelectedValue()).doubleClick(jList, e);
        } else if (listType == ListType.CONTENT) {
            ((AbstractContentCell)jList.getSelectedValue()).doubleClick(jList, e);
        }
    }
    
    private void triggerJListPopup(final AbstractContentCell selectedContent,
            final MouseEvent e) {
        selectedContent.showPopupMenu(versionsContentJList, e);
    }
    
    private void triggerJListPopup(final AbstractVersionCell selectedVersion,
            final MouseEvent e) {
        selectedVersion.showPopupMenu(versionsJList, e);
    }
    
    /**
     * Handle single click below the last entry in a JList.
     */
    private void triggerJListSingleClick() {
    }
    
    /**
     * Handle single click in a JList.
     */
    private void triggerJListSingleClick(final JList jList, final ListType listType, final java.awt.event.MouseEvent e) {        
    }
    
    /**
     * Update the cell that the mouse is over with the mouseOver flag and
     * cause the cell to redraw.
     * 
     * @param jList
     *              JList.
     * @param index
     *              Index of the cell.
     * @param mouseOver
     *              Mouse over flag.
     */
    private void updateCellMouseOver(final JList jList, final Integer index, final Boolean mouseOver) {
        saveSelection(jList);
        if (index < jList.getModel().getSize()) {
            final AbstractCell cell = (AbstractCell) jList.getModel().getElementAt(index);
            cell.setMouseOver(mouseOver);
/*            ((DefaultListModel)jList.getModel()).removeElementAt(index);
            ((DefaultListModel)jList.getModel()).add(index, cell);*/
            restoreSelection(jList);
        }
    }
    
    private void versionsContentJListMouseClicked(final java.awt.event.MouseEvent e) {// GEN-FIRST:event_versionsContentJListMouseClicked
        final JList jList = (JList) e.getSource();
        handleJListMouseClicked(jList, ListType.CONTENT, e);
    }// GEN-LAST:event_versionsContentJListMouseClicked
    
    private void versionsContentJListMousePressed(final java.awt.event.MouseEvent e) {// GEN-FIRST:event_versionsContentJListMousePressed
        final JList jList = (JList) e.getSource();
        handleJListMousePressed(jList, ListType.CONTENT, e);
    }// GEN-LAST:event_versionsContentJListMousePressed
    
    private void versionsContentJListMouseReleased(final java.awt.event.MouseEvent e) {// GEN-FIRST:event_versionsContentJListMouseReleased
        final JList jList = (JList) e.getSource();
        handleJListMouseReleased(jList, ListType.CONTENT, e);
    }
    
    private void versionsContentJListValueChanged(javax.swing.event.ListSelectionEvent e) {//GEN-FIRST:event_versionsContentJListValueChanged
        // Don't allow selection of filler cells.
        if ((null != versionsContentJList.getSelectedValue()) &&
            ((AbstractCell)versionsContentJList.getSelectedValue()).isFillerCell() &&
            (previousContentSelectionIndex != -1)) {
            setSelectedIndex(versionsContentJList, previousContentSelectionIndex);
        } else if (!e.getValueIsAdjusting()) {
            previousContentSelectionIndex = versionsContentJList.getSelectedIndex();
        }
    }//GEN-LAST:event_versionsContentJListValueChanged
    
    private void versionsJListMouseClicked(final java.awt.event.MouseEvent e) {//GEN-FIRST:event_versionsJListMouseClicked
        final JList jList = (JList) e.getSource();
        handleJListMouseClicked(jList, ListType.VERSION, e);
    }//GEN-LAST:event_versionsJListMouseClicked

    private void versionsJListMousePressed(final java.awt.event.MouseEvent e) {//GEN-FIRST:event_versionsJListMousePressed
        final JList jList = (JList) e.getSource();
        handleJListMousePressed(jList, ListType.VERSION, e);
    }//GEN-LAST:event_versionsJListMousePressed

    private void versionsJListMouseReleased(final java.awt.event.MouseEvent e) {//GEN-FIRST:event_versionsJListMouseReleased
        final JList jList = (JList) e.getSource();
        handleJListMouseReleased(jList, ListType.VERSION, e);        
    }//GEN-LAST:event_versionsJListMouseReleased

    private void versionsJListValueChanged(final javax.swing.event.ListSelectionEvent e) {//GEN-FIRST:event_versionsJListValueChanged
        // Don't allow selection of filler cells.
        if ((null != versionsJList.getSelectedValue()) &&
            ((AbstractCell)versionsJList.getSelectedValue()).isFillerCell() &&
            (previousVersionSelectionIndex != -1)) {
            setSelectedIndex(versionsJList, previousVersionSelectionIndex);
        } else if (!e.getValueIsAdjusting()) {
            previousVersionSelectionIndex = versionsJList.getSelectedIndex();
            logger.logVariable("evt", e);
            versionsContentModel.clear();
            for (final Object selectedValue : versionsJList.getSelectedValues()) {
                for (final AbstractContentCell contentCell : ((AbstractVersionCell) selectedValue).contentCells) {
                    versionsContentModel.addElement(contentCell);
                }
            }
        }
        // Selection is indicated by background image, therefore, repaint.
        repaint();
    }//GEN-LAST:event_versionsJListValueChanged

    /** The abstract list cell. */
    abstract class AbstractCell {
        /** The icon associated with this cell. */
        protected Icon icon = null;  
        
        /** A flag indicating the mouse over status. */
        protected Boolean mouseOver = Boolean.FALSE;
        
        /** The text associated with this cell. */
        protected String text;
        
        protected AbstractCell() {
            super();
        }        
        protected abstract void doubleClick(final Component invoker,
                final MouseEvent e);
        protected Integer getContainerIndex() {
            return model.indexOfContainerPanel(container);
        }
        protected Icon getIcon() {
            return icon;
        }
        protected String getText() {
            return text;
        }
        protected Boolean isFillerCell() {
            return Boolean.FALSE;
        }
        protected Boolean isMouseOver() {
            return mouseOver;
        }
        protected Boolean isSelectedContainer() {
            return ContainerVersionsPanel.this.isSelectedContainer();
        }
        protected void setIcon(final Icon icon) {
            this.icon = icon;
        }
        protected void setMouseOver(final Boolean mouseOver) {
            this.mouseOver = mouseOver;  
        }
        protected void setText(final String text) {
            this.text = text;
        }
        protected abstract void showPopupMenu(final Component invoker,
                final MouseEvent e);
    }

    /** The version's content list cell. */
    abstract class AbstractContentCell extends AbstractCell {                
        protected AbstractContentCell() {
            super();
        }
        protected Boolean isFocusOnThisList() {
            return (getFocusList() == FocusManager.FocusList.CONTENT);
        }
    }

    /** The version's list cell. */
    abstract class AbstractVersionCell extends AbstractCell {
        /** The list of content cells. */
        private final List<AbstractContentCell> contentCells;
        
        protected AbstractVersionCell() {
            super();
            this.contentCells = new ArrayList<AbstractContentCell>();
        }
        protected void addContentCell(final AbstractContentCell contentCell) {
            this.contentCells.add(contentCell);
        }
        protected List<AbstractContentCell> getContentCells() {
            return Collections.unmodifiableList(contentCells);
        }
        protected Boolean isFocusOnThisList() {
            return (getFocusList() == FocusManager.FocusList.VERSION);
        }
    }

    /** A filler cell for the content list, so the background can be drawn */
    final class ContentFillerCell extends AbstractContentCell {
        private ContentFillerCell() {
            super();
        }
        @Override
        protected void doubleClick(Component invoker, MouseEvent e) { 
        }
        @Override
        protected Boolean isFillerCell() {
            return Boolean.TRUE;
        }      
        @Override
        protected void showPopupMenu(Component invoker, MouseEvent e) { 
        } 
    }
    
    /** A version document cell. */
    final class DocumentVersionCell extends AbstractContentCell {
        private final DocumentVersion version;
        private DocumentVersionCell(final DocumentVersion version, final Delta delta) {
            super();
            this.version = version;
            initText(version, delta);   
            setIcon(getDocumentIcon(version));
        }
        @Override
        protected void doubleClick(final Component invoker, final MouseEvent e) {
            browser.runOpenDocumentVersion(getDocumentId(), getVersionId());
        }
        @Override
        protected void showPopupMenu(final Component invoker, final MouseEvent e) {
            new ContainerVersionsPopup(model, this).show(invoker, e);
        }
        Long getDocumentId() {
            return version.getArtifactId();
        }
        Long getVersionId() {
            return version.getVersionId();
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
    final class DraftCell extends AbstractVersionCell {
        private final ContainerDraft draft;
        private final Boolean isLocalDraft;
        private DraftCell(final ContainerDraft draft, final Boolean isLocalDraft) {
            super();
            this.draft = draft;
            this.isLocalDraft = isLocalDraft;
            initText(draft, isLocalDraft);
            setIcon(imageCache.read(TabPanelIcon.DRAFT));
            int countCells = 0;
            if (isLocalDraft) {
                for (final Document document : draft.getDocuments()) {
                    addContentCell(new DraftDocumentCell(draft, document));
                    countCells++;
                }
            }
            for (int i = countCells; i < versionsContentJList.getVisibleRowCount(); i++) {
                addContentCell(new ContentFillerCell());
            }
        }
        @Override
        protected void doubleClick(final Component invoker, final MouseEvent e) {
        }
        @Override
        protected void showPopupMenu(final Component invoker, final MouseEvent e) {
            if (isLocalDraft) {
                new ContainerVersionsPopup(model, this).show(invoker, e);
            }
        }
        Long getContainerId() {
            return draft.getContainerId();
        }
        private void initText(final ContainerDraft draft, final Boolean isLocalDraft) {
            if (isLocalDraft) {
                setText(localization.getString("Draft"));
            } else {
                setText(localization.getString("DraftNotLocal", draft.getOwner().getName()));
            }
        }
    }
    
    /** A draft document cell. */
    final class DraftDocumentCell extends AbstractContentCell {
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
        protected void doubleClick(final Component invoker, final MouseEvent e) {
            browser.runOpenDocument(getId());
        }
        @Override
        protected void showPopupMenu(final Component invoker, final MouseEvent e) {
            new ContainerVersionsPopup(model, this).show(invoker, e);
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
    final class UserCell extends AbstractContentCell {
        private final User user;
        private UserCell(final User user, final Boolean isPublisher, final ArtifactReceipt receipt) {
            this.user = user;
            initText(user, isPublisher, receipt);
            initIcon(isPublisher, receipt);
        }
        @Override
        protected void doubleClick(final Component invoker, final MouseEvent e) {
            browser.runReadContact(getId());
        }
        @Override
        protected void showPopupMenu(final Component invoker, final MouseEvent e) {
            new ContainerVersionsPopup(model, this).show(invoker, e);
        }
        JabberId getId() {
            return user.getId();
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
    final class VersionCell extends AbstractVersionCell {
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
            for (int i = countCells; i < versionsContentJList.getVisibleRowCount(); i++) {
                addContentCell(new ContentFillerCell());
            }
        }
        @Override
        protected void doubleClick(final Component invoker, final MouseEvent e) {
            if (isComment()) {
                browser.displayContainerVersionInfoDialog(getArtifactId(), getVersionId());
            }
        }
        @Override
        protected void showPopupMenu(final Component invoker, final MouseEvent e) {
            new ContainerVersionsPopup(model, this).show(invoker, e);
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
            setText(localization.getString("Version", FUZZY_DATE_FORMAT.format(version.getCreatedOn()), publishedBy.getName()));
        } 
    }
    
    /** A filler cell for the version list, so the background can be drawn */
    final class VersionFillerCell extends AbstractVersionCell {
        private VersionFillerCell() {
            super();
        }
        @Override
        protected void doubleClick(final Component invoker, final MouseEvent e) {
        }
        @Override
        protected Boolean isFillerCell() {
            return Boolean.TRUE;
        }
        @Override
        protected void showPopupMenu(final Component invoker, final MouseEvent e) {
        }       
    }
    private enum ClientProperty { SAVE_SELECTION }

    private enum ListType { CONTENT, VERSION }
    /**
     * Transparent JPanel class.
     * 
     * This transparent panel exists to catch drag and drop events for
     * the containerVersionsPanel. An alternate approach might have been to call
     * setTransferHandler() on ContainerVersionsPanel and on the two JLists inside it.
     * This behaves poorly because the selection on the JList changes while dragging.
     */
    private class TransparentJPanel extends javax.swing.JPanel {
        
        /** @see java.io.Serializable */
        private static final long serialVersionUID = 1;
        
        /** Initialized flag. */
        private Boolean initialized = Boolean.FALSE;
        
        /** JFrame ancestor */
        private JFrame jFrameAncestor = null;
        
        public TransparentJPanel(final ContainerVersionsPanel containerVersionsPanel) {
            super();
            setBorder(null);
            setOpaque(false);
        }
        
        /**
         * Initialize method, called after the containerVersionsPanel has been added
         * to its parent.
         * 
         * @param containerVersionsPanel
         *          The panel.
         */
        public void initialize(final ContainerVersionsPanel containerVersionsPanel) {
            if (!initialized && (null != containerVersionsPanel.getParent())) {                
                initialized = Boolean.TRUE;
                
                final Window window = SwingUtilities.getWindowAncestor(containerVersionsPanel);
                if (window instanceof JFrame) {
                    this.jFrameAncestor = (JFrame) window;
                }
                
                jFrameAncestor.getLayeredPane().add(TransparentJPanel.this, JLayeredPane.PALETTE_LAYER);
                reposition(containerVersionsPanel);
                
                containerVersionsPanel.getParent().addContainerListener(new ContainerListener() {
                    public void componentAdded(final ContainerEvent e) { 
                        if (e.getChild().equals(containerVersionsPanel)) {
                            if (null != jFrameAncestor) {
                                jFrameAncestor.getLayeredPane().add(TransparentJPanel.this, JLayeredPane.PALETTE_LAYER);
                            }
                        }
                    }
                    public void componentRemoved(final ContainerEvent e) {  
                        if (e.getChild().equals(containerVersionsPanel)) {
                            if (null != jFrameAncestor) {
                                jFrameAncestor.getLayeredPane().remove(TransparentJPanel.this);
                            }
                        }
                    }                
                });
                
                containerVersionsPanel.addComponentListener(new ComponentAdapter() {
                    @Override
                    public void componentMoved(final ComponentEvent e) {
                        reposition(containerVersionsPanel);
                    }
                    @Override
                    public void componentResized(final ComponentEvent e) {
                        reposition(containerVersionsPanel);
                    }                
                });
            }
        }

        private void reposition(final ContainerVersionsPanel containerVersionsPanel) {
            if (null != jFrameAncestor) {
                final Point location = SwingUtilities.convertPoint(containerVersionsPanel.getParent(),
                        containerVersionsPanel.getLocation(), jFrameAncestor);
                setSize(containerVersionsPanel.getSize());
                setLocation(location.x-1, location.y-1);
            }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList versionsContentJList;
    private javax.swing.JList versionsJList;
    // End of variables declaration//GEN-END:variables  
}
