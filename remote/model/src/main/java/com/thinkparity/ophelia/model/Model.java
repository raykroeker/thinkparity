/*
 * Created On: Aug 6, 2005
 */
package com.thinkparity.ophelia.model;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import com.thinkparity.codebase.DateUtil;
import com.thinkparity.codebase.StreamUtil;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.event.EventListener;
import com.thinkparity.codebase.event.EventNotifier;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.nio.ChannelUtil;

import com.thinkparity.codebase.model.Context;
import com.thinkparity.codebase.model.DownloadMonitor;
import com.thinkparity.codebase.model.ThinkParityException;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.stream.StreamException;
import com.thinkparity.codebase.model.stream.StreamMonitor;
import com.thinkparity.codebase.model.stream.StreamSession;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.codec.MD5Util;

import com.thinkparity.ophelia.model.artifact.ArtifactUtil;
import com.thinkparity.ophelia.model.artifact.InternalArtifactModel;
import com.thinkparity.ophelia.model.container.InternalContainerModel;
import com.thinkparity.ophelia.model.document.InternalDocumentModel;
import com.thinkparity.ophelia.model.index.InternalIndexModel;
import com.thinkparity.ophelia.model.io.IOFactory;
import com.thinkparity.ophelia.model.io.handler.ConfigurationIOHandler;
import com.thinkparity.ophelia.model.user.InternalUserModel;
import com.thinkparity.ophelia.model.user.UserUtils;
import com.thinkparity.ophelia.model.util.ProcessMonitor;
import com.thinkparity.ophelia.model.util.Step;
import com.thinkparity.ophelia.model.workspace.InternalWorkspaceModel;
import com.thinkparity.ophelia.model.workspace.Workspace;

import com.thinkparity.desdemona.model.stream.InternalStreamModel;
import com.thinkparity.desdemona.model.stream.StreamModel;

/**
 * <b>Title:</b>thinkParity Model<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.40
 * @param <T>
 */
public abstract class Model<T extends EventListener> extends
        com.thinkparity.codebase.model.AbstractModelImpl {

    /** An instance of <code>ArtifactUtil<code>. */
    protected static final ArtifactUtil ARTIFACT_UTIL;

    /** A set of user utilitiy methods. */
    protected static final UserUtils USER_UTILS;

    static {
        ARTIFACT_UTIL = ArtifactUtil.getInstance();
        USER_UTILS = UserUtils.getInstance();
    }

    /**
     * Determine if the list of team members contains the user id..
     * 
     * @param team
     *            A <code>List&lt;TeamMember&gt;</code>.
     * @param user
     *            A user id <code>JabberId</code>.
     * @return True if the user id matches one of the team members.
     */
    protected static final <U extends User> Boolean contains(
            final List<U> users, final JabberId userId) {
        return USER_UTILS.contains(users, userId);
    }

    /**
     * Determine if the list of team members contains the user.
     * 
     * @param team
     *            A list of team members.
     * @param user
     *            A user.
     * @return True if the id of the user matches one of the team members.
     */
    protected static final <U extends User, V extends User> Boolean contains(
            final List<U> users, final V user) {
        return USER_UTILS.contains(users, user);
    }

    /**
	 * Obtain the current date\time.
	 * 
	 * @return The current date\time.
	 */
	protected static Calendar currentDateTime() {
        return DateUtil.getInstance();
	}

    /**
     * Obtain the index of a user id in a list of users.
     * 
     * @param users
     *            A list of <code>User</code>s.
     * @param userId
     *            A user id.
     * @return The index of the user id in the list; or -1 if the user does not
     *         exist in the list.
     */
    protected static final <U extends User> int indexOf(final List<U> users,
            final JabberId userId) {
        return USER_UTILS.indexOf(users, userId);
    }

    /**
     * Notify a process monitor that a given number of steps is upcoming.
     * 
     * @param monitor
     *            A <code>ProcessMonitor</code>.
     * @param steps
     *            An <code>Integer</code> number of steps.
     */
    protected static final void notifyDetermine(final ProcessMonitor monitor,
            final Integer steps) {
        monitor.determineSteps(steps);
    }

    /**
     * Notify a process monitor that a given process will begin.
     * 
     * @param monitor
     *            A <code>ProcessMonitor</code>.
     * @param steps
     *            An <code>Integer</code> number of steps.
     */
    protected static final void notifyProcessBegin(final ProcessMonitor monitor) {
        monitor.beginProcess();
    }

    /**
     * Notify a process monitor that a given process will end.
     * 
     * @param monitor
     *            A <code>ProcessMonitor</code>.
     * @param steps
     *            An <code>Integer</code> number of steps.
     */
    protected static final void notifyProcessEnd(final ProcessMonitor monitor) {
        monitor.endProcess();
    }

	/**
     * Notify a process monitor that a given step will begin.
     * 
     * @param monitor
     *            A <code>ProcessMonitor</code>.
     * @param step
     *            A <code>Step</code>.
     */
    protected static final void notifyStepBegin(final ProcessMonitor monitor,
            final Step step) {
        notifyStepBegin(monitor, step, null);
    }

	/**
     * Notify a process monitor that a given step will begin.
     * 
     * @param monitor
     *            A <code>ProcessMonitor</code>.
     * @param step
     *            A <code>Step</code>.
     * @param data
     *            Any extra step data.
     */
    protected static final void notifyStepBegin(final ProcessMonitor monitor,
            final Step step, final Object data) {
        monitor.beginStep(step, data);
    }

	/**
     * Notify a process monitor that a given step will end.
     * 
     * @param monitor
     *            A <code>ProcessMonitor</code>.
     * @param step
     *            A <code>Step</code>.
     */
    protected static final void notifyStepEnd(final ProcessMonitor monitor,
            final Step step) {
        monitor.endStep(step);
    }

    /** The configuration io. */
    protected ConfigurationIOHandler configurationIO;

    /** A thinkParity <code>Environment</code>. */
    protected Environment environment;

	/** A thinkParity <code>InternalModelFactory</code>. */
    protected InternalModelFactory modelFactory;

    /** A thinkParity <code>Workspace</code>. */
	protected Workspace workspace;

    /** A list of all pending <code>EventNotifier</code>s of <code>T</code>. */
    private final List<EventNotifier<T>> notifiers;

    /**
     * Create an Model
     * 
     */
	protected Model() {
		super();
        this.notifiers = new Vector<EventNotifier<T>>(3);
	}

    /**
     * Add a thinkParity event listener.
     * 
     * @param listener
     *            A thinkParity <code>EventListener</code>.
     * @return Whether or not the listener list was modified as a result of
     *         calling add.
     */
    protected void addListener(final T listener) {
        final Context context = new Context();
        InternalWorkspaceModel.getInstance(context).addListener(workspace, this, listener);
    }

    /**
     * Assert that the user is not a team member.
     * 
     * @param assertion
     *            An assertion.
     * @param artifactId
     *            An artifact id.
     * @param userId
     *            A user id.
     */
    protected void assertNotTeamMember(final Object assertion, final Long artifactId, final JabberId userId) {
        final List<TeamMember> team = getArtifactModel().readTeam2(artifactId);
        final User user = getUserModel().read(userId);
        if (null != user)
            Assert.assertNotTrue(assertion, contains(team, user));
    }

    /**
     * Assert that the user is a team member.
     * 
     * @param assertion
     *            An assertion.
     * @param artifactId
     *            An artifact id.
     * @param userId
     *            A user id.
     */
    protected void assertTeamMember(final Object assertion, final Long artifactId, final JabberId userId) {
        final List<TeamMember> team = getArtifactModel().readTeam2(artifactId);
        Assert.assertTrue(assertion, contains(team, getUserModel().read(userId)));
    }

    /**
     * Assert that the xmpp service is online.
     * 
     * @param environment
     *            A thinkParity <code>Environment</code>.
     */
    protected void assertXMPPIsReachable(final Environment environment) {
        Assert.assertTrue(environment.isXMPPReachable(),
                "XMPP environment {0}:{1} is not reachable.",
                environment.getXMPPHost(), environment.getXMPPPort());
    }

    /**
     * Calculate a checksum for a file's contents. Create a channel to read the
     * file.
     * 
     * @param file
     *            A <code>File</code>.
     * @return An MD5 checksum <code>String</code>.
     */
    protected final String checksum(final File file) throws IOException {
        final ReadableByteChannel channel = ChannelUtil.openReadChannel(file);
        try {
            return checksum(channel);
        } finally {
            channel.close();
        }
    }

    /**
     * Calculate a checksum for a readable byte channel. Use the workspace
     * buffer as an intermediary.
     * 
     * @param channel
     *            A <code>ReadableByteChannel</code>.
     * @return An MD5 checksum <code>String</code>.
     */
    protected final String checksum(final ReadableByteChannel channel)
            throws IOException {
        synchronized (workspace.getBufferLock()) {
            return MD5Util.md5Hex(channel, workspace.getBufferArray());
        }
    }

    /**
     * Determine whether or not a latest version exists.
     * 
     * @param artifactId
     *            An artifact id <code>Long</code>.
     * @return True if a version exists; false otherwise.
     */
    protected Boolean doesExistLatestVersion(final Long artifactId) {
        return getArtifactModel().doesVersionExist(artifactId);
    }

    /**
     * Determine whether or not a version exists.
     * 
     * @param artifactId
     *            An artifact id <code>Long</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     * @return True if a version exists; false otherwise.
     */
    protected Boolean doesExistVersion(final Long artifactId, final Long versionId) {
        return getArtifactModel().doesVersionExist(artifactId, versionId);
    }

    /**
     * Start a download from the stream server.
     * 
     * @param downloadMonitor
     *            A download monitor.
     * @param streamId
     *            A stream id <code>String</code>.
     * @return The downloaded <code>File</code>.
     * @throws IOException
     */
    protected final File downloadStream(final DownloadMonitor downloadMonitor,
            final String streamId) throws IOException {
        final File streamFile = buildStreamFile(streamId);
        final InternalStreamModel streamModel = StreamModel.getInternalModel(new Context());
        final StreamSession streamSession = streamModel.createArchiveSession(localUserId());
        logger.logVariable("streamSession.getBufferSize()", streamSession.getBufferSize());
        logger.logVariable("streamSession.getCharset()", streamSession.getCharset());
        logger.logVariable("streamSession.getId()", streamSession.getId());
        final StreamMonitor streamMonitor = new StreamMonitor() {
            long recoverChunkOffset = 0;
            long totalChunks = 0;
            public void chunkReceived(final int chunkSize) {
                logger.logApiId();
                logger.logVariable("chunkSize", chunkSize);
                totalChunks += chunkSize;
                downloadMonitor.chunkDownloaded(chunkSize);
            }
            public void chunkSent(final int chunkSize) {}
            public void headerReceived(final String header) {}
            public void headerSent(final String header) {}
            public void streamError(final StreamException error) {
                if (error.isRecoverable()) {
                    if (recoverChunkOffset <= totalChunks) {
                        logger.logWarning(error, "Network error.");
                        recoverChunkOffset = totalChunks;
                        try {
                            // attempt to resume the download
                            downloadStream(downloadMonitor, this,
                                    streamSession, streamId, streamFile,
                                    Long.valueOf(recoverChunkOffset));
                        } catch (final IOException iox) {
                            throw panic(iox);
                        }
                    } else {
                        throw panic(error);
                    }
                } else {
                    throw panic(error);
                }
            }
        };
        downloadStream(downloadMonitor, streamMonitor, streamSession,
                streamId, streamFile, 0L);
        return streamFile;
    }

    /**
     * Copy the content of a file to a channel. Create a channel to read the
     * file.
     * 
     * @param file
     *            A <code>File</code>.
     * @param channel
     *            A <code>WritableByteChannel</code>.
     * @throws IOException
     */
    protected final void fileToChannel(final File file,
            final WritableByteChannel channel) throws IOException {
        final ReadableByteChannel readChannel = ChannelUtil.openReadChannel(file);
        try {
            channelToChannel(readChannel, channel);
        } finally {
            readChannel.close();
        }
    }

    /**
     * Copy the content of a file to another file. Create a channel to read the
     * file.
     * 
     * @param readFile
     *            A <code>File</code>.
     * @param writeFile
     *            A <code>File</code>.
     * @throws IOException
     */
    protected final void fileToFile(final File readFile, final File writeFile)
            throws IOException {
        final ReadableByteChannel readChannel = ChannelUtil.openReadChannel(readFile);
        try {
            channelToFile(readChannel, writeFile);
        } finally {
            readChannel.close();
        }
    }

    /**
     * Copy the content of a file to a stream. Create a channel to read the
     * file.
     * 
     * @param file
     *            A <code>File</code>.
     * @param stream
     *            An <code>OutputStream</code>.
     * @throws IOException
     */
    protected final void fileToStream(final File file, final OutputStream stream)
            throws IOException {
        final ReadableByteChannel channel = ChannelUtil.openReadChannel(file);
        try {
            channelToStream(channel, stream);
        } finally {
            channel.close();
        }
    }

    /**
     * Find the user in a team.
     * 
     * @param team
     *            The team.
     * @param user
     *            The user to look for.
     * @return The team member.
     */
    protected TeamMember get(final List<TeamMember> team, final User user) {
        return team.get(indexOf(team, user));
    }

    /**
     * Obtain an internal artifact model.
     * 
     * @return An <code>InternalArtifactModel</code>.
     */
    protected final InternalArtifactModel getArtifactModel() {
        return modelFactory.getArtifactModel();
    }

    /**
     * Obtain the default buffer size.
     * 
     * @return An <code>Integer</code> default buffer size.
     */
    protected Integer getBufferSize() {
        return workspace.getBufferSize();
    }

        /**
         * Obtain an internal container model.
         * 
         * @return An instance of <code>InternalContainerModel</code>.
         */
        protected final InternalContainerModel getContainerModel() {
            return modelFactory.getContainerModel();
        }

    /**
     * Obtain an internal document model.
     * 
     * @return An instance of <code>InternalDocumentModel</code>.
     */
	protected final InternalDocumentModel getDocumentModel() {
		return modelFactory.getDocumentModel();
	}

    /**
     * Obtain an internal index model.
     * 
     * @return An instance of <code>InternalIndexModel</code>.
     */
    protected final InternalIndexModel getIndexModel() {
        return modelFactory.getIndexModel();
    }

    /**
     * Obtain an internal user model.
     * 
     * @return An instance of <code>InternalUserModel</code>.
     */
    protected final InternalUserModel getUserModel() {
        return modelFactory.getUserModel();
    }

    /**
     * Initialize the model.
     * 
     * @param environment
     *            A thinkParity <code>Environment</code>.
     * @param workspace
     *            A thinkParity <code>Workspace</code>.
     */
    protected final void initialize(final Environment environment,
            final Workspace workspace) {
        this.environment = environment;
        this.workspace = workspace;

        this.configurationIO = IOFactory.getDefault(workspace).createConfigurationHandler();
        this.modelFactory = InternalModelFactory.getInstance(getContext(), environment, workspace);
    }

    /**
     * Intialize the model.
     * 
     * @param environment
     *            A thinkParity <code>Environment</code>.
     * @param workspace
     *            A thinkParity <code>Workspace</code>.
     */
    protected abstract void initializeModel(final Environment environment,
            final Workspace workspace);

    /**
     * Obtain the team member for the local user.
     * 
     * @param artifactId
     *            An artifact id.
     * @return The team member.
     */
    protected TeamMember localTeamMember(final Long artifactId) {
        final List<TeamMember> team = getArtifactModel().readTeam2(artifactId);
        return get(team, localUser());
    }

    /**
     * Obtain the local user.
     *
     * @return The current user.
     */
    protected User localUser() {
        final JabberId currentUserId = localUserId();
        if (null == currentUserId) {
            return null;
        } else {
            return getUserModel().read(currentUserId);
        }
    }

    /**
	 * Obtain the local user id.
	 * 
	 * @return The jabber id of the local user.
	 */
	protected JabberId localUserId() {
        final com.thinkparity.desdemona.model.user.InternalUserModel serverUserModel =
            com.thinkparity.desdemona.model.user.UserModel.getInternalModel(new Context());
	    return serverUserModel.readBackupUserId();
	}

    /**
     * Notify all event listeners.
     *
     */
    protected void notifyListeners() {
        final Context context = new Context();
        final List<T> listeners = InternalWorkspaceModel.getInstance(context).getListeners(workspace, this);
        try {
            for (final EventNotifier<T> notifier : notifiers) {
                for (final T listener : listeners) {
                    try {
                        notifier.notifyListener(listener);
                    } catch (final Throwable t) {
                        logger.logError(t, "Event notification {0}:{1} failed.",
                            notifier, listener);
                    }
                }
            }
        } finally {
            notifiers.clear();
        }
    }

    /**
     * Notify all event listeners.
     * 
     * @param notifier
     *            A thinkParity <code>EventNotifier</code>.
     */
    protected void notifyListeners(final EventNotifier<T> notifier) {
        notifiers.add(notifier);
    }

    /**
     * @see com.thinkparity.codebase.model.AbstractModelImpl#panic(java.lang.Throwable)
     * 
     */
	@Override
    protected ThinkParityException panic(final Throwable t) {
        return super.panic(t);
    }

    /**
     * Copy the content of a stream to a file. Use a channel to write to the
     * file.
     * 
     * @param stream
     *            An <code>InputStream</code>.
     * @param file
     *            A <code>File</code>.
     * @throws IOException
     */
    protected final void streamToFile(final InputStream stream, final File file)
            throws IOException {
        final WritableByteChannel channel = ChannelUtil.openWriteChannel(file);
        try {
            streamToChannel(stream, channel);
        } finally {
            channel.close();
        }
    }

    /**
     * Build a local file to back a stream. Note that the file is transient in
     * nature and will be deleted when thinkParity is shutdown or the next time
     * it is started up.
     * 
     * @param streamId
     *            A stream id <code>String</code>.
     * @return A <code>File</code>.
     * @throws IOException
     */
    private File buildStreamFile(final String streamId) throws IOException {
        return workspace.createTempFile(streamId);
    }

    /**
     * Copy the content of one channel to another. Use the workspace buffer as
     * an intermediary.
     * 
     * @param readChannel
     *            A <code>ReadableByteChannel</code>.
     * @param writeChannel
     *            A <code>WritableByteChannel</code>.
     * @throws IOException
     */
    private void channelToChannel(final ReadableByteChannel readChannel,
            final WritableByteChannel writeChannel) throws IOException {
        synchronized (workspace.getBufferLock()) {
            ChannelUtil.copy(readChannel, writeChannel, workspace.getBuffer());
        }
    }

    /**
     * Copy the content of a channel to a file. Create a channel to write to the
     * file.
     * 
     * @param channel
     *            A <code>ReadableByteChannel</code>.
     * @param file
     *            A <code>File</code>.
     * @throws IOException
     */
    private void channelToFile(final ReadableByteChannel channel,
            final File file) throws IOException {
        final WritableByteChannel writeChannel = ChannelUtil.openWriteChannel(file);
        try {
            channelToChannel(channel, writeChannel);
        } finally {
            writeChannel.close();
        }
    }

    /**
     * Copy the content of a channel to a stream. Use the workspace buffer as an
     * intermediary.
     * 
     * @param channel
     *            A <code>ReadableByteChannel</code>.
     * @param stream
     *            An <code>OutputStream</code>.
     * @throws IOException
     */
    private void channelToStream(final ReadableByteChannel channel,
            final OutputStream stream) throws IOException {
        synchronized (workspace.getBufferLock()) {
            StreamUtil.copy(channel, stream, workspace.getBuffer());
        }
    }

    /**
     * Obtain the index of a team member in a user list.
     * 
     * @param users
     *            A user list.
     * @param teamMember
     *            A team member.
     * @return The index of the team member in the users list or -1 if the team
     *         member does not exist in the list.
     */
    private <U extends User, V extends User> int indexOf(final List<U> users,
            final V user) {
        return USER_UTILS.indexOf(users, user);
    }

    /**
     * Copy the content of a stream to a channel. Use the workspace byte buffer
     * as an intermediary.
     * 
     * @param stream
     *            An <code>InputStream</code>.
     * @param channel
     *            A <code>WritableByteChannel</code>.
     * @throws IOException
     */
    private void streamToChannel(final InputStream stream,
            final WritableByteChannel channel) throws IOException {
        synchronized (workspace.getBufferLock()) {
            StreamUtil.copy(stream, channel, workspace.getBuffer());
        }
    }
}
