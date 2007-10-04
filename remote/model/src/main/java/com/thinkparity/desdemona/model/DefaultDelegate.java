/*
 * Created On:  27-Apr-07 9:26:53 AM
 */
package com.thinkparity.desdemona.model;

import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.desdemona.model.migrator.InternalMigratorModel;
import com.thinkparity.desdemona.model.queue.InternalQueueModel;
import com.thinkparity.desdemona.model.user.InternalUserModel;

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
public class DefaultDelegate<T extends AbstractModelImpl> implements Delegate<T> {

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
     * @see com.thinkparity.desdemona.model.Delegate#initialize(com.thinkparity.desdemona.model.AbstractModelImpl)
     * 
     */
    public void initialize(final T modelImplementation) {
        this.modelImplementation = modelImplementation;
    }

    /**
     * @see com.thinkparity.desdemona.model.AbstractModelImpl#buildUserTimestampId(JabberId)
     * 
     */
    protected final String buildUserTimestampId(final JabberId userId) {
        return modelImplementation.buildUserTimestampId(userId);
    }

    /**
     * @see com.thinkparity.desdemona.model.AbstractModelImpl#getMigratorModel()
     * 
     */
    protected final InternalMigratorModel getMigratorModel() {
        return modelImplementation.getMigratorModel();
    }

    /**
     * @see com.thinkparity.desdemona.model.AbstractModelImpl#getMigratorModel()
     * 
     */
    protected final InternalMigratorModel getMigratorModel(final User user) {
        return modelImplementation.getMigratorModel(user);
    }

    /**
     * @see com.thinkparity.desdemona.model.AbstractModelImpl#getProductName()
     * 
     */
    protected final String getProductName() {
        return modelImplementation.getProductName();
    }

    /**
     * @see com.thinkparity.desdemona.model.AbstractModelImpl#getQueueModel(User)
     * 
     */
    protected final InternalQueueModel getQueueModel(final User user) {
        return modelImplementation.getQueueModel(user);
    }

    /**
     * @see com.thinkparity.desdemona.model.AbstractModelImpl#getProfileModel()
     * 
     */
    protected final InternalUserModel getUserModel(final User user) {
        return modelImplementation.getUserModel(user);
    }

    /**
     * @see com.thinkparity.desdemona.model.AbstractModelImpl#inject(User, User)
     * 
     */
    protected final void inject(final Profile profile, final User user) {
        modelImplementation.inject(profile, user);
    }
}
