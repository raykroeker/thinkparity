/*
 * Created On: May 9, 2006
 * $Id$
 */
package com.thinkparity.codebase.model.migrator;

import java.util.Calendar;

import com.thinkparity.codebase.OS;

/**
 * <b>Title:</b>thinkParity CommonModel Release<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Release implements Comparable<Release> {

    /** A date <code>Calendar</code>. */
    private Calendar date;

    /** A release id <code>Long</code>. */
    private transient Long id;

    /** A name <code>String</code>. */
    private String name;

    /** An <code>OS</code>. */
    private OS os;

    /**
     * Create Release.
     *
     */
    public Release() {
        super();
    }

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     *
     */
    public int compareTo(final Release o) {
        return date.compareTo(o.date);
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     *
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (null == obj)
            return false;
        if (getClass() != obj.getClass())
            return false;
        return ((Release) obj).name.equals(name)
                && ((Release) obj).os.equals(os);
    }

    /**
     * Obtain date.
     *
     * @return A Calendar.
     */
    public Calendar getDate() {
        return date;
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
     * Obtain name.
     *
     * @return A String.
     */
    public String getName() {
        return name;
    }

    /**
     * Obtain os.
     *
     * @return A OS.
     */
    public OS getOs() {
        return os;
    }

    /**
     * @see java.lang.Object#hashCode()
     *
     */
    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + name.hashCode();
        result = PRIME * result + os.hashCode();
        return result;
    }

    /**
     * Set date.
     *
     * @param date
     *		A Calendar.
     */
    public void setDate(final Calendar date) {
        this.date = date;
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
     * Set name.
     *
     * @param name
     *		A String.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Set os.
     *
     * @param os
     *		A OS.
     */
    public void setOs(final OS os) {
        this.os = os;
    }
}
