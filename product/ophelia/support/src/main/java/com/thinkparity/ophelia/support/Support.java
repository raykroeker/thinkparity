/*
 * Created On:  Nov 7, 2007 12:18:48 PM
 */
package com.thinkparity.ophelia.support;

import com.thinkparity.codebase.model.session.Environment;

import com.thinkparity.ophelia.browser.environment.EnvironmentManager;

import com.thinkparity.ophelia.support.ui.action.ActionFactory;

/**
 * <b>Title:</b>thinkParity Ophelia Support<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Support {

    /** An instance of support. */
    private static Support instance;

    /**
     * Obtain a support instance.
     * 
     * @return A <code>Support</code>.
     */
    public static Support getInstance() {
        return instance;
    }

    /**
     * Start support.
     * 
     * @param args
     *            A <code>String[]</code>.
     */
    public static void main(final String[] args) {
        if (null == instance) {
            try {
                instance = new Support();
                instance.run();
            } catch (final Throwable t) {
                t.printStackTrace(System.err);
                System.exit(1);
            }
        } else {
            System.err.println("Cannot run support.");
            System.exit(1);
        }
    }

    /** An environment. */
    private final Environment environment;

    /**
     * Create Support.
     *
     */
    private Support() {
        super();
        this.environment = new EnvironmentManager(Boolean.FALSE).select();
    }

    /**
     * Obtain the environment.
     * 
     * @return An <code>Environment</code>.
     */
    public Environment getEnvrionment() {
        return environment;
    }

    /**
     * Run support.
     *
     */
    private void run() {
        ActionFactory.getInstance().newRunner("/main/start").run();
    }
}
