/*
 * Created On:  25-Jul-07 10:34:56 PM
 */
package com.thinkparity.codebase.crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;

/**
 * <b>Title:</b>thinkParity Codebase Symmetric Decrypt File<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class DecryptFile {

    /** The decryption cipher. */
    private final Cipher cipher;

    /**
     * Create DecryptFile.
     * 
     * @param transformation
     *            A cipher transformation <code>String</code>.
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     */
    public DecryptFile(final String transformation)
            throws NoSuchPaddingException, NoSuchAlgorithmException {
        super();
        this.cipher = Cipher.getInstance(transformation);
    }

    /**
     * Decrypt a file.
     * 
     * @param key
     *            A <code>Key</code>.
     * @param source
     *            A <code>File</code>.
     * @param target
     *            A <code>File</code>.
     * @param buffer
     *            A <code>byte[]</code>.
     */
    public void decrypt(final Key key, final File source, final File target,
            final byte[] buffer) throws InvalidKeyException, IOException {
        cipher.init(Cipher.DECRYPT_MODE, key);
        final OutputStream outStream = new FileOutputStream(target);
        try {
            final CipherInputStream cipherStream = new CipherInputStream(
                    new FileInputStream(source), cipher);
            try {
                int bytesRead;
                while (true) {
                    bytesRead = cipherStream.read(buffer);
                    if (-1 == bytesRead) {
                        break;
                    }
                    outStream.write(buffer, 0, bytesRead);
                }
            } finally {
                cipherStream.close();
            }
        } finally {
            try {
                outStream.flush();
            } finally {
                outStream.close();
            }
        }
    }
}
