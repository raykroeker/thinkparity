/*
 * Created On: Sun Oct 22 2006 10:28 PDT
 */
package com.thinkparity.codebase.model.stream;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.thinkparity.codebase.model.session.Environment;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class StreamSession {

    /** The size of the transfer buffer. */
    private Integer bufferSize;

    /** The character set to use. */
    private Charset charset;

    /** Completion percentages for each stream within a session. */
    private final Map<String, Float> completion;

    /** The target <code>Environment</code>. */
    private Environment environment;

    /** The session id <code>String</code>. */
    private String id;

    /**
     * Create StreamSession.
     *
     */
    public StreamSession() {
        super();
        this.completion = new HashMap<String, Float>(50, 1.0F);
    }

    /**
     * Obtain the buffer size.
     * 
     * @return A size <code>Integer</code>.
     */
    public final Integer getBufferSize() {
        return bufferSize;
    }

    /**
     * Obtain the character set.
     * 
     * @return A <code>Charset</code>.
     */
    public final Charset getCharset() {
        return charset;
    }

    public final Map<String, Float> getCompletion() {
        return Collections.unmodifiableMap(completion);
    }

    public final Float getCompletion(final String streamId) {
        return completion.get(streamId);
    }

    public final Environment getEnvironment() {
        return environment;
    }

    public final String getId() {
        return id;
    }

    /**
     * Set the buffer size.
     * 
     * @param bufferSize
     *            A size <code>Integer</code>.
     */
    public final void setBufferSize(final Integer bufferSize) {
        this.bufferSize = bufferSize;
    }

    /**
     * Set the character set.
     * 
     * @param charset
     *            A <code>Charset</code>.
     */
    public final void setCharset(final Charset charset) {
        this.charset = charset;
    }

    public final Float setCompletion(final String streamId, final Float completion) {
        return this.completion.put(streamId, completion);
    }

    public final void setEnvironment(final Environment environment) {
        this.environment = environment;
    }

    public final void setId(final String id) {
        this.id = id;
    }
}