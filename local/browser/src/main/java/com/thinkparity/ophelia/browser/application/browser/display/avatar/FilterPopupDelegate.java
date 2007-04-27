/**
 * Created On: Mar 28, 2007 12:07:39 AM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabAvatarFilterBy;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabAvatarFilterDelegate;
import com.thinkparity.ophelia.browser.platform.action.DefaultPopupDelegate;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class FilterPopupDelegate extends DefaultPopupDelegate {

    /** The filter delegate. */
    private TabAvatarFilterDelegate filterDelegate;

    /** The filter menu width. */
    private int filterMenuWidth = 0;

    /** The filter action complete runnable. */
    private Runnable filterActionComplete;

    /**
     * Create a FilterPopupDelegate.
     */
    public FilterPopupDelegate() {
        super();
    }

    /**
     * Set the filter action complete runnable.
     * 
     * @param filterDelegate
     *            The <code>TabAvatarFilterDelegate</code>.
     */
    public void setFilterActionComplete(final Runnable filterActionComplete) {
        this.filterActionComplete = filterActionComplete;
    }

    /**
     * Set the filter delegate.
     * 
     * @param filterDelegate
     *            The <code>TabAvatarFilterDelegate</code>.
     */
    public void setFilterDelegate(final TabAvatarFilterDelegate filterDelegate) {
        this.filterDelegate = filterDelegate;
    }

    /**
     * Set the menu width.
     * 
     * @param filterMenuWidth
     *            The width <code>int</code>.
     */
    public void setFilterMenuWidth(final int filterMenuWidth) {
        this.filterMenuWidth = filterMenuWidth;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.DefaultPopupDelegate#show()
     */
    @Override
    public void show() {
        prepareMenu();
        super.show();
    }

    /**
     * Add an entry to the menu.
     * 
     * @param filterBy
     *            The <code>TabAvatarFilterBy</code>.
     * @param selected
     *            The selected <code>Boolean</code>.     
     */
    private void addFilterBy(final TabAvatarFilterBy filterBy, final Boolean selected) {
        final JMenuItem menuItem = new JCheckBoxMenuItem(filterBy.getText());
        menuItem.setSelected(selected);
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                filterBy.getAction().actionPerformed(
                        new ActionEvent(e.getSource(), e.getID(),
                                "FilterBy", e.getWhen(), e.getModifiers()));
                runFilterActionComplete();
            }
        });
        add(menuItem);
        setMenuPreferredSize(menuItem);
    }

    /**
     * Determine if the filter action complete runnable has been set.
     * 
     * @return true if the filter action complete runnable has been set, false otherwise.
     */
    private boolean isSetFilterActionComplete() {
        return (null != filterActionComplete);
    }

    /**
     * Determine if the menu width has been set.
     * 
     * @return true if the menu width has been set, false otherwise.
     */
    private boolean isSetMenuWidth() {
        return (0 != filterMenuWidth);
    }

    /**
     * Prepare the menu.
     */
    private void prepareMenu() {
        Assert.assertNotNull("Null filter delegate in filter popup.", filterDelegate);
        Assert.assertNotTrue("Filter popup delegate has no filters.", filterDelegate.getFilterBy().isEmpty());
        for (final TabAvatarFilterBy filterBy : filterDelegate.getFilterBy()) {
            addFilterBy(filterBy, filterDelegate.isFilterSelected(filterBy));
        }
    }

    /**
     * Run the filter action complete runnable.
     */
    private void runFilterActionComplete() {
        if (isSetFilterActionComplete()) {
            try {
                filterActionComplete.run();
            } finally {
                filterActionComplete = null;
            }
        }
    }

    /**
     * Set the preferred size of the menu.
     * 
     * @param menuItem
     *            The <code>JMenuItem</code>.
     */
    private void setMenuPreferredSize(final JMenuItem menuItem) {
        if (isSetMenuWidth()) {
            final Dimension preferredSize = menuItem.getPreferredSize();
            preferredSize.width = filterMenuWidth;
            menuItem.setPreferredSize(preferredSize);
        }
    }
}
