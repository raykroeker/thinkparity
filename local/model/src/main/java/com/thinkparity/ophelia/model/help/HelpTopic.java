/*
 * Created On:  24-May-07 5:06:17 PM
 */
package com.thinkparity.ophelia.model.help;

import java.net.URL;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class HelpTopic {

    private Long id;

    private URL movie;

    private String name;

    /**
     * Create HelpTopic.
     *
     */
    public HelpTopic() {
        super();
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
     * Obtain movie.
     *
     * @return A URL.
     */
    public URL getMovie() {
        return movie;
    }

    /**
     * Obtain name.
     *
     * @return A String.
     */
    public String getName() {
        return name;
    }

    /**
     * Determine whether or not the movie url is set.
     * 
     * @return True if the movie is set.
     */
    public Boolean isSetMovie() {
        return null != movie;
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


    /**
     * Set movie.
     *
     * @param movie
     *		A URL.
     */
    public void setMovie(final URL movie) {
        this.movie = movie;
    }


    /**
     * Set name.
     *
     * @param name
     *		A String.
     */
    public void setName(final String name) {
        this.name = name;
    }
}
