/*
 * Created On:  15-Nov-07 1:05:43 PM
 */
package com.thinkparity.desdemona.model.admin.message;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.thinkparity.desdemona.model.node.Node;

/**
 * <b>Title:</b>thinkParity Desdemona Admin Model Message<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Message {

    /** An id. */
    private Long id;

    /** Optional long data. */
    private final List<MessageData<Long>> longData;

    /** A message. */
    private String message;

    /** The node. */
    private Node node;

    /** Optional string data. */
    private final List<MessageData<String>> stringData;

    /** A timestamp. */
    private Calendar timestamp;

    /**
     * Create Message.
     *
     */
    Message() {
        super();
        this.stringData = new ArrayList<MessageData<String>>();
        this.longData = new ArrayList<MessageData<Long>>();
    }

    /**
     * Obtain the id.
     *
     * @return A <code>String</code>.
     */
    public Long getId() {
        return id;
    }

    /**
     * Obtain the long data.
     *
     * @return A <code>List<MessageData<Long>></code>.
     */
    public List<MessageData<Long>> getLongData() {
        return longData;
    }

    /**
     * Obtain the synopsis.
     *
     * @return A <code>String</code>.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Obtain the node.
     *
     * @return A <code>Node</code>.
     */
    public Node getNode() {
        return node;
    }

    /**
     * Obtain the string data.
     *
     * @return A <code>List<MessageData<String>></code>.
     */
    public List<MessageData<String>> getStringData() {
        return stringData;
    }

    /**
     * Obtain the timestamp.
     *
     * @return A <code>Date</code>.
     */
    public Calendar getTimestamp() {
        return timestamp;
    }

    /**
     * Determine if the message is set.
     * 
     * @return A <code>Boolean</code>.
     */
    public Boolean isSetData() {
        return !Boolean.valueOf(longData.isEmpty() && stringData.isEmpty());
    }

    /**
     * Set the id.
     *
     * @param id
     *		A <code>String</code>.
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * Set the message long data.
     *
     * @param data
     *      A <code>List<MessageData<Long>></code>.
     */
    public void setLongData(final List<MessageData<Long>> longData) {
        this.longData.clear();
        this.longData.addAll(longData);
    }

    /**
     * Set the message.
     *
     * @param message
     *		A <code>String</code>.
     */
    public void setMessage(final String message) {
        this.message = message;
    }

    /**
     * Set the node.
     *
     * @param node
     *		A <code>Node</code>.
     */
    public void setNode(final Node node) {
        this.node = node;
    }

    /**
     * Set the message string data.
     *
     * @param data
     *      A <code>List<MessageData<String>></code>.
     */
    public void setStringData(final List<MessageData<String>> stringData) {
        this.stringData.clear();
        this.stringData.addAll(stringData);
    }

    /**
     * Set the timestamp.
     *
     * @param timestamp
     *		A <code>Date</code>.
     */
    public void setTimestamp(final Calendar timestamp) {
        this.timestamp = timestamp;
    }
}
