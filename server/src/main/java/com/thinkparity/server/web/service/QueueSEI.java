/*
 * Created On:  3-Jun-07 2:56:39 PM
 */
package com.thinkparity.desdemona.web.service;

import java.util.List;

import javax.jws.WebService;

import com.thinkparity.codebase.model.queue.notification.NotificationSession;
import com.thinkparity.codebase.model.util.xmpp.event.XMPPEvent;

import com.thinkparity.desdemona.model.queue.QueueModel;

import com.thinkparity.service.AuthToken;
import com.thinkparity.service.QueueService;

/**
 * <b>Title:</b>thinkParity Desdemona Queue Service Endpoint Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
@WebService(endpointInterface = "com.thinkparity.service.QueueService")
public class QueueSEI extends ServiceSEI implements QueueService {

    /**
     * Create QueueSEI.
     *
     */
    public QueueSEI() {
        super();
    }

    /**
     * @see com.thinkparity.service.QueueService#createNotificationSession(com.thinkparity.service.AuthToken)
     *
     */
    public NotificationSession createNotificationSession(final AuthToken authToken) {
        return getModel(authToken).createNotificationSession();
    }

    /**
     * @see com.thinkparity.service.QueueService#deleteEvent(com.thinkparity.service.AuthToken, java.lang.String)
     *
     */
    public void deleteEvent(final AuthToken authToken, final XMPPEvent event) {
        getModel(authToken).deleteEvent(event);
    }

    /**
     * @see com.thinkparity.service.QueueService#readEvents(com.thinkparity.service.AuthToken)
     *
     */
    public List<XMPPEvent> readEvents(final AuthToken authToken) {
        return getModel(authToken).readEvents();
    }

    /**
     * @see com.thinkparity.service.QueueService#readSize(com.thinkparity.service.AuthToken)
     *
     */
    public Integer readSize(final AuthToken authToken) {
        return getModel(authToken).readSize();
    }

    /**
     * Obtain a queue model for an authenticated session.
     * 
     * @param authToken
     *            A sesion <code>AuthToken</code>.
     * @return An instance of <code>QueueModel</code>.
     */
    private QueueModel getModel(final AuthToken authToken) {
        return getModelFactory(authToken).getQueueModel();
    }
}
