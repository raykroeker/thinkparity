/*
 * Created On: Jul 21, 2006 10:27:21 AM
 */
package com.thinkparity.desdemona.util.smtp;

import java.io.UnsupportedEncodingException;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.jivesoftware.util.EmailService;

import com.thinkparity.desdemona.model.Constants.Internet.Mail;


/**
 * <b>Title:</b>thinkParity Message Factory<br>
 * <b>Description:</b>A message factory for jive plugins.
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class MessageFactory {

    /** A singleton message factory. */
    private static final MessageFactory SINGLETON;

    static { SINGLETON = new MessageFactory(); }

    /**
     * Create an internet Mime Message.
     * 
     * @return A mime message.
     */
    public static MimeMessage createMimeMessage() {
        return SINGLETON.doCreateMimeMessage();
    }

    /** All messages are copied to this address. */
    private final Address bccAddress;

    /** The jive messenger e-mail service. */
    private final EmailService emailService;

    /** All messages are from this address. */
    private final Address fromAddress;

    /** Create MessageFactory. */
    private MessageFactory() {
        super();
        this.emailService = EmailService.getInstance();
        try {
            this.bccAddress =
                new InternetAddress(Mail.BCC_ADDRESS, Mail.BCC_PERSONAL);
            this.fromAddress =
                new InternetAddress(Mail.FROM_ADDRESS, Mail.FROM_PERSONAL);
        }
        catch(final UnsupportedEncodingException uex) {
            throw SMTPException.translate(uex);
        }
    }

    /**
     * Add a bcc to the message.
     * 
     * @param mimeMessage
     *            A mime message.
     * @return A mime message.
     */
    private MimeMessage addBlindCarbonCopy(final MimeMessage mimeMessage) {
        try { mimeMessage.addRecipient(Message.RecipientType.BCC, bccAddress); }
        catch(final MessagingException mx) { throw SMTPException.translate(mx); }
        return mimeMessage;
    }

    /**
     * Create an internet Mime Message using Jive's e-mail service.
     * 
     * @return A mime message.
     */
    private MimeMessage doCreateMimeMessage() {
        return addBlindCarbonCopy(setFrom(emailService.createMimeMessage()));
    }

    /**
     * Set the from address in the message.
     * 
     * @param mimeMessage
     *            A mime message.
     * @return A mime message.
     */
    private MimeMessage setFrom(final MimeMessage mimeMessage) {
        try { mimeMessage.setFrom(fromAddress); }
        catch(final MessagingException mx) { throw SMTPException.translate(mx); }
        return mimeMessage;
    }
}
