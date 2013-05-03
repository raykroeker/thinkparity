/*
 * Created On:  8-Sep-07 4:43:14 PM
 */
package com.thinkparity.desdemona.model;

import java.nio.ByteBuffer;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class Buffer {

    /** A buffer array. */
    private byte[] array;

    /** A direct allocation byte buffer. */
    private ByteBuffer direct;

    /** A buffer lock. */
    private final Object lock;

    /**
     * Create Buffer.
     *
     */
    public Buffer() {
        super();
        this.lock = new Object();
    }

    /**
     * Obtain the array buffer.
     * 
     * @return A <code>byte[]</code>.
     */
    byte[] getArray() {
        if (null == array) {
            // BUFFER - Buffer#getArray() - 2MB
            array = new byte[getSize()];
        }
        return array;
    }

    /**
     * Obtain a direct allocation byte buffer.
     * 
     * @return A <code>ByteBuffer</code>.
     */
    ByteBuffer getDirect() {
        if (null == direct) {
            // BUFFER - Buffer#getDirect() - 2MB
            direct = ByteBuffer.allocateDirect(getSize());
        }
        return direct;
    }

    /**
     * Obtain the buffer synchronization lock.
     * 
     * @return An <code>Object</code>.
     */
    Object getLock() {
        return lock;
    }

    /**
     * Obtain the size of the buffer.
     * 
     * @return An <code>int</code>.
     */
    private int getSize() {
        // BUFFER - Buffer#getSize() - 2MB
        return 1024 * 1024 * 2;
    }
}
