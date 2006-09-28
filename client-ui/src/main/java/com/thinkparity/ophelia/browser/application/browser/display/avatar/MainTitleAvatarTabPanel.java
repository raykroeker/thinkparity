/*
 * Created On: July 31, 2006, 5:27 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.JLabel;

import com.thinkparity.codebase.model.profile.Profile;

import com.thinkparity.ophelia.browser.Constants.Icons.BrowserTitle;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.component.LabelFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.MainTitleAvatar.TabId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.plugin.Plugin;
import com.thinkparity.ophelia.browser.platform.plugin.PluginId;
import com.thinkparity.ophelia.browser.platform.plugin.PluginRegistry;
import com.thinkparity.ophelia.browser.platform.plugin.extension.TabExtension;


/**
 * <b>Title:</b>thinkParity Main Title Tabs<br>
 * <b>Description:</b>The tabs in the title portion of the UI are a simple
 * panel with two images controlling the contexts.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class MainTitleAvatarTabPanel extends MainTitleAvatarAbstractPanel {

    /** A resource bundle. */
    private static final ResourceBundle BUNDLE;

    static {
        BUNDLE = ResourceBundle.getBundle("localization/JPanel_Messages");
    }

    /** A list of all core tabs. */
    private final Map<TabId, Tab> allTabs;

    /** A list of all plugin tabs. */
    private final Map<TabExtension, Tab> pluginTabs;

    /** A thinkParity user's profile. */
    private Profile profile;

    /** The selected tab. */
    private Tab selectedTab;
    
    /** The Resizer */
    @SuppressWarnings("unused")
    private final Resizer resizer;

    /** Creates new form BrowserTitleTabs */
    public MainTitleAvatarTabPanel() {
        super();
        this.allTabs = new HashMap<TabId, Tab>();
        this.pluginTabs = new HashMap<TabExtension, Tab>();
        initComponents();
        this.resizer = new Resizer(getBrowser(), this, Boolean.TRUE, Resizer.ResizeEdges.LEFT);
    }

    /**
     * @see javax.swing.JComponent#paintChildren(java.awt.Graphics)
     */
    @Override
    protected void paintChildren(final Graphics g) {
        super.paintChildren(g);
        final Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setFont(Fonts.DefaultFontBold);
            for (final Tab tab : allTabs.values()) {
                tab.drawText(g2, g2.getFontMetrics());
            }
            for (final Tab tab : pluginTabs.values()) {
                tab.drawText(g2, g2.getFontMetrics());
            }
        } finally {
            g2.dispose();
        }
    }

    /**
     * Initialize tabs for the plugins.
     *
     */
    void initPluginTabs(final PluginRegistry pluginRegistry) {
        final Plugin archivePlugin = pluginRegistry.getPlugin(PluginId.ARCHIVE);
        if (null != archivePlugin) {
            final TabExtension archiveTab =
                pluginRegistry.getTabExtension(PluginId.ARCHIVE, "ArchiveTab");
            final GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 1;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
            gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 0);
            add(createTab(archivePlugin, archiveTab).jLabel, gridBagConstraints);
        }
    }

    /**
     * Select a tab.
     * 
     * @param tabExtension
     *            A tab extension.
     */
    void selectTab(final TabExtension tabExtension) {
        this.selectedTab = pluginTabs.get(tabExtension);
        reloadDisplay();
    }

    /**
     * Select a tab.
     * 
     * @param tabId
     *            A tab.
     */
    void selectTab(final TabId tabId) {
        this.selectedTab = allTabs.get(tabId);
        reloadDisplay();
    }

    /**
     * Set localProfile.
     *
     * @param localProfile The Profile.
     */
    void setProfile(final Profile profile) {
        this.profile = profile;
        reloadDisplay();
    }

    /**
     * Create a plugin extension tab.
     * 
     * @param tabExtension
     *            A tab extension.
     * @return A <code>Tab</code>.
     */
    private Tab createTab(final Plugin plugin, final TabExtension tabExtension) {
        final Tab tab = new Tab(tabExtension);
        pluginTabs.put(tabExtension, tab);
        return tab;
    }
    /**
     * Create a tab.
     * 
     * @param tabId
     *            An enumerated tab <code>TabId</code>.
     * @return A <code>Tab</code>.
     */
    private Tab createTab(final TabId tabId) {
        allTabs.put(tabId, new Tab(tabId));
        return allTabs.get(tabId);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        containerJLabel = createTab(TabId.CONTAINER).jLabel;
        contactJLabel = createTab(TabId.CONTACT).jLabel;

        nameJLabel = new javax.swing.JLabel();
        fillJLabel = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        setOpaque(false);
        containerJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/BrowserTitle_TabSelected.png")));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        add(containerJLabel, gridBagConstraints);

        contactJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/BrowserTitle_Tab.png")));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 0);
        add(contactJLabel, gridBagConstraints);

        nameJLabel.setFont(Fonts.DefaultFontBold);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 0);
        add(nameJLabel, gridBagConstraints);

        fillJLabel.setFont(Fonts.DefaultFontBold);
        fillJLabel.setMaximumSize(new java.awt.Dimension(100, 18));
        fillJLabel.setMinimumSize(new java.awt.Dimension(100, 18));
        fillJLabel.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.weightx = 1.0;
        add(fillJLabel, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
    /**
     * Reload the display.  This will examine the current tab; then call the
     * controller to display the correct avatar; as well as update the images
     * representing the tabs.
     *
     */
    private void reloadDisplay() {
        reloadDisplayTab();
        reloadDisplayName();
    }
    /**
     * Reload the name label.
     * 
     */
    private void reloadDisplayName() {
        nameJLabel.setText("");
        if(null != profile) {
            nameJLabel.setText(profile.getName());
        }
    }
    /**
     * Reload the tab images.
     * 
     */
    private void reloadDisplayTab() {
        for (final Tab tab : pluginTabs.values()) {
            tab.jLabel.setIcon(BrowserTitle.TAB);
        }
        for (final Tab tab : allTabs.values()) {
            tab.jLabel.setIcon(BrowserTitle.TAB);
        }
        selectedTab.jLabel.setIcon(BrowserTitle.TAB_SELECTED);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel contactJLabel;
    private javax.swing.JLabel containerJLabel;
    private javax.swing.JLabel fillJLabel;
    private javax.swing.JLabel nameJLabel;
    // End of variables declaration//GEN-END:variables

    /** A tab definition. */
    private final class Tab {

        /** The tab label. */
        private final JLabel jLabel;

        /** The tab text. */
        private final String jLabelText;

        /**
         * Create Tab.
         * 
         * @param tabExtension
         *            A plugin tab extension.
         */
        private Tab(final TabExtension tabExtension) {
            super();
            this.jLabel = LabelFactory.create(BrowserTitle.TAB);
            this.jLabelText = tabExtension.getText();
            this.jLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(final MouseEvent e) {
                    final Data data = (Data) ((Data) mainTitleAvatar.getInput()).clone();
                    data.unset(MainTitleAvatar.DataKey.TAB_ID);
                    data.set(MainTitleAvatar.DataKey.TAB_EXTENSION, tabExtension);
                    mainTitleAvatar.setInput(data);
                }
                @Override
                public void mouseEntered(final MouseEvent e) {
                    if (!MainTitleAvatarTabPanel.Tab.this.equals(selectedTab))
                        jLabel.setIcon(BrowserTitle.TAB_ROLLOVER);
                }
                @Override
                public void mouseExited(final MouseEvent e) {
                    if (!MainTitleAvatarTabPanel.Tab.this.equals(selectedTab))
                        jLabel.setIcon(BrowserTitle.TAB);
                }
            });
            final Dimension minimumSize = jLabel.getMinimumSize();
            minimumSize.height = 20;
            minimumSize.width = 76;
            this.jLabel.setMinimumSize(minimumSize);
        }

        /**
         * Create Tab.
         * 
         * @param tabId
         *            A tab id.
         */
        private Tab(final TabId tabId) {
            super();
            this.jLabel = LabelFactory.create(BrowserTitle.TAB);
            this.jLabelText = BUNDLE.getString(
                    new StringBuffer("MAIN_TITLE.MainTitleAvatar$TabId.")
                            .append(tabId).toString());
            this.jLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(final MouseEvent e) {
                    final Data data = (Data) ((Data) mainTitleAvatar.getInput()).clone();
                    data.set(MainTitleAvatar.DataKey.TAB_ID, tabId);
                    mainTitleAvatar.setInput(data);   
                }
                @Override
                public void mouseEntered(final MouseEvent e) {
                    if (!MainTitleAvatarTabPanel.Tab.this.equals(selectedTab))
                        jLabel.setIcon(BrowserTitle.TAB_ROLLOVER);
                }
                @Override
                public void mouseExited(final MouseEvent e) {
                    if (!MainTitleAvatarTabPanel.Tab.this.equals(selectedTab))
                        jLabel.setIcon(BrowserTitle.TAB);
                }
            });
            final Dimension minimumSize = jLabel.getMinimumSize();
            minimumSize.height = 20;
            minimumSize.width = 76;
            this.jLabel.setMinimumSize(minimumSize);
        }

        /**
         * Draw the tab's text.
         * 
         * @param g2
         *            The <code>Graphics2D</code>.
         * @param fm
         *            The <code>FontMetrics</code>.
         */
        private void drawText(final Graphics2D g2, final FontMetrics fm) {
            final Point labelLocation = jLabel.getLocation();
            final Dimension labelSize = jLabel.getSize();
            final int textWidth = fm.stringWidth(jLabelText);
            final int textHeight = fm.getHeight();
            g2.drawString(jLabelText,
                    labelLocation.x + (labelSize.width - textWidth) / 2,
                    labelLocation.y + (labelSize.height - textHeight) / 2 + 12);
        }
    }
}
