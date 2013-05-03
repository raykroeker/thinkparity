/*
 * Created On:  Nov 19, 2007 10:05:10 AM
 */
package com.thinkparity.ophelia.support.ui.avatar;

import javax.swing.JPanel;

import com.thinkparity.ophelia.support.ui.Input;

/**
 * <b>Title:</b>thinkParity Ophelia Support UI Abstract Avatar<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class AbstractAvatar extends JPanel implements Avatar {

    /** The avatar input. */
    protected Input input;

    /** An avatar context. */
    private final AvatarContext context;

    /** An avatar id. */
    private final String id;

    /**
     * Create AbstractAvatar.
     *
     */
    protected AbstractAvatar(final String id) {
        super();
        this.id = id;

        this.context = new AvatarContext();
    }

    /**
     * @see com.thinkparity.ophelia.support.ui.avatar.Avatar#getId()
     *
     */
    public String getId() {
        return id;
    }

    /**
     * @see com.thinkparity.ophelia.support.ui.avatar.Avatar#reload()
     *
     */
    public void reload() {
    }

    /**
     * @see com.thinkparity.ophelia.support.ui.avatar.Avatar#setInput(com.thinkparity.ophelia.support.ui.Input)
     *
     */
    public void setInput(final Input input) {
        final Input oldValue = this.input;
        this.input = input;
        this.firePropertyChange("input", oldValue, input);
    }

    /**
     * Obtain a context.
     * 
     * @return An <code>AvatarContext</code>.
     */
    protected AvatarContext getContext() {
        return context;
    }
}
