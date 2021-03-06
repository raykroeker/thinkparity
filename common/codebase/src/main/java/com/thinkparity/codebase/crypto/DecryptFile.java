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

import com.thinkparity.codebase.delegate.CancelException;
import com.thinkparity.codebase.delegate.Cancelable;

/**
 * <b>Title:</b>thinkParity Codebase Symmetric Decrypt File<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class DecryptFile implements Cancelable {

    /** A null (does nothing) monitor. */
    private static final CryptoMonitor NULL_MONITOR;

    static {
        NULL_MONITOR = new CryptoMonitor() {
            /**
             * @see com.thinkparity.codebase.crypto.CryptoMonitor#chunkDecrypted(int)
             * 
             */
            @Override
            public void chunkDecrypted(final int chunkSize) {}
            /**
             * @see com.thinkparity.codebase.crypto.CryptoMonitor#chunkEncrypted(int)
             *
             */
            @Override
            public void chunkEncrypted(final int chunkSize) {}
        };
    }

    /** A cancel indicator. */
    private boolean cancel;

    /** The decryption cipher. */
    private final Cipher cipher;

    /** A decryption monitor. */
    private final CryptoMonitor monitor;

    /** A run indicator. */
    private boolean running;

    /**
     * Create DecryptFile.
     * 
     * @param monitor
     *            A <code>CryptoMonitor</code>.
     * @param transformation
     *            A cipher transformation <code>String</code>.
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     */
    public DecryptFile(final CryptoMonitor monitor, final String transformation)
            throws NoSuchPaddingException, NoSuchAlgorithmException {
        super();
        this.cancel = false;
        this.cipher = Cipher.getInstance(transformation);
        this.monitor = monitor;
        this.running = false;
    }

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
        this(NULL_MONITOR, transformation);
    }

    /**
     * @see com.thinkparity.codebase.delegate.Cancelable#cancel()
     * 
     */
    public void cancel() throws CancelException {
        this.cancel = true;
        if (running) {
            synchronized (this) {
                try {
                    wait();
                } catch (final InterruptedException ix) {
                    throw new CancelException(ix);
                }
            }
        }
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
        running = true;
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            final OutputStream outStream = new FileOutputStream(target);
            try {
                final CipherInputStream cipherStream = new CipherInputStream(
                        new FileInputStream(source), cipher);
                try {
                    int bytesRead;
                    while (true) {
                        if (cancel) {
                            break;
                        } else {
                            bytesRead = cipherStream.read(buffer);
                            if (-1 == bytesRead) {
                                break;
                            }
                            if (cancel) {
                                break;
                            } else {
                                outStream.write(buffer, 0, bytesRead);
                                notifyDecrypted(bytesRead);
                            }
                        }
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
        } finally {
            running = false;
            synchronized (this) {
                notifyAll();
            }
        }
    }

    /**
     * Notify a number of bytes has been decrypted.
     * 
     * @param bytes
     *            An <code>Integer</code>.
     */
    private void notifyDecrypted(final Integer bytes) {
        monitor.chunkDecrypted(bytes);
    }
}
