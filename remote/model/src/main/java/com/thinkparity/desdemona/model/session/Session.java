/*
 * Dec 1, 2005
 */
package com.thinkparity.desdemona.model.session;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

import com.thinkparity.codebase.jabber.JabberId;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface Session {

    /**
     * Create a temporary directory within the session. The sesion will attempt
     * to delete the directory when the user logs out. If the jvm abrubptly
     * exits; the directory will be deleted the next time the session is
     * created.
     * 
     * @return A temporary directory <code>File</code>.
     * @throws IOException
     */
    public File createTempDirectory() throws IOException;

    /**
     * Create a temporary directory within the session. The session will attempt
     * to delete the directory when the user logs out. If the jvm abrubptly
     * exits; the directory will be deleted the next time the session is
     * created.
     * 
     * @param suffix
     *            The suffix string to be used in generating the directory's
     *            name.
     * @return A temporary directory <code>File</code>.
     * @throws IOException
     */
    public File createTempDirectory(final String suffix) throws IOException;

	/**
     * Create a temporary file within the session. The session will attempt to
     * delete the file when the user logs out. If the jvm abrubpty exits the
     * file will be deleted the next time the session is created.
     * 
     * @return A <code>File</code>.
     */
    public File createTempFile() throws IOException;

    /**
     * Create a temporary file within the session. The session will attempt to
     * delete the file when the user logs out. If the jvm abrubpty exits the
     * file will be deleted the next time the session is created.
     * 
     * @param suffix
     *            The suffix string to be used in generating the file's name.
     * @return A <code>File</code>.
     * @throws IOException
     */
    public File createTempFile(final String suffix) throws IOException;

    /**
     * Obtain the session's internet address.
     * 
     * @return An <code>InetAddress</code>.
     */
    public InetAddress getInetAddress();

    /**
     * Obtain the session's user id.
     * 
     * @return A <code>JabberId</code>.
     */
	public JabberId getJabberId();

    /**
     * Obtain the server xmpp domain.
     * 
     * @return The server xmpp domain.
     */
    public String getXmppDomain();
}
