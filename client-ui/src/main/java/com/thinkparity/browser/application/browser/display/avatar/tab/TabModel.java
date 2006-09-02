/*
 * Created On: Sep 1, 2006 11:41:31 AM
 */
package com.thinkparity.browser.application.browser.display.avatar.tab;

import java.awt.Component;
import java.awt.event.MouseEvent;

import javax.swing.ListModel;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.application.browser.display.provider.ContentProvider;
import com.thinkparity.browser.application.browser.display.renderer.tab.TabCell;
import com.thinkparity.browser.platform.action.Data;
import com.thinkparity.browser.platform.application.ApplicationId;
import com.thinkparity.browser.platform.application.ApplicationRegistry;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;


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
    protected abstract ListModel getListModel();

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
     * Trigger a double click on a tab cell.
     * 
     * @param tabCell
     *            A <code>TabCell</code>.
     */
    protected void triggerDoubleClick(final TabCell tabCell) {}

    /**
     * Trigger an expand for a tab cell.
     * 
     * @param tabCell
     *            A <code>TabCell</code>.
     */
    protected void triggerExpand(final TabCell tabCell) {}

    /**
     * Trigger a popup menu for a tab cell.
     * 
     * @param tabCell
     *            A <code>TabCell</code>.
     * @param invoker
     *            The <code>Component</code> invoking the popup.
     * @param e
     *          The <code>MouseEvent</code>.
     */
    protected void triggerPopup(final TabCell tabCell, final Component invoker,
            final MouseEvent e) {}

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
}
