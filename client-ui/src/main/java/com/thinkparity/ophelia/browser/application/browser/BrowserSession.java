/*
 * Created On:  6-Dec-06 8:28:54 AM
 */
package com.thinkparity.ophelia.browser.application.browser;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface BrowserSession {

    /**
     * Obtain a session attribute.
     * 
     * @param name
     *            An attribute name <code>String</code>.
     * @return An attribute value <code>Object</code>.
     */
    public Object getAttribute(final String name);

    /**
     * Obtain the attribute names.
     * 
     * @return An attibute name <code>Iterable</code>.
     */
    public Iterable<String> getAttributeNames();

    /**
     * Obtain the session's time of inception.
     * 
     * @return A <code>Long</code> representing the number of milliseconds
     *         since the session's creation.
     */
    public Long getInception();

    /**
     * Remove an attribute.
     * 
     * @param name
     *            An attribute name <code>String</code>.
     */
    public void removeAttribute(final String name);

    /**
     * Set a session attribute.
     * 
     * @param name
     *            An attribute name <code>String</code>.
     * @param value
     *            An attribute value <code>Object</code>.
     */
    public void setAttribute(final String name, final Object value);
}
