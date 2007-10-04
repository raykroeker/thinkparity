/*
 * Created On:  26-Sep-07 1:41:20 PM
 */
package com.thinkparity.desdemona.model.profile;

import java.util.Calendar;
import java.util.List;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import com.thinkparity.codebase.model.migrator.Feature;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.payment.PaymentInfo;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.xmpp.event.XMPPEvent;

import com.thinkparity.desdemona.model.DefaultDelegate;
import com.thinkparity.desdemona.model.io.sql.PaymentSql;
import com.thinkparity.desdemona.model.io.sql.UserSql;
import com.thinkparity.desdemona.model.node.NodeService;
import com.thinkparity.desdemona.model.profile.ProfileModelImpl.XAContextId;
import com.thinkparity.desdemona.model.profile.payment.PaymentPlan;
import com.thinkparity.desdemona.model.profile.payment.provider.PaymentProvider;

/**
 * <b>Title:</b>thinkParity Desdemona Model Default Profile Delegate<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class ProfileDelegate extends DefaultDelegate<ProfileModelImpl> {

    /** A node service. */
    protected NodeService nodeService;

    /** A payment sql implementation. */
    protected PaymentSql paymentSql;

    /** A user sql implementation. */
    protected UserSql userSql;

    /**
     * Create ProfileDelegate.
     *
     */
    protected ProfileDelegate() {
        super();
    }

    /**
     * @see com.thinkparity.desdemona.model.DefaultDelegate#initialize(com.thinkparity.desdemona.model.AbstractModelImpl)
     *
     */
    @Override
    public void initialize(final ProfileModelImpl modelImplementation) {
        super.initialize(modelImplementation);
        this.paymentSql = modelImplementation.getPaymentSql();
        this.userSql = modelImplementation.getUserSql();
        this.nodeService = modelImplementation.getNodeService();
    }

    /**
     * @see com.thinkparity.desdemona.model.profile.ProfileModelImpl#beginXA(Object)
     * 
     */
    protected final void beginXA(final Object xaContext)
            throws NotSupportedException, SystemException {
        modelImplementation.beginXA(xaContext);
    }

    /**
     * @see com.thinkparity.desdemona.model.profile.ProfileModelImpl#completeXA(Object)
     * 
     */
    protected final void completeXA(final Object xaContext) throws SystemException,
            HeuristicMixedException, HeuristicRollbackException,
            RollbackException, SystemException {
        modelImplementation.completeXA(xaContext);
    }

    /**
     * @see com.thinkparity.desdemona.model.profile.ProfileModelImpl#createEvent(List,
     *      XMPPEvent, Calendar)
     * 
     */
    protected final void createEvent(final List<Profile> profileList,
            final XMPPEvent event, final Calendar eventDate) {
        modelImplementation.createEvent(profileList, event, eventDate);
    }

    /**
     * @see com.thinkparity.desdemona.model.profile.ProfileModelImpl#createEvent(User,
     *      XMPPEvent, Calendar)
     * 
     */
    protected final void createEvent(final User user, final XMPPEvent event,
            final Calendar eventDate) {
        modelImplementation.createEvent(user, event, eventDate);
    }

    /**
     * @see com.thinkparity.desdemona.model.profile.ProfileModelImpl#newXAContext(XAContextId)
     * 
     */
    protected final Object newXAContext(final XAContextId xaContextId) {
        return modelImplementation.newXAContext(xaContextId);
    }

    /**
     * @see com.thinkparity.desdemona.model.profile.ProfileModelImpl#notifyComplete(PaymentPlan,
     *      Calendar)
     * 
     */
    protected final void notifyComplete(final PaymentPlan plan,
            final Calendar completedOn) {
        modelImplementation.notifyComplete(plan, completedOn);
    }

    /**
     * @see com.thinkparity.desdemona.modle.profile.ProfileModeImpl#readFeatures(User)
     * 
     */
    protected final List<Feature> readFeatures(final User user) {
        return modelImplementation.readFeatures(user);
    }

    /**
     * @see com.thinkparity.desdemona.model.profile.ProfileModelImpl#readPaymentPlanProfiles(PaymentPlan)
     * 
     */
    protected final List<Profile> readPaymentPlanProfiles(final PaymentPlan plan) {
        return modelImplementation.readPaymentPlanProfiles(plan);
    }

    /**
     * @see com.thinkparity.desdemona.model.profile.ProfileModelImpl#readPaymentProvider(com.thinkparity.codebase.model.profile.payment.PaymentInfo)
     * 
     */
    protected final PaymentProvider readPaymentProvider(
            final PaymentInfo paymentInfo) {
        return modelImplementation.readPaymentProvider(paymentInfo);
    }

    /**
     * @see com.thinkparity.desdemona.model.profile.ProfileModelImpl#rollbackXA()
     * 
     */
    protected final void rollbackXA() throws SystemException {
        modelImplementation.rollbackXA();
    }

    /**
     * @see com.thinkparity.desdemona.model.profile.ProfileModelImpl#updatePlanArrears(PaymentPlan,
     *      Boolean)
     * 
     */
    protected final void updatePlanArrears(final PaymentPlan plan, final Boolean arrears) {
        modelImplementation.updatePlanArrears(plan, arrears);
    }

    /**
     * @see com.thinkparity.desdemona.model.profile.ProfileModelImpl#updateProfilesActive(List,
     *      Boolean)
     * 
     */
    protected final void updateProfilesActive(final List<Profile> profileList,
            final Boolean active) {
        modelImplementation.updateProfilesActive(profileList, active);
    }

    /**
     * @see com.thinkparity.desdemona.model.profile.ProfileModelImpl#wakePaymentProcessor()
     * 
     */
    protected final void wakePaymentProcessor() {
        modelImplementation.wakePaymentProcessor();
    }
}
