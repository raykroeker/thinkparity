/*
 * Created On: Jun 25, 2006 11:43:06 AM
 * $Id$
 */
package com.thinkparity.migrator.application.migrator.avatar;

import java.awt.Color;
import java.awt.Component;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.migrator.application.migrator.Application;
import com.thinkparity.migrator.javax.swing.AbstractJPanel;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class Avatar extends AbstractJPanel {

    /** The avatar's content provider. */
    protected ContentProvider contentProvider;

    /** The avatar's error. */
    protected final List<Throwable> errors;

    /** The avatar input. */
    protected Object input;

    /** The migrator application. */
    private final Application application;

    /**
     * The avatar's scrolling policy.
     * 
     */
    private final ScrollPolicy scrollPolicy;

    /**
     * Create an Avatar.
     * 
     * @param l18nContext
     *            The localization context.
     * @param scrollPolicy
     *            The scrolling policy.
     */
    protected Avatar(final Application application,
            final String l18nContext, final ScrollPolicy scrollPolicy) {
        super(l18nContext);
        this.application = application;
        this.errors = new LinkedList<Throwable>();
        this.scrollPolicy = scrollPolicy;
    }

    /**
     * Create an Avatar.
     * 
     * @param l18nContext
     *            The localization context.
     * @param scrollPolicy
     *            The scrolling policy.
     * @param background
     *            The background color.
     */
    protected Avatar(final Application application,
            final String l18nContext, final ScrollPolicy scrollPolicy,
            final Color background) {
        super(l18nContext, background);
        this.application = application;
        this.errors = new LinkedList<Throwable>();
        this.scrollPolicy = scrollPolicy;
    }

    /**
     * Create a Avatar.
     * 
     * @param l18nContext
     *            The localization context.
     */
    protected Avatar(final Application application,
            final String l18nContext) {
        this(application, l18nContext, ScrollPolicy.NONE);
    }

    /**
     * Create an Avatar.
     * 
     * @param l18nContext
     *            The localiztaion context.
     * @param background
     *            The avatar background colour.
     */
    protected Avatar(final Application application,
            final String l18nContext, final Color background) {
        this(application, l18nContext, ScrollPolicy.NONE, background);
    }

    /**
     * Set an error for display.
     * 
     * @param error
     *            The error.
     */
    public void addError(final Throwable error) { errors.add(error); }

    /**
     * Clear all display errors.
     *
     */
    public void clearErrors() { errors.clear(); }

    /**
     * Determine whether or not the error has been set.
     * 
     * @return True if error has been set; false otherwise.
     */
    public Boolean containsErrors() { return 0 < errors.size(); }

    /**
     * Obtain the migrator application.
     * 
     * @return The migrator application.
     */
    public Application getApplication() { return application; }

    /**
     * Obtain the content provider.
     * 
     * @return The content provider.
     */
    public ContentProvider getContentProvider() { return contentProvider; }

    /**
     * Obtain the error.
     * 
     * @return The error.
     */
    public List<Throwable> getErrors() { return errors; }

    /**
     * Obtain the avatar id.
     * 
     * @return The avatar id.
     */
    public abstract AvatarId getId();

    /**
     * Obtain the avatar's input.
     * 
     * @return The input.
     */
    public Object getInput() { return input; }

    /**
     * Obtain the scroll policy for the avatar.
     * 
     * @return The scroll policy for the avatar.
     */
    public ScrollPolicy getScrollPolicy() { return scrollPolicy; }

    /**
     * Reload the avatar. This event is called when either the content provider
     * or the input has changed; or as a manual reload of the avatar.
     * 
     */
    public void reload() {}

    /**
     * Set the content provider.
     * 
     * @param contentProvider
     *            The content provider.
     */
    public void setContentProvider(final ContentProvider contentProvider) {
        Assert.assertNotNull(
                "Cannot set a null content provider:  " + getId(), contentProvider);
        if(this.contentProvider == contentProvider
                || contentProvider.equals(this.contentProvider)) { return; }
        
        this.contentProvider = contentProvider;
        reload();
    }

    /**
     * Set the avatar's input.
     * 
     * @param input
     *            The avatar input.
     */
    public void setInput(final Object input) {
        Assert.assertNotNull("Cannot set null input:  " + getId(), input);
        if(this.input == input || input.equals(this.input)) { return; }
        
        this.input = input;
        reload();
    }

    /**
     * Causes <i>doRun.run()</i> to be executed asynchronously on the AWT event
     * dispatching thread.
     * 
     * @param doRun
     *            The runnable to execute.
     * @see SwingUtilities#invokeLater(java.lang.Runnable)
     */
    protected void invokeLater(final Runnable doRun) {
        SwingUtilities.invokeLater(doRun);
    }

    /**
     * Provide a visual cue to the user that work is being done.
     *
     */
    protected void toggleVisualFeedback(final Boolean isWorking) {
        if(isWorking) { setIsWorking(); }
        else { setIsNotWorking(); }
    }

    private void setIsNotWorking() {
        final Component[] components = getComponents();
        for(final Component c : components) {
            if(c.getClass().isAssignableFrom(JButton.class)) {
                c.setEnabled(true);
            }
        }

    }

    private void setIsWorking() {
        final Component[] components = getComponents();
        for(final Component c : components) {
            if(c.getClass().isAssignableFrom(JButton.class)) {
                c.setEnabled(false);
            }
        }
    }

    /**
     * The scrolling policy for the avatar.
     * 
     */
    public enum ScrollPolicy { BOTH, HORIZONTAL, NONE, VERTICAL }
}
