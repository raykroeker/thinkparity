/*
 * Created On:  28-Feb-07 8:23:34 PM
 */
package com.thinkparity.desdemona.model.rules;

import java.util.ArrayList;
import java.util.List;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.migrator.Feature;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.desdemona.model.AbstractModelImpl;
import com.thinkparity.desdemona.model.Constants.Product.Ophelia;
import com.thinkparity.desdemona.model.user.InternalUserModel;

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
     * @see com.thinkparity.desdemona.model.rules.RuleModel#isInviteRestricted(java.util.List)
     *
     */
    @Override
    public Boolean isInviteRestricted(final List<EMail> emailList) {
        try {
            if (isCoreEnabled(user)) {
                return Boolean.FALSE;
            } else {
                User user;
                final InternalUserModel userModel = getUserModel();
                for (final EMail email : emailList) {
                    user = userModel.read(email);
                    if (null == user) {
                        return Boolean.TRUE;
                    } else {
                        if (isInviteRestricted(user)) {
                            return Boolean.TRUE;
                        }
                    }
                }
                return Boolean.FALSE;
            }
        } catch (final Exception x) {
            throw panic(x);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.rules.RuleModel#isInviteRestricted(com.thinkparity.codebase.model.user.User)
     *
     */
    public Boolean isInviteRestricted(final User user) {
        try {
            if (isCoreEnabled(this.user)) {
                return Boolean.FALSE;
            } else if (isCoreEnabled(user)) {
                return Boolean.FALSE;
            } else {
                return Boolean.TRUE;
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.rules.InternalRuleModel#isPublishRestricted()
     *
     */
    public Boolean isPublishRestricted() {
        try {
            if (isCoreEnabled(user)) {
                return Boolean.FALSE;
            } else {
                return Boolean.TRUE;
            }
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
            return isPublishRestricted(user, getUserModel().read(publishTo));
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.rules.RuleModel#isPublishRestrictedTo(java.util.List, java.util.List)
     *
     */
    public Boolean isPublishRestrictedTo(final List<EMail> emails,
            final List<User> users) {
        try {
            if (isCoreEnabled(user)) {
                return Boolean.FALSE;
            } else {
                User user;
                final InternalUserModel userModel = getUserModel();
                for (final EMail email : emails) {
                    user = userModel.read(email);
                    if (null == user) {
                        return Boolean.TRUE;
                    } else {
                        if (isPublishRestricted(this.user, user)) {
                            return Boolean.TRUE;
                        }
                    }
                }
                final List<User> localUsers = localize(users);
                for (final User localUser : localUsers) {
                    if (isPublishRestricted(this.user, localUser)) {
                        return Boolean.TRUE;
                    }
                }
                return Boolean.FALSE;
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * Determine whether or not the "CORE" feature is enabled for a user.
     * 
     * @param user
     *            A <code>User</code>.
     * @param name
     *            A feature name <code>String</code>.
     * @return True if the feature is enabled.
     */
    private boolean isCoreEnabled(final User user) {
        return isFeatureEnabled(user, Ophelia.Feature.CORE);
    }

    /**
     * Determine whether or not a feature is enabled for a user.
     * 
     * @param user
     *            A <code>User</code>.
     * @param name
     *            A feature name <code>String</code>.
     * @return True if the feature is enabled.
     */
    private boolean isFeatureEnabled(final User user, final String name) {
        final List<Feature> features = readFeatures(user);
        for (final Feature feature : features) {
            if (feature.getName().equals(name)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Determine if publish is restricted when publishing from one user to
     * another. The rule is that if either user has the core feature; publish is
     * not restricted. Another view is that it is only restricted when both
     * users do not have the core feature.
     * 
     * @param from
     *            A publish from user id <code>JabberId</code>.
     * @param to
     *            A publish to user id <code>JabberId</code>.
     * @return True if publish to the user is restricted.
     */
    private Boolean isPublishRestricted(final User from, final User to) {
        if (isCoreEnabled(from)) {
            return Boolean.FALSE;
        } else if (isCoreEnabled(to)) {
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }

    /**
     * Localize a list of users.
     * 
     * @param user
     *            A <code>List<User></code>.
     * @return A <code>List<User></code>.
     */
    private List<User> localize(final List<User> users) {
        final List<User> localUsers = new ArrayList<User>(users.size());
        for (final User user : users) {
            localUsers.add(localize(user));
        }
        return localUsers;
    }

    /**
     * Localize a user.
     * 
     * @param user
     *            A <code>User</code>.
     * @return A <code>User</code>.
     */
    private User localize(final User user) {
        return getUserModel().read(user.getId());
    }

    /**
     * Read the features for a user.
     * 
     * @param user
     *            A <code>User</code>.
     * @return A <code>List<Feature></code>.
     */
    private List<Feature> readFeatures(final User user) {
        /* NOTE the product id/name should be read from the interface once the
         * migrator code is complete */
        return getUserModel(user).readProductFeatures(Ophelia.PRODUCT_NAME);
    }
}
