/*
 * Created On:  15-Nov-07 1:19:32 PM
 */
package com.thinkparity.desdemona.model.admin.message;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

import com.thinkparity.codebase.StackUtil;
import com.thinkparity.codebase.codec.MD5Util;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.migrator.Error;
import com.thinkparity.codebase.model.migrator.Product;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.InvalidTokenException;
import com.thinkparity.codebase.model.util.Token;

import com.thinkparity.desdemona.model.migrator.MigratorModel;
import com.thinkparity.desdemona.model.node.Node;
import com.thinkparity.desdemona.model.node.NodeService;

import com.thinkparity.desdemona.service.application.ApplicationService;

import com.thinkparity.desdemona.util.DateTimeProvider;
import com.thinkparity.desdemona.util.DesdemonaProperties;

/**
 * <b>Title:</b>thinkParity Desdemona Admin Model Message Bus<br>
 * <b>Description:</b>Used to deliver persistable messages to the
 * administration model.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class MessageBus {

    /** An instance of the application service. */
    private static final ApplicationService applicationService;

    /** An enabled flag. */
    private static final boolean enabled;

    /** A log4j wrapper. */
    private static final Log4JWrapper logger;

    /** A resource bundle of messages. */
    private static final ResourceBundle messageBundle;

    /** An instance of the application message model. */
    private static final InternalMessageModel messageModel;

    /** An instance of the application migrator model. */
    private static final MigratorModel migratorModel;

    /** An instance of the node. */
    private static final Node node;

    /** The server's latest release. */
    private static final Release serverLatestRelease;

    /** The server's latest product. */
    private static final Product serverProduct;

    /** A token to access the message model interface. */
    private static final Token token;

    static {
        logger = new Log4JWrapper(MessageBus.class);
        final Properties properties = DesdemonaProperties.getInstance();
        enabled = Boolean.parseBoolean(properties.getProperty("thinkparity.messagebus.enabled", "true"));
        if (enabled) {
            applicationService = ApplicationService.getInstance();
            node = NodeService.getInstance().getNode();
            messageModel = applicationService.getInternalAdminModelFactory().newMessageModel();
            migratorModel = applicationService.getModelFactory().getMigratorModel();
            serverProduct = applicationService.getServerProduct();
            serverLatestRelease = applicationService.getServerLatestRelease();
            messageBundle = ResourceBundle.getBundle("localization.MessageMessages");
            token = new Token();
            token.setValue(MD5Util.md5Base64(String.valueOf(System.currentTimeMillis())));
        } else {
            applicationService = null;
            node = null;
            messageModel = null;
            migratorModel = null;
            serverProduct = null;
            serverLatestRelease = null;
            messageBundle = null;
            token = null;
        }
    }

    /**
     * Obtain an instance of a message bus for a model.
     * 
     * @param model
     *            A <code>Model</code>.
     * @return A <code>MessageBus</code>.
     */
    public static MessageBus getInstance(final User user,
            final String channelName) {
        Channel channel = null;
        try {
            channel = messageModel.readChannel(token, channelName);
            if (null == channel) {
                channel = new Channel();
                channel.setName(channelName);
                messageModel.createChannel(token, channel);
            }
        } catch (final InvalidTokenException itx) {
            channel = null;
            logError(itx, MessageFormat.format(
                    "Cannot initialize message bus for channel {0}.", channelName));
        }
        return new MessageBus(channel);
    }

    /**
     * Obtain the bus token.
     * 
     * @return A <code>Token</code>.
     */
    static Token getToken() {
        return token;
    }

    /**
     * Determine if the message bus is enabled.
     * 
     * @return True if it is enabled.
     */
    static Boolean isEnabled() {
        return enabled;
    }

    /**
     * Log an error.
     * 
     * @param cause
     *            A <code>Throwable</code>.
     * @param message
     *            A <code>String</code>.
     * @param level
     *            A <code>Level</code>.
     * @param synopsis
     *            A <code>String</code>.
     */
    private static void logError(final Throwable cause, final String message) {
        final Error error = new Error();
        error.setArguments(new String[] { message });
        error.setMethod(StackUtil.getCallerMethodName());
        error.setOccuredOn(DateTimeProvider.getCurrentDateTime());
        final StringWriter writer = new StringWriter();
        cause.printStackTrace(new PrintWriter(writer));
        error.setStack(writer.toString());
        migratorModel.logError(serverProduct, serverLatestRelease, error);
    }

    /**
     * Instantiate a resource bundle key for a model/content.
     * 
     * @param model
     *            A <code>Model</code>.
     * @param content
     *            A <code>String</code>.
     * @return A <code>String</code>.
     */
    private static String newKey(final Channel channel, final String synopsis) {
        return MessageFormat.format("{0}:{1}", channel.getName(), synopsis);
    }

    /**
     * Instantiate a message.
     * 
     * @param channel
     *            A <code>Channel</code>.
     * @param message
     *            A <code>String</code>.
     * @param longData
     *            A <code>List<MessageData<Long>></code>.
     * @param stringData
     *            A <code>List<MessageData<String>></code>.
     * @return A <code>Message</code>.
     */
    private static Message newMessage(final Channel channel,
            final String message, final List<MessageData<Long>> longData,
            final List<MessageData<String>> stringData) {
        final Message m = new Message();
        m.setLongData(longData);
        m.setStringData(stringData);
        m.setNode(node);
        final String key = newKey(channel, message);
        try {
            m.setMessage(messageBundle.getString(key));
        } catch (final MissingResourceException mrx) {
            logger.logWarning(mrx, "Missing message string.");
            m.setMessage("!" + key + "!");
        }
        m.setTimestamp(newTimestamp());
        return m;
    }

    /**
     * Obtain a new timestamp.
     * 
     * @return A <code>Date</code>.
     */
    private static Calendar newTimestamp() {
        return DateTimeProvider.getCurrentDateTime();
    }

    /** A channel. */
    private final Channel channel;

    /** The long data. */
    private final List<MessageData<Long>> longData;

    /** The string data. */
    private final List<MessageData<String>> stringData;

    /**
     * Create MessageBus.
     *
     */
    private MessageBus(final Channel channel) {
        super();
        this.channel = channel;
        this.longData = new ArrayList<MessageData<Long>>();
        this.stringData = new ArrayList<MessageData<String>>();
    }

    /**
     * Deliver a message.
     * 
     * @param message
     *            A <code>String</code>.
     */
    public void deliver(final String message) {
        if (enabled) {
            if (null == channel) {
                logger.logError("Cannot deliver message.  No channel");
            } else {
                try {
                    messageModel.enqueue(token, channel, newMessage(channel,
                            message, longData, stringData));
                } catch (final Throwable t) {
                    try {
                        logError(t, message);
                    } catch (final Throwable t2) {
                        try {
                            logger.logError(t, "Cannot deliver message:  {0}", message);
                            logger.logError(t2, "Cannot log error.");
                        } catch (final Throwable t3) {
                            /* we tried */
                        }
                    }
                } finally {
                    clearData();
                }
            }
        }
    }

    /**
     * Set long data.
     * 
     * @param name
     *            A <code>String</code>.
     * @param value
     *            A <code>Long</code>.
     */
    public void setLong(final String name, final Long value) {
        this.longData.add(new MessageData<Long>(name, value));
    }

    /**
     * Set string data.
     * 
     * @param name
     *            A <code>String</code>.
     * @param value
     *            A <code>String</code>.
     */
    public void setString(final String name, final String value) {
        this.stringData.add(new MessageData<String>(name, value));
    }

    /**
     * Clear the message data.
     * 
     */
    private void clearData() {
        longData.clear();
        stringData.clear();
    }
}
