/*
 * Created On:  28-Feb-07 8:23:22 PM
 */
package com.thinkparity.desdemona.model.rules;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.Context;

import com.thinkparity.desdemona.model.AbstractModel;
import com.thinkparity.desdemona.model.session.Session;

/**
 * <b>Title:</b>thinkParity Desdemona Rules Model<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class RulesModel extends AbstractModel<RulesModelImpl> {

    /**
     * Obtain an instance of the rules model.
     * 
     * @param session
     *            A user <code>Session</code>.
     * @return An instance of <code>RulesModel</code>.
     */
    public static RulesModel getModel(final Session session) {
        return new RulesModel(session);
    }

    /**
     * Obtain an instance of the rules model.
     * 
     * @param context
     *            A <code>Context</code>.
     * @return An instance of <code>InternalRulesModel</code>.
     */
    public static InternalRulesModel getInternalModel(final Context context) {
        return new InternalRulesModel(context);
    }

    /**
     * Create RulesModel.
     * 
     * @param session
     *            A user <code>Session</code>.
     */
    protected RulesModel(final Session session) {
        super(new RulesModelImpl(session));
    }

    /**
     * Create RulesModel.
     * 
     * @param session
     *            A user <code>Session</code>.
     */
    protected RulesModel() {
        super(new RulesModelImpl());
    }

    /**
     * Determine if publish is restricted for the user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param publishFrom
     *            A publish from user id <code>JabberId</code>.
     * @param publishTo
     *            A publish to user id <code>JabberId</code>.
     * @return True if publish to the user is restricted.
     */
    public Boolean isPublishRestricted(final JabberId userId,
            final JabberId publishFrom, final JabberId publishTo) {
        synchronized (getImplLock()) {
            return getImpl().isPublishRestricted(userId, publishFrom, publishTo);
        }
    }
}
