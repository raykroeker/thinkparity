/*
 * Created On: Sep 1, 2006 11:41:31 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab;

import javax.swing.DefaultListModel;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.application.browser.display.provider.ContentProvider;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.application.ApplicationId;
import com.thinkparity.ophelia.browser.platform.application.ApplicationRegistry;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;


/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class TabModel {

    /** The tab model's content provider. */
    protected ContentProvider contentProvider;

    /** The tab model's input. */
    protected Object input;

    /** The thinkParity application registry. */
    private final ApplicationRegistry applicationRegistry;

    /**
     * A flag indictating whether or not the model has been initialized.
     * 
     * @see #reload()
     * @see #initialize()
     */
    private Boolean initialized;

    /** Create TabModel. */
    protected TabModel() {
        super();
        this.applicationRegistry = new ApplicationRegistry();
        this.initialized = Boolean.FALSE;
    }

    /**
     * @see Avatar#setContentProvider(ContentProvider)
     */
    public final void setContentProvider(final ContentProvider contentProvider) {
        this.contentProvider = contentProvider;
    }

    /**
     * @see Avatar#setInput(Object)
     */
    public final void setInput(final Object input) {
        this.input = input;
    }

    /**
     * Apply a search to the tab.
     * 
     * @param searchExpression
     *            A search expression <code>String</code>.
     */
    protected void applySearch(final String searchExpression) {}

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
            final String searchExpression = getInputSearchExpression();
            if (null == searchExpression) {
                removeSearch();
            } else {
                applySearch(searchExpression);
            }
            return;
        } else {
            if (null != contentProvider) {
                initialize();
                initialized = Boolean.TRUE;
                synchronize();
            }
        }
    }

    /**
     * Remove any search.
     *
     */
    protected void removeSearch() {}

    /**
     * Synchronize the internal state of the model with the display.
     *
     */
    protected abstract void synchronize();

    /**
     * Obtain the input search expression.
     * 
     * @return A search expression <code>String</code>.
     */
    private String getInputSearchExpression() {
        if (null == input) {
            return null;
        } else {
            return (String) ((Data) input).get(TabListAvatar.DataKey.SEARCH_EXPRESSION);
        }
    }
}
