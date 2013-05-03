/**
 * 
 */
package com.thinkparity.codebase.ui.avatar;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.log4j.Log4JWrapper;
import com.thinkparity.codebase.swing.AbstractJPanel;
import com.thinkparity.codebase.ui.application.Application;
import com.thinkparity.codebase.ui.platform.Platform;
import com.thinkparity.codebase.ui.provider.Provider;

/**
 * @author raymond
 *
 */
public abstract class DefaultAvatar<T extends Platform, U extends Application<T>, V extends Provider>
        extends AbstractJPanel implements Avatar<T, U, V> {

    /** An apache logger. */
    protected final Log4JWrapper logger;

    /** The avatar input <code>Object</code>. */
    private Object input;

    /** An avatar's <code>Provider</code>. */
    private V provider;

    /**
     * Create DefaultAvatar.
     * 
     */
    public DefaultAvatar() {
        super();
        this.logger = new Log4JWrapper();
    }

    /**
     * @see com.thinkparity.codebase.ui.avatar.Avatar#getInput()
     * 
     */
    public Object getInput() {
        return input;
    }

    /**
     * @see com.thinkparity.codebase.ui.avatar.Avatar#getProvider()
     * 
     */
    public V getProvider() {
        return provider;
    }

    /**
     * @see com.thinkparity.codebase.ui.avatar.Avatar#setInput(java.lang.Object)
     * 
     */
    public void setInput(final Object input) {
        this.input = input;
        reload();
    }

    /**
     * @see com.thinkparity.codebase.ui.avatar.Avatar#setProvider()
     * 
     */
    public void setProvider(final V provider) {
        Assert.assertNotNull("Cannot set a null provider.", provider);
        this.provider = provider;
        reload();
    }
}
