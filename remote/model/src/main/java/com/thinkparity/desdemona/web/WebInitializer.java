/*
 * Created On:  29-May-07 11:16:19 AM
 */
package com.thinkparity.desdemona.web;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.thinkparity.codebase.PropertiesUtil;
import com.thinkparity.codebase.log4j.Log4JWrapper;
import com.thinkparity.codebase.log4j.or.CalendarRenderer;
import com.thinkparity.codebase.log4j.or.DateRenderer;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.migrator.Product;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.migrator.Resource;
import com.thinkparity.codebase.model.session.InvalidCredentialsException;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.logging.or.*;
import com.thinkparity.codebase.model.util.xmpp.event.XMPPEvent;

import com.thinkparity.desdemona.model.Version;
import com.thinkparity.desdemona.model.backup.BackupService;
import com.thinkparity.desdemona.model.io.IOService;
import com.thinkparity.desdemona.model.io.IOServiceException;
import com.thinkparity.desdemona.model.io.jta.TransactionService;
import com.thinkparity.desdemona.model.migrator.MigratorService;
import com.thinkparity.desdemona.model.node.NodeCredentials;
import com.thinkparity.desdemona.model.node.NodeException;
import com.thinkparity.desdemona.model.node.NodeService;
import com.thinkparity.desdemona.model.profile.payment.Currency;
import com.thinkparity.desdemona.model.profile.payment.PaymentException;
import com.thinkparity.desdemona.model.profile.payment.PaymentPlan;
import com.thinkparity.desdemona.model.profile.payment.PaymentService;
import com.thinkparity.desdemona.model.queue.QueueItem;
import com.thinkparity.desdemona.model.queue.notification.NotificationService;

import com.thinkparity.desdemona.service.persistence.PersistenceService;

import com.thinkparity.desdemona.util.DesdemonaProperties;
import com.thinkparity.desdemona.util.logging.or.CurrencyRenderer;
import com.thinkparity.desdemona.util.logging.or.OperationRenderer;
import com.thinkparity.desdemona.util.logging.or.PaymentPlanRenderer;
import com.thinkparity.desdemona.util.logging.or.QueueItemRenderer;
import com.thinkparity.desdemona.util.logging.or.ServiceRenderer;
import com.thinkparity.desdemona.util.logging.or.ServiceRequestRenderer;
import com.thinkparity.desdemona.web.service.Operation;
import com.thinkparity.desdemona.web.service.Service;
import com.thinkparity.desdemona.web.service.Services;

import org.apache.log4j.PropertyConfigurator;

/**
 * <b>Title:</b>thinkParity Desdemona Web Listener<br>
 * <b>Description:</b>A hook into the servlet context intialization. All
 * thinkParity services are created and initialized.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class WebInitializer implements ServletContextListener {

    /** A log4j wrapper. */
    private final Log4JWrapper logger;

    /** A transaction service. */
    private TransactionService transactionService;

    /**
     * Create WebListener.
     *
     */
    public WebInitializer() {
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
        stopPayment();
        stopBackup();
        stopMigrator();
        stopNotification();
        stopNode();
        stopIO();
        stopTransaction();
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

        // TODO - WebServiceListener#contextInitialized - create logging service
        final Properties loggingProperties = new Properties();
        setRenderer(loggingProperties, Calendar.class, CalendarRenderer.class);
        setRenderer(loggingProperties, Container.class, ContainerRenderer.class);
        setRenderer(loggingProperties, Document.class, DocumentRenderer.class);
        setRenderer(loggingProperties, DocumentVersion.class, DocumentVersionRenderer.class);
        setRenderer(loggingProperties, Date.class, DateRenderer.class);
        setRenderer(loggingProperties, Currency.class, CurrencyRenderer.class);
        setRenderer(loggingProperties, Operation.class, OperationRenderer.class);
        setRenderer(loggingProperties, PaymentPlan.class, PaymentPlanRenderer.class);
        setRenderer(loggingProperties, Product.class, ProductRenderer.class);
        setRenderer(loggingProperties, QueueItem.class, QueueItemRenderer.class);
        setRenderer(loggingProperties, Release.class, ReleaseRenderer.class);
        setRenderer(loggingProperties, Resource.class, ResourceRenderer.class);
        setRenderer(loggingProperties, Service.class, ServiceRenderer.class);
        setRenderer(loggingProperties, ServiceRequest.class, ServiceRequestRenderer.class);
        setRenderer(loggingProperties, User.class, UserRenderer.class);
        setRenderer(loggingProperties, XMPPEvent.class, XMPPEventRenderer.class);
        PropertyConfigurator.configure(loggingProperties);

        // TODO create a configuration service
        logger.logInfo("Loading configuration.");
        final DesdemonaProperties properties = DesdemonaProperties.getInstance();
        setProperty(properties, "thinkparity.amazon.accesskeyid-file", servletContext);
        setProperty(properties, "thinkparity.amazon.secretaccesskey-file", servletContext);
        setProperty(properties, "thinkparity.backup.root", servletContext);
        setProperty(properties, "thinkparity.crypto.providerimpl", servletContext);
        setProperty(properties, "thinkparity.datasource-driver", servletContext);
        setProperty(properties, "thinkparity.datasource-url", servletContext);
        setProperty(properties, "thinkparity.datasource-user", servletContext);
        setProperty(properties, "thinkparity.datasource-password", servletContext);
        setProperty(properties, "thinkparity.environment", servletContext);
        setProperty(properties, "thinkparity.io.factoryimpl", servletContext);
        setProperty(properties, "thinkparity.mail.transport-host", servletContext);
        setProperty(properties, "thinkparity.mail.transport-port", servletContext);
        setProperty(properties, "thinkparity.migrator.logerror.notify", servletContext);
        setProperty(properties, "thinkparity.migrator.logerror.notify.to", servletContext);
        setProperty(properties, "thinkparity.migrator.logerror.notify.cc", servletContext);
        setProperty(properties, "thinkparity.mode", servletContext);
        setProperty(properties, "thinkparity.node.username", servletContext);
        setProperty(properties, "thinkparity.node.password", servletContext);
        setProperty(properties, "thinkparity.product-name", servletContext);
        setProperty(properties, "thinkparity.persistence.derbyarchiverschedule", servletContext);
        setProperty(properties, "thinkparity.payment.plansleep", servletContext);
        setProperty(properties, "thinkparity.payment.invoicesleep", servletContext);
        setProperty(properties, "thinkparity.queue.notification.bind-host", servletContext);
        setProperty(properties, "thinkparity.queue.notification.bind-port", servletContext);
        setProperty(properties, "thinkparity.queue.notification-charset", servletContext);
        setProperty(properties, "thinkparity.queue.notification-host", servletContext);
        setProperty(properties, "thinkparity.queue.notification-port", servletContext);
        setProperty(properties, "thinkparity.release-name", servletContext);
        setProperty(properties, "thinkparity.temp.root", servletContext);
        setProperty(properties, "xmpp.domain", servletContext);
        setSystemProperty(properties, "thinkparity.release-name");
        setSystemProperty(properties, "thinkparity.datasource-driver");
        setSystemProperty(properties, "thinkparity.datasource-url");
        setSystemProperty(properties, "thinkparity.datasource-user");
        setSystemProperty(properties, "thinkparity.datasource-password");
        
        properties.println(System.out);
        final StringBuffer buffer = new StringBuffer();
        PropertiesUtil.print(buffer, System.getProperties());
        System.out.println(buffer);

        startPersistence(properties);
        startTransaction(properties);
        startIO(properties);
        startNode(properties);
        startNotification(properties);
        startMigrator(properties);
        startBackup(properties);
        startPayment(properties);
        startServices(properties);
        logger.logInfo("thinkParity {0} started.", getVersionString());
    }

    /**
     * Fail startup of the application.
     * 
     * @param cause
     *            A <code>Throwable</code>.
     * @param message
     *            A <code>String</code>.
     * @param messageArguments
     *            Optional <code>Object[]</code>.
     */
    private void failStart(final Throwable cause, final String message,
            final Object... messageArguments) {
        logger.logFatal(cause, message, messageArguments);
        System.exit(1);
    }

    /**
     * Obtain a version string.
     * 
     * @return A version <code>String</code>.
     */
    private String getVersionString() {
        return MessageFormat.format("{0} Service - {1} - {2}",
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
        final String value = MessageFormat.format("{0}", renderer.getName());
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
     * Start the io service.
     * 
     * @param properties
     *            A <code>DesdemonaProperties</code>.
     */
    private void startIO(final DesdemonaProperties properties) {
        logger.logTraceId();
        logger.logInfo("Starting io service.");
        try {
            IOService.getInstance().start(properties);
        } catch (final IOServiceException iosx) {
            failStart(iosx, "Cannot start io servce.");
        }
        logger.logInfo("IO service started.");
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
     * Start the node.
     * 
     * @param properties
     *            A <code>DesdemonaProperties</code>.
     */
    private void startNode(final DesdemonaProperties properties) {
        logger.logTraceId();
        logger.logInfo("Starting node service.");
        final NodeCredentials credentials = new NodeCredentials();
        credentials.setUsername(properties.getProperty("thinkparity.node.username"));
        credentials.setPassword(properties.getProperty("thinkparity.node.password"));
        try {
            NodeService.getInstance().start(credentials);
        } catch (final InvalidCredentialsException icx) {
            failStart(icx, "Cannot start node {0}.", credentials.getUsername());
        } catch (final NodeException nx) {
            failStart(nx, "Cannot start node {0}.", credentials.getUsername());
        }
        logger.logInfo("Node service started.");
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
     * Start the payment service.
     * 
     * @param properties
     *            A <code>DesdemonaProperties</code>.
     * @throws PaymentException
     *             if the service cannot be started
     */
    private void startPayment(final DesdemonaProperties properties) {
        logger.logTraceId();
        logger.logInfo("Starting payment service.");
        final PaymentService paymentService = PaymentService.getInstance();
        try {
            paymentService.start(properties);
        } catch (final PaymentException px) {
            failStart(px, "Could not start payment service.");
        }
        logger.logInfo("Payment service started.");
    }

    /**
     * Start the persistence service.
     * 
     * @param properties
     *            A <code>DesdemonaProperties</code>.
     */
    private void startPersistence(final DesdemonaProperties properties) {
        logger.logTraceId();
        logger.logInfo("Starting persistence service.");
        PersistenceService.getInstance().start(properties);
        logger.logInfo("Persistence service started.");
    }

    /**
     * Start the services.
     * 
     * @param properties
     *            A <code>DesdemonaProperties</code>.
     */
    private void startServices(final DesdemonaProperties properties) {
        logger.logTraceId();
        logger.logInfo("Starting services.");
        Services.getInstance().start();
        logger.logInfo("Services started.");
    }

    /**
     * Start the transaction service.
     * 
     * @param properties
     *            A <code>DesdemonaProperties</code>.
     */
    private void startTransaction(final DesdemonaProperties properties) {
        logger.logTraceId();
        logger.logInfo("Starting transaction service.");
        transactionService = TransactionService.getInstance();
        transactionService.start();
        logger.logInfo("Transaction service started.");
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
     * Stop the io service.
     * 
     */
    private void stopIO() {
        logger.logTraceId();
        logger.logInfo("Stopping io service.");
        IOService.getInstance().stop();
        logger.logInfo("IO service stopped.");
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
     * Stop the node.
     * 
     */
    private void stopNode() {
        logger.logTraceId();
        logger.logInfo("Stopping node service.");
        NodeService.getInstance().stop();
        logger.logInfo("Node service stopped.");        
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
     * Stop the payment service.
     * 
     */
    private void stopPayment() {
        logger.logTraceId();
        logger.logInfo("Stopping payment service.");
        final PaymentService paymentService = PaymentService.getInstance();
        paymentService.stop();
        logger.logInfo("Payment service stopped.");
    }

    /**
     * Start the persistence service.
     * 
     */
    private void stopPersistence() {
        logger.logTraceId();
        logger.logInfo("Stopping persistence service.");
        PersistenceService.getInstance().stop();
        logger.logInfo("Persistence service stopped.");
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
     * Stop the transaction service.
     * 
     */
    private void stopTransaction() {
        logger.logTraceId();
        logger.logInfo("Stopping transaction service.");
        try {
            transactionService.stop();
        } finally {
            transactionService = null;
            logger.logInfo("Transaction service stopped.");
        }
    }
}
