/*
 * Created On:  24-May-07 5:06:17 PM
 */
package com.thinkparity.ophelia.model.help;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class HelpTopic {

    private Long id;

    private final List<HelpTopicMovie> movies;

    private String name;

    /**
     * Create HelpTopic.
     *
     */
    public HelpTopic() {
        super();
        this.movies = new ArrayList<HelpTopicMovie>();
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
     * @return A <code>List<HelpTopicMovie></code>.
     */
    public List<HelpTopicMovie> getMovies() {
        return movies;
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
    public void setMovies(final List<HelpTopicMovie> movies) {
        this.movies.clear();
        this.movies.addAll(movies);
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
