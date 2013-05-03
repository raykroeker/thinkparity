/*
 * Created On:  Nov 2, 2007 2:58:55 PM
 */
package com.thinkparity.ophelia.model.queue;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * <b>Title:</b>thinkParity Ophelia Model Queue Processor Map<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class QueueProcessorMap {

    /** A queue processor map. */
    private final Map<Object, QueueProcessor> map;

    /**
     * Create QueueProcessorMap.
     *
     */
    QueueProcessorMap() {
        super();
        this.map = new Hashtable<Object, QueueProcessor>();
    }

    /**
     * Obtain a processor.
     * 
     * @param id
     *            An <code>Object</code>.
     * @return A <code>QueueProcessor</code>.
     */
    QueueProcessor get(final Object id) {
        return map.get(id);
    }

    /**
     * Obtain all of the queue processor ids.
     * 
     * @return A <code>List<Object></code>.
     */
    List<Object> getAllIds() {
        final List<Object> keys = new ArrayList<Object>(map.size());
        keys.addAll(map.keySet());
        return keys;
    }

    /**
     * Determine if the queue processor stack is empty.
     * 
     * @return A <code>Boolean</code>.
     */
    Boolean isEmpty() {
        return Boolean.valueOf(map.isEmpty());
    }

    /**
     * Set the queue processor in the map.
     * 
     * @param queueProcessor
     *            A <code>QueueProcessor</code>.
     * @return An <code>Object</code>.
     */
    Object put(final QueueProcessor queueProcessor) {
        final Object id = new Object();
        map.put(id, queueProcessor);
        return id;
    }

    /**
     * Remove a processor.
     * 
     * @param id
     *            An <code>Object</code>.
     * @return A <code>QueueProcessor</code>.
     */
    QueueProcessor remove(final Object id) {
        return map.remove(id);
    }
}
