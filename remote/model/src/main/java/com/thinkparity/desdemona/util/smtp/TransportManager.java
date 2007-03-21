/*
 * Created On: Jul 21, 2006 10:43:02 AM
 */
package com.thinkparity.desdemona.util.smtp;

import javax.mail.internet.MimeMessage;

import org.jivesoftware.util.EmailService;

/**
 * <b>Title:</b>thinkParity SMTP Transport Manager<br>
 * <b>Description:</b>An SMTP transport manager for jive plugins.
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class TransportManager {

    /** A singleton transport manager. */
    private static final TransportManager SINGLETON;

    static { SINGLETON = new TransportManager(); }

    /**
     * Deliver an internet message.
     * 
     * @param message
     *            An internet message.
     */
    public static void deliver(final MimeMessage mimeMessage) {
        synchronized (SINGLETON) {
            SINGLETON.doDeliver(mimeMessage);
        }
    }

    /** The jive messenger e-mail service. */
    private final EmailService emailService;

    /** Create TransportManager. */
    private TransportManager() {
        super();
        this.emailService = EmailService.getInstance();
    }

    /**
     * Deliver an internet mime message via the jive e-mail service.
     * 
     * @param mimeMessage
     *            An internet mime message.
     */
    private void doDeliver(final MimeMessage mimeMessage) {
        emailService.sendMessage(mimeMessage);
    }
}
