/*
 * Created On:  29-Aug-07 5:17:49 PM
 */
package com.thinkparity.ophelia.model.help;


/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class HelpTopicMovie {

    private Long id;

    private String title;

    private String url;

    /**
     * Create HelpTopicMovie.
     *
     */
    public HelpTopicMovie() {
        super();
    }

    /**
     * Obtain the id.
     *
     * @return A <code>Long</code>.
     */
    public Long getId() {
        return id;
    }

    /**
     * Obtain the title.
     *
     * @return A <code>String</code>.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Obtain the movie.
     *
     * @return A <code>URL</code>.
     */
    public String getURL() {
        return url;
    }

    /**
     * Set the id.
     *
     * @param id
     *		A <code>Long</code>.
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * Set the title.
     *
     * @param title
     *		A <code>String</code>.
     */
    public void setTitle(final String title) {
        this.title = title;
    }

    /**
     * Set the movie.
     *
     * @param movie
     *		A <code>URL</code>.
     */
    public void setURL(final String url) {
        this.url = url;
    }
}
