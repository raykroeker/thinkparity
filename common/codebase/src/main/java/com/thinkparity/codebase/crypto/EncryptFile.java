/*
 * Created On:  25-Jul-07 10:25:36 PM
 */
package com.thinkparity.codebase.crypto;

import java.io.File;
import java.io.FileInputStream;
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
public final class EncryptFile {

    /** The encryption cipher. */
    private final Cipher cipher;

    /**
     * Create EncryptFile.
     * 
     * @param transform
     *            The cipher transform <code>String</code>.
     */
    public EncryptFile(final String transformation)
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
     *            A <code>File</code>.
     * @param target
     *            A <code>File</code>.
     * @param buffer
     *            A <code>byte[]</code>.
     * @throws InvalidKeyException
     * @throws IOException
     */
    public void encrypt(final Key key, final File source, final File target,
            final byte[] buffer) throws InvalidKeyException, IOException {
        if (null == key || null == source || null == target || null == buffer) {
            throw new NullPointerException();
        }
        if (!source.exists()) {
            throw new IllegalArgumentException("Source " + source.getAbsolutePath() + " does not exist.");
        }
        if (!source.isFile()) {
            throw new IllegalArgumentException("Source " + source.getAbsolutePath() + " is not a file.");
        }
        if (!source.canRead()) {
            throw new IllegalArgumentException("Source " + source.getAbsolutePath() + " cannot be read.");
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
        final InputStream inStream = new FileInputStream(source);
        try {
            final CipherOutputStream cipherStream = new CipherOutputStream(
                    new FileOutputStream(target), cipher);
            try {
                int bytesRead;
                while (true) {
                    bytesRead = inStream.read(buffer);
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
        } finally {
            inStream.close();
        }
    }
}
