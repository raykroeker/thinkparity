/*
 * Apr 26, 2006
 */
package com.thinkparity.ophelia.browser.application.system.dialog;

/**
 * <b>Title:</b>thinkParity Ophelia UI System Application Notification<br>
 * <b>Description:</b>A notification can be uniquely identified by an id. It
 * can contain many lines/headings and a title. It is also invokeable.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface Notification {

    /**
     * Obtain the first line of the content.
     * 
     * @return A content <code>String</code>.
     */
    String getContentLine1();

    /**
     * Obtain the second line of the content.
     * 
     * @return A content <code>String</code>.
     */
    String getContentLine2();

    /**
     * Obtain the notification group id.
     * 
     * @return A notification group id <code>String</code>.
     */
    String getGroupId();

    /**
     * Obtain the first line of the heading.
     * 
     * @return A heading <code>String</code>.
     */
    String getHeadingLine1();

    /**
     * Obtain the second line of the heading.
     * 
     * @return A heading <code>String</code>.
     */
    String getHeadingLine2();

    /**
     * Obtain the notification id.
     * 
     * @return A notification id <code>String</code>.
     */
    String getId();

    /**
     * Obtain the title of the notification as a link.
     * 
     * @return A link <code>String</code>.
     */
    String getLinkTitle();

    /**
     * Obtain the number of lines of content.
     * 
     * @return A positive <code>int</code>.
     */
    int getNumberLines();

    /**
     * Obtain the title of the notification a text.
     * 
     * @return A text <code>String</code>.
     */
    String getTextTitle();

    /**
     * Invoke the notification's action.
     *
     */
    void invokeAction();

    /**
     * Determine if the specified id is a matching id.
     * 
     * @param id
     *            An id <code>String</code>.
     * @return true if the specified id is a matching id.
     */
    Boolean isMatchingId(final String id);
}
