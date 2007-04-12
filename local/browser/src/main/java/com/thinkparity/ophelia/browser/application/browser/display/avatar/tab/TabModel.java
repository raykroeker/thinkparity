/*
 * Created On: Sep 1, 2006 11:41:31 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

import javax.swing.DefaultListModel;

import com.thinkparity.codebase.log4j.Log4JWrapper;
import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.application.browser.display.provider.ContentProvider;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.application.ApplicationId;
import com.thinkparity.ophelia.browser.platform.application.ApplicationRegistry;


/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class TabModel implements TabDelegate {

    /** The model's <code>ContentProvider</code>. */
    protected ContentProvider contentProvider;

    /** The model's input <code>Object</code>. */
    protected Object input;

    /** An apache logger wrapper. */
    protected final Log4JWrapper logger;

    /** The thinkParity <code>ApplicationRegistry</code>. */
    private final ApplicationRegistry applicationRegistry;

    /**
     * A flag indictating whether or not the model has been initialized.
     * 
     * @see #reload()
     * @see #initialize()
     */
    private Boolean initialized;

    /**
     * Create TabModel.
     * 
     */
    protected TabModel() {
        super();
        this.applicationRegistry = new ApplicationRegistry();
        this.initialized = Boolean.FALSE;
        this.logger = new Log4JWrapper(getClass());
    }

    /**
     * Set the content provider.
     * 
     * @param contentProvider
     *            A <code>ContentProvider</code>.
     */
    public final void setContentProvider(final ContentProvider contentProvider) {
        this.contentProvider = contentProvider;
    }

    /**
     * Set input.
     * 
     * @param input
     *            An input <code>Object</code>.
     */
    public final void setInput(final Object input) {
        this.input = input;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabDelegate#toggleExpand(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel)
     * 
     */
    public void toggleExpansion(final TabPanel tabPanel) {}

    /**
     * Apply a filter on the panel.
     * 
     */
    protected void applyFilter() {}

    /**
     * Apply a search filter on the panel.
     * 
     */
    protected void applySearchFilter() {}

    /**
     * Apply a search to the tab.
     * 
     * @param searchExpression
     *            A search expression <code>String</code>.
     */
    protected void applySearch(final String searchExpression) {}

    /**
     * Apply the sort to the filtered list of panels.
     *
     */
    protected void applySort() {}

    /**
     * Determine whether an import is possible.
     * 
     * @param transferFlavors
     *            The import <code>DataFlavor</code>s.
     * @return True if the model accepts import for this panel; for these data
     *         flavors.
     */
    protected boolean canImportData(final DataFlavor[] transferFlavors) {
        return false;
    }

    /**
     * Determine whether an import on a panel is possible.
     * 
     * @param tabPanel
     *            A <code>TabPanel</code>.
     * @param transferFlavors
     *            The import <code>DataFlavor</code>s.
     * @return True if the model accepts import for this panel; for these data
     *         flavors.
     */
    protected boolean canImportData(final TabPanel tabPanel,
            final DataFlavor[] transferFlavors) {
        return false;
    }

    /**
     * Debug the model.
     *
     */
    protected abstract void debug();

    /**
     * Obtain the thinkParity browser application.
     * 
     * @return The thinkParity browser application.
     */
    protected Browser getBrowser() {
        return (Browser) applicationRegistry.get(ApplicationId.BROWSER);
    }

    /**
     * Obtain the <code>JList</code>'s <code>ListModel</code> for the tab.
     * 
     * @return A <code>ListModel</code>.
     */
    protected abstract DefaultListModel getListModel();

    /**
     * Import transferrable data onto a panel.
     * 
     * @param transferable
     *            The import data.
     */
    protected void importData(final TabPanel tabPanel,
            final Transferable transferable) {
    }

    /**
     * Import transferrable data.
     * 
     * @param transferable
     *            The import data.
     */
    protected void importData(final Transferable transferable) {}

    /**
     * Initialize the tab model.
     *
     */
    protected abstract void initialize();

    /**
     * Reload the tab's model. Since a tab avatar contains a list of content
     * that is retreived once then manually synchronzied the reload is
     * controlled in the abstraction and initialization is done by the
     * implementing classes.
     * 
     */
    protected final void reload() {
        if (initialized) {
            reloadSearch();
            return;
        } else {
            if (null != contentProvider) {
                SwingUtil.ensureDispatchThread(new Runnable() {
                    public void run() {
                        initialize();    
                    }
                });
                initialized = Boolean.TRUE;
                synchronize();
            }
        }
    }

    /**
     * Remove any ordering.
     *
     */
    protected void removeOrdering() {}

    /**
     * Remove any search.
     *
     */
    protected void removeSearch() {}

    /**
     * Synchronize the internal state of the model with the display.
     * Ensure the AWT event dispatching thread is used.
     */
    protected void synchronize() {
        // Use the AWT event dispatching thread. If we are already on the
        // thread then call directly so the sequence of code is not changed.
        SwingUtil.ensureDispatchThread(new Runnable() {
            public void run() {
                synchronizeImpl();    
            }
        });
    }

    /**
     * Synchronize the internal state of the model with the display.
     *
     */
    protected abstract void synchronizeImpl();

    /**
     * Obtain the input search expression.
     * 
     * @return A search expression <code>String</code>.
     */
    private String getInputSearchExpression() {
        if (null == input) {
            return null;
        } else {
            return (String) ((Data) input).get(TabAvatar.DataKey.SEARCH_EXPRESSION);
        }
    }

    /**
     * Reload search.
     */
    private void reloadSearch() {
        final String searchExpression = getInputSearchExpression();
        if (null == searchExpression) {
            SwingUtil.ensureDispatchThread(new Runnable() {
                public void run() {
                    removeSearch();    
                }
            });
        } else {
            SwingUtil.ensureDispatchThread(new Runnable() {
                public void run() {
                    applySearch(searchExpression);    
                }
            });
        }
    }
}
