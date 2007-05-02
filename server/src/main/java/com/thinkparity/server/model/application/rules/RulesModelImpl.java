/*
 * Created On:  28-Feb-07 8:23:34 PM
 */
package com.thinkparity.desdemona.model.rules;

import java.util.List;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.migrator.Feature;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.desdemona.model.AbstractModelImpl;
import com.thinkparity.desdemona.model.Constants.Product.Ophelia;
import com.thinkparity.desdemona.model.session.Session;
import com.thinkparity.desdemona.model.user.InternalUserModel;

/**
 * <b>Title:</b>thinkParity DesdemonaModel Rules Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class RulesModelImpl extends AbstractModelImpl {

    /**
     * Create RulesModelImpl.
     *
     */
    RulesModelImpl() {
        super();
    }

    /**
     * Create RulesModelImpl.
     *
     * @param session
     */
    RulesModelImpl(final Session session) {
        super(session);
    }

    /**
     * Determine if publish is restricted when publishing from one user to an e-mail
     * address.  The rule is that if the user has the core feature; publish is
     * not restricted.
     * 
     * @param publishFrom
     *            A publish from user id <code>JabberId</code>.
     * @return True if publish to the user is restricted.
     */
    Boolean isPublishRestricted(final JabberId publishFrom) {
        logger.logApiId();
        logger.logVariable("publishFrom", publishFrom);
        try {
            final InternalUserModel userModel = getUserModel();
            /* NOTE the product id/name should be read from the interface once the
             * migrator code is complete */
            final Long productId = Ophelia.PRODUCT_ID;
            final User publishFromUser = userModel.read(publishFrom);
            final List<Feature> publishFromFeatures = userModel.readFeatures(
                    publishFromUser.getLocalId(), productId);
            for (final Feature feature : publishFromFeatures) {
                if (Ophelia.Feature.CORE.equals(feature.getName())) {
                    return Boolean.FALSE;
                }
            }
            return Boolean.TRUE;
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * Determine if publish is restricted when publishing from one user to
     * another. The rule is that if either user has the core feature; publish is
     * not restricted. Another view is that it is only restricted when both
     * users do not have the core feature.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param publishFrom
     *            A publish from user id <code>JabberId</code>.
     * @param publishTo
     *            A publish to user id <code>JabberId</code>.
     * @return True if publish to the user is restricted.
     */
    Boolean isPublishRestricted(final JabberId publishFrom, final JabberId publishTo) {
        logger.logApiId();
        logger.logVariable("publishFrom", publishFrom);
        logger.logVariable("publishTo", publishTo);
        try {
            final InternalUserModel userModel = getUserModel();
            /* NOTE the product id/name should be read from the interface once the
             * migrator code is complete */
            final Long productId = Ophelia.PRODUCT_ID;
            final User publishFromUser = userModel.read(publishFrom);
            final User publishToUser = userModel.read(publishTo);
            final List<Feature> publishFromFeatures = userModel.readFeatures(
                    publishFromUser.getLocalId(), productId);
            final List<Feature> publishToFeatures = userModel.readFeatures(
                    publishToUser.getLocalId(), productId);
            for (final Feature feature : publishFromFeatures) {
                if (Ophelia.Feature.CORE.equals(feature.getName())) {
                    return Boolean.FALSE;
                }
            }
            for (final Feature feature : publishToFeatures) {
                if (Ophelia.Feature.CORE.equals(feature.getName())) {
                    return Boolean.FALSE;
                }
            }
            return Boolean.TRUE;
        } catch (final Throwable t) {
            throw panic(t);
        }
    }


    /**
     * Determine if publish is restricted when publishing from one user to
     * another. The rule is that if either user has the core feature; publish is
     * not restricted. Another view is that it is only restricted when both
     * users do not have the core feature.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param publishFrom
     *            A publish from user id <code>JabberId</code>.
     * @param publishTo
     *            A publish to user id <code>JabberId</code>.
     * @return True if publish to the user is restricted.
     */
    Boolean isPublishRestricted(final JabberId userId,
            final JabberId publishFrom, final JabberId publishTo) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("publishFrom", publishFrom);
        logger.logVariable("publishTo", publishTo);
        try {
            assertIsAuthenticatedUser(userId);
            assertEquals("Cannot access rule.", userId, publishFrom);

            return isPublishRestricted(publishFrom, publishTo);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }
}
