/*
 * Created On:  17-Nov-06 10:07:38 AM
 */
package com.thinkparity.codebase.sort;

import java.util.Comparator;

import com.thinkparity.codebase.OSUtil;
import com.thinkparity.codebase.Platform;
import com.thinkparity.codebase.assertion.Assert;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class StringComparator implements Comparator<String> {

    /** An ascending/descending <code>Boolean</code> flag. */
    private final Boolean ascending;

    /** The operating system <code>Platform</code>. */
    private final Platform platform;

    /**
     * Create StringComparator.
     *
     */
    public StringComparator(final Boolean ascending) {
        super();
        this.ascending = ascending;
        this.platform = OSUtil.getOS().getPlatform();
    }

    /**
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     *
     */
    public int compare(final String o1, final String o2) {
        final int compareResult;
        if (null == o1) {
            if (null == o2) {
                compareResult = 0;
            } else {
                compareResult = -1;
            }
        } else {
            if (null == o2) {
                compareResult = 1;
            } else {
                switch (platform) {
                case WIN32:
                    compareResult = o1.toLowerCase().compareTo(o2.toLowerCase());
                    break;
                case LINUX:
                case UNIX:
                    compareResult = o1.compareTo(o2);
                    break;
                default:
                    throw Assert.createUnreachable("Unknown platform.");
                }                
            }
        }
        return compareResult * (ascending ? 1 : -1);
    }
}
