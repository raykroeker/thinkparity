/*
 * Created On: Fri May 19 2006 11:52 PDT
 * $Id$
 */
package com.thinkparity.migrator;

/**
 * Provides an ability to compare the release dates of two releases.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class ReleaseDateComparator {

    /** The release used in the date comparisons. */
    private final Release release;

    /**
     * Create ReleaseDateComparator.
     *
     * @param release.
     *      A release.
     */
    public ReleaseDateComparator(final Release release) {
        this.release = release;
    }

    /**
     * Determine if the release date's comparator came after the provided
     * release.
     *
     * @param release
     *      A release.
     * @return True if the comparator's release came after the release.
     */
    public Boolean isAfter(final Release release) {
        return this.release.getCreatedOn().after(release.getCreatedOn());
    }

    /**
     * Determine if the release date's comparator came before the provided
     * release.
     *
     * @param release
     *      A release.
     * @return True if the comparator's release came before the release.
     */
    public Boolean isBefore(final Release release) {
        return this.release.getCreatedOn().before(release.getCreatedOn());
    }
}
