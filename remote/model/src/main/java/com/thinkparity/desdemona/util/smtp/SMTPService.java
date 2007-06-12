/*
 * Created On: Jul 21, 2006 10:43:02 AM
 */
package com.thinkparity.desdemona.util.smtp;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import com.thinkparity.desdemona.util.DesdemonaProperties;

/**
 * <b>Title:</b>thinkParity Desdemona Util SMTP Transport Manager<br>
 * <b>Description:</b>A java mail SMTP transport manager for desdemona.
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.3
 */
public final class SMTPService {

    /** A singleton instance of the <code>SMTPService</code>. */
    private static final SMTPService SINGLETON;

    static {
        SINGLETON = new SMTPService();
    }

    /**
     * Obtain an instance of the smtp service.
     * 
     * @return An instance of <code>SMTPService</code>.
     */
    public static SMTPService getInstance() {
        return SINGLETON;
    }

    /** A mail authentiator. */
    private final Authenticator authenticator;

    /** The transport host <code>String</code>. */
    private final String host;

    /** A synchronization lock <code>Object</code>. */
    private final Object lock;

    /** A java mail <code>Session</code>. */
    private Session mailSession;

    /** The transport port <code>int</code>. */
    private final int port;

    /**
     * Create SMTPService.
     *
     */
    private SMTPService() {
        super();
        final DesdemonaProperties properties = DesdemonaProperties.getInstance();
        this.host = properties.getProperty("thinkparity.mail.transport-host");
        this.port = Integer.valueOf(properties.getProperty("thinkparity.mail.transport-port"));
        this.lock = new Object();
        this.authenticator = null;
    }

    /**
     * Create a new message.
     * 
     * @return A <code>MimeMessage</code>.
     */
    public MimeMessage createMessage() {
        return new MimeMessage(mailSession);
    }

    /**
     * Deliver an internet mime message via the jive e-mail service.
     * 
     * @param mimeMessage
     *            An internet mime message.
     */
    public void deliver(final MimeMessage mimeMessage) {
        synchronized (lock) {
            try {
                deliverImpl(mimeMessage);
            } catch (final NoSuchProviderException nspx) {
                throw new SMTPException(nspx);
            } catch (final MessagingException mx) {
                throw new SMTPException(mx);
            }
        }
    }

    /**
     * The deliver implementation.
     * 
     * @param mimeMessage
     *            A <code>MimeMessage</code>.
     * @throws NoSuchProviderException
     * @throws MessagingException
     */
    private void deliverImpl(final MimeMessage mimeMessage)
            throws NoSuchProviderException, MessagingException {
        openSession();
        final Transport mailTransport = mailSession.getTransport();
        mailTransport.connect();
        try {
            mailTransport.sendMessage(mimeMessage,
                    mimeMessage.getAllRecipients());
        } finally {
            mailTransport.close();
        }
    }

    /**
     * Open a mail session.
     *
     */
    private void openSession() {
        final Properties mailProperties = new Properties();
        mailProperties.setProperty("mail.transport.protocol", "smtp");
        mailProperties.setProperty("mail.smtp.host", host);
        mailProperties.setProperty("mail.smtp.port", String.valueOf(port));
        mailSession = Session.getInstance(mailProperties, authenticator);
    }
}
