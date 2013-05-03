/*
 * Created On:  30-May-07 10:15:56 AM
 */
package com.thinkparity.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.thinkparity.codebase.model.queue.notification.NotificationSession;
import com.thinkparity.codebase.model.util.xmpp.event.XMPPEvent;

/**
 * <b>Title:</b>thinkParity Queue Service<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
@WebService(name = "Queue")
public interface QueueService {

    @WebMethod
    NotificationSession createNotificationSession(AuthToken authToken);

    @WebMethod
    void deleteEvent(AuthToken authToken, XMPPEvent event);

    @WebMethod
    List<XMPPEvent> readEvents(AuthToken authToken);

    @WebMethod
    Integer readSize(AuthToken authToken);
}
