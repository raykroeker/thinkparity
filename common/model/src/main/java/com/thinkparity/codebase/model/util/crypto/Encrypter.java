/*
 * Created On:  7-Oct-07 11:48:50 AM
 */
package com.thinkparity.codebase.model.util.crypto;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

/**
 * <b>Title:</b>thinkParity Common Model Encrypter<br>
 * <b>Description:</b>A decrypter interface.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface Encrypter {

    /**
     * Encrypt the clear text into cipher text.
     * 
     * @param clearText
     *            A <code>String</code>.
     * @return A <code>String</code>.
     * @throws IllegalBlockSizeException
     *             if this cipher is a block cipher, no padding has been
     *             requested (only in encryption mode), and the total input
     *             length of the data processed by this cipher is not a multiple
     *             of block size; or if this encryption algorithm is unable to
     *             process the input data provided
     * @throws BadPaddingException
     *             if this cipher is in decryption mode, and (un)padding has
     *             been requested, but the decrypted data is not bounded by the
     *             appropriate padding bytes
     */
    String encrypt(String clearText) throws IllegalBlockSizeException,
            BadPaddingException;
}
