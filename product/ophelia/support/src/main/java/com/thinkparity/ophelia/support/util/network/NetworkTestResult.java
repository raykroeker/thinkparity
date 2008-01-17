/*
 * Created On:  13-Dec-07 2:13:18 PM
 */
package com.thinkparity.ophelia.support.util.network;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.text.MessageFormat;
import java.util.Date;


/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class NetworkTestResult {

    /** A passed flag. */
    private Boolean pass;

    /** The failure cause. */
    private String text;

    /** A uri. */
    private URI uri;

    /**
     * Create NetworkTestResult.
     *
     */
    public NetworkTestResult() {
        super();
    }

    /**
     * Obtain the text.
     *
     * @return A <code>String</code>.
     */
    public String getText() {
        return text;
    }

    /**
     * Obtain the uri.
     *
     * @return A <code>URI</code>.
     */
    public URI getURI() {
        return uri;
    }

    /**
     * Determine the result pass.
     * 
     */
    public Boolean isFail() {
        return Boolean.valueOf(Boolean.FALSE == pass);
    }

    /**
     * Determine the result pass.
     * 
     */
    public Boolean isPass() {
        return pass;
    }

    /**
     * Set the pass flag.
     * 
     * @param pass
     *            A <code>Boolean</code>.
     */
    public void setFail() {
        this.pass = Boolean.FALSE;
        this.text = MessageFormat.format("{0,date,yyyy-MM-dd HH:mm} - {1}\nNetwork test failed.", new Date(), uri.toString());
    }

    /**
     * Set the pass flag.
     * 
     * @param pass
     *            A <code>Boolean</code>.
     */
    public void setFail(final String text, final Object... textArguments) {
        this.pass = Boolean.FALSE;
        this.text = MessageFormat.format(
                "{0,date,yyyy-MM-dd HH:mm} - {1}\nNetwork test failed.\n{2}",
                new Date(), uri.toString(),
                MessageFormat.format(text, textArguments));
    }

    /**
     * Set the pass flag.
     * 
     * @param pass
     *            A <code>Boolean</code>.
     */
    public void setFail(final Throwable cause) {
        this.pass = Boolean.FALSE;
        final StringWriter stackTrace = new StringWriter();
        cause.printStackTrace(new PrintWriter(stackTrace));
        this.text = MessageFormat.format("{0,date,yyyy-MM-dd HH:mm} - {1}\nNetwork test failed.\n{2}", new Date(), uri.toString(), stackTrace.toString());
    }

    /**
     * Set the pass flag.
     * 
     * @param pass
     *            A <code>Boolean</code>.
     */
    public void setPass() {
        this.pass = Boolean.TRUE;
        this.text = MessageFormat.format("{0,date,yyyy-MM-dd HH:mm} - {1}\nNetwork test passed.", new Date(), uri.toString());
    }

    /**
     * Set the uri.
     *
     * @param uri
     *		A <code>URI</code>.
     */
    public void setURI(final URI uri) {
        this.uri = uri;
    }
}
