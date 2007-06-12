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

/**
 * <b>Title:</b>thinkParity Desdemona Rules Model Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class RuleModelImpl extends AbstractModelImpl implements
        RuleModel, InternalRuleModel {

    /**
     * Create RuleModelImpl.
     *
     */
    public RuleModelImpl() {
        super();
    }

    /**
     * @see com.thinkparity.desdemona.model.rules.InternalRuleModel#isPublishRestricted()
     *
     */
    public Boolean isPublishRestricted() {
        try {
            final Long productId = Ophelia.PRODUCT_ID;
            final List<Feature> features = getUserModel().readFeatures(productId);
            for (final Feature feature : features) {
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
     * @see com.thinkparity.desdemona.model.rules.RuleModel#isPublishRestrictedFrom(com.thinkparity.codebase.jabber.JabberId)
     *
     */
    public Boolean isPublishRestrictedFrom(final JabberId publishFrom) {
        try {
            final User from = getUserModel().read(publishFrom);
            return isPublishRestricted(from, user);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.rules.RuleModel#isPublishRestricted(com.thinkparity.codebase.jabber.JabberId)
     * 
     */
    public Boolean isPublishRestrictedTo(final JabberId publishTo) {
        try {
            final User to = getUserModel().read(publishTo);
            return isPublishRestricted(user, to);
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
    private Boolean isPublishRestricted(final User from, final User to) {
        try {
            /* NOTE the product id/name should be read from the interface once the
             * migrator code is complete */
            final Long productId = Ophelia.PRODUCT_ID;

            final List<Feature> fromFeatures = getUserModel(from).readFeatures(productId);
            final List<Feature> toFeatures = getUserModel(to).readFeatures(productId);

            for (final Feature feature : fromFeatures) {
                if (Ophelia.Feature.CORE.equals(feature.getName())) {
                    return Boolean.FALSE;
                }
            }
            for (final Feature feature : toFeatures) {
                if (Ophelia.Feature.CORE.equals(feature.getName())) {
                    return Boolean.FALSE;
                }
            }
            return Boolean.TRUE;
        } catch (final Throwable t) {
            throw panic(t);
        }
    }
}
