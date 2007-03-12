/*
 * Created On:  28-Feb-07 8:33:01 PM
 */
package com.thinkparity.desdemona.model.user;

import java.util.List;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.Context;
import com.thinkparity.codebase.model.migrator.Feature;

import com.thinkparity.desdemona.model.session.Session;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class InternalUserModel extends UserModel {

    /**
     * Create InternalUserModel.
     *
     */
    InternalUserModel(final Context context, final Session session) {
        super(session);
    }

    public List<EMail> readEMails(final JabberId userId, final Long localUserId) {
        synchronized (getImplLock()) {
            return getImpl().readEMails(userId, localUserId);
        }
    }

    /**
     * Read all features for a user.
     * 
     * @param productId
     *            A product id <code>Long</code>.
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>List&lt;Feature&gt</code>.
     */
    public List<Feature> readFeatures(final JabberId userId,
            final Long productId) {
        synchronized (getImplLock()) {
            return getImpl().readFeatures(userId, productId);
        }
    }
}
