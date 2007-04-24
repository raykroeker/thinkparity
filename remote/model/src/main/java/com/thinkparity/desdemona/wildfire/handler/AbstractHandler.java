/*
 * Created On: May 10, 2006 2:38:59 PM
 */
package com.thinkparity.desdemona.wildfire.handler;

import java.util.HashMap;
import java.util.Map;

import com.thinkparity.codebase.StringUtil;
import com.thinkparity.codebase.Constants.Xml;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.desdemona.model.ModelFactory;
import com.thinkparity.desdemona.model.artifact.ArtifactModel;
import com.thinkparity.desdemona.model.backup.BackupModel;
import com.thinkparity.desdemona.model.contact.ContactModel;
import com.thinkparity.desdemona.model.container.ContainerModel;
import com.thinkparity.desdemona.model.migrator.MigratorModel;
import com.thinkparity.desdemona.model.profile.ProfileModel;
import com.thinkparity.desdemona.model.queue.QueueModel;
import com.thinkparity.desdemona.model.rules.RulesModel;
import com.thinkparity.desdemona.model.session.Session;
import com.thinkparity.desdemona.model.stream.StreamModel;
import com.thinkparity.desdemona.model.user.UserModel;
import com.thinkparity.desdemona.util.service.ServiceModelProvider;
import com.thinkparity.desdemona.util.service.ServiceRequestReader;
import com.thinkparity.desdemona.util.service.ServiceResponseWriter;
import com.thinkparity.desdemona.util.xmpp.IQReader;
import com.thinkparity.desdemona.util.xmpp.IQWriter;
import com.thinkparity.desdemona.wildfire.util.SessionUtil;

import org.jivesoftware.wildfire.IQHandlerInfo;
import org.jivesoftware.wildfire.auth.UnauthorizedException;
import org.jivesoftware.wildfire.container.PluginClassLoader;
import org.xmpp.packet.IQ;
import org.xmpp.packet.PacketError;

/**
 * An abstraction of the use-case controller from the MVC paradigm. The IQ
 * controller is a stateless class that handles actions from the IQDispatcher.
 * All state is convened in the IQ as well as the Session.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.8
 */
public abstract class AbstractHandler extends
		org.jivesoftware.wildfire.handler.IQHandler {

    /** An apache iq logger. */
    private static final Log4JWrapper IQ_LOGGER;

    /** A map of service locks. */
    private static final Map<Object, Object> SERVICE_LOCKS;

    /** thinkParity session utilities. */
	private static final SessionUtil SESSION_UTIL;

    static {
        IQ_LOGGER = new Log4JWrapper("DESDEMONA_XMPP_DEBUGGER");
        SERVICE_LOCKS = new HashMap<Object, Object>();
        SESSION_UTIL = SessionUtil.getInstance();
    }

    /** An apache logger wrapper. */
    protected final Log4JWrapper logger;

    /** The info. */
	private final IQHandlerInfo info;

	/** A <code>PluginClassLoader</code>. */
    private PluginClassLoader pluginClassLoader;

    /**
     * Create AbstractHandler2.
     * 
     * @param service
     *            The service <code>String</code> this handler can provide.
     */
	protected AbstractHandler(final String service) {
		super(service);
		this.info = new IQHandlerInfo(Xml.NAME, Xml.NAMESPACE + ":" + service);
        this.logger = new Log4JWrapper(getClass());
	}

    /**
     * @see org.jivesoftware.wildfire.handler.IQHandler#getInfo()
     * 
     */
	public IQHandlerInfo getInfo() {
        return info;
	}
    
    /**
     * Obtain the plugin class loader.
     *
     * @return A <code>PluginClassLoader</code>.
     */
    public PluginClassLoader getPluginClassLoader() {
        return pluginClassLoader;
    }

    /**
     * @see org.jivesoftware.wildfire.handler.IQHandler#handleIQ(org.xmpp.packet.IQ)
     * 
	 */
    public IQ handleIQ(final IQ iq) throws UnauthorizedException {
        Thread.currentThread().setContextClassLoader(pluginClassLoader.getClassLoader());

        final ServiceRequestReader reader = new IQReader(iq);
        final IQ response = createResponse(iq);
        final ServiceResponseWriter writer = new IQWriter(response);
        final Session session = SESSION_UTIL.lookupSession(iq);
        final ModelFactory modelFactory = ModelFactory.getInstance(session);
        try {
            synchronized (getServiceLock(session, reader)) {
                IQ_LOGGER.logVariable("iq", iq);
                IQ_LOGGER.logVariable("iq length", iq.getChildElement().asXML().length());
                service(new ServiceModelProvider() {
                    public ArtifactModel getArtifactModel() {
                        return ArtifactModel.getModel(session);
                    }
                    public BackupModel getBackupModel() {
                        return modelFactory.getBackupModel();
                    }
                    public ContactModel getContactModel() {
                        return modelFactory.getContactModel();
                    }
                    public ContainerModel getContainerModel() {
                        return modelFactory.getContainerModel();
                    }
                    public MigratorModel getMigratorModel() {
                        return modelFactory.getMigratorModel();
                    }
                    public ProfileModel getProfileModel() {
                        return modelFactory.getProfileModel();
                    }
                    public QueueModel getQueueModel() {
                        return QueueModel.getModel(session);
                    }
                    public RulesModel getRulesModel() {
                        return RulesModel.getModel(session);
                    }
                    public StreamModel getStreamModel() {
                        return StreamModel.getModel(session);
                    }
                    public UserModel getUserModel() {
                        return UserModel.getModel(session);
                    }
                }, reader, writer);
            }
        } catch (final Throwable t) {
            logger.logFatal(t, "A non-recoverable error has occured trying to service {0} for {1}.",
                    info.getNamespace(), session.getJabberId().getUsername());
            return createErrorResponse(iq, t);
        }
        IQ_LOGGER.logVariable("response", response);
        IQ_LOGGER.logVariable("response length", response.getChildElement().asXML().length());
        return response;
    }

    /**
     * Set the plugin class loader.
     * 
     * @param pluginClassLoader
     *            A <code>PluginClassLoader</code>.
     */
    public void setPluginClassLoader(final PluginClassLoader pluginClassLoader) {
        this.pluginClassLoader = pluginClassLoader;
    }

	/**
     * Provides a thinkParity service.
     * 
     * @param provider
     *            A thinkParity <code>ServiceModelProvider</code>.
     * @param reader
     *            A thinkParity <code>ServiceRequestReader</code>.
     * @param writer
     *            A thinkParity <code>ServiceResponseWriter</code>.
     */
    protected abstract void service(final ServiceModelProvider provider,
            final ServiceRequestReader reader,
            final ServiceResponseWriter writer);

    /**
     * Create an error response for the query, for the error.
     * 
     * @param iq
     *            The internet query.
     * @param x
     *            The error.
     * @return The error response.
     */
    private IQ createErrorResponse(final IQ iq, final Throwable t) {
        final IQ errorResult = IQ.createResultIQ(iq);

        final PacketError packetError = new PacketError(
                PacketError.Condition.internal_server_error,
                PacketError.Type.cancel, StringUtil.printStackTrace(t));

		errorResult.setError(packetError);
        return errorResult;
    }

    /**
     * Create a response for the query.
     * 
     * @param iq
     *            The xmpp internet query <code>IQ</code>.
     * @return An xmpp internet query response <code>IQ</code>.
     */
    private IQ createResponse(final IQ iq) {
        final IQ response = IQ.createResultIQ(iq);
        response.setChildElement(info.getName(), Xml.RESPONSE_NAMESPACE);
        return response;
    }

    /**
     * Obtain a service synchronization lock.
     * 
     * @param session
     *            A thinkParity <code>Session</code>.
     * @return A service synchronization lock.
     */
    private Object getServiceLock(final Session session,
            final ServiceRequestReader reader) {
        if (SERVICE_LOCKS.containsKey(session.getJabberId())) {
            return SERVICE_LOCKS.get(session.getJabberId());
        } else {
            SERVICE_LOCKS.put(session.getJabberId(), new ServiceLock(session.getJabberId()));
            return getServiceLock(session, reader);
        }
    }

    /**
     * <b>Title:</b>thinkParity Service Lock<br>
     * <b>Description:</b>Provides a per-session synchronization lock for xmpp
     * requests.<br>
     */
    private class ServiceLock {
        /** A user id <code>JabberId</code>. */
        private final JabberId userId;
        /**
         * Create ServiceLock.
         * 
         * @param userId
         *            A user id <code>JabberId</code>.
         */
        private ServiceLock(final JabberId userId) {
            super();
            this.userId = userId;
        }
        /**
         * @see java.lang.Object#equals(java.lang.Object)
         *
         */
        @Override
        public boolean equals(Object obj) {
            if (null == obj)
                return false;
            if (this == obj)
                return true;
            if (obj.getClass().equals(getClass()))
                return userId.equals(((ServiceLock) obj).userId);
            return false;
        }
        /**
         * @see java.lang.Object#hashCode()
         *
         */
        @Override
        public int hashCode() {
            return userId.hashCode();
        }
    }
}
