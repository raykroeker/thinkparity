/*
 * Created On:  29-May-07 11:16:19 AM
 */
package com.thinkparity.desdemona.web;

import java.text.MessageFormat;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.thinkparity.codebase.PropertiesUtil;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.desdemona.model.Version;
import com.thinkparity.desdemona.model.backup.BackupService;
import com.thinkparity.desdemona.model.migrator.MigratorService;
import com.thinkparity.desdemona.model.queue.notification.NotificationService;
import com.thinkparity.desdemona.model.stream.StreamService;
import com.thinkparity.desdemona.util.DesdemonaProperties;
import com.thinkparity.desdemona.util.logging.or.ServiceRequestRenderer;
import com.thinkparity.desdemona.web.service.Services;
import com.thinkparity.desdemona.wildfire.util.PersistenceManager;

import org.apache.log4j.PropertyConfigurator;

/**
 * <b>Title:</b>thinkParity Desdemona Web Listener<br>
 * <b>Description:</b>A hook into the servlet context intialization. All
 * thinkParity services are created and initialized.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class WebListener implements ServletContextListener {

    /** A <code>Log4JWrapper</code>. */
    private final Log4JWrapper logger;

    /** A reference to the <code>PersistenceManager</code>. */
    private PersistenceManager persistenceService;

    /**
     * Create WebListener.
     *
     */
    public WebListener() {
        super();
        this.logger = new Log4JWrapper();
    }

    /**
     * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
     *
     */
    public void contextDestroyed(final ServletContextEvent e) {
        logger.logTraceId();
        logger.logVariable("e", e);
        logger.logInfo("Stopping thinkParity {0}.", getVersionString());
        stopServices();
        stopBackup();
        stopStream();
        stopMigrator();
        stopNotification();
        stopPersistence();
        logger.logInfo("thinkParity {0} stopped.", getVersionString());
    }

    /**
     * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
     *
     */
    public void contextInitialized(final ServletContextEvent e) {
        logger.logTraceId();
        logger.logVariable("e", e);
        logger.logInfo("Starting thinkParity {0}", getVersionString());
        final ServletContext servletContext = e.getServletContext();

        // TODO - WebListener#contextInitialized - create logging service
        final Properties loggingProperties = new Properties();
        setRenderer(loggingProperties, ServiceRequest.class, ServiceRequestRenderer.class);
        PropertyConfigurator.configure(loggingProperties);

        // TODO create a configuration service
        logger.logInfo("Loading configuration.");
        final DesdemonaProperties properties = DesdemonaProperties.getInstance();
        setProperty(properties, "thinkparity.backup.root", servletContext);
        setProperty(properties, "thinkparity.datasource-driver", servletContext);
        setProperty(properties, "thinkparity.datasource-url", servletContext);
        setProperty(properties, "thinkparity.datasource-user", servletContext);
        setProperty(properties, "thinkparity.datasource-password", servletContext);
        setProperty(properties, "thinkparity.environment", servletContext);
        setProperty(properties, "thinkparity.mail.transport-host", servletContext);
        setProperty(properties, "thinkparity.mail.transport-port", servletContext);
        setProperty(properties, "thinkparity.migrator.logerror.notify", servletContext);
        setProperty(properties, "thinkparity.migrator.logerror.notify.to", servletContext);
        setProperty(properties, "thinkparity.migrator.logerror.notify.cc", servletContext);
        setProperty(properties, "thinkparity.mode", servletContext);
        setProperty(properties, "thinkparity.product-name", servletContext);
        setProperty(properties, "thinkparity.queue.notification.bind-host", servletContext);
        setProperty(properties, "thinkparity.queue.notification-charset", servletContext);
        setProperty(properties, "thinkparity.queue.notification-host", servletContext);
        setProperty(properties, "thinkparity.queue.notification-port", servletContext);
        setProperty(properties, "thinkparity.release-name", servletContext);
        setProperty(properties, "thinkparity.stream.bind-host", servletContext);
        setProperty(properties, "thinkparity.stream.root", servletContext);
        setProperty(properties, "thinkparity.stream-host", servletContext);
        setProperty(properties, "thinkparity.stream-port", servletContext);
        setProperty(properties, "thinkparity.temp.root", servletContext);
        setProperty(properties, "xmpp.domain", servletContext);
        setSystemProperty(properties, "thinkparity.release-name");
        // NOCOMMIT - WebListener#contextInitialized
        properties.println(System.out);
        final StringBuffer buffer = new StringBuffer();
        PropertiesUtil.print(buffer, System.getProperties());
        System.out.println(buffer);

        startPersistence(properties);
        startNotification(properties);
        startMigrator(properties);
        startStream(properties);
        startBackup(properties);
        startServices(properties, servletContext);
        logger.logInfo("thinkParity {0} started.", getVersionString());
    }

    /**
     * Obtain a version string.
     * 
     * @return A version <code>String</code>.
     */
    private String getVersionString() {
        return MessageFormat.format("{0} - {1} - {2}",
                Version.getProductName(), Version.getReleaseName(),
                Version.getBuildId());
    }

    /**
     * Replace any property declarations in the value with the system property.
     * 
     * @param value
     *            A value <code>String</code>.
     * @param key
     *            A key <code>String</code>.
     * @return A value <code>String</code>.
     */
    private String replace(final String value, final String key) {
        final String systemValue = System.getProperty(key);
        return value.replace("${" + key + "}",
                null == systemValue ? "${" + key + "}" : systemValue);
    }

    /**
     * Push the servlet context init param into the properties object.
     * 
     * @param properties
     *            An instance of <code>Properties</code>.
     * @param name
     *            A property name <code>String</code>.
     * @param servletContext
     *            An instance of <code>ServletContext</code>.
     */
    private void setProperty(final Properties properties, final String name,
            final ServletContext servletContext) {
        String value = servletContext.getInitParameter(name);
        value = replace(value, "thinkparity.home");
        properties.setProperty(name, value);
    }

    /**
     * Set a log4j renderer property.
     * 
     * @param properties
     *            A <code>Properties</code>.
     * @param renderable
     *            A <code>Class<?></code> to render.
     * @param renderer
     *            A <code>Class<?></code> that renders.
     */
    private void setRenderer(final Properties properties,
            final Class<?> renderable, final Class<?> renderer) {
        final String key = MessageFormat.format("log4j.renderer.{0}",
                renderable.getName());
        final String value = MessageFormat.format("{1}", renderer.getName());
        properties.setProperty(key, value);
    }

    /**
     * Push the property into the system properties object.
     * 
     * @param properties
     *            An instance of <code>Properties</code>.
     * @param name
     *            A property name <code>String</code>.
     */
    private void setSystemProperty(final Properties properties,
            final String name) {
        System.setProperty(name, properties.getProperty(name));
    }

    /**
     * Start the backup service.
     * 
     * @param servletContext
     *            A <code>ServletContext</code>.
     */
    private void startBackup(final DesdemonaProperties properties) {
        logger.logTraceId();
        logger.logInfo("Starting backup service.");
        BackupService.getInstance().start();
        logger.logInfo("Backup service started.");
    }

    /**
     * 
     * Start the migrator service.
     * 
     * @param servletContext
     *            A <code>ServletContext</code>.
     */
    private void startMigrator(final DesdemonaProperties properties) {
        logger.logTraceId();
        logger.logInfo("Starting migrator service.");
        MigratorService.getInstance().start();
        logger.logInfo("Migrator service started service.");
    }

    /**
     * Start the notification service.
     * 
     * @param properties
     *            An instance of <code>DesdemonaProperties</code>.
     */
    private void startNotification(final DesdemonaProperties properties) {
        logger.logTraceId();
        logger.logInfo("Starting notification service.");
        final NotificationService service = NotificationService.getInstance();
        service.start();
        service.logStatistics();
        logger.logInfo("Notification service started.");
    }

    /**
     * Start the persistence service.
     * 
     * @param servletContext
     *            A <code>ServletContext</code>.
     */
    private void startPersistence(final DesdemonaProperties properties) {
        logger.logTraceId();
        logger.logInfo("Starting persistence service.");
        System.setProperty("thinkparity.datasource-driver", properties.getProperty("thinkparity.datasource-driver"));
        System.setProperty("thinkparity.datasource-url", properties.getProperty("thinkparity.datasource-url"));
        System.setProperty("thinkparity.datasource-user", properties.getProperty("thinkparity.datasource-user"));
        System.setProperty("thinkparity.datasource-password", properties.getProperty("thinkparity.datasource-password"));
        persistenceService = PersistenceManager.getInstance();
        persistenceService.start();
        logger.logInfo("Persistence service started.");
    }

    /**
     * Start the services.
     * 
     * @param properties
     *            A <code>DesdemonaProperties</code>.
     */
    private void startServices(final DesdemonaProperties properties,
            final ServletContext servletContext) {
        logger.logTraceId();
        logger.logInfo("Starting services.");
        Services.getInstance().start();
        logger.logInfo("Services started.");
    }

    /**
     * Start the stream service.
     * 
     * @param servletContext
     *            A <code>ServletContext</code>.
     */
    private void startStream(final DesdemonaProperties properties) {
        logger.logTraceId();
        logger.logInfo("Starting streaming service.");
        StreamService.getInstance().start();
        logger.logInfo("Streaming service started.");
    }

    /**
     * Stop the backup serivce.
     *
     */
    private void stopBackup() {
        logger.logTraceId();
        logger.logInfo("Stopping backup service.");
        BackupService.getInstance().stop();
        logger.logInfo("Backup service stopped.");
    }

    /**
     * Stop the migrator service.
     *
     */
    private void stopMigrator() {
        logger.logTraceId();
        logger.logInfo("Stopping migrator.");
        MigratorService.getInstance().stop();
        logger.logInfo("Migrator stopped.");
    }

    /**
     * Stop the notification service.
     * 
     * @param properties
     *            An instance of <code>DesdemonaProperties</code>.
     */
    private void stopNotification() {
        logger.logTraceId();
        logger.logInfo("Stopping notification service.");
        final NotificationService service = NotificationService.getInstance();
        service.logStatistics();
        service.stop();
        logger.logInfo("Notification service stopped.");
    }

    /**
     * Start the persistence service.
     * 
     */
    private void stopPersistence() {
        logger.logTraceId();
        logger.logInfo("Stopping persistence.");
        try {
            /* TODO refactor persistence service to behave like other services;
             * do not require a local instance */
            persistenceService.stop();
        } finally {
            persistenceService = null;
        }
        logger.logInfo("Persistence stopped.");
    }

    /**
     * Stop the services.
     *
     */
    private void stopServices() {
        logger.logTraceId();
        logger.logInfo("Stopping services.");
        Services.getInstance().stop();
        logger.logInfo("Services stopped.");
    }

    /**
     * Stop the streaming service.
     *
     */
    private void stopStream() {
        logger.logTraceId();
        logger.logInfo("Stopping streaming service.");
        StreamService.getInstance().stop();
        logger.logInfo("Streaming service stopped.");
    }
}
