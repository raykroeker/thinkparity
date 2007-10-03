/**
 * Created On: 2-Oct-07 1:47:40 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.system.dialog;

/**
 * @author robert@thinkparity.com
 * @version $Revision$
 */
public interface Invitation {

    /**
     * Obtain the invitation id.
     * 
     * @return An invitation id <code>String</code>.
     */
    String getId();

    /**
     * Obtain the message.
     * 
     * @return A message <code>String</code>.
     */
    String getMessage();

    /**
     * Invoke the invitation's accept action.
     */
    void invokeAccept();

    /**
     * Invoke the invitation's decline action.
     */
    void invokeDecline();
}
