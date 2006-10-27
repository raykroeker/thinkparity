/*
 * Created On:  26-Oct-06 6:34:25 PM
 */
package com.thinkparity.desdemona.model.stream;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.stream.StreamHeader;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class StreamReader {

    private final Charset charset;

    private final Log4JWrapper logger;

    /** An <code>InputStream</code>. */
    private final InputStream stream;

    /**
     * Create StreamReader.
     *
     */
    StreamReader(final Charset charset, final InputStream stream) {
        super();
        this.charset = charset;
        this.logger = new Log4JWrapper();
        this.stream = stream;
    }

    /**
     * Read the next stream header.
     * 
     * @return A stream header; or null if the end of stream is reached.
     * @throws IOException
     */
    StreamHeader readNextHeader() throws IOException {
        int nextByte;
        final StringBuffer headerBuffer = new StringBuffer();
        while (-1 != (nextByte = read())) {
            final char c = new String(new byte[] { (byte) nextByte },
                    charset.name()).charAt(0);
            headerBuffer.append(c);
            if (';' == c) {
                /*
                 * here we have just completed what looks like a
                 * header declaration - we analyze the header and
                 * take appropriate action
                 */
                logger.logVariable("header", headerBuffer);
                return StreamHeader.valueOf(headerBuffer.toString());
            }
        }
        return null;
    }

    private final int read() throws IOException {
        return stream.read();
    }
}
