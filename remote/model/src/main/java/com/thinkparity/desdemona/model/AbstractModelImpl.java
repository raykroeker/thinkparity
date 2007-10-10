/*
 * Created On: Nov 28, 2005
 */
package com.thinkparity.desdemona.model;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.thinkparity.codebase.Constants;
import com.thinkparity.codebase.DateUtil;
import com.thinkparity.codebase.FileSystem;
import com.thinkparity.codebase.FileUtil;
import com.thinkparity.codebase.StreamUtil;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.nio.ChannelUtil;

import com.thinkparity.codebase.model.Context;
import com.thinkparity.codebase.model.ThinkParityException;
import com.thinkparity.codebase.model.annotation.ThinkParityBackupEvent;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.codec.MD5Util;
import com.thinkparity.codebase.model.util.xmpp.event.XMPPEvent;
import com.thinkparity.codebase.model.util.xstream.XStreamUtil;

import com.thinkparity.ophelia.model.user.UserUtils;

import com.thinkparity.desdemona.model.amazon.s3.InternalAmazonS3Model;
import com.thinkparity.desdemona.model.artifact.InternalArtifactModel;
import com.thinkparity.desdemona.model.backup.BackupService;
import com.thinkparity.desdemona.model.backup.InternalBackupModel;
import com.thinkparity.desdemona.model.contact.InternalContactModel;
import com.thinkparity.desdemona.model.container.InternalContainerModel;
import com.thinkparity.desdemona.model.crypto.InternalCryptoModel;
import com.thinkparity.desdemona.model.io.sql.ConfigurationSql;
import com.thinkparity.desdemona.model.migrator.InternalMigratorModel;
import com.thinkparity.desdemona.model.profile.InternalProfileModel;
import com.thinkparity.desdemona.model.queue.InternalQueueModel;
import com.thinkparity.desdemona.model.rules.InternalRuleModel;
import com.thinkparity.desdemona.model.session.InternalSessionModel;
import com.thinkparity.desdemona.model.stream.InternalStreamModel;
import com.thinkparity.desdemona.model.user.InternalUserModel;

import com.thinkparity.desdemona.util.DesdemonaProperties;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class AbstractModelImpl
    extends com.thinkparity.codebase.model.AbstractModelImpl {

    /** A <code>UserUtils</code> utility. */
    protected static final UserUtils USER_UTIL;

    /** A <code>XStreamUtil</code> xml serialization utility. */
    protected static final XStreamUtil XSTREAM_UTIL;

    static {
        XSTREAM_UTIL = XStreamUtil.getInstance();
        USER_UTIL = UserUtils.getInstance();
    }

    /** The current execution user. */
	protected User user;

    /** A thinkParity configuration sql interface. */
    private ConfigurationSql configurationSql;

    /** A <code>ModelConfiguration</code>. */
    private ModelConfiguration modelConfiguration;

    /** An instance of the desdmona properties. */
    private DesdemonaProperties properties;

    /** An encryption secret key spec. */
    private transient SecretKeySpec secretKeySpec;

    /** The model user's temporary file system. */
    private FileSystem tempFileSystem;

    /**
     * Create AbstractModelImpl.
     * 
     */
    protected AbstractModelImpl() {
        super();
    }

    /**
     * Set the model user.
     * 
     * @param user
     *            A <code>User</code>.
     */
    public final void setUser(final User user) {
        setContext(new Context());
        this.user = user;
        this.modelConfiguration = ModelConfiguration.getInstance(getClass());
    }

    /**
     * Add a to email recipient to a mime message.
     * 
     * @param mimeMessage
     *            A <code>MimeMessage</code>.
     * @param email
     *            An <code>EMail</code>.
     * @throws MessagingException
     */
    protected void addRecipient(final MimeMessage mimeMessage, final EMail email)
            throws MessagingException {
        addRecipient(mimeMessage, Message.RecipientType.TO, email);
    }

    /**
     * Add a to email recipient to a mime message.
     * 
     * @param mimeMessage
     *            A <code>MimeMessage</code>.
     * @param email
     *            An <code>EMail</code>.
     * @throws MessagingException
     */
    protected void addRecipient(final MimeMessage mimeMessage,
            final Message.RecipientType recipientType, final EMail email)
            throws MessagingException {
        mimeMessage.addRecipient(recipientType, new InternetAddress(
                email.toString(), Boolean.TRUE));
    }

    /**
     * Assert that the actual and expected jabber id's are equal.
     * 
     * @param assertion
     *            The assertion.
     * @param expected
     *            The expected jabber id.
     * @param actual
     *            The actual jabber id.
     */
    protected void assertEquals(final Object assertion,
            final JabberId expected, final JabberId actual) {
        Assert.assertTrue(assertion, expected.equals(actual));
    }

    /**
     * Build a unique id for a user in time. Use the user id plus the current
     * timestamp to generate a unique id.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A unique id <code>String</code>.
     */
    protected String buildUserTimestampId(final JabberId userId) {
        /*
         * NOTE A user timestamp id is unique per user per timestamp
         */
       // TIME A global timestamp
       final String hashString = new StringBuffer(userId.toString())
           .append(currentTimeMillis()).toString();
       return MD5Util.md5Base64(hashString);
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
        synchronized (getBufferLock()) {
            return MD5Util.md5Base64(channel, getBufferArray());
        }
    }

    /**
     * Determine if a list contains a user.
     * 
     * @param <U>
     *            A type of <code>User</code>.
     * @param list
     *            A <code>User</code> <code>List</code>.
     * @param o
     *            A <code>User</code>
     * @return True if the list contains the user.
     */
    protected final <T extends User> boolean contains(final List<T> list,
            final JabberId id) {
        return -1 < indexOf(list, id);
    }

    /**
     * Determine if a list contains a user.
     * 
     * @param <U>
     *            A type of <code>User</code>.
     * @param list
     *            A <code>User</code> <code>List</code>.
     * @param o
     *            A <code>User</code>
     * @return True if the list contains the user.
     */
    protected final <T extends User, U extends User> boolean contains(
            final List<T> list, final U o) {
        return -1 < indexOf(list, o);
    }

    /**
     * Create a temporary directory for the model user.
     * 
     * @return A <code>File</code>.
     */
    protected final File createTempDirectory() throws IOException {
        final String suffix = new StringBuffer(".")
                .append(System.currentTimeMillis())
                .toString();
        return createTempDirectory(suffix);
    }

    /**
     * Create a temporary directory for the model user.
     * 
     * @param suffix
     *            A temp file name suffix <code>String</code>.
     * @return A <code>File</code>.
     */
    protected final File createTempDirectory(final String suffix)
            throws IOException {
        final File tempDirectory = new File(getTempFileSystem().getRoot(), suffix);
        Assert.assertTrue(tempDirectory.mkdir(),
                "Could not create temp directory {0}.", tempDirectory);
        return tempDirectory;
    }

    /**
     * Create a temporary file for the model user.
     * 
     * @return A <code>File</code>.
     */
    protected final File createTempFile() throws IOException {
        final String suffix = new StringBuffer(".")
                .append(System.currentTimeMillis())
                .toString();
        return File.createTempFile(Constants.File.TEMP_FILE_PREFIX, suffix,
                getTempFileSystem().getRoot());

    }

    /**
     * Create a temporary file for the model user.
     * 
     * @param suffix
     *            A temp file name suffix <code>String</code>.
     * @return A <code>File</code>.
     */
    protected final File createTempFile(final String suffix) throws IOException {
        return File.createTempFile(Constants.File.TEMP_FILE_PREFIX, suffix,
                getTempFileSystem().getRoot());

    }

    /**
     * Obtain the date and time.
     * 
     * @return A <code>Calendar</code>.
     */
    protected Calendar currentDateTime() {
        // TIME This is a global date
        return DateUtil.getInstance();
    }

    /**
     * Obtain the current time in milliseconds.
     * 
     * @return The current time <code>Long</code>.
     */
    protected Long currentTimeMillis() {
        // TIME This is a global timestamp
        return System.currentTimeMillis();
    }

    /**
     * Encrypt the password within the credentials.
     * 
     * @param credentials
     *            The <code>Credentials</code>.
     * @return A <code>Credentials</code>.
     */
    protected final Credentials encryptPassword(final Credentials credentials)
            throws BadPaddingException, IOException, IllegalBlockSizeException,
            InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException {
        credentials.setPassword(encrypt(credentials.getPassword()));
        return credentials;
    }

    /**
     * Enqueue an event for a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param event
     *            An <code>XMPPEvent</code>.
     */
    protected final void enqueueEvent(final JabberId userId,
            final XMPPEvent event) {
        // fire backup event
        if (event.getClass().isAnnotationPresent(ThinkParityBackupEvent.class)) {
            BackupService.getInstance().getEventHandler().handleEvent(user, event);
        } else {
            logger.logInfo("Event {0} cannot be backed up.", event.getClass());
        }

        final InternalUserModel userModel = getUserModel();
        final InternalQueueModel queueModel = getQueueModel(userModel.read(userId));
        queueModel.enqueueEvent(event);
        queueModel.flush();
    }

    /**
     * Enqueue an event for a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param event
     *            An <code>XMPPEvent</code>.
     */
    protected final void enqueueEvent(final User user, final XMPPEvent event) {
        // fire backup event
        if (event.getClass().isAnnotationPresent(ThinkParityBackupEvent.class)) {
            BackupService.getInstance().getEventHandler().handleEvent(user, event);
        } else {
            logger.logInfo("Event {0} cannot be backed up.", event.getClass());
        }

        final InternalQueueModel queueModel = getQueueModel(user);
        queueModel.enqueueEvent(event);
        queueModel.flush();
    }

    /**
     * Enqueue an event for a list of user ids.
     * 
     * @param userIds
     *            A <code>List<JabberId></code>.
     * @param event
     *            An <code>XMPPEvent</code>.
     */
    protected final void enqueueEvents(final List<JabberId> userIds,
            final XMPPEvent event) {
        // fire backup event
        if (event.getClass().isAnnotationPresent(ThinkParityBackupEvent.class)) {
            BackupService.getInstance().getEventHandler().handleEvent(user, event);
        } else {
            logger.logInfo("Event {0} cannot be backed up.", event.getClass());
        }

        InternalQueueModel queueModel;
        final InternalUserModel userModel = getUserModel();
        for (final JabberId userId : userIds) {
            queueModel = getQueueModel(userModel.read(userId));
            queueModel.enqueueEvent(event);
            queueModel.flush();
        }
    }

    /**
     * Enqueue an event for a list of user ids.
     * 
     * @param userIds
     *            A <code>List<JabberId></code>.
     * @param event
     *            An <code>XMPPEvent</code>.
     */
    protected final void enqueuePriorityEvents(final List<JabberId> userIds,
            final XMPPEvent event) {
        // fire backup event
        if (event.getClass().isAnnotationPresent(ThinkParityBackupEvent.class)) {
            BackupService.getInstance().getEventHandler().handleEvent(user, event);
        } else {
            logger.logInfo("Event {0} cannot be backed up.", event.getClass());
        }

        InternalQueueModel queueModel;
        final InternalUserModel userModel = getUserModel();
        for (final JabberId userId : userIds) {
            queueModel = getQueueModel(userModel.read(userId));
            queueModel.enqueueEvent(event, XMPPEvent.Priority.HIGH);
            queueModel.flush();
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
     * Obtain the internal amazon s3 model.
     * 
     * @return An instance of <code>InternalAmazonS3Model</code>.
     */
    protected final InternalAmazonS3Model getAmazonS3Model() {
        return InternalModelFactory.getInstance(getContext(), user).getAmazonS3Model();
    }

    /**
     * Obtain an internal artifact model.
     * 
     * @return An instance of <code>InternalArtifactModel</code>.
     */
    protected final InternalArtifactModel getArtifactModel() {
        return InternalModelFactory.getInstance(getContext(), user).getArtifactModel();
    }

    /**
     * Obtain an internal artifact model.
     * 
     * @param user
     *            A <code>User</code>.
     * @return An instance of <code>InternalArtifactModel</code>.
     */
    protected final InternalArtifactModel getArtifactModel(final User user) {
        return InternalModelFactory.getInstance(getContext(), user).getArtifactModel();
    }

    /**
     * Obtain an internal backup model.
     * 
     * @return An instance of <code>InternalBackupModel</code>.
     */
    protected final InternalBackupModel getBackupModel() {
        return InternalModelFactory.getInstance(getContext(), user).getBackupModel();
    }

    /**
     * Obtain an internal backup model.
     * 
     * @return An instance of <code>InternalBackupModel</code>.
     */
    protected final InternalBackupModel getBackupModel(final User user) {
        return InternalModelFactory.getInstance(getContext(), user).getBackupModel();
    }

    /**
     * Obtain the default buffer size.
     * 
     * @return A buffer size <code>Integer</code>.
     */
    protected final ByteBuffer getBuffer() {
        // BUFFER - AbstractModelImpl#getBuffer() - 2MB
        return ByteBuffer.allocateDirect(getBufferSize());
    }

    /**
     * Obtain a buffer byte array.
     * 
     * @return A <code>byte[]</code>.
     */
    protected final byte[] getBufferArray() {
        // BUFFER - AbstractModelImpl#getBufferArray() - 2MB
        return new byte[getBufferSize()];
    }

    /**
     * Obtain a buffer lock. In the server model the buffer is not shared.
     * 
     * @return A buffer lock synchronization <code>Object</code>.
     */
    protected final Object getBufferLock() {
        return new Object();
    }

    /**
     * Obtain the default buffer size.
     * 
     * @return A buffer size <code>Integer</code>.
     */
    protected final Integer getBufferSize() {
        return 1024 * 1024 * 2; // BUFFER 2MB  - AbstractModelImpl#getBufferSize()
    }

    /**
     * Obtain the default buffer size.
     * 
     * @return A buffer size <code>Integer</code>.
     */
    protected final Integer getBufferSize(final String context) {
        if ("stream-session".equals(context)) {
            return 1024 * 1; // BUFFER 1KB - AbstractModelImpl#getBufferSize(String)
        } else {
            return getBufferSize();
        }
    }

    /**
     * Obtain the cipher key used to encrypt configuration information.
     * 
     * @return A cipher key <code>String</code>.
     */
    protected final String getCipherKey() {
        return "18273-4897-12-53974-816523-49-81623-95-4-91-8723-56974812-63498-612395-498-7125-349871265-47892-1539784-1523954-19-287356-4";
    }

    /**
     * Obtain configuration.
     * 
     * @param key
     *            A configuration key <code>String</code>.
     * @return A configuration value <code>String</code>.
     */
    protected final String getConfiguration(final String key) {
        return modelConfiguration.getConfiguration(key);
    }

    /**
     * Obtain an internal contact model.
     * 
     * @return An instance of <code>InternalContactModel</code>.
     */
    protected final InternalContactModel getContactModel() {
        return InternalModelFactory.getInstance(getContext(), user).getContactModel();
    }

    /**
     * Obtain an internal contact model.
     * 
     * @return An instance of <code>InternalContactModel</code>.
     */
    protected final InternalContactModel getContactModel(final User user) {
        return InternalModelFactory.getInstance(getContext(), user).getContactModel();
    }

    /**
     * Obtain an internal container model.
     * 
     * @return An instance of <code>InternalContainerModel</code>.
     */
    protected final InternalContainerModel getContainerModel() {
        return InternalModelFactory.getInstance(getContext(), user).getContainerModel();
    }

    /**
     * Obtain an internal container model for a user.
     * 
     * @return An instance of <code>InternalContainerModel</code>.
     */
    protected final InternalContainerModel getContainerModel(final User user) {
        return InternalModelFactory.getInstance(getContext(), user).getContainerModel();
    }

    /**
     * Obtain an internal crypto model.
     * 
     * @return An instance of <code>InternalCryptoModel</code>.
     */
    protected final InternalCryptoModel getCryptoModel() {
        return InternalModelFactory.getInstance(getContext(), user).getCryptoModel();
    }

    /**
     * Obtain an internal crypto model.
     * 
     * @param user
     *            A <code>User</code>.
     * @return An instance of <code>InternalCryptoModel</code>.
     */
    protected final InternalCryptoModel getCryptoModel(final User user) {
        return InternalModelFactory.getInstance(getContext(), user).getCryptoModel();
    }

    /**
     * Obtain the local environment.
     * 
     * @return An instance of <code>Environment</code>.
     */
    protected Environment getEnvironment() {
        return Environment.valueOf(getProperty("thinkparity.environment"));
    }

    /**
     * Build a list of user ids from a list of users.
     * 
     * @param <U>
     *            A <code>User</code> type.
     * @param users
     *            A <code>List</code> of <code>User</code>s of type
     *            <code>T</code>.
     * @param userIds
     *            A <code>List</code> of <code>JabberId</code>.
     * @return A <code>List</code> of <code>JabberId</code>.
     */
    protected final <U extends User> List<JabberId> getIds(final List<U> users,
            final List<JabberId> userIds) {
        for (final U user : users) {
            userIds.add(user.getId());
        }
        return userIds;
    }

    protected final InternalMigratorModel getMigratorModel() {
        return InternalModelFactory.getInstance(getContext(), user).getMigratorModel();
    }

    protected final InternalMigratorModel getMigratorModel(final User user) {
        return InternalModelFactory.getInstance(getContext(), user).getMigratorModel();
    }

    /**
     * Obtain the product name.
     * 
     * @return A <code>String</code>.
     */
    protected final String getProductName() {
        return getProperty("thinkparity.product-name");
    }

    protected final InternalProfileModel getProfileModel() {
        return InternalModelFactory.getInstance(getContext(), user).getProfileModel();
    }

    protected final InternalProfileModel getProfileModel(final User user) {
        return InternalModelFactory.getInstance(getContext(), user).getProfileModel();
    }

    /**
     * Obtain an instance of the desdemona properties.
     * 
     * @return An instance of <code>DesdemonaProperties</code>.
     */
    protected final DesdemonaProperties getProperties() {
        if (null == properties) {
            properties = DesdemonaProperties.getInstance();
        }
        return properties;
    }

    /**
     * Obtain a property.
     * 
     * @param name
     *            A property name.
     * @return A property value.
     */
    protected final String getProperty(final String name) {
        return getProperties().getProperty(name);
    }

    /**
     * Obtain a property.
     * 
     * @param name
     *            A property name.
     * @param defaultValue
     *            A default value <code>String</code>.
     * @return A property value.
     */
    protected final String getProperty(final String name,
            final String defaultValue) {
        return getProperties().getProperty(name, defaultValue);
    }

    protected final InternalQueueModel getQueueModel() {
        return InternalModelFactory.getInstance(getContext(), user).getQueueModel();
    }

    protected final InternalQueueModel getQueueModel(final User user) {
        return InternalModelFactory.getInstance(getContext(), user).getQueueModel();
    }

    protected final InternalRuleModel getRuleModel() {
        return InternalModelFactory.getInstance(getContext(), user).getRuleModel();
    }

    protected final InternalRuleModel getRuleModel(final User user) {
        return InternalModelFactory.getInstance(getContext(), user).getRuleModel();
    }

	/**
     * @see com.thinkparity.codebase.model.AbstractModelImpl#getSecretKeySpec()
     *
     */
    @Override
    protected SecretKeySpec getSecretKeySpec() throws IOException, NoSuchAlgorithmException {
        if (null == secretKeySpec) {
            final byte[] rawKey = MD5Util.md5("010932671-023769081237450981735098127-1280397-181-2387-6581972689-1728-9671-8276-892173-5971283-751-239875-182735-98712-85971-2897-867-9823-56823165-8365-89236-987-214981265-9-9-65623-5896-35-3296-289-65893-983-932-5928734-302894719825-99181-28497612-8375".getBytes());
            secretKeySpec = new SecretKeySpec(rawKey, "AES");
        }
        return secretKeySpec;
    }

    protected final InternalSessionModel getSessionModel() {
        return InternalModelFactory.getInstance(getContext(), user).getSessionModel();
    }

	protected final InternalStreamModel getStreamModel() {
        return InternalModelFactory.getInstance(getContext(), user).getStreamModel();
    }

	protected final InternalStreamModel getStreamModel(final User user) {
        return InternalModelFactory.getInstance(getContext(), user).getStreamModel();
    }

    /**
     * Obtain the temp file system for the model user.
     * 
     * @return A <code>FileSystem</code>.
     */
    protected final FileSystem getTempFileSystem() {
        if (null == tempFileSystem) {
            final File tempRoot = new File(getProperty("thinkparity.temp.root"));
            final File userTempRoot = new File(tempRoot, String.valueOf(user.getLocalId()));
            if (userTempRoot.exists())
                FileUtil.deleteTree(userTempRoot);
            Assert.assertTrue(userTempRoot.mkdir(),
                    "Cannot create temporary directory {0}.", userTempRoot);
            tempFileSystem = new FileSystem(userTempRoot);
        }
        return tempFileSystem;
    }

    protected final InternalUserModel getUserModel() {
        return InternalModelFactory.getInstance(getContext(), user).getUserModel();
    }

	protected final InternalUserModel getUserModel(final User user) {
        return InternalModelFactory.getInstance(getContext(), user).getUserModel();
    }

    /**
     * Obtain the index of a user in the list.
     * 
     * @param <U>
     *            A type of <code>User</code>.
     * @param list
     *            A user <code>List</code>.
     * @param o
     *            A <code>User</code>
     * @return The index of the first user in the list with a matching id; or -1
     *         if no such user exists.
     */
    protected final <T extends User, U extends User> int indexOf(
            final List<T> users, final U user) {
        return indexOf(users, user.getId());
    }

    /**
     * Obtain the index of a user in the list with the given id.
     * 
     * @param <U>
     *            A type of <code>User</code>.
     * @param list
     *            A user <code>List</code>.
     * @param id
     *            A user id <code>JabberId</code>.
     * @return The index of the first user in the list with a matching id; or -1
     *         if no such user exists.
     */
    protected final <U extends User> int indexOf(final List<U> list,
            final JabberId o) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(o))
                return i;
        }
        return -1;
    }

	/**
     * Intialize the model.
     * 
     */
    protected void initialize() {}

	/**
     * Inject the fields of a user into a user type object.
     * 
     * @param <T>
     *            A type of <code>User</code>.
     * @param type
     *            A <code>T</code>.
     * @param user
     *            A <code>User</code>.
     * @return A <code>T</code>.
     */
    protected <T extends User> T inject(final T type, final User user) {
        type.setId(user.getId());
        type.setLocalId(user.getLocalId());
        type.setName(user.getName());
        type.setOrganization(user.getOrganization());
        type.setTitle(user.getTitle());
        return logger.logVariable("type", type);
    }

    /**
     * Determine if the user id is a system user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return True if the user is a system user.
     */
    protected Boolean isSystemUser(final JabberId userId) {
        return userId.equals(User.THINKPARITY.getId());
    }

    /** Log an api id. */
    protected final void logApiId() {
        logger.logApiId();
    }

	/**
     * Log an api id with a message.
     * 
     * @param message
     *            A message.
     */
    protected final void logApiId(final Object message) {
        logger.logApiId();
    }

	/**
     * Log an info message.
     * 
     * @param infoPattern
     *            An info pattern.
     * @param infoArguments
     *            Info arguments.
     */
    protected final void logInfo(final String infoPattern,
            final Object... infoArguments) {
        logger.logInfo(infoPattern, infoArguments);
    }

    /** Log a trace id. */
    protected final void logTraceId() {
        logger.logApiId();
    }

    /**
     * Log a named variable. Note that the logging renderer will be used only
     * for the value.
     * 
     * @param name
     *            A variable name.
     * @param value
     *            A variable.
     * @return The value.
     */
    protected final <T> T logVariable(final String name, final T value) {
        return logger.logVariable(name, value);
    }

    /**
     * Log a warning.
     * 
     * @param warning
     *            A warning.
     */
    protected final void logWarning(final String warningPattern,
            final Object... warningArguments) {
        logger.logWarning(warningPattern, warningArguments);
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
     * Read thinkParity configuration.
     * 
     * @return A configuration <code>Properties</code>.
     */
    protected Properties readConfiguration() {
        final Properties properties = new Properties();
        final List<String> keys = configurationSql.readKeys();
        for (final String key : keys) {
            properties.setProperty(key, configurationSql.read(key));
        }
        return properties;
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
     * Translate an error into a parity unchecked error.
     * 
     * @param t
     *            An error.
     */
    protected ThinkParityException translateError(final Throwable t) {
        return panic(t);
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
        synchronized (getBufferLock()) {
            ChannelUtil.copy(readChannel, writeChannel, getBuffer());
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
        synchronized (getBufferLock()) {
            StreamUtil.copy(channel, stream, getBuffer());
        }
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
        synchronized (getBufferLock()) {
            StreamUtil.copy(stream, channel, getBuffer());
        }
    }
}
