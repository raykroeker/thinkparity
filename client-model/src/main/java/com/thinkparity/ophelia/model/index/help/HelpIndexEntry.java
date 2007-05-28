/*
 * Created On:  28-May-07 3:31:50 PM
 */
package com.thinkparity.ophelia.model.index.help;

import com.thinkparity.ophelia.model.help.HelpContent;
import com.thinkparity.ophelia.model.help.HelpTopic;

/**
 * <b>Title:</b>thinkParity OpheliaModel Help Index Entry<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class HelpIndexEntry {

    /** A <code>HelpContent</code>. */
    private HelpContent content;

    /** A <code>HelpTopic</code>. */
    private HelpTopic topic;

    /**
     * Create HelpIndexEntry.
     *
     */
    public HelpIndexEntry() {
        super();
    }

    /**
     * Obtain content.
     *
     * @return A <code>HelpContent</code>.
     */
    public HelpContent getContent() {
        return content;
    }

    /**
     * Obtain topic.
     *
     * @return A <code>HelpTopic</code>.
     */
    public HelpTopic getTopic() {
        return topic;
    }

    /**
     * Set content.
     *
     * @param content
     *		A <code>HelpContent</code>.
     */
    public void setContent(final HelpContent content) {
        this.content = content;
    }

    /**
     * Set topic.
     *
     * @param topic
     *		A <code>HelpTopic</code>.
     */
    public void setTopic(final HelpTopic topic) {
        this.topic = topic;
    }
}
