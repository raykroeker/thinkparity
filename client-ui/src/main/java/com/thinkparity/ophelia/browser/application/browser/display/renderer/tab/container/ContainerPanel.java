/*
 * Created On: October 6, 2006, 5:03 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.artifact.PublishedToEMail;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta.Delta;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.container.ContainerDraft;
import com.thinkparity.ophelia.model.container.ContainerDraft.ArtifactState;

import com.thinkparity.ophelia.browser.Constants.Colors;
import com.thinkparity.ophelia.browser.application.browser.BrowserSession;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Colours;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.component.ButtonFactory;
import com.thinkparity.ophelia.browser.application.browser.component.LabelFactory;
import com.thinkparity.ophelia.browser.application.browser.component.MenuFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.KeyboardPopupHelper;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.FileIconReader;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.MainPanelImageCache.TabPanelIcon;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.view.DocumentView;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.view.DraftView;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.view.PublishedToView;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.*;
import com.thinkparity.ophelia.browser.util.localization.BrowserLocalization;
import com.thinkparity.ophelia.browser.util.localization.Localization;

/**
 * <b>Title:</b>thinkParity Container Panel<br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com, robert@thinkparity.com
 * @version 1.1.2.1
 */
public class ContainerPanel extends DefaultTabPanel {

    /** The space between container text and additional text. */
    private static final int CONTAINER_TEXT_SPACE_BETWEEN;

    /** The space to leave at the end of the container text. */
    private static final int CONTAINER_TEXT_SPACE_END;

    /** The X location of the container text. */
    private static final int CONTAINER_TEXT_X;

    /** The Y location of the container text. */
    private static final int CONTAINER_TEXT_Y;

    /** The fraction of width allowed for the draft owner name. */
    private static final float FRACTION_WIDTH_DRAFT_OWNER_NAME;

    /** The number of rows in the version panel. */
    private static final int NUMBER_VISIBLE_ROWS;

    static {
        CONTAINER_TEXT_SPACE_BETWEEN = 5;
        CONTAINER_TEXT_SPACE_END = 20;
        CONTAINER_TEXT_X = 56;
        CONTAINER_TEXT_Y = 5;
        FRACTION_WIDTH_DRAFT_OWNER_NAME = 0.25f;
        NUMBER_VISIBLE_ROWS = 6;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private final javax.swing.JButton actionsJButton = ButtonFactory.create();
    private final javax.swing.JPanel collapsedJPanel = new javax.swing.JPanel();
    private final javax.swing.JButton commonActionJButton = ButtonFactory.create();
    private final javax.swing.JLabel eastCountJLabel = new javax.swing.JLabel();
    private final javax.swing.JPanel eastJPanel = new javax.swing.JPanel();
    private javax.swing.JPanel eastListJPanel;
    private final javax.swing.JLabel eastNextJLabel = LabelFactory.create(readIcon(TabPanelIcon.PAGE_NEXT), readIcon(TabPanelIcon.PAGE_NEXT_ROLLOVER));
    private final javax.swing.JLabel eastPreviousJLabel = LabelFactory.create(readIcon(TabPanelIcon.PAGE_PREVIOUS), readIcon(TabPanelIcon.PAGE_PREVIOUS_ROLLOVER));
    private final javax.swing.JLabel expandIconJLabel = new javax.swing.JLabel();
    private final javax.swing.JPanel expandedJPanel = new javax.swing.JPanel();
    private final javax.swing.JLabel iconJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel textJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel westCountJLabel = new javax.swing.JLabel();
    private final javax.swing.JPanel westJPanel = new javax.swing.JPanel();
    private javax.swing.JPanel westListJPanel;
    private final javax.swing.JLabel westNextJLabel = LabelFactory.create(readIcon(TabPanelIcon.PAGE_NEXT), readIcon(TabPanelIcon.PAGE_NEXT_ROLLOVER));
    private final javax.swing.JLabel westPreviousJLabel = LabelFactory.create(readIcon(TabPanelIcon.PAGE_PREVIOUS), readIcon(TabPanelIcon.PAGE_PREVIOUS_ROLLOVER));
    // End of variables declaration//GEN-END:variables

    /** The container tab's <code>DefaultActionDelegate</code>. */
    private ActionDelegate actionDelegate;

    /** A <code>Container</code>. */
    private Container container;

    /**
     * A <code>Map</code> of the <code>ContainerVersion</code> to its
     * <code>List</code> of <code>DocumentView</code>s.
     */
    private final Map<ContainerVersion, List<DocumentView>> documentViews;

    /** A <code>DraftView</code>. */
    private DraftView draft;

    /** The earliest <code>ContainerVersion</code>. */
    private ContainerVersion earliestVersion;

    /** The east list of <code>PanelCellRenderer</code>.*/
    private final List<PanelCellRenderer> eastCellPanels;

    /** The visible east list model. */
    private final PanelCellListModel eastListModel;

    /** A <code>Boolean</code> flagging that expanded data has been set. */
    private Boolean expandedData;

    /** A  <code>FileIconReader</code>. */
    private final FileIconReader fileIconReader;

    /** The most recent <code>ContainerVersion</code>. */
    private ContainerVersion latestVersion;

    /** The panel localization. */
    private final Localization localization;

    /** The container tab's <code>PopupDelegate</code>. */
    private PopupDelegate popupDelegate;

    /** A <code>Map</code> of <code>ContainerVersion</code> to <code>User</code>. */
    private final Map<ContainerVersion, User> publishedBy;

    /** A <code>Map</code> of <code>ContainerVersion</code> to <code>PublishedToView</code>. */
    private final Map<ContainerVersion, PublishedToView> publishedTo;

    /** A <code>List</code> of all <code>TeamMember</code>s. */
    private final List<TeamMember> team;

    /** The user id <code>JabberId</code>. */
    private JabberId userId;

    /** The <code>List</code> of <code>ContainerVersion</code>s. */
    private final List<ContainerVersion> versions;

    /** The west list of <code>PanelCellRenderer</code>.*/
    private final List<PanelCellRenderer> westCellPanels;

    /** The west list of <code>Cell</code>. */
    private final List<Cell> westCells;

    /** The visible west list model. */
    private final PanelCellListModel westListModel;

    /**
     * Create ContainerPanel.
     * 
     */
    public ContainerPanel(final BrowserSession session) {
        super(session);            
        this.documentViews = new HashMap<ContainerVersion, List<DocumentView>>();
        this.eastCellPanels = new ArrayList<PanelCellRenderer>();
        this.fileIconReader = new FileIconReader();
        this.localization = new BrowserLocalization("ContainerPanel");
        this.publishedBy = new HashMap<ContainerVersion, User>();
        this.publishedTo = new HashMap<ContainerVersion, PublishedToView>();
        this.team = new ArrayList<TeamMember>();
        this.versions = new ArrayList<ContainerVersion>();
        this.westCells = new ArrayList<Cell>();
        this.westCellPanels = new ArrayList<PanelCellRenderer>();
        for (int index = 0; index < NUMBER_VISIBLE_ROWS; index++) {
            eastCellPanels.add(new EastCellRenderer(this));
            if (0 == index) {
                westCellPanels.add(new TopWestCellRenderer(this));
            } else {
                westCellPanels.add(new WestCellRenderer(this));
            }
        }

        this.eastListModel = new PanelCellListModel(this, "eastList",
                localization, NUMBER_VISIBLE_ROWS, eastPreviousJLabel,
                eastCountJLabel, eastNextJLabel);
        this.westListModel = new PanelCellListModel(this, "westList",
                localization, NUMBER_VISIBLE_ROWS, westPreviousJLabel,
                westCountJLabel, westNextJLabel);
        initComponents();
        expandedData = Boolean.FALSE;
    }

    /**
     * Collapse the panel.
     * 
     */
    public void collapse(final boolean animate) {
        setBorder(BORDER_COLLAPSED);
        doCollapse(animate);
    }

    /**
     * Expand the panel.
     *
     */
    public void expand(final boolean animate) {
        setBorder(BORDER_EXPANDED);
        doExpand(animate);
    }

    /**
     * Handle mouse press on the cell expand/collapse icon.
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel#expandIconMousePressed(java.awt.event.MouseEvent)
     */
    @Override
    public void expandIconMousePressed(final MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1 && isExpandable() && !isAnimating()) {
            selectPanel();
            // request focus after the expand or collapse, otherwise the
            // focus will not be gained as it should be
            addPropertyChangeListener("expanded",
                    new PropertyChangeListener() {
                public void propertyChange(final PropertyChangeEvent e) {
                    removePropertyChangeListener("expanded", this);
                    requestFocusInWindow();
                }
            });
            tabDelegate.toggleExpansion(this);
        }
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
     * Get the date of the first version, or the creation
     * date of the container if there is no version yet.
     * 
     * @return The first version date <code>Calendar</code>.
     */
    public Calendar getDateFirstSeen() {
        if (!isDistributed()) {
            return container.getCreatedOn();
        } else {
            return earliestVersion.getCreatedOn();  
        }
    }

    /**
     * Get the date of the most recent version, or the creation
     * date of the container if there is no version yet.
     * 
     * @return The last version date <code>Calendar</code>.
     */
    public Calendar getDateLastSeen() {
        if (!isDistributed()) {
            return container.getCreatedOn();
        } else {
            return latestVersion.getCreatedOn();
        }
    }

    /**
     * Obtain the container draft.
     * 
     * @return A <code>ContainerDraft</code>.
     */
    public ContainerDraft getDraft() {
        return null == draft ? null : draft.getDraft();
    }

    /**
     * Obtain the container draft view.
     * 
     * @return A <code>DraftView</code>.
     */
    public DraftView getDraftView() {
        return draft;
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
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel#getPopupDelegate()
     * 
     */
    public PopupDelegate getPopupDelegate() {
        return popupDelegate;
    }

    /**
     * Obtain the published to information for a version.
     * 
     * @param version
     *            A <code>ContainerVersion</code>.
     * @return A <code>PublishedToView</code>.
     */
    public PublishedToView getPublishedTo(final ContainerVersion version) {
        return publishedTo.get(version);
    }

    /**
     * Obtain the team.
     * 
     * @return A list of <code>TeamMember</code>.
     */
    public List<TeamMember> getTeam() {
        return team;
    }

    /**
     * Obtain the versions.
     * 
     * @return A list of <code>ContainerVersion</code>.
     */
    public List<ContainerVersion> getVersions() {
        return versions;
    }

    /**
     * Determine if the container has been distributed.
     * 
     * @return True if the container has been distributed, false otherwise.
     */
    public Boolean isDistributed() {
        return null != latestVersion;
    }

    /**
     * Determine whether or not there is a local draft set.
     * 
     * @return True if there is a local draft.
     */
    public Boolean isLocalDraft() {
        return isSetDraft() && draft.isLocal();
    }

    /**
     * Determine whether or not the draft is set.
     * 
     * @return True if the draft is set.
     */
    public Boolean isSetDraft() {
        return null != draft && draft.isSetDraft();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel#isSetExpandedData()
     */
    public Boolean isSetExpandedData() {
        return expandedData;
    }

    /**
     * Handle mouse press on cells.
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel#panelCellMousePressed(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.Cell, com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel.PanelLocation, java.awt.event.MouseEvent)
     */
    @Override
    public void panelCellMousePressed(final Cell cell, final PanelLocation panelLocation, final MouseEvent e) {
        if (cell instanceof WestCell) {
            if (0 == westListModel.getIndexOf(cell)) {
                selectPanel();
            } else {
                westListModel.setSelectedCell(cell);
            }
            if (0 < westListModel.getIndexOf(cell)) {
                PanelFocusHelper.setFocus(PanelFocusHelper.Focus.WEST);
            }
            if (0 == westListModel.getIndexOf(cell)
                    && panelLocation == PanelLocation.BODY
                    && e.getClickCount() % 2 == 0
                    && e.getButton() == MouseEvent.BUTTON1) {
                tabDelegate.toggleExpansion(this);
            }
        } else if (cell instanceof EastCell) {
            eastListModel.setSelectedCell(cell);
            if (0 < eastListModel.getIndexOf(cell)) {
                PanelFocusHelper.setFocus(PanelFocusHelper.Focus.EAST);
            }
            if (panelLocation == PanelLocation.BODY
                    && e.getClickCount() % 2 == 0
                    && e.getButton() == MouseEvent.BUTTON1) {
                if (0 == eastListModel.getIndexOf(cell)) {
                    tabDelegate.toggleExpansion(this);
                } else if (cell.isActionAvailable()) {
                    cell.invokeAction();
                }
            }
        }
        repaint();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel#panelCellSelectionChanged()
     */
    @Override
    public void panelCellSelectionChanged(final Cell cell) {
        if (cell instanceof WestCell) {
            if (westListModel.isSelectionEmpty()) {
                eastListModel.initialize(null);
            } else {
                eastListModel.initialize(((WestCell)cell).getEastCells());
            }
            if (cell instanceof VersionCell) {
                ((VersionCell)cell).invokeSelectAction();
            }
            reloadButtons();
            repaint();
        } else if (cell instanceof EastCell) {
            repaint();
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel#refresh()
     */
    public void refresh() {
        if (isSetExpandedData()) {
            // fuzzy dates may be out of date, so refresh cells
            buildWestList();
        }
        repaint();
    }

    /**
     * Reload because the connection status has changed.
     */
    public void reloadConnection() {
        reloadButtons();
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
     * Select a draft document.
     * This method has no effect if the draft is not selected.
     * 
     * @param documentId
     *            The document id <code>Long</code>.
     */
    public void setDraftDocumentSelection(final Long documentId) {
        final Cell westCell = westListModel.getSelectedCell();
        if (westCell instanceof DraftCell) {
            final List<EastCell> eastCells = ((WestCell)westCell).getEastCells();
            for (final Cell cell : eastCells) {
                if (cell instanceof DraftDocumentCell) {
                    if (((DraftDocumentCell)cell).getDocument().getId().equals(documentId)) {
                        eastListModel.setSelectedCell(cell);
                        break;
                    }
                }
            }
        }
    }

    /**
     * Set the selection to be the draft.
     * This method has no effect if there is no draft cell.
     */
    public void setDraftSelection() {
        Assert.assertNotNull(draft,
                "Cannot set draft selection for container:  {0}",
                container.getId());
        if (westCells.size() > 1 &&
                westCells.get(1) instanceof DraftCell) {
            westListModel.showFirstPage();
            westListModel.setSelectedCell(westCells.get(1));
        }
    }

    /**
     * Set the panel data.
     * 
     * This version is appropriate (for example) when a flag has changed
     * or when the container name has changed.
     * 
     * @param container
     *            A <code>Container</code>.
     */
    public void setPanelData(final Container container) {
        this.container = container;
        if (isSetExpandedData()) {
            // set the container cell.
            westCells.set(0, new ContainerCell(draft, earliestVersion,
                    latestVersion, versions));
            // re-initialize
            westListModel.initialize(westCells);
        }
        iconJLabel.setIcon(container.isBookmarked() 
                ? IMAGE_CACHE.read(TabPanelIcon.CONTAINER_BOOKMARK) 
                : IMAGE_CACHE.read(TabPanelIcon.CONTAINER));
        reloadText();
    }

    /**
     * Set the panel data.
     * 
     * This version is appropriate (for example) when a flag has
     * changed on a version.
     * 
     * @param container
     *            A <code>Container</code>.
     * @param containerVersion
     *            A <code>ContainerVersion</code>.
     */
    public void setPanelData(final Container container,
            final ContainerVersion containerVersion) {
        this.container = container;
        if (containerVersion.equals(earliestVersion)) {
            earliestVersion = containerVersion;
        }
        if (containerVersion.equals(latestVersion)) {
            latestVersion = containerVersion;
        }
        for (final ContainerVersion existingVersion : versions) {
            if (containerVersion.equals(existingVersion)) {
                versions.set(versions.indexOf(existingVersion), containerVersion);
                break;
            }
        }
        if (isSetExpandedData()) {
            // set the container cell.
            westCells.set(0, new ContainerCell(draft, earliestVersion,
                    latestVersion, versions));
            // set the version cell
            for (final Cell cell : westCells) {
                if (cell instanceof VersionCell) {
                    if (((VersionCell) cell).getVersionId() == containerVersion
                            .getVersionId()) {
                        final Cell newCell = new VersionCell(containerVersion,
                                documentViews.get(containerVersion),
                                publishedTo.get(containerVersion),
                                publishedBy.get(containerVersion));
                        westCells.set(westCells.indexOf(cell), newCell);
                        break;
                    }
                }
            }
            // re-initialize
            westListModel.initialize(westCells);
        }
        iconJLabel.setIcon(container.isBookmarked() 
                ? IMAGE_CACHE.read(TabPanelIcon.CONTAINER_BOOKMARK) 
                : IMAGE_CACHE.read(TabPanelIcon.CONTAINER));
        reloadText();
    }

    /**
     * Set the panel data.
     * 
     * This version is appropriate (for example) when a document is added
     * or modified or removed, or when a draft is added or removed.
     * 
     * @param container
     *            A <code>Container</code>.
     * @param draft
     *            A <code>ContainerDraft</code>.
     */
    public void setPanelData(final Container container,
            final DraftView draftView) {
        this.container = container;
        this.draft = draftView;
        if (isSetExpandedData()) {
            // set the container cell.
            westCells.set(0, new ContainerCell(draft, earliestVersion,
                    latestVersion, versions));
            final Boolean wasLocalDraft = ((westCells.size() > 1) &&
                    (westCells.get(1) instanceof DraftCell));
            if (wasLocalDraft && isLocalDraft()) {
                // set the draft cell
                westCells.set(1, new DraftCell(draft));
            } else if (wasLocalDraft && !isLocalDraft()) {
                // remove the draft cell
                westCells.remove(1);
            } else if (!wasLocalDraft && isLocalDraft()) {
                // add the draft cell
                westCells.add(1, new DraftCell(draft));
            }
            // re-initialize
            westListModel.initialize(westCells);
        }
        iconJLabel.setIcon(container.isBookmarked() 
                ? IMAGE_CACHE.read(TabPanelIcon.CONTAINER_BOOKMARK) 
                : IMAGE_CACHE.read(TabPanelIcon.CONTAINER));
        reloadText();
    }

    /**
     * Set the panel data.
     * 
     * This version is appropriate for initializing collapsed panels.
     * 
     * @param container
     *            A <code>Container</code>.
     * @param draftView
     *            A <code>DraftView</code>.
     * @param earliestVersion
     *            The earliest <code>ContainerVersion</code>.
     * @param latestVersion
     *            The latest <code>ContainerVersion</code>.
     * @param userId
     *            The user id <code>JabberId</code>.     
     */
    public void setPanelData(final Container container,
            final DraftView draftView, final ContainerVersion earliestVersion,
            final ContainerVersion latestVersion,
            final JabberId userId) {
        this.container = container;
        this.draft = draftView;
        this.earliestVersion = earliestVersion;
        this.latestVersion = latestVersion;
        this.userId = userId;
        iconJLabel.setIcon(container.isBookmarked()
                ? IMAGE_CACHE.read(TabPanelIcon.CONTAINER_BOOKMARK)
                : IMAGE_CACHE.read(TabPanelIcon.CONTAINER));
        reloadText();
    }
    
    /**
     * Set the panel data.
     * 
     * This version is appropriate when the team has changed.
     * 
     * @param container
     *            A <code>Container</code>.
     * @param team
     *            A <code>List</code> of <code>TeamMember</code>s.
     */
    public void setPanelData(final Container container,
            final List<TeamMember> team) {
        this.container = container;
        this.team.clear();
        this.team.addAll(team);
        if (isSetExpandedData()) {
            // set the container cell.
            westCells.set(0, new ContainerCell(draft, earliestVersion,
                    latestVersion, versions));
            // re-initialize
            westListModel.initialize(westCells);
        }
        iconJLabel.setIcon(container.isBookmarked() 
                ? IMAGE_CACHE.read(TabPanelIcon.CONTAINER_BOOKMARK) 
                : IMAGE_CACHE.read(TabPanelIcon.CONTAINER));
        reloadText();
    }

    /**
     * Set the published to information for a version. We find the version cell
     * within the west cell list; and replace it with a new one; then initialize
     * the model.
     * 
     * @param version
     *            A <code>ContainerVersion</code>.
     * @param publishedTo
     *            A <code>PublishedToView</code>.
     */
    public void setPanelData(final ContainerVersion version,
            final PublishedToView publishedTo) {
        this.publishedTo.put(version, publishedTo);
        for (final Cell cell : westCells) {
            if (cell.getClass().isAssignableFrom(VersionCell.class)) {
                final VersionCell versionCell = (VersionCell) cell;
                if (versionCell.getVersionId().equals(version.getVersionId())) {
                    final Cell newVersionCell = new VersionCell(
                            versionCell.version, versionCell.documentViews,
                            publishedTo, versionCell.publishedBy);
                    westCells.set(westCells.indexOf(versionCell), newVersionCell);
                    westListModel.initialize(westCells);
                }
            }
        }
    }

    /**
     * Set the panel data.
     * 
     * This version is appropriate for initializing expanded panels. It should
     * be called after the version for collapsed panels.
     * 
     * @param versions
     *            The <code>List</code> of <code>ContainerVersion</code>s.
     * @param documentViews
     *            A <code>Map</code> of <code>ContainerVersion</code> to the <code>List</code> of <code>DocumentView</code>s.
     * @param publishedTo
     *            A <code>Map</code> of <code>ContainerVersion</code> to <code>PublishedToView</code>.
     * @param publishedBy
     *            A <code>Map</code> of <code>ContainerVersion</code> to <code>User</code>.
     * @param team
     *            A <code>List</code> of <code>TeamMember</code>s.
     */
    public void setPanelData(
            final List<ContainerVersion> versions,
            final Map<ContainerVersion, List<DocumentView>> documentViews,
            final Map<ContainerVersion, PublishedToView> publishedTo,
            final Map<ContainerVersion, User> publishedBy,
            final List<TeamMember> team) {
        Assert.assertNotNull("Container is null, setting panel data.", container);
        Assert.assertNotNull("Draft view is null, setting panel data.", draft);
        this.documentViews.clear();
        this.documentViews.putAll(documentViews);
        this.publishedTo.clear();
        this.publishedTo.putAll(publishedTo);
        this.publishedBy.clear();
        this.publishedBy.putAll(publishedBy);
        this.team.clear();
        this.team.addAll(team);
        this.versions.clear();
        this.versions.addAll(versions);
        buildWestList();
        iconJLabel.setIcon(container.isBookmarked()
                ? IMAGE_CACHE.read(TabPanelIcon.CONTAINER_BOOKMARK)
                : IMAGE_CACHE.read(TabPanelIcon.CONTAINER));
        reloadText();
        expandedData = Boolean.TRUE;
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
     * Select a version.
     * 
     * @param versionId
     *            The version id <code>Long</code>.
     */
    public void setVersionSelection(final Long versionId) {
        for (final Cell cell : westCells) {
            if (cell instanceof VersionCell) {
                if (((VersionCell)cell).getVersionId().equals(versionId)) {
                    westListModel.setSelectedCell(cell);
                    break;
                }
            }
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel#addPopupListeners()
     */
    @Override
    protected void addPopupListeners() {
        final KeyboardPopupHelper keyboardPopupHelper = new KeyboardPopupHelper();
        keyboardPopupHelper.addPopupListener(ContainerPanel.this, new Runnable() {
            public void run() {
                if (!isExpanded()) {
                    selectPanel();
                    popupDelegate.initialize(ContainerPanel.this,
                                    ContainerPanel.this.getWidth() / 5,
                                    ContainerPanel.this.getHeight() / 2);
                    popupDelegate.showForContainer(container, getDraft());
                } else {
                    // if focus is on the east but the selection in the east is
                    // row 0, move the focus to the west.
                    if (PanelFocusHelper.Focus.EAST == PanelFocusHelper.getFocus() &&
                            0 == eastListModel.getSelectedIndex()) {
                        PanelFocusHelper.setFocus(PanelFocusHelper.Focus.WEST);
                    }
                    if (PanelFocusHelper.Focus.EAST == PanelFocusHelper.getFocus()) {
                        if (!eastListModel.isSelectionEmpty()) {
                            final Cell cell = eastListModel.getSelectedCell();
                            final int index = eastListModel.getSelectedIndex();
                            if (cell.isPopupAvailable()) {
                                final JComponent jComponent = (JComponent)getPanelCellRenderer(
                                                ListType.EAST_LIST, index);
                                popupDelegate.initialize(jComponent,
                                                jComponent.getWidth() / 5,
                                                jComponent.getHeight() / 2);
                                cell.showPopup(Boolean.FALSE);
                            }
                        }
                    } else {
                        if (!westListModel.isSelectionEmpty()) {
                            final Cell cell = westListModel.getSelectedCell();
                            final int index = westListModel.getSelectedIndex();
                            if (cell.isPopupAvailable()) {
                                final JComponent jComponent = (JComponent)getPanelCellRenderer(
                                        ListType.WEST_LIST, index);
                                popupDelegate.initialize(jComponent,
                                                jComponent.getWidth() / 4,
                                                jComponent.getHeight() / 2);
                                cell.showPopup(Boolean.FALSE);
                            }
                        }
                    }
                }
            }
        });
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel#bindExpandedKeys(java.lang.Boolean)
     */
    @Override
    protected void bindExpandedKeys(final Boolean expanded) {
        if (expanded) {
            bindKeyStrokeToLists(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, 0));
            bindKeyStrokeToLists(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, 0));
            bindKeyStrokeToLists(KeyStroke.getKeyStroke(KeyEvent.VK_HOME, 0));
            bindKeyStrokeToLists(KeyStroke.getKeyStroke(KeyEvent.VK_END, 0));
            bindKeyStrokeToLists(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0));
            bindKeyStrokeToLists(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0));
            bindKey(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), new AbstractAction() {
                public void actionPerformed(final ActionEvent e) {
                    invokeDeleteAction();
                }
            });
        } else {
            unbindKey(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, 0));
            unbindKey(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, 0));
            unbindKey(KeyStroke.getKeyStroke(KeyEvent.VK_HOME, 0));
            unbindKey(KeyStroke.getKeyStroke(KeyEvent.VK_END, 0));
            unbindKey(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0));
            unbindKey(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0));
            unbindKey(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel#bindKeys()
     */
    @Override
    protected void bindKeys() {
        super.bindKeys();

        // cursor left: focus on west list
        bindKey(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0),
                new AbstractAction() {
                    public void actionPerformed(final ActionEvent e) {
                        if (isExpanded()) {
                            PanelFocusHelper.setFocus(PanelFocusHelper.Focus.WEST);
                            repaint();
                        }
                    }
                });
        // cursor right: focus on east list
        bindKey(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0),
                new AbstractAction() {
                    public void actionPerformed(final ActionEvent e) {
                        if (isExpanded()) {
                            PanelFocusHelper.setFocus(PanelFocusHelper.Focus.EAST);
                            if (eastListModel.getListModel().size() > 1) {
                                eastListModel.setSelectedIndex(1);
                            }
                            repaint();
                        }
                    }
                });
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel#doCollapse(boolean, javax.swing.JPanel, javax.swing.JPanel)
     */
    @Override
    protected void doCollapse(final boolean animate, final JPanel collapsedJPanel, final JPanel expandedJPanel) {
        super.doCollapse(animate, collapsedJPanel, expandedJPanel);
        if (animate) {
            addPropertyChangeListener("expanded", new PropertyChangeListener() {
                public void propertyChange(final PropertyChangeEvent evt) {
                    removePropertyChangeListener("expanded", this);
                    reloadText();
                }
            });
        } else {
            reloadText();
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel#doExpand(boolean, javax.swing.JPanel, javax.swing.JPanel)
     */
    @Override
    protected void doExpand(final boolean animate, final JPanel collapsedJPanel, final JPanel expandedJPanel) {
        // select the first west cell
        if (westListModel.getListModel().size() > 1) {
            westListModel.showFirstPage();
            westListModel.setSelectedCell(westCells.get(1));
        }
        super.doExpand(animate, collapsedJPanel, expandedJPanel);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel#invokeAction()
     */
    @Override
    protected void invokeAction() {
        super.invokeAction();
        if (isExpanded()) {
            if (PanelFocusHelper.Focus.EAST == PanelFocusHelper.getFocus()) {
                if (!eastListModel.isSelectionEmpty()) {
                    final Cell cell = eastListModel.getSelectedCell();
                    if (cell.isActionAvailable()) {
                        cell.invokeAction();
                    }
                }
            } else {
                if (!westListModel.isSelectionEmpty()) {
                    final Cell cell = westListModel.getSelectedCell();
                    if (cell.isActionAvailable()) {
                        cell.invokeAction();
                    }
                }
            }
        }
    }

    /**
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     * 
     */
    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final int borderHeight = getBorder().getBorderInsets(this).top
                + getBorder().getBorderInsets(this).bottom;
        final int height = getHeight() - borderHeight;
        adjustBorderColor(isExpanded());
        if (isExpanded() || isAnimating()) {
            renderer.paintExpandedBackground(g, this);
            if (!westListModel.isSelectionEmpty()) {
                final int westSelectionIndex = westListModel.getSelectedIndex();
                final int eastSelectionIndex = eastListModel.getSelectedIndex();
                final int westWidth = westJPanel.getWidth()
                        + SwingUtilities.convertPoint(westJPanel, new Point(0, 0), this).x;
                renderer.paintExpandedBackgroundWest(g, westWidth, height, westSelectionIndex, this);
                renderer.paintExpandedBackgroundCenter(g, westWidth, height, westSelectionIndex, this);
                renderer.paintExpandedBackgroundEast(g, westWidth, getWidth() - westWidth, height, westSelectionIndex, this);
                if (westSelectionIndex>0 && eastSelectionIndex>0 && PanelFocusHelper.Focus.EAST == PanelFocusHelper.getFocus()) {
                    renderer.paintExpandedBackgroundEastSelection(g, westWidth,
                            getWidth() - westWidth, eastSelectionIndex, this);
                }
            }
        } else {
            renderer.paintBackground(g, getWidth(), height, selected);
        }

        // Paint text.
        // Text is painted (instead of using JLabels) so that when the container
        // is expanded the top west row can paint all the way across. Also
        // it is easier to control exact behavior of text clipping and positioning.
        final Graphics2D g2 = (Graphics2D)g.create();
        try {
            paintText(g2);
        }
        finally { g2.dispose(); }
    }

    private void actionsJButtonActionPerformed(final java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actionsJButtonActionPerformed
        if (!westListModel.isSelectionEmpty() && !MenuFactory.isPopupMenu()) {
            final Cell cell = westListModel.getSelectedCell();
            if (cell.isPopupAvailable()) {
                final JComponent jComponent = (JComponent)evt.getSource();
                final int bottomBorderHeight = jComponent.getBorder().getBorderInsets(jComponent).bottom;
                popupDelegate.initialize(jComponent, jComponent.getWidth(),
                        jComponent.getHeight() - bottomBorderHeight + 2,
                        Boolean.FALSE, Boolean.TRUE);
                cell.showPopup(Boolean.TRUE);
            }
        }
    }//GEN-LAST:event_actionsJButtonActionPerformed

    /**
     * Bind a keystroke destined for the east and west lists.
     * 
     * @param keyStroke
     *            A <code>KeyStroke</code>.
     */
    private void bindKeyStrokeToLists(final KeyStroke keyStroke) {
        bindKey(keyStroke, new AbstractAction() {
            public void actionPerformed(final ActionEvent e) {
                if (isExpanded()) {
                    if (PanelFocusHelper.Focus.EAST == PanelFocusHelper.getFocus()) {
                        eastListModel.processKeyStroke(keyStroke);
                    } else {
                        westListModel.processKeyStroke(keyStroke);
                    }
                }
            }
        });
    }

    /**
     * Build the west list.
     */
    private void buildWestList() {
        westCells.clear();
        westCells.add(new ContainerCell(draft, earliestVersion, latestVersion, versions));
        if (isLocalDraft()) {
            westCells.add(new DraftCell(draft));
        }
        for (final ContainerVersion version : versions) {
            westCells.add(new VersionCell(version, documentViews.get(version),
                    publishedTo.get(version), publishedBy.get(version)));
        }
        westListModel.initialize(westCells);
    }

    private void collapsedJPanelMousePressed(final java.awt.event.MouseEvent e) {//GEN-FIRST:event_collapsedJPanelMousePressed
        logger.logApiId();
        logger.logVariable("e", e);
        if (e.getClickCount() == 1 && e.isPopupTrigger()) {
            selectPanel();
            popupDelegate.initialize((Component) e.getSource(), e.getX(), e.getY());
            popupDelegate.showForContainer(container, getDraft());
        } else if (e.getClickCount() == 1 && e.getButton() == MouseEvent.BUTTON1) {
            selectPanel();
        } else if ((e.getClickCount() % 2) == 0 && e.getButton() == MouseEvent.BUTTON1) {
            tabDelegate.toggleExpansion(this);
        }
    }//GEN-LAST:event_collapsedJPanelMousePressed

    private void collapsedJPanelMouseReleased(final java.awt.event.MouseEvent e) {//GEN-FIRST:event_collapsedJPanelMouseReleased
        logger.logApiId();
        logger.logVariable("e", e);
        if (e.getClickCount() == 1 && e.isPopupTrigger()) {
            selectPanel();
            popupDelegate.initialize((Component) e.getSource(), e.getX(), e.getY());
            popupDelegate.showForContainer(container, getDraft());
        }
    }//GEN-LAST:event_collapsedJPanelMouseReleased

    /**
     * Collapse the panel.
     * 
     * @param animate
     *            Whether or not to animate.
     */
    private void doCollapse(final boolean animate) {
        doCollapse(animate, collapsedJPanel, expandedJPanel);
    }

    /**
     * Expand the panel.
     * 
     * @param animate
     *            Whether or not to animate.
     */
    private void doExpand(final boolean animate) {
        doExpand(animate, collapsedJPanel, expandedJPanel);
    }

    private void eastJPanelMousePressed(final java.awt.event.MouseEvent e) {//GEN-FIRST:event_eastJPanelMousePressed
        selectPanel();
    }//GEN-LAST:event_eastJPanelMousePressed

    private void eastJPanelMouseReleased(final java.awt.event.MouseEvent e) {//GEN-FIRST:event_eastJPanelMouseReleased
    }//GEN-LAST:event_eastJPanelMouseReleased

    private void expandedJPanelMousePressed(final java.awt.event.MouseEvent e) {//GEN-FIRST:event_expandedJPanelMousePressed
        selectPanel();
    }//GEN-LAST:event_expandedJPanelMousePressed

    private void expandedJPanelMouseReleased(final java.awt.event.MouseEvent e) {//GEN-FIRST:event_expandedJPanelMouseReleased
    }//GEN-LAST:event_expandedJPanelMouseReleased

    private void expandIconJLabelMouseEntered(final java.awt.event.MouseEvent e) {//GEN-FIRST:event_expandIconJLabelMouseEntered
        if (!isAnimating()) {
            SwingUtil.setCursor((javax.swing.JLabel) e.getSource(), java.awt.Cursor.HAND_CURSOR);
        }
    }//GEN-LAST:event_expandIconJLabelMouseEntered

    private void expandIconJLabelMouseExited(final java.awt.event.MouseEvent e) {//GEN-FIRST:event_expandIconJLabelMouseExited
        SwingUtil.setCursor((javax.swing.JLabel) e.getSource(), null);
    }//GEN-LAST:event_expandIconJLabelMouseExited

    private void expandIconJLabelMousePressed(final java.awt.event.MouseEvent e) {//GEN-FIRST:event_expandIconJLabelMousePressed
        SwingUtil.setCursor((javax.swing.JLabel) e.getSource(), java.awt.Cursor.WAIT_CURSOR);
        expandIconMousePressed(e);
        SwingUtil.setCursor((javax.swing.JLabel) e.getSource(), null);
    }//GEN-LAST:event_expandIconJLabelMousePressed

    private void expandIconJLabelMouseReleased(final java.awt.event.MouseEvent e) {//GEN-FIRST:event_expandIconJLabelMouseReleased
        if (e.getClickCount() == 1 && e.isPopupTrigger()) {
            popupDelegate.initialize((Component) e.getSource(), e.getX(), e.getY());
            popupDelegate.showForContainer(container, getDraft());
        }
    }//GEN-LAST:event_expandIconJLabelMouseReleased

    /**
     * Get additional text associated with the container.
     * 
     * @param container
     *       The <code>Container</code>.
     * @return A <code>String</code>.
     */
    private String getContainerAdditionalText(final Container container) {
        if (null != latestVersion) {
            if (container.isLatest()) {
                return localization.getString("ContainerMessagePublishDate",
                        new Object[] {formatFuzzy(latestVersion.getCreatedOn())});
            } else {
                return localization.getString("ContainerMessageNotLatest");
            }
        } else {
            return "";
        }
    }

    /**
     * Get the color for the container additional text.
     * 
     * @param container
     *       The <code>Container</code>.
     * @return A <code>Color</code>.
     */
    private Color getContainerAdditionalTextColor(final Container container) {
        return Colors.Browser.Panel.PANEL_ADDITIONAL_TEXT_FG;
    }

    /**
     * Get the font for the container additional text.
     * 
     * @return A <code>Font</code>.
     */
    private Font getContainerAdditionalTextFont() {
        return getContainerTextFont();
    }

    /**
     * Get the east text associated with the container.
     * 
     * @param container
     *       The <code>Container</code>.
     * @return A <code>String</code>.
     */
    private String getContainerEastText(final Container container) {
        if (container.isLatest() && isSetDraft()) {
            if (isDistributed()) {
                return localization.getString("ContainerDraftOwner",
                        new Object[] { getDraft().getOwner().getName() });
            } else {
                return localization.getString("ContainerNotDistributed");
            }
        } else {
            return null;
        }
    }

    /**
     * Get the color for the container east text.
     * 
     * @param container
     *       The <code>Container</code>.
     * @return A <code>Color</code>.
     */
    private Color getContainerEastTextColor(final Container container) {
        return Colors.Browser.Panel.PANEL_ADDITIONAL_TEXT_FG;
    }

    /**
     * Get the font for the container east text.
     * 
     * @return A <code>Font</code>.
     */
    private Font getContainerEastTextFont() {
        return Fonts.DefaultFont;
    }

    /**
     * Get the text associated with the container.
     * 
     * @param container
     *       The <code>Container</code>.
     * @return A <code>String</code>.
     */
    private String getContainerText(final Container container) {
        return container.getName();
    }

    /**
     * Get the color for the container text.
     * 
     * @param container
     *       The <code>Container</code>.
     * @return A <code>Color</code>.
     */
    private Color getContainerTextColor(final Container container) {
        return Colors.Browser.Panel.PANEL_CONTAINER_TEXT_FG;
    }

    /**
     * Get the font for the container text.
     * 
     * @return A <code>Font</code>.
     */
    private Font getContainerTextFont() {
        if (!container.isSeen().booleanValue()) {
            return Fonts.DefaultFontBold;
        } else {
            return Fonts.DefaultFont;
        }
    }

    /**
     * Get a panel cell.
     * 
     * @param listType
     *          The list type.
     * @return The panel cell.
     */
    private PanelCellRenderer getPanelCellRenderer(final ListType listType, final int index) {
        Assert.assertTrue("INVALID PANEL CELL INDEX", (index>=0 && index<NUMBER_VISIBLE_ROWS));
        if (ListType.WEST_LIST == listType) {
            return westCellPanels.get(index);
        } else if (ListType.EAST_LIST == listType) {
            return eastCellPanels.get(index);
        } else {
            throw Assert.createUnreachable("UNKNOWN LIST TYPE");
        }
    }

    private void iconJLabelMouseEntered(final java.awt.event.MouseEvent e) {//GEN-FIRST:event_iconJLabelMouseEntered
        SwingUtil.setCursor((javax.swing.JLabel) e.getSource(), java.awt.Cursor.HAND_CURSOR);
    }//GEN-LAST:event_iconJLabelMouseEntered

    private void iconJLabelMouseExited(final java.awt.event.MouseEvent e) {//GEN-FIRST:event_iconJLabelMouseExited
        SwingUtil.setCursor((javax.swing.JLabel) e.getSource(), null);
    }//GEN-LAST:event_iconJLabelMouseExited

    private void iconJLabelMousePressed(final java.awt.event.MouseEvent e) {//GEN-FIRST:event_iconJLabelMousePressed
        if (e.getButton() == MouseEvent.BUTTON1) {
            selectPanel();
            actionDelegate.invokeForContainer(container);
        }
    }//GEN-LAST:event_iconJLabelMousePressed

    private void iconJLabelMouseReleased(final java.awt.event.MouseEvent e) {//GEN-FIRST:event_iconJLabelMouseReleased
        if (e.getClickCount() == 1 && e.isPopupTrigger()) {
            popupDelegate.initialize((Component) e.getSource(), e.getX(), e.getY());
            popupDelegate.showForContainer(container, getDraft());
        }
    }//GEN-LAST:event_iconJLabelMouseReleased

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        final javax.swing.JPanel fixedSizeJPanel = new javax.swing.JPanel();
        westListJPanel = new PanelCellListJPanel(westListModel, ListType.WEST_LIST);
        final javax.swing.JLabel westFillerJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel westFiller2JLabel = new javax.swing.JLabel();
        eastListJPanel = new PanelCellListJPanel(eastListModel, ListType.EAST_LIST);
        final javax.swing.JLabel eastFillerJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel eastFiller2JLabel = new javax.swing.JLabel();
        final javax.swing.JPanel fillerJPanel = new javax.swing.JPanel();

        setLayout(new java.awt.GridBagLayout());

        setBorder(BORDER_COLLAPSED);
        collapsedJPanel.setLayout(new java.awt.GridBagLayout());

        collapsedJPanel.setOpaque(false);
        collapsedJPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                collapsedJPanelMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                collapsedJPanelMouseReleased(evt);
            }
        });

        expandIconJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/IconExpand.png")));
        expandIconJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                expandIconJLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                expandIconJLabelMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                expandIconJLabelMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                expandIconJLabelMouseReleased(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 10, 0, 0);
        collapsedJPanel.add(expandIconJLabel, gridBagConstraints);

        iconJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/IconContainer.png")));
        iconJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                iconJLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                iconJLabelMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                iconJLabelMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                iconJLabelMouseReleased(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 1, 0, 5);
        collapsedJPanel.add(iconJLabel, gridBagConstraints);

        textJLabel.setText("!Package Text!");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 4, 0);
        collapsedJPanel.add(textJLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        add(collapsedJPanel, gridBagConstraints);

        expandedJPanel.setOpaque(false);
        expandedJPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                expandedJPanelMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                expandedJPanelMouseReleased(evt);
            }
        });

        fixedSizeJPanel.setLayout(new java.awt.GridLayout(1, 0));

        fixedSizeJPanel.setOpaque(false);
        westJPanel.setLayout(new java.awt.GridBagLayout());

        westJPanel.setOpaque(false);
        westListJPanel.setLayout(new java.awt.GridBagLayout());

        westListJPanel.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        westJPanel.add(westListJPanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 73, 8, 0);
        westJPanel.add(westFillerJLabel, gridBagConstraints);

        westPreviousJLabel.setFont(Fonts.DialogPageFont);
        westPreviousJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/PagePrevious.png")));
        westPreviousJLabel.setIconTextGap(3);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 8, 6);
        westJPanel.add(westPreviousJLabel, gridBagConstraints);

        westCountJLabel.setFont(Fonts.DialogPageFont);
        westCountJLabel.setForeground(Colours.DIALOG_PAGE_TEXT_FG);
        westCountJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("ContainerPanel.countJLabel"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 8, 0);
        westJPanel.add(westCountJLabel, gridBagConstraints);

        westNextJLabel.setFont(Fonts.DialogPageFont);
        westNextJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/PageNext.png")));
        westNextJLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        westNextJLabel.setIconTextGap(2);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 8, 0);
        westJPanel.add(westNextJLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 8, 0);
        westJPanel.add(westFiller2JLabel, gridBagConstraints);

        fixedSizeJPanel.add(westJPanel);

        eastJPanel.setLayout(new java.awt.GridBagLayout());

        eastJPanel.setOpaque(false);
        eastJPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                eastJPanelMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                eastJPanelMouseReleased(evt);
            }
        });

        eastListJPanel.setLayout(new java.awt.GridBagLayout());

        eastListJPanel.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        eastJPanel.add(eastListJPanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 32, 3, 0);
        eastJPanel.add(eastFillerJLabel, gridBagConstraints);

        eastPreviousJLabel.setFont(Fonts.DialogPageFont);
        eastPreviousJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/PagePrevious.png")));
        eastPreviousJLabel.setIconTextGap(3);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 6);
        eastJPanel.add(eastPreviousJLabel, gridBagConstraints);

        eastCountJLabel.setFont(Fonts.DialogPageFont);
        eastCountJLabel.setForeground(Colours.DIALOG_PAGE_TEXT_FG);
        eastCountJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("ContainerPanel.countJLabel"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 0);
        eastJPanel.add(eastCountJLabel, gridBagConstraints);

        eastNextJLabel.setFont(Fonts.DialogPageFont);
        eastNextJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/PageNext.png")));
        eastNextJLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        eastNextJLabel.setIconTextGap(2);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 3, 0);
        eastJPanel.add(eastNextJLabel, gridBagConstraints);

        commonActionJButton.setFont(Fonts.DialogButtonFont);
        commonActionJButton.setText("!Action!");
        commonActionJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                commonActionJButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 6);
        eastJPanel.add(commonActionJButton, gridBagConstraints);

        actionsJButton.setFont(Fonts.DialogButtonFont);
        actionsJButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ButtonDownArrow.png")));
        actionsJButton.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("containerPanel.MoreActions"));
        actionsJButton.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        actionsJButton.setMargin(new java.awt.Insets(2, 11, 2, 9));
        actionsJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actionsJButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 13);
        eastJPanel.add(actionsJButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 0);
        eastJPanel.add(eastFiller2JLabel, gridBagConstraints);

        fixedSizeJPanel.add(eastJPanel);

        org.jdesktop.layout.GroupLayout expandedJPanelLayout = new org.jdesktop.layout.GroupLayout(expandedJPanel);
        expandedJPanel.setLayout(expandedJPanelLayout);
        expandedJPanelLayout.setHorizontalGroup(
            expandedJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, fixedSizeJPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 599, Short.MAX_VALUE)
            .add(fillerJPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 599, Short.MAX_VALUE)
        );
        expandedJPanelLayout.setVerticalGroup(
            expandedJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(expandedJPanelLayout.createSequentialGroup()
                .add(fixedSizeJPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 169, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(fillerJPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        add(expandedJPanel, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents

    /**
     * Invoke the delete action on the selected object.
     */
    private void invokeDeleteAction() {
        if (isExpanded()) {
            // delete document
            if (PanelFocusHelper.Focus.EAST == PanelFocusHelper.getFocus()) {
                if (!eastListModel.isSelectionEmpty()) {
                    final Cell cell = eastListModel.getSelectedCell();
                    if (cell.isDeleteActionAvailable()) {
                        cell.invokeDeleteAction();
                    }
                }
            } else {
                if (!westListModel.isSelectionEmpty()) {
                    final Cell cell = westListModel.getSelectedCell();
                    if (cell.isDeleteActionAvailable()) {
                        cell.invokeDeleteAction();
                    }
                }
            }
        }
    }

    /**
     * Determine if the specified user is the local user.
     * 
     * @param user
     *            A <code>User</code>.
     * @return True if the specified user is the local user.
     */
    private boolean isLocalUser(final User user) {
        return user.getId().equals(userId);
    }

    /**
     * Paint text on the panel.
     * 
     * @param g2
     *            The <code>Graphics2D</code>.
     */
    private void paintText(final Graphics2D g2) {
        final String containerText = getContainerText(container);
        final String additionalText = getContainerAdditionalText(container);
        final String draftOwnerText = getContainerEastText(container);

        // paint container name text
        g2.setFont(getContainerTextFont());
        final Point location = new Point(CONTAINER_TEXT_X, CONTAINER_TEXT_Y + g2.getFontMetrics().getMaxAscent());
        int availableWidth = getWidth() - location.x - CONTAINER_TEXT_SPACE_END;
        if (null != draftOwnerText) {
            availableWidth -= (getWidth() * FRACTION_WIDTH_DRAFT_OWNER_NAME + CONTAINER_TEXT_SPACE_END);
        }
        final String clippedText = SwingUtil.limitWidthWithEllipsis(containerText, availableWidth, g2);
        paintText(g2, location, getContainerTextColor(container), clippedText);

        // paint additional text after container text
        if (null != clippedText && clippedText.equals(containerText)) {
            g2.setFont(getContainerAdditionalTextFont());
            final int adjustX = SwingUtil.getStringWidth(containerText, g2) + CONTAINER_TEXT_SPACE_BETWEEN;
            location.x += adjustX;
            availableWidth -= adjustX;
            final String clippedAdditionalText = SwingUtil.limitWidthWithEllipsis(additionalText, availableWidth, g2);
            if (null != clippedAdditionalText) {
                paintText(g2, location, getContainerAdditionalTextColor(container), clippedAdditionalText);
            }
        }

        // paint draft owner text on the right
        if (null != draftOwnerText) {
            g2.setFont(getContainerEastTextFont());
            availableWidth = (int)(getWidth() * FRACTION_WIDTH_DRAFT_OWNER_NAME);
            final String clippedDraftOwnerText = SwingUtil.limitWidthWithEllipsis(draftOwnerText, availableWidth, g2);
            final int textWidth = SwingUtil.getStringWidth(clippedDraftOwnerText, g2);
            location.x = getWidth() - textWidth - CONTAINER_TEXT_SPACE_END;
            paintText(g2, location, getContainerEastTextColor(container), clippedDraftOwnerText);
        }
    }

    /**
     * Paint text on the panel.
     * 
     * @param g2
     *            The <code>Graphics2D</code>.
     * @param location
     *            The text location <code>Point</code>.
     * @param color
     *            The text <code>Color</code>.
     * @param text
     *            The text <code>String</code>.
     */
    private void paintText(final Graphics2D g2, final Point location, final Color color, final String text) {
        g2.setPaint(color);
        g2.drawString(text, location.x, location.y);
    }

    private void commonActionJButtonActionPerformed(final java.awt.event.ActionEvent evt) {//GEN-FIRST:event_primaryActionJButtonActionPerformed
        final Cell cell = westListModel.getSelectedCell();
        if (cell.isCommonActionAvailable()) {
            cell.invokeCommonAction();
        }
    }//GEN-LAST:event_primaryActionJButtonActionPerformed

    /**
     * Reload the buttons.
     */
    private void reloadButtons() {
        // handle empty selection, which may be the case if the panel has never been expanded
        if (!westListModel.isSelectionEmpty()) {
            final Cell cell = westListModel.getSelectedCell();
            commonActionJButton.setVisible(cell.isCommonActionAvailable().booleanValue());
            if (cell.isCommonActionAvailable()) {
                commonActionJButton.setEnabled(cell.isCommonActionEnabled().booleanValue());
                commonActionJButton.setText(cell.getCommonActionDisplayName());
            }
        }
    }

    /**
     * Reload the text on the panel.
     */
    private void reloadText() {
        // The container text is painted instead of setting JLabel text.
        // This makes it possible to draw text all the way across the panel when
        // the container is expanded. So, make the text in the JLabel blank.
        textJLabel.setText(" ");
    }

    /**
     * Select this panel.
     */
    private void selectPanel() {
        if (!isExpanded()) {
            // if expanded, leave the focus east or west
            PanelFocusHelper.setFocus(PanelFocusHelper.Focus.PANEL);
        }
        tabDelegate.selectPanel(this);
    }

    /** An east list cell. */
    private abstract class AbstractEastCell extends DefaultCell implements
            EastCell {

        /**
         * Create AbstractEastCell.
         *
         */
        private AbstractEastCell(final WestCell parent) {
            super();
        }
    }

    /** A west list cell. */
    private abstract class AbstractWestCell extends WestCell {
        /**
         * Create AbstractWestCell.
         * 
         * @param emptyEastCellPopupAvailable
         *       A <code>Boolean</code> indicating if there is a popup in the empty east cell.
         */
        private AbstractWestCell(final Boolean emptyEastCellPopupAvailable) {
            super();
            add(new EmptyEastCell(this, emptyEastCellPopupAvailable));
        }

        /**
         * Get the selected east cell.
         * 
         * @return The selected <code>EastCell</code> or null if there is none.
         */
        protected EastCell getSelectedEastCell() {
            if (isSelectedEastCell()) {
                return (EastCell) eastListModel.getSelectedCell();
            } else {
                return null;
            }
        }

        /**
         * Determine if there is a selected east cell.
         * 
         * @return True if there is a selected east cell.
         */
        protected Boolean isSelectedEastCell() {
            return !eastListModel.isSelectionEmpty()
                    && PanelFocusHelper.Focus.EAST == PanelFocusHelper.getFocus();
        }
    }

    /** A container cell. */
    private final class ContainerCell extends AbstractWestCell {
        /**
         * Create ContainerCell.
         */
        private ContainerCell(final DraftView draftView,
                final ContainerVersion earliestVersion,
                final ContainerVersion latestVersion,
                final List<ContainerVersion> versions) {
            super(Boolean.TRUE);
        }
        @Override
        public String getCommonActionDisplayName() {
            final StringBuffer key = new StringBuffer("CommonAction.")
                    .append(actionDelegate.getCommonActionForContainerDisplayNameKey(container, getDraft()));
            return localization.getString(key.toString());
        }
        @Override
        public Icon getIcon() {
            if (container.isBookmarked())
                return IMAGE_CACHE.read(TabPanelIcon.CONTAINER_BOOKMARK);
            else
                return IMAGE_CACHE.read(TabPanelIcon.CONTAINER);
        }
        public String getId() {
            return "container";
        }
        @Override
        public String getText() {
            return container.getName();
        }
        @Override
        public void invokeAction() {
            actionDelegate.invokeForContainer(container);
        }
        @Override
        public void invokeCommonAction() {
            actionDelegate.invokeCommonActionForContainer(container, getDraft());
        }
        @Override
        public void invokeDeleteAction() {
            actionDelegate.invokeDeleteForContainer(container);
        }
        @Override
        public Boolean isActionAvailable() {
            return Boolean.TRUE;
        }
        @Override
        public Boolean isActionDelayRequired() {
            return Boolean.FALSE;
        }
        @Override
        public Boolean isCommonActionAvailable() {
            // Note that as of the December 4, 2007 update the container can be selected
            // only in the circumstance that there is no draft and no version, a state
            // that is supported for backwards compatibility only. In this state the 
            // the 'create draft' common action is always available.
            return Boolean.TRUE;
        }
        @Override
        public Boolean isCommonActionEnabled() {
            return actionDelegate.isCommonActionForContainerEnabled(container, getDraft());
        }
        @Override
        public Boolean isDeleteActionAvailable() {
            return Boolean.TRUE;
        }
        @Override
        public void showPopup(final Boolean showAll) {
            // the local context popup is not displayed for the container
            // when it is expanded.
            if (showAll) {
                popupDelegate.showAll(container, getDraft());
            }
        }
    }

    /** A draft cell. */
    private final class DraftCell extends AbstractWestCell {
        /**
         * Create DraftCell.
         *
         */
        private DraftCell(final DraftView draftView) {
            super(Boolean.FALSE);
            for (final Document document : draftView.getDocuments()) {
                add(new DraftDocumentCell(this, document));
            }
        }
        @Override
        public String getCommonActionDisplayName() {
            final StringBuffer key = new StringBuffer("CommonAction.")
                    .append(actionDelegate.getCommonActionForDraftDisplayNameKey(container, getDraft()));
            return localization.getString(key.toString());
        }
        @Override
        public Icon getIcon() {
            return IMAGE_CACHE.read(TabPanelIcon.DRAFT);
        }
        @Override
        public String getId() {
            return "draft";
        }
        @Override
        public String getText() {
            if (isLocalDraft()) {
                return localization.getString("Draft");
            } else {
                return localization.getString("DraftNotLocal", new Object[] {getDraft().getOwner().getName()});
            }
        }
        @Override
        public void invokeAction() {
            actionDelegate.invokeForDraft(getDraft());
        }
        @Override
        public void invokeCommonAction() {
            actionDelegate.invokeCommonActionForDraft(container, getDraft());
        }
        @Override
        public void invokeDeleteAction() {
            actionDelegate.invokeDeleteForDraft(container, getDraft());
        }
        @Override
        public Boolean isActionAvailable() {
            return Boolean.TRUE;
        }
        @Override
        public Boolean isCommonActionAvailable() {
            return Boolean.TRUE;
        }
        @Override
        public Boolean isCommonActionEnabled() {
            return actionDelegate.isCommonActionForDraftEnabled(container, getDraft());
        }
        @Override
        public Boolean isDeleteActionAvailable() {
            return Boolean.TRUE;
        }
        @Override
        public void showPopup(final Boolean showAll) {
            if (showAll) {
                boolean documentSelected = false;
                if (isSelectedEastCell()) {
                    final EastCell cell = getSelectedEastCell();
                    if (cell instanceof DraftDocumentCell) {
                        documentSelected = true;
                        popupDelegate.showAll(container, getDraft(),
                                ((DraftDocumentCell)cell).getDocument());
                    }
                }
                if (!documentSelected) {
                    popupDelegate.showAll(container, getDraft());
                }
            } else {
                popupDelegate.showForDraft(container, getDraft());
            }
        }
    }

    /** A draft document cell. */
    private final class DraftDocumentCell extends AbstractEastCell {
        /** A <code>Document</code>. */
        private final Document document;
        private DraftDocumentCell(final WestCell parent, final Document document) {
            super(parent);
            this.document = document;
            setIcon(fileIconReader.getIcon(document));
            switch (getDraft().getState(document)) {
            case ADDED:
            case MODIFIED:
            case REMOVED:
                setAdditionalText(localization.getString(getDraft().getState(document)));
                break;
            case NONE:
                break;
            default:
                throw Assert.createUnreachable("UNKNOWN DOCUMENT STATE");
            }
            setText(document.getName());
        }
        public Document getDocument() {
            return document;
        }
        @Override
        public String getId() {
            return new StringBuffer("draftDocument-").append(document.getId()).toString();
        }
        @Override
        public void invokeAction() {
            actionDelegate.invokeForDocument(getDraft(), document);
        }
        @Override
        public void invokeDeleteAction() {
            actionDelegate.invokeDeleteForDocument(getDraft(), document);
        }
        @Override
        public Boolean isActionAvailable() {
            return (getDraft().getState(document) != ArtifactState.REMOVED);
        }
        @Override
        public Boolean isDeleteActionAvailable() {
            return (getDraft().getState(document) != ArtifactState.REMOVED);
        }
        @Override
        public void showPopup(final Boolean showAll) {
            popupDelegate.showForDocument(getDraft(), document);
        }
    }

    private final class EmptyEastCell extends AbstractEastCell {
        
        /** A <code>Boolean</code> indicating if the popup is available. */
        private final Boolean popupAvailable;
        
        /**
         * Create EmptyEastCell.
         */
        private EmptyEastCell(final WestCell parent, final Boolean popupAvailable) {
            super(parent);
            this.popupAvailable = popupAvailable;
        }
        /**
         * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.DefaultCell#getId()
         */
        @Override
        public String getId() {
            return "empty";
        }
        /**
         * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.DefaultCell#isActionAvailable()
         */
        @Override
        public Boolean isActionAvailable() {
            return Boolean.FALSE;
        }
        /**
         * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.DefaultCell#isPopupAvailable()
         */
        @Override
        public Boolean isPopupAvailable() {
            return popupAvailable;
        }
    }

    private enum ListType { EAST_LIST, WEST_LIST }

    private class PanelCellListJPanel extends javax.swing.JPanel {
        
        /**
         * The set of <code>GridBagConstraints</code> used when adding a fill
         * component.
         */
        private final GridBagConstraints fillConstraints;
        
        /** A fill <code>JLabel</code>. */
        private final javax.swing.JLabel fillJLabel = new javax.swing.JLabel();
        
        /** The list type. */
        private final ListType listType;

        /** The list model. */
        private final PanelCellListModel panelCellListModel;
        
        /** The set of <code>GridBagConstraints</code> used when adding a panel. */
        private final GridBagConstraints panelConstraints;
        
        PanelCellListJPanel(final PanelCellListModel panelCellListModel,
                final ListType listType) {
            super();
            this.panelCellListModel = panelCellListModel;
            this.listType = listType;
            this.fillConstraints = new GridBagConstraints();
            this.fillConstraints.fill = GridBagConstraints.HORIZONTAL;
            this.fillConstraints.weightx = 1.0F;
            this.fillConstraints.weighty = 1.0F;
            this.fillConstraints.gridx = 0;
            this.fillConstraints.gridy = GridBagConstraints.RELATIVE;
            this.panelConstraints = new GridBagConstraints();
            this.panelConstraints.fill = GridBagConstraints.BOTH;
            this.panelConstraints.gridx = 0;
            add(fillJLabel, new java.awt.GridBagConstraints());
            installDataListener();
        }
        
        /**
         * Add the fill component.
         * 
         * @param listSize
         *            The <code>int</code> size of the list.
         */
        private void addFill(final int listSize) {
            add(fillJLabel, fillConstraints, listSize);
        }

        /**
         * Add a panel from a model at an index.
         * 
         * @param cell
         *            A <code>Cell</code>.
         * @param index
         *            An <code>int</code> index.
         */
        private void addPanel(final Cell cell, final int index) {
            panelConstraints.gridy = index;
            final PanelCellRenderer panelCell = getPanelCellRenderer(listType, index);
            panelCell.renderComponent(cell, index);
            panelCell.setPanelSelectionManager(panelCellListModel);
            add((Component)panelCell, panelConstraints.clone(), index);
        }

        private void installDataListener() {
            final DefaultListModel listModel = panelCellListModel.getListModel();
            listModel.addListDataListener(new ListDataListener() {
                /**
                 * @see javax.swing.event.ListDataListener#contentsChanged(javax.swing.event.ListDataEvent)
                 */
                public void contentsChanged(final ListDataEvent e) {
                    removeFill();
                    for (int i = e.getIndex0(); i <= e.getIndex1(); i++) {
                        removePanel(i);
                        addPanel((Cell) listModel.get(i), i);
                    }
                    addFill(listModel.size());
                    reloadPanels();
                }
                
                /**
                 * @see javax.swing.event.ListDataListener#intervalAdded(javax.swing.event.ListDataEvent)
                 */
                public void intervalAdded(final ListDataEvent e) {
                    removeFill();
                    for (int i = e.getIndex0(); i <= e.getIndex1(); i++) {
                        addPanel((Cell) listModel.get(i), i);
                    }
                    addFill(listModel.size());
                    reloadPanels();
                }
                
                /**
                 * @see javax.swing.event.ListDataListener#intervalRemoved(javax.swing.event.ListDataEvent)
                 */
                public void intervalRemoved(final ListDataEvent e) {
                    removeFill();
                    for (int i = e.getIndex1(); i >= e.getIndex0(); i--) {
                        removePanel(i);
                    }
                    addFill(listModel.size());
                    reloadPanels();
                } 
            });
        }
        
        /**
         * Reload the panels display. The jpanel is revalidated.
         * 
         */
        private void reloadPanels() {
            revalidate();
            repaint();
        }
        
        /**
         * Remove the fill component.
         *
         */
        private void removeFill() {
            remove(fillJLabel);
        }

        /**
         * Remove a panel at an index.
         * 
         * @param index
         *            An <code>int</code> index.
         */
        private void removePanel(final int index) {
            remove(index);
        }
    }

    /**
     * <b>Title:</b>Conainer Panel Published To EMail East Cell<br>
     * 
     */
    private final class PublishedToEMailCell extends AbstractEastCell {

        /** A <code>PublishedToEMail</code>. */
        private final PublishedToEMail publishedTo;

        /**
         * Create PublishedToEMailCell.
         * 
         * @param parent
         *            A parent <code>WestCell</code>.
         * @param publishedTo
         *            A <code>PublishedToEMail</code>.
         */
        private PublishedToEMailCell(final WestCell parent,
                final PublishedToEMail publishedTo) {
            super(parent);
            this.publishedTo = publishedTo;
            setIcon(IMAGE_CACHE.read(TabPanelIcon.USER_NOT_RECEIVED));
            setText(publishedTo.getEMail().toString());
        }
        /**
         * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.DefaultCell#getId()
         */
        @Override
        public String getId() {
            return new StringBuffer("email-").append(publishedTo.getEMail()).toString();
        }
        /**
         * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.DefaultCell#isActionAvailable()
         */
        @Override
        public Boolean isActionAvailable() {
            return Boolean.FALSE;
        }
        /**
         * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.DefaultCell#isPopupAvailable()
         */
        @Override
        public Boolean isPopupAvailable() {
            return Boolean.FALSE;
        }
    }

    /** A version cell. */
    private final class VersionCell extends AbstractWestCell {
        /** The <code>DocumentView</code>s. */ 
        private final List<DocumentView> documentViews;
        /** The published by <code>User</code>. */
        private final User publishedBy;
        /** A <code>ContainerVersion</code>. */
        private final ContainerVersion version;
        /**
         * Create VersionCell.
         * 
         * @param version
         *            A <code>ContainerVersion</code>.
         * @param documentVersions
         *            A <code>List</code> of <code>DocumentVersion</code>s.
         * @param publishedTo
         *            A <code>PublishedToView</code>.
         * @param publishedBy
         *            A published by <code>User</code>.
         */
        private VersionCell(final ContainerVersion version,
                final List<DocumentView> documentViews,
                final PublishedToView publishedTo,
                final User publishedBy) {
            super(Boolean.FALSE);
            this.documentViews = documentViews;
            this.version = version;
            this.publishedBy = publishedBy;
            for (final DocumentView documentView : documentViews) {
                add(new VersionDocumentCell(this, documentView.getVersion(),
                        documentView.getDelta()));
            }
            // add version user east cells, but note that the publisher is not included.
            for (final ArtifactReceipt artifactReceipt : publishedTo.getArtifactReceipts()) {
                add(new VersionUserCell(this, artifactReceipt));
            }
            for (final PublishedToEMail publishedToEMail : publishedTo.getEMails()) {
                add(new PublishedToEMailCell(this, publishedToEMail));
            }
        }
        @Override
        public String getAdditionalText() {
            return localization.getString("VersionAdditionalText", new Object[] {formatFuzzy(version.getCreatedOn())});
        }
        @Override
        public String getCommonActionDisplayName() {
            final StringBuffer key = new StringBuffer("CommonAction.")
                    .append(actionDelegate.getCommonActionForVersionDisplayNameKey(container, getDraft(), version, isLatestVersion()));
            return localization.getString(key.toString());
        }
        @Override
        public Icon getIcon() {
            if (version.isSetComment()) {
                return IMAGE_CACHE.read(TabPanelIcon.VERSION_WITH_COMMENT);
            } else {
                return IMAGE_CACHE.read(TabPanelIcon.VERSION); 
            }
        }
        @Override
        public String getId() {
            return new StringBuffer("version-").append(version.getVersionId()).toString();
        }
        @Override
        public String getText() {
            if (isLocalUser(publishedBy)) {
                return localization.getString("VersionLocalUserPublished", new Object[] {publishedBy.getName()});
            } else {
                return localization.getString("Version", new Object[] {publishedBy.getName()});
            }
        }
        public Long getVersionId() {
            return version.getVersionId();
        }
        @Override
        public void invokeAction() {
            actionDelegate.invokeForVersion(version, Boolean.TRUE);
        }
        @Override
        public void invokeCommonAction() {
            actionDelegate.invokeCommonActionForVersion(container, getDraft(), version, isLatestVersion());
        }
        public void invokeSelectAction() {
            actionDelegate.invokeForVersion(version, Boolean.FALSE);
        }
        @Override
        public Boolean isActionAvailable() {
            return version.isSetComment();
        }
        @Override
        public Boolean isCommonActionAvailable() {
            return Boolean.TRUE;
        }
        @Override
        public Boolean isCommonActionEnabled() {
            return actionDelegate.isCommonActionForVersionEnabled(container, getDraft(), version, isLatestVersion());
        }
        @Override
        public Boolean isEmphasized() {
            // checking local user prevents flicker as events arrive
            // during a local publish
            return !version.isSeen() && !isLocalUser(publishedBy);
        }
        public Boolean isLatestVersion() {
            return version.equals(latestVersion);
        }
        @Override
        public void showPopup(final Boolean showAll) {
            if (showAll) {
                boolean eastCellSelected = false;
                if (isSelectedEastCell()) {
                    final EastCell cell = getSelectedEastCell();
                    if (cell instanceof VersionDocumentCell) {
                        eastCellSelected = true;
                        popupDelegate.showAll(container, getDraft(), version, documentViews, 
                                isLatestVersion(), ((VersionDocumentCell)cell).getDocumentVersion());
                    } else if (cell instanceof VersionUserCell) {
                        eastCellSelected = true;
                        popupDelegate.showAll(container, getDraft(), version, documentViews,
                                isLatestVersion(), ((VersionUserCell)cell).getUser());
                    }
                }
                if (!eastCellSelected) {
                    popupDelegate.showAll(container, getDraft(), version, documentViews, isLatestVersion());
                }
            } else {
                popupDelegate.showForVersion(container, getDraft(), version, documentViews, isLatestVersion());
            }
        }
    }

    /** A version document cell. */
    private final class VersionDocumentCell extends AbstractEastCell {
        /** A <code>Delta</code>. */
        private final Delta delta;
        /** A <code>DocumentVersion</code>. */
        private final DocumentVersion version;
        /**
         * Create VersionDocumentCell.
         * 
         * @param version
         *            A <code>DocumentVersion</code>.
         * @param delta
         *            A <code>Delta</code>.
         */
        private VersionDocumentCell(final WestCell parent,
                final DocumentVersion version, final Delta delta) {
            super(parent);
            this.delta = delta;
            this.version = version;
            setIcon(fileIconReader.getIcon(version));
            switch (delta) {
            case ADDED:
            case MODIFIED:
            case REMOVED:
                setAdditionalText(localization.getString(delta));
                break;
            case NONE:
                break;
            default:
                throw Assert.createUnreachable("UNKNOWN DOCUMENT STATE");
            }
            setText(version.getArtifactName());
        }
        public DocumentVersion getDocumentVersion() {
            return version;
        }
        /**
         * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.DefaultCell#getId()
         */
        @Override
        public String getId() {
            return new StringBuffer("versionDocument-").append(version.getArtifactId()).toString();
        }
        /**
         * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.DefaultCell#invokeAction()
         */
        @Override
        public void invokeAction() {
            actionDelegate.invokeForDocument(version, delta);
        }
        /**
         * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.DefaultCell#isActionAvailable()
         */
        @Override
        public Boolean isActionAvailable() {
            return (delta != Delta.REMOVED);
        }
        /**
         * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.DefaultCell#isPopupAvailable()
         */
        @Override
        public Boolean isPopupAvailable() {
            return (delta != Delta.REMOVED);
        }
        /**
         * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.DefaultCell#showPopup()
         */
        @Override
        public void showPopup(final Boolean showAll) {
            popupDelegate.showForDocument(version);
        }
    }

    /** A user cell. */
    private final class VersionUserCell extends AbstractEastCell {
        /** A <code>User</code>. */
        private final User user;

        /**
         * Create VersionUserCell.
         * 
         * @param parent
         *            The parent <code>WestCell</code>.
         * @param receipt
         *            An <code>ArtifactReceipt</code>.
         */
        private VersionUserCell(final WestCell parent,
                final ArtifactReceipt receipt) {
            super(parent);
            this.user = receipt.getUser();
            setIcon(receipt.isSetReceivedOn()
                    ? IMAGE_CACHE.read(TabPanelIcon.USER)
                    : IMAGE_CACHE.read(TabPanelIcon.USER_NOT_RECEIVED));
            setText(user.getName());
            if (receipt.isSetReceivedOn()) {
                setAdditionalText(localization.getString("UserReceived",
                        new Object[] {formatFuzzy(receipt.getReceivedOn())}));
            } else {
                setAdditionalText(localization.getString("UserNotReceived"));
            }
        }

        /**
         * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.DefaultCell#getId()
         */
        @Override
        public String getId() {
            return new StringBuffer("user-").append(user.getId()).toString();
        }
        public User getUser() {
            return user;
        }
        /**
         * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.DefaultCell#invokeAction()
         */
        @Override
        public void invokeAction() {
            actionDelegate.invokeForUser(user);
        }
        /**
         * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.DefaultCell#showPopup()
         */
        @Override
        public void showPopup(final Boolean showAll) {
            popupDelegate.showForUser(user);
        }
    }
}
