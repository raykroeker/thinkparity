/*
 * Created On:  7-Nov-06 3:49:17 PM
 */
package com.thinkparity.codebase.crypto;

/**
 * <b>Title:</b>thinkParity Crypto Monitor<br>
 * <b>Description:</b>A crypto monitor is notified by the core encryption
 * manipulation implementation of the chunks as they are encrypted and
 * decrypted.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface CryptoMonitor {

    /**
     * A chunk of the stream was encrypted.
     * 
     * @param chunkSize
     *            The <code>int</code> size of the chunk.
     */
    public void chunkEncrypted(final int chunkSize);

    /**
     * A chunk of the stream was decrypted.
     * 
     * @param chunkSize
     *            The <code>int</code> size of the chunk.
     */
    public void chunkDecrypted(final int chunkSize);
}
