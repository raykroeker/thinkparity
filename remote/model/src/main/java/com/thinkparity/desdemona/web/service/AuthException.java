/*
 * Created On:  2-Jun-07 10:21:34 AM
 */
package com.thinkparity.desdemona.web.service;

import java.text.MessageFormat;

import com.thinkparity.codebase.StringUtil.Separator;

import com.thinkparity.service.AuthToken;

/**
 * <b>Title:</b>thinkParity Desdemona Web Service Authentication Exception<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class AuthException extends RuntimeException {

    /** Authentication exception message format pattern. */
    private static final String PATTERN;

    static {
        PATTERN = "Authentication is invalid.{0}\tClient Id:{1}{0}\tSession Id:{2}{0}\tExpires On:{3,date,yyyy-MM-dd HH:mm:ss.SSS Z}";
    }

    /**
     * Create AuthException.
     * 
     * @param authToken
     *            An <code>AuthToken</code>.
     */
    AuthException(final AuthToken authToken) {
        super(MessageFormat.format(PATTERN, Separator.SystemNewLine,
                authToken.getClientId(), authToken.getSessionId(),
                authToken.getExpiresOn()));
    }
}
