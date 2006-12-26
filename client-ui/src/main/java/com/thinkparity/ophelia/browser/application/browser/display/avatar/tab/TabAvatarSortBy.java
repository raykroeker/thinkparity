/*
 * Created On:  12-Dec-06 7:02:23 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab;

import javax.swing.Action;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface TabAvatarSortBy {

    public Action getAction();

    public String getText();
    
    public SortDirection getDirection();

    public enum SortDirection { ASCENDING, DESCENDING, NONE }
}
