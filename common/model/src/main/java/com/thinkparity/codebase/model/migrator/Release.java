/*
 * Created On: May 9, 2006
 * $Id$
 */
package com.thinkparity.codebase.model.migrator;

import java.util.Calendar;

import com.thinkparity.codebase.OS;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class Release implements Comparable<Release> {

    public static final Release THIS;

    static {
        THIS = new Release();
    }

    /** A checksum <code>String</code>. */
    private String checksum;

    /** A date <code>Calendar</code>. */
    private Calendar date;

    /** A release id <code>Long</code>. */
    private Long id;

    /** A name <code>String</code>. */
    private String name;

    /** An <code>OS</code>. */
    private OS os;

    /** A product name <code>String</code>. */
    private Product product;

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
        if (null == obj)
            return false;
        if (this == obj)
            return true;
        if (!getClass().equals(obj.getClass()))
            return false;
        final Release objRelease = (Release) obj;
        return objRelease.name.equals(name) && objRelease.os.equals(os)
                && objRelease.product.equals(product);
    }

    /**
     * Obtain checksum.
     *
     * @return A String.
     */
    public String getChecksum() {
        return checksum;
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
     * Obtain product.
     *
     * @return A Product.
     */
    public Product getProduct() {
        return product;
    }

    /**
     * @see java.lang.Object#hashCode()
     *
     */
    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((name == null) ? 0 : name.hashCode());
        result = PRIME * result + ((os == null) ? 0 : os.hashCode());
        result = PRIME * result + ((product == null) ? 0 : product.hashCode());
        return result;
    }

    /**
     * Set checksum.
     *
     * @param checksum
     *		A String.
     */
    public void setChecksum(final String checksum) {
        this.checksum = checksum;
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

    /**
     * Set product.
     *
     * @param product
     *		A Product.
     */
    public void setProduct(final Product product) {
        this.product = product;
    }
}
