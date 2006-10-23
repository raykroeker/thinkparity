/*
 * Created On: Sun Oct 22 2006 10:28 PDT
 */
package com.thinkparity.codebase.model.stream;

import java.util.HashMap;
import java.util.Map;

import com.thinkparity.codebase.model.session.Environment;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Session {

    /** Completion percentages for each stream within a session. */
    private final Map<String, Float> completion;

    private Direction direction;

    private Environment environment;

    private String id;

    /**
     * Create Session.
     *
     */
    public Session() {
        super();
        this.completion = new HashMap<String, Float>(50, 1.0F);
    }

    public Float getCompletion(final String streamId) {
        return completion.get(streamId);
    }

    /**
     * Obtain the direction
     *
     * @return The Direction.
     */
    public Direction getDirection() {
        return direction;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public String getId() {
        return id;
    }

    public Float setCompletion(final String streamId, final Float completion) {
        return this.completion.put(streamId, completion);
    }

    /**
     * Set direction.
     *
     * @param direction The Direction.
     */
    public void setDirection(final Direction direction) {
        this.direction = direction;
    }

    public void setEnvironment(final Environment environment) {
        this.environment = environment;
    }

    public void setId(final String id) {
        this.id = id;
    }
}