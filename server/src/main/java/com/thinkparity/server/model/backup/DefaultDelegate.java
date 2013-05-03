/*
 * Created On:  27-Apr-07 9:26:53 AM
 */
package com.thinkparity.ophelia.model;

import java.util.List;

import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.Context;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.artifact.InternalArtifactModel;
import com.thinkparity.ophelia.model.index.InternalIndexModel;
import com.thinkparity.ophelia.model.user.InternalUserModel;

/**
 * <b>Title:</b>thinkParity OpheliaModel Default Delegate<br>
 * <b>Description:</b>A default delegate used to access the common
 * implementation contained within the model.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 * @param <T>
 *            A type of model implementation.
 */
public class DefaultDelegate<T extends Model> implements Delegate<T> {

    /**
     * @see Model#contains(List, JabberId)
     * 
     */
    protected static final <U extends User> Boolean contains(
            final List<U> users, final JabberId userId) {
        return Model.contains(users, userId);
    }

    /**
     * @see Model#contains(List, User)
     * 
     */
    protected static final <U extends User, V extends User> Boolean contains(
            final List<U> users, final V user) {
        return Model.contains(users, user);
    }

    /**
     * @see Model#indexOf(List, JabberId)
     * 
     */
    protected static final <U extends User> int indexOf(final List<U> users,
            final JabberId userId) {
        return Model.indexOf(users, userId);
    }

    /** A <code>Log4JWrapper</code>. */
    protected final Log4JWrapper logger;

    /** A model implementation. */
    protected T modelImplementation;

    /**
     * Create DefaultDelegate.
     *
     */
    public DefaultDelegate() {
        super();
        this.logger = new Log4JWrapper();
    }

    /**
     * @see com.thinkparity.ophelia.model.Delegate#initialize(com.thinkparity.ophelia.model.Model,
     *      com.thinkparity.ophelia.model.io.IOFactory,
     *      com.thinkparity.ophelia.model.InternalModelFactory)
     * 
     */
    public void initialize(final T modelImplementation) {
        this.modelImplementation = modelImplementation;
    }

    /**
     * Obtain an internal artifact model.
     * 
     * @return An <code>InternalArtifactModel</code>.
     */
    protected final InternalArtifactModel getArtifactModel() {
        return modelImplementation.getArtifactModel();
    }

    /**
     * Obtain an internal index model.
     * 
     * @return An instance of <code>InternalIndexModel</code>.
     */
    protected final InternalIndexModel getIndexModel() {
        return modelImplementation.getIndexModel();
    }

    /**
     * Obtain a server artifact model.
     * 
     * @return An instance of<code>InternalArtifactModel</code>.
     */
    protected final com.thinkparity.desdemona.model.artifact.InternalArtifactModel getServerArtifactModel() {
        return com.thinkparity.desdemona.model.InternalModelFactory.getInstance(new Context(), localUser()).getArtifactModel();
    }

    /**
     * Obtain an internal user model.
     * 
     * @return An instance of <code>InternalUserModel</code>.
     */
    protected final InternalUserModel getUserModel() {
        return modelImplementation.getUserModel();
    }

    /**
     * Obtain the local user.
     * 
     * @return A <code>User</code>.
     */
    protected final User localUser() {
        return modelImplementation.localUser();
    }

    /**
     * Obtain the local user id.
     * 
     * @return A local user id <code>JabberId</code>.
     */
    protected final JabberId localUserId() {
        return modelImplementation.localUserId();
    }
}
