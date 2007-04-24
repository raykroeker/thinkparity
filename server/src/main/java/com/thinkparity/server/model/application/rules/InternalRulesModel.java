/*
 * Created On:  28-Feb-07 8:24:03 PM
 */
package com.thinkparity.desdemona.model.rules;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.Context;

/**
 * <b>Title:</b>thinkParity DesdemonaModel Internal Rules Interface<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class InternalRulesModel extends RulesModel {

    /**
     * Create InternalRulesModel.
     *
     * @param impl
     */
    InternalRulesModel(final Context context) {
        super();
    }

    /**
     * Determine if publish is restricted for the user.
     * 
     * @param publishFrom
     *            A publish from user id <code>JabberId</code>.
     * @param publishTo
     *            A publish to user id <code>JabberId</code>.
     * @return True if publish to the user is restricted.
     */
    public Boolean isPublishRestricted(final JabberId publishFrom,
            final JabberId publishTo) {
        synchronized (getImplLock()) {
            return getImpl().isPublishRestricted(publishFrom, publishTo);
        }
    }
}
