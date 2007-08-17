/*
 * Created On:  25-Jul-07 10:25:36 PM
 */
package com.thinkparity.codebase.crypto;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;

/**
 * <b>Title:</b>thinkParity Codebase Symmetric Encrypt File<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class EncryptStream {

    /** The encryption cipher. */
    private final Cipher cipher;

    /**
     * Create EncryptFile.
     * 
     * @param transform
     *            The cipher transform <code>String</code>.
     */
    public EncryptStream(final String transformation)
            throws NoSuchPaddingException, NoSuchAlgorithmException {
        super();
        this.cipher = Cipher.getInstance(transformation);
    }

    /**
     * Encrypt a file.
     * 
     * @param key
     *            A <code>Key</code>.
     * @param source
     *            An <code>InputStream</code>.
     * @param target
     *            A <code>File</code>.
     * @param buffer
     *            A <code>byte[]</code>.
     * @throws InvalidKeyException
     * @throws IOException
     */
    public void encrypt(final Key key, final InputStream source,
            final File target, final byte[] buffer) throws InvalidKeyException,
            IOException {
        if (null == key || null == source || null == target || null == buffer) {
            throw new NullPointerException();
        }
        if (!target.exists()) {
            throw new IllegalArgumentException("Target " + target.getAbsolutePath() + " does not exist.");
        }
        if (!target.isFile()) {
            throw new IllegalArgumentException("Target " + target.getAbsolutePath() + " is not a file.");
        }
        if (!target.canRead()) {
            throw new IllegalArgumentException("Target " + target.getAbsolutePath() + " cannot be read.");
        }
        if (!target.canWrite()) {
            throw new IllegalArgumentException("Target " + target.getAbsolutePath() + " cannot be written to.");
        }
        cipher.init(Cipher.ENCRYPT_MODE, key);
        final CipherOutputStream cipherStream = new CipherOutputStream(
                new FileOutputStream(target), cipher);
        try {
            int bytesRead;
            while (true) {
                bytesRead = source.read(buffer);
                if (-1 == bytesRead) {
                    break;
                }
                cipherStream.write(buffer, 0, bytesRead);
            }
        } finally {
            try {
                cipherStream.flush();
            } finally {
                cipherStream.close();
            }
        }
    }
}
