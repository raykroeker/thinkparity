/*
 * Created On:  27-Apr-07 9:26:53 AM
 */
package com.thinkparity.ophelia.model;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.util.List;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.ThinkParityException;
import com.thinkparity.codebase.model.profile.ProfileEMail;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.artifact.InternalArtifactModel;
import com.thinkparity.ophelia.model.backup.InternalBackupModel;
import com.thinkparity.ophelia.model.contact.InternalContactModel;
import com.thinkparity.ophelia.model.container.InternalContainerModel;
import com.thinkparity.ophelia.model.crypto.InternalCryptoModel;
import com.thinkparity.ophelia.model.document.InternalDocumentModel;
import com.thinkparity.ophelia.model.index.InternalIndexModel;
import com.thinkparity.ophelia.model.profile.InternalProfileModel;
import com.thinkparity.ophelia.model.session.InternalSessionModel;
import com.thinkparity.ophelia.model.stream.InternalStreamModel;
import com.thinkparity.ophelia.model.user.InternalUserModel;
import com.thinkparity.ophelia.model.util.ProcessMonitor;
import com.thinkparity.ophelia.model.util.Step;
import com.thinkparity.ophelia.model.workspace.Workspace;
import com.thinkparity.ophelia.model.workspace.impl.DefaultRetryHandler;

import com.thinkparity.service.AuthToken;

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
public class DefaultDelegate<T extends Model<?>> implements Delegate<T> {

    /**
     * @see com.thinkparity.ophelia.model.Model#contains(List, List)
     * 
     */
    protected static final Boolean contains(
            final List<ProfileEMail> profileEMails, final List<EMail> emails) {
        return Model.contains(profileEMails, emails);
    }

    /**
     * @see com.thinkparity.ophelia.model.Model#contains(List, JabberId)
     * 
     */
    protected static final <U extends User> Boolean contains(
            final List<U> users, final JabberId userId) {
        return Model.contains(users, userId);
    }

    /**
     * @see com.thinkparity.ophelia.model.Model#contains(List, User)
     * 
     */
    protected static final <U extends User, V extends User> Boolean contains(
            final List<U> users, final V user) {
        return Model.contains(users, user);
    }

    /**
     * @see com.thinkparity.ophelia.model.Model#getIds(List, List)
     * 
     */
    protected static final <U extends User> List<JabberId> getIds(
            final List<U> users, final List<JabberId> userIds) {
        return Model.getIds(users, userIds);
    }

    /**
     * @see com.thinkparity.ophelia.model.Model#indexOf(List, JabberId)
     * 
     */
    protected static final <U extends User> int indexOf(final List<U> users,
            final JabberId userId) {
        return Model.indexOf(users, userId);
    }

    /**
     * @see com.thinkparity.ophelia.model.Model#indexOf(List, User)
     * 
     */
    protected static final <U extends User, V extends User> int indexOf(
            final List<U> users, final V user) {
        return Model.indexOf(users, user);
    }

    /**
     * @see com.thinkparity.ophelia.model.Model#notifyDetermine(ProcessMonitor, Integer)
     * 
     */
    protected static final void notifyDetermine(final ProcessMonitor monitor,
            final Integer steps) {
        Model.notifyDetermine(monitor, steps);
    }

    /**
     * @see com.thinkparity.ophelia.model.Model#notifyStepBegin(ProcessMonitor, Step)
     * 
     */
    protected static final void notifyStepBegin(final ProcessMonitor monitor,
            final Step step) {
        Model.notifyStepBegin(monitor, step);
    }

    /**
     * @see com.thinkparity.ophelia.model.Model#notifyStepBegin(ProcessMonitor, Step, Object)
     * 
     */
    protected static final void notifyStepBegin(final ProcessMonitor monitor,
            final Step step, final Object data) {
        Model.notifyStepBegin(monitor, step, data);
    }

    /**
     * @see com.thinkparity.ophelia.model.Model#notifyStepEnd(ProcessMonitor, Step)
     * 
     */
    protected static final void notifyStepEnd(final ProcessMonitor monitor,
            final Step step) {
        Model.notifyStepEnd(monitor, step);
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
     * @see Model#checksum(File)
     * 
     */
    protected final String checksum(final File file) throws IOException {
        return modelImplementation.checksum(file);
    }

    /**
     * @see com.thinkparity.ophelia.model.Model#createTempFile()
     * 
     */
    protected final File createTempFile() throws IOException {
        return modelImplementation.createTempFile();
    }

    /**
     * @see Model#createTempFile(String)
     * 
     */
    protected final File createTempFile(final String suffix) throws IOException {
        return modelImplementation.createTempFile(suffix);
    }

    /**
     * @see Model#deleteCredentials()
     * 
     */
    protected final void deleteCredentials() {
        modelImplementation.deleteCredentials();
    }

    /**
     * @see Model#deleteToken()
     * 
     */
    protected final void deleteToken() {
        modelImplementation.deleteToken();
    }

    /**
     * @see Model#fileToChannel(File, WritableByteChannel)
     * 
     */
    protected final void fileToChannel(final File file,
            final WritableByteChannel channel) throws IOException {
        modelImplementation.fileToChannel(file, channel);
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
     * Obtain the web-service authentication token.
     * 
     * @return An <code>AuthToken</code>.
     */
    protected final AuthToken getAuthToken() {
        return getSessionModel().getAuthToken();
    }

    /**
     * Obtain an internal backup model.
     * 
     * @return An instance of <code>InternalBackupModel</code>.
     */
    protected final InternalBackupModel getBackupModel() {
        return modelImplementation.getBackupModel();
    }

    /**
     * @see com.thinkparity.ophelia.model.Model#getBuffer()
     * 
     */
    protected final ByteBuffer getBuffer() {
        return modelImplementation.getBuffer();
    }

    /**
     * @see com.thinkparity.ophelia.model.Model#getBufferArray()
     * 
     */
    protected final byte[] getBufferArray() {
        return modelImplementation.getBufferArray();
    }

    /**
     * @see com.thinkparity.ophelia.model.Model#getBufferLock()
     * 
     */
    protected final Object getBufferLock() {
        return modelImplementation.getBufferLock();
    }

    /**
     * Obtain the default buffer size.
     * 
     * @return An <code>Integer</code> buffer size.
     */
    protected final Integer getBufferSize() {
        return modelImplementation.getBufferSize();
    }

    /**
     * Obtain an internal contact model.
     * 
     * @return An instance of <code>InternalContactModel</code>.
     */
    protected final InternalContactModel getContactModel() {
        return modelImplementation.getContactModel();
    }

    /**
     * Obtain an internal container model.
     * 
     * @return An instance of <code>InternalContainerModel</code>.
     */
    protected final InternalContainerModel getContainerModel() {
        return modelImplementation.getContainerModel();
    }

    /**
     * Obtain an internal crypto model.
     * 
     * @return An instance of <code>InternalCryptoModel</code>.
     */
    protected final InternalCryptoModel getCryptoModel() {
        return modelImplementation.getCryptoModel();
    }

    /**
     * Obtain an internal document model.
     * 
     * @return An instance of <code>InternalDocumentModel</code>.
     */
    protected final InternalDocumentModel getDocumentModel() {
        return modelImplementation.getDocumentModel();
    }

    /**
     * @see com.thinkparity.ophelia.model.Model#environment
     * 
     */
    protected final Environment getEnvironment() {
        return modelImplementation.environment;
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
     * Obtain an internal profile model.
     * 
     * @return An <code>InternalProfileModel</code>.
     */
    protected final InternalProfileModel getProfileModel() {
        return modelImplementation.getProfileModel();
    }

    /**
     * Obtain an internal session model.
     * 
     * @return An instance of <code>InternalSessionModel</code>.
     */
    protected final InternalSessionModel getSessionModel() {
        return modelImplementation.getSessionModel();
    }

    /**
     * Obtain an internal stream model.
     * 
     * @return An instance of <code>InternalStreamModel</code>.
     */
    protected final InternalStreamModel getStreamModel() {
        return modelImplementation.getStreamModel();
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
     * @see com.thinkparity.ophelia.model.Model#workspace
     * 
     */
    protected final Workspace getWorkspace() {
        return modelImplementation.workspace;
    }

    /**
     * @see com.thinkparity.ophelia.model.Model#localTeamMember(Long)
     * 
     */
    protected final TeamMember localTeamMember(final Long artifactId) {
        return modelImplementation.localTeamMember(artifactId);
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

    /**
     * @see Model#newDefaultRetryHandler()
     * 
     */
    protected final DefaultRetryHandler newDefaultRetryHandler() {
        return modelImplementation.newDefaultRetryHandler();
    }

    /**
     * @see Model#newThread(String, Runnable)
     * 
     */
    protected Thread newThread(final String name, final Runnable runnable) {
        return modelImplementation.newThread(name, runnable);
    }

    /**
     * @see Model#panic(Throwable)
     * 
     */
    protected final ThinkParityException panic(final Throwable cause) {
        return modelImplementation.panic(cause);
    }

    /**
     * Read the next version id for an artifact.
     * 
     * @param artifactId
     *            An artifact id <code>Long</code>.
     * @return The next version id <code>Long</code>.
     */
    protected final Long readNextVersionId(final Long artifactId) {
        return modelImplementation.readNextVersionId(artifactId);
    }

    /**
     * @see Model#streamToChannel(InputStream, WritableByteChannel)
     * 
     */
    protected final void streamToChannel(final InputStream stream,
            final WritableByteChannel channel) throws IOException {
        modelImplementation.streamToChannel(stream, channel);
    }
}
