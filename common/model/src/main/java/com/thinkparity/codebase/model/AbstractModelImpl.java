/*
 * Created On: Sep 16, 2006 1:54:18 PM
 */
package com.thinkparity.codebase.model;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import com.thinkparity.codebase.ErrorHelper;
import com.thinkparity.codebase.StringUtil;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import org.apache.commons.codec.binary.Base64;

/**
 * <b>Title:</b>thinkParity Common Codebase Model Abstraction<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class AbstractModelImpl {

    /** The charset (encoding) used when encrypting/decrypting. */
    private static final Charset ENCRYPTION_CHARSET;

    static {
        ENCRYPTION_CHARSET = StringUtil.Charset.UTF_8.getCharset();
    }

    /** An apache logger wrapper. */
    protected final Log4JWrapper logger;

    private Context context;

    /** The decryption cipher. */
    private transient Cipher decryptionCipher;

    /** The encryption cipher. */
    private transient Cipher encryptionCipher;

    /**
     * Create AbstractModelImpl.
     * 
     * 
     */
    protected AbstractModelImpl() {
        super();
        this.logger = new Log4JWrapper(getClass());
    }

    /**
     * Set context.
     *
     * @param context The Context.
     */
    public final void setContext(final Context context) {
        this.context = context;
    }

    /**
     * Decrypt the cipher text into clear text using the cipher key.
     * 
     * @param cipherText
     *            The cipher text.
     * @return The clear text.
     * @throws BadPaddingException
     * @throws IOException
     * @throws IllegalBlockSizeException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     */
    protected final String decrypt(final String cipherText)
            throws BadPaddingException, IOException, IllegalBlockSizeException,
            InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException {
        final Cipher cipher = getDecryptionCipher();
        return new String(cipher.doFinal(Base64.decodeBase64(
                cipherText.getBytes(ENCRYPTION_CHARSET))), ENCRYPTION_CHARSET);
    }

    /**
     * Encrypt clear text into a base 64 encoded cipher text.
     * 
     * @param clearText
     *            The clean text to encrypt.
     * @return The cipher text.
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     */
    protected final String encrypt(final String clearText)
            throws BadPaddingException, IOException, IllegalBlockSizeException,
            InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException {
        final Cipher cipher = getEncryptionCipher();
        return new String(Base64.encodeBase64(cipher.doFinal(
                clearText.getBytes(ENCRYPTION_CHARSET))), ENCRYPTION_CHARSET);
    }

    /**
     * Obtain the context
     *
     * @return The Context.
     */
    protected final Context getContext() {
        return context;
    }

    /**
     * Obtain the secret key; creating it if necessary.
     * 
     * @return The secret key.
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    protected abstract SecretKeySpec getSecretKeySpec() throws IOException,
            NoSuchAlgorithmException;
    
    /**
     * Panic. Nothing can be done about the error that has been generated. An
     * appropriate error is constructed suitable for throwing beyond the model
     * interface.
     * 
     * @param t
     *            A <code>Throwable</code>.
     * @return A <code>RuntimeException</code>.
     */
    protected ThinkParityException panic(final Throwable t) {
        if (ThinkParityException.class.isAssignableFrom(t.getClass())) {
            return (ThinkParityException) t;
        } else {
            final String errorId = new ErrorHelper().getErrorId(t);
            logger.logError(t, "{0}", errorId);
            return new ThinkParityException(errorId.toString(), t);
        }
    }

    /**
     * Obtain the decryption cipher; creating it if necessary.
     * 
     * @return A decryption cipher.
     * @throws IOException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     */
    private Cipher getDecryptionCipher() throws IOException,
            InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException {
        if(null == decryptionCipher) {
            decryptionCipher = Cipher.getInstance("AES");
            decryptionCipher.init(Cipher.DECRYPT_MODE, getSecretKeySpec());
        }
        return decryptionCipher;
    }

    /**
     * Obtain the encryption cipher; creating it if need be.
     * 
     * @return The encryption cipher.
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     */
    private Cipher getEncryptionCipher() throws IOException,
            InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException {
        if (null == encryptionCipher) {
            encryptionCipher = Cipher.getInstance("AES");
            encryptionCipher.init(Cipher.ENCRYPT_MODE, getSecretKeySpec());
        }
        return encryptionCipher;
    }
}
