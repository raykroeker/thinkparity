/**
 * Created On: 27-May-07 11:31:06 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.help;

import java.util.ArrayList;
import java.util.List;

import javax.swing.text.JTextComponent;

import com.thinkparity.common.StringUtil;
import com.thinkparity.common.StringUtil.Separator;


import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabDelegate;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.PanelListManager;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.PanelListModel;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class HelpContentModel implements PanelListModel {

    /** The <code>JTextComponent</code>. */
    private final JTextComponent jTextComponent;

    /** The <code>List</code> of <code>String</code> pages. */
    private List<String> pages;

    /** The <code>PanelListManager</code>. */
    private PanelListManager panelListManager;

    /** The selected page <code>int</code>. */
    private int selectedPage;

    /** The <code>DefaultTabPanel</code>. */
    private final DefaultTabPanel tabPanel;

    /**
     * Create a HelpContentModel.
     * 
     * @param tabPanel
     *            A <code>DefaultTabPanel</code>.
     * @param jTextComponent
     *            A <code>JTextComponent</code>.
     */
    HelpContentModel(final DefaultTabPanel tabPanel,
            final JTextComponent jTextComponent) {
        super();
        this.jTextComponent = jTextComponent;
        this.tabPanel = tabPanel;
        setSelectedPage(-1);
    }

    /**
     * Initialize the model.
     * 
     * @param content
     *            The content <code>String</code>.
     */
    public void initialize(final String content) {
        if (null == content) {
            pages = null;
            setSelectedPage(-1);
        } else {
            pages = StringUtil.tokenize(content, Separator.Paragraph, new ArrayList<String>());
            setSelectedPage(0);
        }
        panelListManager.initialize();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.PanelListModel#getNumberPages()
     */
    public int getNumberPages() {
        return null == pages ? 0 : pages.size();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.PanelListModel#getSelectedPage()
     */
    public int getSelectedPage() {
        return selectedPage;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.PanelListModel#setPanelListManager(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.PanelListManager)
     */
    public void setPanelListManager(final PanelListManager panelListManager) {
        this.panelListManager = panelListManager;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.PanelListModel#setSelectedPage(int)
     */
    public void setSelectedPage(final int selectedPage) {
        selectPanel();
        this.selectedPage = selectedPage;
        if (-1 == selectedPage) {
            jTextComponent.setText(" ");
        } else {
            jTextComponent.setText(pages.get(selectedPage));
        }
    }

    private void selectPanel() {
        // This is done so other panels will deselect when there is activity in
        // the expanded panel. Note also that the null check is because this method
        // may get called during initialization before the delegate is set up.
        final TabDelegate delegate = tabPanel.getTabDelegate();
        if (null != delegate) {
            delegate.selectPanel(tabPanel);
        }
    }
}
