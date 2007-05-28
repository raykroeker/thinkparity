/*
 * Created On:  24-May-07 5:06:17 PM
 */
package com.thinkparity.ophelia.model.help;


/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class HelpContent {

    private String content;

    private Long id;

    /**
     * Create HelpTopic.
     *
     */
    public HelpContent() {
        super();
    }


    /**
     * Obtain content.
     *
     * @return A String.
     */
    public String getContent() {
        return content;
    }


    /**
     * Obtain id.
     *
     * @return A Long.
     */
    public Long getId() {
        return id;
    }

    /**
     * Set content.
     *
     * @param content
     *		A String.
     */
    public void setContent(final String content) {
        this.content = content;
    }

    /**
     * Set id.
     *
     * @param id
     *		A Long.
     */
    public void setId(final Long id) {
        this.id = id;
    }
}
