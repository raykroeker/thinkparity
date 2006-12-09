/*
 * Created On: October 6, 2006, 5:03 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JList;
import javax.swing.border.Border;

import com.thinkparity.codebase.FuzzyDateFormat;
import com.thinkparity.codebase.StringUtil.Separator;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.swing.SwingUtil;
import com.thinkparity.codebase.swing.border.BottomBorder;

import com.thinkparity.codebase.model.artifact.ArtifactFlag;
import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta.Delta;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.container.ContainerDraft;

import com.thinkparity.ophelia.browser.Constants.Colors;
import com.thinkparity.ophelia.browser.application.browser.BrowserSession;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.FileIconReader;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.MainPanelImageCache;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.MainPanelImageCache.TabPanelIcon;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabDelegate;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanelPopupDelegate;
import com.thinkparity.ophelia.browser.util.localization.MainCellL18n;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ContainerPanel extends DefaultTabPanel {
    
    /** The panel <code>Border</code>. */
    private static final Border BORDER;

    /** The number of rows in the version panel. */
    private static final int NUMBER_VISIBLE_ROWS;

    /** A <code>FuzzyDateFormat</code>. */
    private static final FuzzyDateFormat FUZZY_DATE_FORMAT;

    /** A session key pattern for the east list's selected index. */
    private static final String SK_EAST_LIST_SELECTED_INDEX_PATTERN;

    /** A session key pattern for the west list's selected index. */
    private static final String SK_WEST_LIST_SELECTED_INDEX_PATTERN;

    static {
        BackgroundRenderer.initialize();
        BORDER = new BottomBorder(Colors.Browser.List.LIST_CONTAINERS_BORDER);
        FUZZY_DATE_FORMAT = new FuzzyDateFormat();
        NUMBER_VISIBLE_ROWS = 5;
        SK_EAST_LIST_SELECTED_INDEX_PATTERN =
            "ContainerPanel#eastJList.getSelectedIndex({0})";
        SK_WEST_LIST_SELECTED_INDEX_PATTERN =
            "ContainerPanel#westJList.getSelectedIndex({0})";
    }

    /** A <code>Container</code>. */
    protected Container container;

    /** A <code>ContainerDraft</code>. */
    protected ContainerDraft draft;

    /** The expanded <code>Boolean</code> state. */
    protected boolean expanded;

    /** An image cache. */
    protected final MainPanelImageCache imageCache;

    /** The container tab's <code>DefaultActionDelegate</code>. */
    private ActionDelegate actionDelegate;

    /** A <code>Animator</code> used by expand and collapse. */
    private Animator animator;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel collapsedJPanel;
    private final javax.swing.JLabel eastCountJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel eastFirstJLabel = new javax.swing.JLabel();
    private final javax.swing.JList eastJList = new javax.swing.JList();
    private javax.swing.JPanel eastJPanel;
    private final javax.swing.JLabel eastLastJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel eastNextJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel eastPreviousJLabel = new javax.swing.JLabel();
    private final javax.swing.JPanel expandedJPanel = new javax.swing.JPanel();
    private javax.swing.JLabel expansionJLabel;
    private final javax.swing.JLabel iconJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel textJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel westCountJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel westFirstJLabel = new javax.swing.JLabel();
    private final javax.swing.JList westJList = new javax.swing.JList();
    private javax.swing.JPanel westJPanel;
    private final javax.swing.JLabel westLastJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel westNextJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel westPreviousJLabel = new javax.swing.JLabel();
    // End of variables declaration//GEN-END:variables

    /** A <code>TabDelegate</code>. */
    private TabDelegate tabDelegate;

    /** The east list <code>DefaultListModel</code>. */
    private final DefaultListModel eastListModel;

    /** A  <code>FileIconReader</code>. */
    private final FileIconReader fileIconReader;

    /** The most recent <code>ContainerVersion</code>. */
    private ContainerVersion latestVersion;

    /** The panel localization. */
    private final MainCellL18n localization;

    /** The container tab's <code>PopupDelegate</code>. */
    private PopupDelegate popupDelegate;

    /** A <code>BackgroundRenderer</code>. */
    private final BackgroundRenderer renderer;

    /** A <code>BrowserSession</code>. */
    private final BrowserSession session;

    /** The west list <code>DefaultListModel</code>. */
    private final DefaultListModel westListModel;

    /**
     * Create ContainerPanel.
     * 
     */
    public ContainerPanel(final BrowserSession session) {
        super();
        this.constraints = new GridBagConstraints();
        this.constraints.fill = GridBagConstraints.BOTH;
        this.constraints.weightx = this.constraints.weighty = 1.0F;
        this.eastListModel = new DefaultListModel();
        this.expanded = Boolean.FALSE;
        this.fileIconReader = new FileIconReader();
        this.imageCache = new MainPanelImageCache();
        this.localization = new MainCellL18n("ContainerPanel");
        this.renderer = new BackgroundRenderer();
        this.session = session;
        this.westListModel = new DefaultListModel();
        initComponents();
        this.animator = new Animator(this, 12);
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
     * Obtain the container draft.
     * 
     * @return A <code>ContainerDraft</code>.
     */
    public ContainerDraft getDraft() {
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
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel#getPanelPopupDelegate()
     *
     */
    public TabPanelPopupDelegate getPanelPopupDelegate() {
        return popupDelegate;
    }

    /**
     * Obtain popupDelegate.
     *
     * @return A ContainerTabPopupDelegate.
     */
    public PopupDelegate getPopupDelegate() {
        return popupDelegate;
    }

    /**
     * Determine the expanded state.
     * 
     * @return A <code>Boolean</code> expanded state.
     */
    public Boolean isExpanded() {
        return Boolean.valueOf(expanded);
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

    private final GridBagConstraints constraints;
    /**
     * Set the expanded state.
     * 
     * @param expanded
     *            A <code>Boolean</code> expanded state.
     */
    public void setExpanded(final Boolean expanded) {
        this.expanded = expanded.booleanValue();
        if (this.expanded) {
            remove(collapsedJPanel);
            add(expandedJPanel, constraints.clone());
            revalidate();
            animator.expand(20, 165);
        } else {
            animator.collapse(20, 25);
            remove(expandedJPanel);
            add(collapsedJPanel, constraints.clone());
            revalidate();
        }
        reload();
    }

    /**
     * Set the panel data.
     * 
     * @param container
     *            A <code>Container</code>.
     * @param draft
     *            A <code>ContainerDraft</code>.
     * @param latestVersion
     *            The latest <code>ContainerVersion</code>.
     */
    public void setPanelData(
            final Container container,
            final ContainerDraft draft,
            final ContainerVersion latestVersion,
            final List<ContainerVersion> versions,
            final Map<ContainerVersion, Map<DocumentVersion, Delta>> documentVersions,
            final Map<ContainerVersion, Map<User, ArtifactReceipt>> publishedTo,
            final Map<ContainerVersion, User> publishedBy) {
        this.container = container;
        this.draft = draft;
        this.latestVersion = latestVersion;
        westListModel.addElement(new ContainerCell());
        // if a draft exists and the user is the creator of the draft or the
        // user has the latest version of the container display a draft element
        if (container.isLocalDraft()) {
            westListModel.addElement(new DraftCell());
        }
        // display all version elements
        for (final ContainerVersion version : versions) {
            westListModel.addElement(new VersionCell(version, documentVersions
                    .get(version), publishedTo.get(version), publishedBy
                    .get(version)));
        }
        iconJLabel.setIcon(container.isBookmarked()
                ? imageCache.read(TabPanelIcon.CONTAINER_BOOKMARK)
                : imageCache.read(TabPanelIcon.CONTAINER));
        reloadText();
        restoreSelection(SK_EAST_LIST_SELECTED_INDEX_PATTERN, eastListModel, eastJList);
        restoreSelection(SK_WEST_LIST_SELECTED_INDEX_PATTERN, westListModel, westJList);
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
     * Set the tab delegate.
     * 
     * @param tabDelegate
     *            A <code>TabDelegate</code>.
     */
    public void setTabDelegate(final TabDelegate tabDelegate) {
        this.tabDelegate = tabDelegate;
    }

    /**
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     * 
     */
    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        if (expanded) {
            renderer.paintExpandedBackground(g, this);
            if (!westJList.isSelectionEmpty()) {
                final int selectionIndex = westJList.getSelectedIndex();
                renderer.paintExpandedBackgroundWest(g, westJPanel.getWidth(), getHeight(), selectionIndex, this);
                renderer.paintExpandedBackgroundCenter(g, westJPanel.getWidth(), getHeight(), selectionIndex, this);
                renderer.paintExpandedBackgroundEast(g, getWidth() - eastJPanel.getWidth(), getHeight(), this);
            }
        } else {
            renderer.paintBackground(g, getWidth(), getHeight());
        }
    }

    private void eastJListFocusGained(java.awt.event.FocusEvent e) {//GEN-FIRST:event_eastJListFocusGained
        jListFocusGained((JList) e.getSource(), e);
    }//GEN-LAST:event_eastJListFocusGained

    private void eastJListFocusLost(java.awt.event.FocusEvent e) {//GEN-FIRST:event_eastJListFocusLost
        jListFocusLost((JList) e.getSource(), e);
    }//GEN-LAST:event_eastJListFocusLost

    private void eastJListMouseClicked(java.awt.event.MouseEvent e) {//GEN-FIRST:event_eastJListMouseClicked
        jListMouseClicked((JList) e.getSource(), e);
    }//GEN-LAST:event_eastJListMouseClicked

    private void eastJListMousePressed(java.awt.event.MouseEvent e) {//GEN-FIRST:event_eastJListMousePressed
        jListMousePressed((JList) e.getSource(), e);
    }//GEN-LAST:event_eastJListMousePressed

    private void eastJListMouseReleased(java.awt.event.MouseEvent e) {//GEN-FIRST:event_eastJListMouseReleased
        jListMouseReleased((JList) e.getSource(), e);
    }//GEN-LAST:event_eastJListMouseReleased

    private void eastJListValueChanged(javax.swing.event.ListSelectionEvent e) {//GEN-FIRST:event_eastJListValueChanged
        if (e.getValueIsAdjusting() || ((javax.swing.JList) e.getSource()).isSelectionEmpty()) {
            repaint();
            return;
        }
        saveSelection(SK_EAST_LIST_SELECTED_INDEX_PATTERN, (javax.swing.JList) e.getSource());
    }//GEN-LAST:event_eastJListValueChanged

    private void iconJLabelMouseClicked(java.awt.event.MouseEvent e) {//GEN-FIRST:event_iconJLabelMouseClicked
        actionDelegate.invokeForContainer(container);
    }//GEN-LAST:event_iconJLabelMouseClicked

    private void iconJLabelMouseEntered(java.awt.event.MouseEvent e) {//GEN-FIRST:event_iconJLabelMouseEntered
        SwingUtil.setCursor((javax.swing.JLabel) e.getSource(), java.awt.Cursor.HAND_CURSOR);
    }//GEN-LAST:event_iconJLabelMouseEntered

    private void iconJLabelMouseExited(java.awt.event.MouseEvent e) {//GEN-FIRST:event_iconJLabelMouseExited
        SwingUtil.setCursor((javax.swing.JLabel) e.getSource(), java.awt.Cursor.DEFAULT_CURSOR);
    }//GEN-LAST:event_iconJLabelMouseExited

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        collapsedJPanel = new javax.swing.JPanel();
        westJPanel = new javax.swing.JPanel();
        expansionJLabel = new javax.swing.JLabel();
        eastJPanel = new javax.swing.JPanel();

        setLayout(new java.awt.GridBagLayout());

        setBorder(BORDER);
        collapsedJPanel.setLayout(new java.awt.GridBagLayout());

        collapsedJPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent e) {
                collapsedJPanelMousePressed(e);
            }
            public void mouseReleased(java.awt.event.MouseEvent e) {
                collapsedJPanelMouseReleased(e);
            }
        });

        iconJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/IconContainer.png")));
        iconJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                iconJLabelMouseClicked(e);
            }
            public void mouseEntered(java.awt.event.MouseEvent e) {
                iconJLabelMouseEntered(e);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                iconJLabelMouseExited(e);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 32, 0, 4);
        collapsedJPanel.add(iconJLabel, gridBagConstraints);

        textJLabel.setText("!Package!");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 4, 0);
        collapsedJPanel.add(textJLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(collapsedJPanel, gridBagConstraints);

        expandedJPanel.setLayout(new java.awt.GridBagLayout());

        expandedJPanel.setOpaque(false);
        westJPanel.setLayout(new java.awt.GridBagLayout());

        westJPanel.setOpaque(false);
        expansionJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/IconCollapse.png")));
        expansionJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                expansionJLabelMouseClicked(e);
            }
            public void mouseEntered(java.awt.event.MouseEvent e) {
                expansionJLabelMouseEntered(e);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                expansionJLabelMouseExited(e);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 12, 0, 4);
        westJPanel.add(expansionJLabel, gridBagConstraints);

        westJList.setModel(westListModel);
        westJList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        westJList.setCellRenderer(new WestCellRenderer());
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

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        westJPanel.add(westJList, gridBagConstraints);

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("localization/ListItem_Messages"); // NOI18N
        westLastJLabel.setText(bundle.getString("ContainerPanel.firstJLabel")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        westJPanel.add(westLastJLabel, gridBagConstraints);

        westNextJLabel.setText(bundle.getString("ContainerPanel.nextJLabel")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        westJPanel.add(westNextJLabel, gridBagConstraints);

        westCountJLabel.setText(bundle.getString("ContainerPanel.countJLabel")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        westJPanel.add(westCountJLabel, gridBagConstraints);

        westPreviousJLabel.setText(bundle.getString("ContainerPanel.previousJLabel")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        westJPanel.add(westPreviousJLabel, gridBagConstraints);

        westFirstJLabel.setText(bundle.getString("ContainerPanel.firstJLabel")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        westJPanel.add(westFirstJLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        expandedJPanel.add(westJPanel, gridBagConstraints);

        eastJPanel.setLayout(new java.awt.GridBagLayout());

        eastJPanel.setOpaque(false);
        eastJList.setModel(eastListModel);
        eastJList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        eastJList.setCellRenderer(new EastCellRenderer());
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
        eastJList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent e) {
                eastJListValueChanged(e);
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

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        eastJPanel.add(eastJList, gridBagConstraints);

        eastLastJLabel.setText(bundle.getString("ContainerPanel.lastJLabel")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        eastJPanel.add(eastLastJLabel, gridBagConstraints);

        eastFirstJLabel.setText(bundle.getString("ContainerPanel.firstJLabel")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        eastJPanel.add(eastFirstJLabel, gridBagConstraints);

        eastNextJLabel.setText(bundle.getString("ContainerPanel.nextJLabel")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        eastJPanel.add(eastNextJLabel, gridBagConstraints);

        eastCountJLabel.setText(bundle.getString("ContainerPanel.countJLabel")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        eastJPanel.add(eastCountJLabel, gridBagConstraints);

        eastPreviousJLabel.setText(bundle.getString("ContainerPanel.previousJLabel")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        eastJPanel.add(eastPreviousJLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        expandedJPanel.add(eastJPanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(expandedJPanel, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents

    private void collapsedJPanelMouseReleased(java.awt.event.MouseEvent e) {//GEN-FIRST:event_collapsedJPanelMouseReleased
        logger.logApiId();
        logger.logVariable("e", e);
        if (e.isPopupTrigger()) {
            tabDelegate.toggleExpansion(this);
            popupDelegate.initialize(expandedJPanel, e.getX(), e.getY());
            popupDelegate.showForContainer(container);
        } else {
            tabDelegate.toggleExpansion(this);
        }
    }//GEN-LAST:event_collapsedJPanelMouseReleased

    private void collapsedJPanelMousePressed(java.awt.event.MouseEvent e) {//GEN-FIRST:event_collapsedJPanelMousePressed
        logger.logApiId();
        logger.logVariable("e", e);
        if (e.isPopupTrigger()) {
            tabDelegate.toggleExpansion(this);
            popupDelegate.initialize(expandedJPanel, e.getX(), e.getY());
            popupDelegate.showForContainer(container);
        }
    }//GEN-LAST:event_collapsedJPanelMousePressed

    private void expansionJLabelMouseExited(java.awt.event.MouseEvent e) {//GEN-FIRST:event_expansionJLabelMouseExited
        SwingUtil.setCursor((javax.swing.JLabel) e.getSource(), java.awt.Cursor.DEFAULT_CURSOR);
    }//GEN-LAST:event_expansionJLabelMouseExited

    private void expansionJLabelMouseEntered(java.awt.event.MouseEvent e) {//GEN-FIRST:event_expansionJLabelMouseEntered
        SwingUtil.setCursor((javax.swing.JLabel) e.getSource(), java.awt.Cursor.HAND_CURSOR);
    }//GEN-LAST:event_expansionJLabelMouseEntered

    private void expansionJLabelMouseClicked(java.awt.event.MouseEvent e) {//GEN-FIRST:event_expansionJLabelMouseClicked
        tabDelegate.toggleExpansion(this);
    }//GEN-LAST:event_expansionJLabelMouseClicked

    /**
     * Handle the focus gained event on the list.
     * 
     * @param jList
     *            A <code>JList</code>.
     * @param e
     *            A <code>FocusEvent</code>.
     */
    private void jListFocusGained(final javax.swing.JList jList,
            final java.awt.event.FocusEvent e) {
        eastJList.repaint();
        westJList.repaint();
    }

    /**
     * Handle the focus lost event on the list.
     * 
     * @param jList
     *            A <code>JList</code>.
     * @param e
     *            A <code>FocusEvent</code>.
     */
    private void jListFocusLost(final javax.swing.JList jList,
            final java.awt.event.FocusEvent e) {
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
    private void jListMouseClicked(final javax.swing.JList jList,
            final java.awt.event.MouseEvent e) {
        logger.logApiId();
        logger.logVariable("e", e);
        // a click count of 2, 4, 6, etc. triggers double click event
        if (e.getClickCount() % 2 == 0) {
            ((DefaultCell) jList.getSelectedValue()).invokeAction();
        }
    }

    /**
     * Handle the mouse pressed event for the given list.
     * 
     * @param jList
     *            A <code>JList</code>.
     * @param e
     *            A <code>MouseEvent</code>.
     */
    private void jListMousePressed(final javax.swing.JList jList,
            final java.awt.event.MouseEvent e) {
        logger.logApiId();
        logger.logVariable("e", e);
        if (e.isPopupTrigger()) {
            jList.setSelectedIndex(jList.locationToIndex(e.getPoint()));
            popupDelegate.initialize((Component) e.getSource(), e.getX(), e.getY());
            ((DefaultCell) jList.getSelectedValue()).showPopup();
        }
    }

    /**
     * Handle the mouse released event for the given list.
     * 
     * @param jList
     *            A <code>JList</code>.
     * @param e
     *            A <code>MouseEvent</code>.
     */
    private void jListMouseReleased(final javax.swing.JList jList,
            final java.awt.event.MouseEvent e) {
        logger.logApiId();
        logger.logVariable("e", e);
        if (e.isPopupTrigger()) {
            jList.setSelectedIndex(jList.locationToIndex(e.getPoint()));
            popupDelegate.initialize((Component) e.getSource(), e.getX(), e.getY());
            ((DefaultCell) jList.getSelectedValue()).showPopup();
        }
    }

    /**
     * Reload the container panel based upon internal criteria; more
     * specifically whether or not the panel is expanded controls the text to
     * display; the icons and fonts to use.
     * 
     */
    private void reload() {
        reloadText();
    }

    /**
     * Reload the text on the panel.
     *
     */
    private void reloadText() {
        final StringBuffer text = new StringBuffer();
        // if expanded display the container name
        // if not expanded display the container name; if there exists a draft
        // and the the latest version also display the draft owner; otherwise
        // if there exists the latest version display the published on date
        if (expanded) {
            text.append(container.getName());
        } else {
            text.append(container.getName())
                .append(Separator.DoubleSpace);
            if (container.isDraft() && container.isLatest()) {
                text.append(localization.getString("ContainerMessageDraftOwner",
                        draft.getOwner().getName()));
            } else if (null != latestVersion) {
                text.append(localization.getString(
                        "ContainerMessagePublishDate",
                        FUZZY_DATE_FORMAT.format(latestVersion.getUpdatedOn())));
            }
        }
        textJLabel.setText(text.toString());
        if (!expanded && !container.contains(ArtifactFlag.SEEN)) {
            textJLabel.setFont(Fonts.DefaultFontBold);
        }
        if (!container.isLocalDraft() && !container.isLatest()) {
            textJLabel.setForeground(
                    Colors.Browser.List.LIST_LACK_MOST_RECENT_VERSION_FG);
        }
    }

    /**
     * Restore the saved selection for the list. If there is no saved selection
     * then the first index is used.
     * 
     * @param keyPattern
     *            A session attribute key pattern.
     * @param listModel
     *            A <code>DefaultListModel</code>.
     * @param jList
     *            The <code>JList</code>.
     */
    private void restoreSelection(final String keyPattern,
            final DefaultListModel listModel, final javax.swing.JList jList) {
        final Integer selectedIndex = (Integer) session.getAttribute(
                MessageFormat.format(keyPattern, container.getId()));
        if (null == selectedIndex) {
            if (listModel.isEmpty()) {
                return;
            } else {
                jList.setSelectedIndex(0);
            }
        } else {
            jList.setSelectedIndex(selectedIndex.intValue());
        }
    }

    /**
     * Save the current selection.
     * 
     * @param keyPattern
     *            A session attribute key pattern.
     * @param jList
     *            The <code>JList</code>.
     */
    private void saveSelection(final String keyPattern, final javax.swing.JList jList) {
        session.setAttribute(MessageFormat.format(
                keyPattern, container.getId()),
                Integer.valueOf(jList.getSelectedIndex()));
    }

    private void westJListFocusGained(java.awt.event.FocusEvent e) {//GEN-FIRST:event_westJListFocusGained
        jListFocusGained((JList) e.getSource(), e);
    }//GEN-LAST:event_westJListFocusGained

    private void westJListFocusLost(java.awt.event.FocusEvent e) {//GEN-FIRST:event_westJListFocusLost
        jListFocusLost((JList) e.getSource(), e);
    }//GEN-LAST:event_westJListFocusLost

    private void westJListMouseClicked(java.awt.event.MouseEvent e) {//GEN-FIRST:event_westJListMouseClicked
        jListMouseClicked((JList) e.getSource(), e);
    }//GEN-LAST:event_westJListMouseClicked

    private void westJListMousePressed(java.awt.event.MouseEvent e) {//GEN-FIRST:event_westJListMousePressed
        jListMousePressed((JList) e.getSource(), e);
    }//GEN-LAST:event_westJListMousePressed

    private void westJListMouseReleased(java.awt.event.MouseEvent e) {//GEN-FIRST:event_westJListMouseReleased
        jListMouseReleased((JList) e.getSource(), e);
    }//GEN-LAST:event_westJListMouseReleased

    private void westJListValueChanged(javax.swing.event.ListSelectionEvent e) {//GEN-FIRST:event_westJListValueChanged
        if (e.getValueIsAdjusting() || ((javax.swing.JList) e.getSource()).isSelectionEmpty()) {
            repaint();
            return;
        }
        saveSelection(SK_WEST_LIST_SELECTED_INDEX_PATTERN, (javax.swing.JList) e.getSource());
        eastListModel.clear();
        for (final Object selectedValue : westJList.getSelectedValues()) {
            for (final Cell cell : ((WestCell) selectedValue).eastCells) {
                eastListModel.addElement(cell);
            }
        }
    }//GEN-LAST:event_westJListValueChanged

    /** An east list cell. */
    private abstract class AbstractEastCell extends DefaultCell implements
            EastCell {
        /** The additional text <code>String</code>. */
        private String additionalText;
        /**
         * Set the east cell's additional text.
         * 
         * @param additionalText
         *            The additional text <code>String</code>.
         */
        protected void setAdditionalText(final String additionalText) {
            this.additionalText = additionalText;
        }
        /**
         * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.EastCell#isSetAdditionalText()
         */
        public Boolean isSetAdditionalText() {
            return null != additionalText;
        }
        /**
         * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.EastCell#getAdditionalText()
         */
        public String getAdditionalText() {
            return additionalText;
        }
    }

    /** A container cell. */
    private final class ContainerCell extends WestCell {
        @Override
        public Icon getIcon() {
            return imageCache.read(TabPanelIcon.CONTAINER);
        }
        @Override
        public String getText() {
            return container.getName();
        }
        @Override
        public void showPopup() {
            popupDelegate.showForContainer(container);
        }
    }

    /** A draft cell. */
    private final class DraftCell extends WestCell {
        private DraftCell() {
            super();
            setIcon(imageCache.read(TabPanelIcon.DRAFT));
            for (final Document document : draft.getDocuments()) {
                eastCells.add(new DraftDocumentCell(document));
            }
        }
        @Override
        public String getText() {
            if (container.isLocalDraft()) {
                return localization.getString("Draft");
            } else {
                return localization.getString("DraftNotLocal", draft.getOwner().getName());
            }
        }
        
        @Override
        public void showPopup() {
            popupDelegate.showForDraft(container, draft);
        }
    }

    /** A draft document cell. */
    private final class DraftDocumentCell extends AbstractEastCell {
        private final Document document;
        private DraftDocumentCell(final Document document) {
            super();
            this.document = document;
            setIcon(fileIconReader.getIcon(document));
            switch (draft.getState(document)) {
            case ADDED:
            case MODIFIED:
            case REMOVED:
                setAdditionalText(localization.getString(draft.getState(document)));
                break;
            case NONE:
                break;
            default:
                throw Assert.createUnreachable("UNKNOWN DOCUMENT STATE");
            }
            setText(document.getName());
        }
        @Override
        public void invokeAction() {
            actionDelegate.invokeForDocument(draft, document);
        }
        @Override
        public void showPopup() {
            popupDelegate.showForDraft(container, draft);
        }
    }

    /** A version cell. */
    private final class VersionCell extends WestCell {
        private final ContainerVersion version;
        private VersionCell(final ContainerVersion version,
                final Map<DocumentVersion, Delta> documentVersions,
                final Map<User, ArtifactReceipt> users, final User publishedBy) {
            this.version = version;
            setText(localization.getString("Version", FUZZY_DATE_FORMAT
                    .format(version.getCreatedOn()), publishedBy.getName()));
            if (version.isSetComment()) {
                setIcon(imageCache.read(TabPanelIcon.VERSION_WITH_COMMENT));
            } else {
                setIcon(imageCache.read(TabPanelIcon.VERSION)); 
            }
            for (final Entry<DocumentVersion, Delta> entry : documentVersions.entrySet()) {
                eastCells.add(new VersionDocumentCell(entry.getKey(), entry.getValue()));
            }
            eastCells.add(new VersionUserCell(publishedBy));
            for (final Entry<User, ArtifactReceipt> entry : users.entrySet()) {
                eastCells.add(new VersionUserCell(entry.getKey(), entry.getValue()));
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
        private VersionDocumentCell(final DocumentVersion version,
                final Delta delta) {
            super();
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
            setText(version.getName());
        }
        /**
         * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.DefaultCell#invokeAction()
         */
        @Override
        public void invokeAction() {
            actionDelegate.invokeForDocument(version, delta);
        }
        /**
         * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.DefaultCell#showPopup()
         */
        @Override
        public void showPopup() {
            popupDelegate.showForDocument(version, delta);
        }
    }

    /** A user cell. */
    private final class VersionUserCell extends AbstractEastCell {
        /** A <code>User</code>. */
        private final User user;
        /**
         * Create VersionUserCell.
         * 
         * @param user
         *            A <code>User</code>.
         */
        private VersionUserCell(final User user) {
            super();
            this.user = user;
            setIcon(imageCache.read(TabPanelIcon.USER));
            setText(user.getName());
            setAdditionalText(localization.getString("UserPublished"));
        }
        /**
         * Create VersionUserCell.
         * 
         * @param user
         *            A <code>User</code>.
         * @param receipt
         *            An <code>ArtifactReceipt</code>.
         */
        private VersionUserCell(final User user, final ArtifactReceipt receipt) {
            super();
            this.user = user;
            setIcon(imageCache.read(TabPanelIcon.USER_NOT_RECEIVED));
            setText(user.getName());
            setAdditionalText(receipt.isSetReceivedOn()
                    ? localization.getString("UserReceived", FUZZY_DATE_FORMAT.format(receipt.getReceivedOn()))
                    : localization.getString("UserDidNotReceive"));
                    
        }
        /**
         * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.DefaultCell#invokeAction()
         */
        @Override
        public void invokeAction() {
            actionDelegate.invokeForUser(user);
        }
        /**
         * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.DefaultCell#showPopup()
         */
        @Override
        public void showPopup() {
            popupDelegate.showForUser(user);
        }
    }

    /** A west list cell. */
    private class WestCell extends DefaultCell {
        protected final List<EastCell> eastCells = new ArrayList<EastCell>();
    }
}
