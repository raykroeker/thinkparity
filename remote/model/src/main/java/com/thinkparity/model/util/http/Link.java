/*
 * Created On: Jul 21, 2006 2:08:58 PM
 */
package com.thinkparity.model.util.http;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import com.thinkparity.codebase.util.Base64;

import com.thinkparity.model.util.MD5Util;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class Link {

    /** The decryption cipher. */
    private static transient Cipher decryptionCipher;

    /** The encryption cipher. */
    private static transient Cipher encryptionCipher;

    /** The secret key spec. */
    private static transient SecretKeySpec secretKeySpec;

    /**
     * Decrypt the cipher text into clear text using the cipher key.
     * 
     * @param cipherKey
     *            The cipher key.
     * @param cipherText
     *            The cipher text.
     * @return The clear text.
     */
    private static String decrypt(final String cipherText) {
        Cipher cipher = null;
        try { cipher = getDecryptionCipher(); }
        catch(final IOException iox) { throw new RuntimeException(iox); }
        catch(final InvalidKeyException ikx) { throw new RuntimeException(ikx); }
        catch(final NoSuchAlgorithmException nsax) { throw new RuntimeException(nsax); }
        catch(final NoSuchPaddingException nspx) { throw new RuntimeException(nspx); }

        
        try { return new String(cipher.doFinal(Base64.decode(cipherText))); }
        catch(final BadPaddingException bpx) { throw new RuntimeException(bpx); }
        catch(final IllegalBlockSizeException ibsx) { throw new RuntimeException(ibsx); }
    }

    /**
     * Encrypt clear text into a base 64 encoded cipher text.
     * 
     * @param cipherKey
     *            The cipher key
     * @param clearText
     *            The clean text to encrypt.
     * @return The cipher text.
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     */
    private static String encrypt(final String clearText) {
        Cipher cipher = null;
        try { cipher = getEncryptionCipher(); }
        catch(final IOException iox) { throw new RuntimeException(iox); }
        catch(final InvalidKeyException ikx) { throw new RuntimeException(ikx); }
        catch(final NoSuchAlgorithmException nsax) { throw new RuntimeException(nsax); }
        catch(final NoSuchPaddingException nspx) { throw new RuntimeException(nspx); }
        
        try { return Base64.encode(cipher.doFinal(clearText.getBytes())); }
        catch(final BadPaddingException bpx) { throw new RuntimeException(bpx); }
        catch(final IllegalBlockSizeException ibsx) { throw new RuntimeException(ibsx); }
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
    private static Cipher getDecryptionCipher() throws IOException,
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
    private static Cipher getEncryptionCipher() throws IOException,
            InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException {
        if(null == encryptionCipher) {
            encryptionCipher = Cipher.getInstance("AES");
            encryptionCipher.init(Cipher.ENCRYPT_MODE, getSecretKeySpec());
        }
        return encryptionCipher;
    }

    /**
     * Obtain the secret key; creating it if necessary.
     * 
     * @return The secret key.
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    private static SecretKeySpec getSecretKeySpec() throws IOException,
            NoSuchAlgorithmException {
        if(null == secretKeySpec) {
            final byte[] rawKey = MD5Util.md5("010932671-023769081237450981735098127-1280397-181-2387-6581972689-1728-9671-8276-892173-5971283-751-239875-182735-98712-85971-2897-867-9823-56823165-8365-89236-987-214981265-9-9-65623-5896-35-3296-289-65893-983-932-5928734-302894719825-99181-28497612-8375".getBytes());
            secretKeySpec = new SecretKeySpec(rawKey, "AES");
        }
        return secretKeySpec;
    }

    /** A stack of contexts. */
    private final Queue<String> context;



    /** A map of parameter name value pairs. */
    private final Map<String, String> parameters;

    /** The link root. */
    private final String root;

    /** Create Link. */
    Link(final String root) {
        super();
        this.context = new LinkedList<String>();
        this.parameters = new HashMap<String, String>();
        this.root = root;
    }

    /**
     * Add context to the link.
     * 
     * @param context
     *            A context.
     * @return True if the context is added; false otherwise.
     */
    public boolean addContext(final String context) {
        return this.context.offer(context);
    }

    /**
     * Set the link parameter.
     * 
     * @param name
     *            The parameter name.
     * @param value
     *            The parameter value.
     * @return The previous value of the parameter.
     */
    public String addParameter(final String name, final String value) {
        return parameters.put(name, value);
    }

    /** @see java.lang.Object#equals(java.lang.Object) */
    @Override
    public boolean equals(Object obj) {
        if(null != obj && obj instanceof Link) {
            return obj.toString().equals(toString());
        }
        return false;
    }

    /** @see java.lang.Object#hashCode() */
    @Override
    public int hashCode() { return toString().hashCode(); }

    /** @see java.lang.Object#toString() */
    @Override
    public String toString() {
        final StringBuffer toString = new StringBuffer(root);
        for(final String context : this.context) {
            toString.append("/").append(encrypt(context));
        }
        Boolean firstKey = Boolean.TRUE;
        for(final String key : parameters.keySet()) {
            if(firstKey) {
                toString.append("?");
                firstKey = Boolean.FALSE;
            }
            else { toString.append("&"); }
            toString.append(encrypt(key)).append("=").append(parameters.get(encrypt(key)));
        }
        return toString.toString();
    }
}
