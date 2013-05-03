/*
 * Created On:  14-Mar-07 10:00:09 AM
 */
package com.thinkparity.ophelia.model.util.sort;

import java.util.Comparator;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ArtifactReceiptReceivedOn implements Comparator<ArtifactReceipt> {

    /** An ascending/descending flag. */
    private final int multiplier;

    /**
     * Create ArtifactReceiptReceivedOn.
     *
     */
    public ArtifactReceiptReceivedOn(final Boolean ascending) {
        super();
        this.multiplier = ascending.booleanValue() ? 1 : -1;
    }

    /**
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     *
     */
    public int compare(final ArtifactReceipt o1, final ArtifactReceipt o2) {
        final int result;
        if (o1.isSetReceivedOn()) {
            if (o2.isSetReceivedOn()) {
                result = o1.getReceivedOn().compareTo(o2.getReceivedOn());
            } else {
                result = -1;
            }
        } else {
            if (o2.isSetReceivedOn()) {
                result = 1;
            } else {
                result = 0;
            }
        }
        return multiplier * result;
    }
}
