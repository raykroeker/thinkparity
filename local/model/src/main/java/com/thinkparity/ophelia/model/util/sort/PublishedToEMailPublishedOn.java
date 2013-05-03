/*
 * Created On:  9-Sep-07 8:01:20 PM
 */
package com.thinkparity.ophelia.model.util.sort;

import java.util.Comparator;

import com.thinkparity.codebase.model.artifact.PublishedToEMail;

/**
 * <b>Title:</b>thinkParity Published To E-Mail Published On Comparator<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class PublishedToEMailPublishedOn implements
        Comparator<PublishedToEMail> {

    /** An ascending flag. */
    private final int multiplier;

    /**
     * Create PublishedToEMailPublishedOn.
     *
     */
    public PublishedToEMailPublishedOn(final Boolean ascending) {
        super();
        this.multiplier = ascending.booleanValue() ? 1 : -1;
    }

    /**
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     *
     */
    @Override
    public int compare(final PublishedToEMail o1, final PublishedToEMail o2) {
        return multiplier * o1.getPublishedOn().compareTo(o2.getPublishedOn());
    }
}
