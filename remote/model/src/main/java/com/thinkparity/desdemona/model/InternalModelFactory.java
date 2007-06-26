/*
 * Created On: Sep 11, 2006 4:06:57 PM
 */
package com.thinkparity.desdemona.model;

import com.thinkparity.codebase.model.Context;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.desdemona.model.amazon.s3.AmazonS3ModelImpl;
import com.thinkparity.desdemona.model.amazon.s3.InternalAmazonS3Model;
import com.thinkparity.desdemona.model.artifact.ArtifactModelImpl;
import com.thinkparity.desdemona.model.artifact.InternalArtifactModel;
import com.thinkparity.desdemona.model.backup.BackupModelImpl;
import com.thinkparity.desdemona.model.backup.InternalBackupModel;
import com.thinkparity.desdemona.model.contact.ContactModelImpl;
import com.thinkparity.desdemona.model.contact.InternalContactModel;
import com.thinkparity.desdemona.model.container.ContainerModelImpl;
import com.thinkparity.desdemona.model.container.InternalContainerModel;
import com.thinkparity.desdemona.model.migrator.InternalMigratorModel;
import com.thinkparity.desdemona.model.migrator.MigratorModelImpl;
import com.thinkparity.desdemona.model.profile.InternalProfileModel;
import com.thinkparity.desdemona.model.profile.ProfileModelImpl;
import com.thinkparity.desdemona.model.queue.InternalQueueModel;
import com.thinkparity.desdemona.model.queue.QueueModelImpl;
import com.thinkparity.desdemona.model.rules.InternalRuleModel;
import com.thinkparity.desdemona.model.rules.RuleModelImpl;
import com.thinkparity.desdemona.model.session.InternalSessionModel;
import com.thinkparity.desdemona.model.session.SessionModelImpl;
import com.thinkparity.desdemona.model.stream.InternalStreamModel;
import com.thinkparity.desdemona.model.stream.StreamModelImpl;
import com.thinkparity.desdemona.model.user.InternalUserModel;
import com.thinkparity.desdemona.model.user.UserModelImpl;

/**
 * <b>Title:</b>thinkParity DesdemonaModel Internal Model Factory<br>
 * <b>Description:</b>An internal model factory is used by the model to
 * generate references to interal models outside the scope of the model impl
 * classes; usually to pass off to helper command pattern objects.
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class InternalModelFactory {

    /**
     * Obtain an instance of <code>InternalModelFactory</code>.
     * 
     * @param context
     *            A thinkParity <code>Context</code>.
     * @param environment
     *            A thinkParity <code>Environment</code>.
     * @param workspace
     *            A thinkParity <code>Workspace</code>.
     * @return A <code>InternalModelFactory</code>.
     */
    public static InternalModelFactory getInstance(final Context context,
            final User user) {
        return new InternalModelFactory(context, user);
    }

    /** A <code>ClassLoader</code>. */
    private final ClassLoader classLoader;

    /** The model user. */
    private final User user;

    /**
     * Create InternalModelFactory.
     * 
     * @param context
     *            A thinkParity <code>Context</code>.
     * @param session
     *            A user <code>Session</code>.
     */
    private InternalModelFactory(final Context context, final User user) {
        super();
        this.user = user;
        this.classLoader = context.getClass().getClassLoader();
    }

    /**
     * Obtain an internal amazon s3 model.
     * 
     * @return An instance of <code>InternalAmazonS3Model</code>.
     */
    public final InternalAmazonS3Model getAmazonS3Model() {
        return (InternalAmazonS3Model) newModelProxy(
                InternalAmazonS3Model.class, AmazonS3ModelImpl.class);
    }

    /**
     * Obtain an internal artifact model.
     * 
     * @return An instance of <code>InternalArtifactModel</code>.
     */
    public final InternalArtifactModel getArtifactModel() {
        return (InternalArtifactModel) newModelProxy(
                InternalArtifactModel.class, ArtifactModelImpl.class);
    }

    /**
     * Obtain an internal backup model.
     * 
     * @return An instance of <code>InternalBackupModel</code>.
     */
    public final InternalBackupModel getBackupModel() {
        return (InternalBackupModel) newModelProxy(
                InternalBackupModel.class, BackupModelImpl.class);
    }

    /**
     * Obtain an internal contact model.
     * 
     * @return An instance of <code>InternalContactModel</code>.
     */
    public final InternalContactModel getContactModel() {
        return (InternalContactModel) newModelProxy(
                InternalContactModel.class, ContactModelImpl.class);
    }

    /**
     * Obtain an internal container model.
     * 
     * @return An instance of <code>InternalContainerModel</code>.
     */
    public final InternalContainerModel getContainerModel() {
        return (InternalContainerModel) newModelProxy(
                InternalContainerModel.class, ContainerModelImpl.class);
    }

    /**
     * Obtain an internal migrator model.
     * 
     * @return An instance of <code>IntegernalMigratorModel</code>.
     */
    public final InternalMigratorModel getMigratorModel() {
        return (InternalMigratorModel) newModelProxy(
                InternalMigratorModel.class, MigratorModelImpl.class);
    }

    /**
     * Obtain an internal profile model.
     * 
     * @return An instance of <code>InternalProfileModel</code>.
     */
    public final InternalProfileModel getProfileModel() {
        return (InternalProfileModel) newModelProxy(
                InternalProfileModel.class, ProfileModelImpl.class);
    }

    /**
    * Obtain a internal queue model.
    * 
    * @return An instance of <code>InternalQueueModel</code>.
    */
   public final InternalQueueModel getQueueModel() {
    return (InternalQueueModel) newModelProxy(
            InternalQueueModel.class, QueueModelImpl.class);
   }

    /**
     * Obtain a internal rule model.
     * 
     * @return An instance of <code>InternalRuleModel</code>.
     */
    public final InternalRuleModel getRuleModel() {
        return (InternalRuleModel) newModelProxy(
                InternalRuleModel.class, RuleModelImpl.class);
    }

    /**
     * Obtain an internal session model.
     * 
     * @return An instance of <code>InternalSessionModel</code>.
     */
    public final InternalSessionModel getSessionModel() {
        return (InternalSessionModel) newModelProxy(
                InternalSessionModel.class, SessionModelImpl.class);
    }

    /**
     * Obtain a internal stream model.
     * 
     * @return An instance of <code>InternalStreamModel</code>.
     */
    public final InternalStreamModel getStreamModel() {
        return (InternalStreamModel) newModelProxy(
                InternalStreamModel.class, StreamModelImpl.class);
    }

    /**
     * Obtain an internal user model.
     * 
     * @return An instance of <code>InternalUserModel</code>.
     */
    public final InternalUserModel getUserModel() {
        return (InternalUserModel) newModelProxy(
                InternalUserModel.class, UserModelImpl.class);
    }

    /**
     * Create a proxy for a thinkParity model.
     * 
     * @param modelInterface
     *            A thinkParity model interface.
     * @param modelImplementation
     *            A thinkParity model implementation.
     * @return A new proxy instance <code>Object</code>.
     */
    private Object newModelProxy(final Class<?> modelInterface,
            final Class<?> modelImplementation) {
        return ModelFactory.newModelProxy(user, classLoader, modelInterface,
                modelImplementation);
    }
}
