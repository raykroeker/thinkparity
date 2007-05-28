/**
 * Created On: 27-May-07 11:31:06 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.help;

import java.util.ArrayList;
import java.util.List;

import javax.swing.text.JTextComponent;

import com.thinkparity.codebase.StringUtil;
import com.thinkparity.codebase.StringUtil.Separator;

import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.PanelListManager;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.PanelListModel;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class HelpContentModel implements PanelListModel {

    /** The <code>List</code> of <code>String</code> pages. */
    private List<String> pages;

    /** The <code>PanelListManager</code>. */
    private PanelListManager panelListManager;

    /** The selected page <code>int</code>. */
    private int selectedPage;

    /** The <code>JTextComponent</code>. */
    private final JTextComponent jTextComponent;

    /**
     * Create a HelpContentModel.
     * 
     * @param jTextComponent
     *            A <code>JTextComponent</code>.
     */
    HelpContentModel(final JTextComponent jTextComponent) {
        super();
        this.jTextComponent = jTextComponent;
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
        this.selectedPage = selectedPage;
        if (-1 == selectedPage) {
            jTextComponent.setText(" ");
        } else {
            jTextComponent.setText(pages.get(selectedPage));
        }
    }
}
