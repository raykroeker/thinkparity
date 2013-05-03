/*
 * Created On:  7-Oct-07 1:29:56 PM
 */
package com.thinkparity.desdemona.util.crypto;

import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import com.thinkparity.common.StringUtil;

import com.thinkparity.codebase.codec.MD5Util;

import org.apache.commons.codec.binary.Base64;

/**
 * <b>Title:</b>thinkParity Desdemona Crypto Provider Implementation<br>
 * <b>Description:</b>A crypto provider implementation.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class CryptoProviderImpl implements CryptoProvider {

    /** A character set to use when converting to/from bytes. */
    private static final Charset CHARSET;

    static {
        CHARSET = StringUtil.Charset.UTF_8.getCharset();
    }

    /** An decryption cipher. */
    private final Cipher decryptionCipher;

    /** An encryption cipher. */
    private final Cipher encryptionCipher;

    /**
     * Create CryptoProviderImpl.
     *
     */
    public CryptoProviderImpl() throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException {
        super();
        final byte[] rawKey = MD5Util.md5("010932671-023769081237450981735098127-1280397-181-2387-6581972689-1728-9671-8276-892173-5971283-751-239875-182735-98712-85971-2897-867-9823-56823165-8365-89236-987-214981265-9-9-65623-5896-35-3296-289-65893-983-932-5928734-302894719825-99181-28497612-8375".getBytes());
        final SecretKeySpec secretKeySpec = new SecretKeySpec(rawKey, "AES");

        decryptionCipher = Cipher.getInstance("AES");
        decryptionCipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

        encryptionCipher = Cipher.getInstance("AES");
        encryptionCipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
    }

    /**
     * @see com.thinkparity.codebase.model.util.crypto.Decrypter#decrypt(java.lang.String)
     *
     */
    @Override
    public String decrypt(final String cipherText)
            throws IllegalBlockSizeException, BadPaddingException {
        return new String(decryptionCipher.doFinal(Base64.decodeBase64(
                cipherText.getBytes(CHARSET))), CHARSET);
    }

    /**
     * @see com.thinkparity.codebase.model.util.crypto.Encrypter#encrypt(java.lang.String)
     *
     */
    @Override
    public String encrypt(final String clearText)
            throws IllegalBlockSizeException, BadPaddingException {
        return new String(Base64.encodeBase64(encryptionCipher.doFinal(
                clearText.getBytes(CHARSET))), CHARSET);
    }
}
