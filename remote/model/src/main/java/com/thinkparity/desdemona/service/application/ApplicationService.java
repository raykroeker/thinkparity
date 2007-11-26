/*
 * Created On:  15-Nov-07 1:39:16 PM
 */
package com.thinkparity.desdemona.service.application;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import com.thinkparity.codebase.OSUtil;
import com.thinkparity.codebase.StringUtil;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.Context;
import com.thinkparity.codebase.model.migrator.Product;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.desdemona.model.Constants;
import com.thinkparity.desdemona.model.InternalModelFactory;
import com.thinkparity.desdemona.model.ModelFactory;
import com.thinkparity.desdemona.model.admin.AdminModelFactory;
import com.thinkparity.desdemona.model.admin.InternalAdminModelFactory;
import com.thinkparity.desdemona.model.migrator.MigratorModel;
import com.thinkparity.desdemona.model.user.UserModel;

import com.thinkparity.desdemona.service.Service;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

/**
 * <b>Title:</b>thinkParity Desdemona Application Service<br>
 * <b>Description:</b>A service used to obtain user/model references for the
 * internal application.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ApplicationService extends Service {

    /** A singleton instance. */
    private static final ApplicationService SINGLETON;

    static {
        SINGLETON = new ApplicationService();
    }

    /**
     * Obtain an instance of the application service.
     * 
     * @return An <code>ApplicationService</code>.
     */
    public static ApplicationService getInstance() {
        return SINGLETON;
    }

    /**
     * Tokenize a property value into a string list.
     * 
     * @param properties
     *            A set of <code>Properties</code>.
     * @param key
     *            A <code>String</code>.
     * @param delimiter
     *            A <code>String</code>.
     * @param defaultValue
     *            A <code>String</code>.
     * @return A <code>List<String></code>.
     */
    private static List<String> tokenize(final Properties properties,
            final String key, final String delimiter, final String defaultValue) {
        final String value = properties.getProperty(key, defaultValue);
        return StringUtil.tokenize(value, delimiter, new ArrayList<String>());
    }

    /** An application internal admin model factory. */
    private AdminModelFactory applicationAdminModelFactory;

    /** An application internal admin model factory. */
    private InternalAdminModelFactory applicationInternalAdminModelFactory;

    /** An application internal model factory. */
    private InternalModelFactory applicationInternalModelFactory;

    /** An application model factory. */
    private ModelFactory applicationModelFactory;

    /** An application user. */
    private User applicationUser;

    /** A logger. */
    private final Log4JWrapper logger;

    /** A quartz scheduler. */
    private Scheduler scheduler;

    /** The server latest release. */
    private Release serverLatestRelease;

    /** The server product. */
    private Product serverProduct;

    /** A started flag. */
    private boolean started;

    /** A stopped flag. */
    private boolean stopped;

    /**
     * Create ConstantService.
     *
     */
    private ApplicationService() {
        super();
        this.logger = new Log4JWrapper(getClass());
    }

    /**
     * Obtain the applicationAdminModelFactory.
     *
     * @return A <code>AdminModelFactory</code>.
     */
    public AdminModelFactory getAdminModelFactory() {
        return applicationAdminModelFactory;
    }

    /**
     * Obtain the applicationInternalAdminModelFactory.
     *
     * @return A <code>InternalAdminModelFactory</code>.
     */
    public InternalAdminModelFactory getInternalAdminModelFactory() {
        return applicationInternalAdminModelFactory;
    }

    /**
     * Obtain the applicationInternalModelFactory.
     *
     * @return A <code>InternalModelFactory</code>.
     */
    public InternalModelFactory getInternalModelFactory() {
        return applicationInternalModelFactory;
    }

    /**
     * Obtain the applicationModelFactory.
     *
     * @return A <code>ModelFactory</code>.
     */
    public ModelFactory getModelFactory() {
        return applicationModelFactory;
    }

    /**
     * Obtain the serverLatestRelease.
     *
     * @return A <code>Release</code>.
     */
    public Release getServerLatestRelease() {
        return serverLatestRelease;
    }

    /**
     * Obtain the serverProduct.
     *
     * @return A <code>Product</code>.
     */
    public Product getServerProduct() {
        return serverProduct;
    }

    /**
     * Obtain the applicationUser.
     *
     * @return A <code>User</code>.
     */
    public User getUser() {
        return applicationUser;
    }

    /**
     * Start the application service.
     * 
     * @param properties
     *            A set of <code>Properties</code>.
     * @throws IOException
     *             if the scheduler configuration cannot be loaded
     * @throws ParseException
     *             if the scheduler's schedule cannot be parsed
     * @throws SchedulerException
     *             if the schdule cannot be set
     * @throws ClassNotFoundException
     *             if the job cannot be found
     */
    public void start(final Properties properties) throws IOException,
            ParseException, SchedulerException, ClassNotFoundException {
        if (true == started) {
            throw new IllegalStateException("Cannot start.  Constant service already started.");
        }
        if (true == stopped) {
            throw new IllegalStateException("Cannot start.  Constant service already stopped.");
        }

        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        final UserModel userModel = ModelFactory.getInstance(loader).getUserModel();
        /* reference application user */
        applicationUser = userModel.read(User.THINKPARITY.getId());
        /* instantiate application model factories */
        applicationModelFactory = ModelFactory.getInstance(applicationUser, loader);
        applicationAdminModelFactory = AdminModelFactory.getInstance(applicationUser, loader);
        /* instantiate internal application model factories */
        final Context applicationContext = new Context();
        applicationInternalModelFactory = InternalModelFactory.getInstance(applicationContext, applicationUser);
        applicationInternalAdminModelFactory = InternalAdminModelFactory.getInstance(applicationContext, applicationUser);
        /* reference migrator's product/release */
        final String serverProductName = Constants.Product.Desdemona.PRODUCT_NAME;
        final MigratorModel migratorModel = applicationModelFactory.getMigratorModel();
        serverProduct = migratorModel.readProduct(serverProductName);
        serverLatestRelease = migratorModel.readLatestRelease(serverProductName, OSUtil.getOS());
        /* create a quartz scheduler */
        startScheduler(properties);
 
        started = true;
    }

    /**
     * Stop the constant service.
     * 
     */
    public void stop() throws SchedulerException {
        if (false == started) {
            throw new IllegalStateException("Cannot stop.  Constant service is not started.");
        }
        if (true == stopped) {
            throw new IllegalStateException("Cannot stop.  Constant service already stopped.");
        }
        try {
            scheduler.shutdown();
        } finally {
            applicationUser = null;
            applicationModelFactory = null;
            applicationAdminModelFactory = null;
            applicationInternalModelFactory = null;
            applicationInternalAdminModelFactory = null;
            serverProduct = null;
            serverLatestRelease = null;
            scheduler = null;
            stopped = true;
        }
    }

    /**
     * Instantiate a quartz scheduler.
     * 
     * @param properties
     *            A set of <code>Properties</code>.
     * @return A <code>Scheduler</code>.
     * @throws IOException
     * @throws SchedulerException
     */
    private Scheduler newScheduler(final Properties properties)
            throws IOException, SchedulerException {
        return new StdSchedulerFactory(
                newSchedulerConfiguration(properties)).getScheduler();
    }

    /**
     * Instantiate configuration for the scheduler.
     * 
     * @param properties
     *            A set of <code>Properties</code> containing the location of
     *            the scheduler's configuration as
     *            "thinkparity.scheduler.configuration".
     * @return A set <code>Properties</code>.
     * @throws IOException
     */
    private Properties newSchedulerConfiguration(final Properties properties)
            throws IOException {
        final String configurationPath = properties.getProperty("thinkparity.scheduler.configurationfile");
        final Properties configuration = new Properties();
        final InputStream inputStream = new FileInputStream(configurationPath);
        try {
            configuration.load(inputStream);
        } finally {
            inputStream.close();
        }
        return configuration;
    }

    /**
     * Schedule all configured jobs.
     * 
     * @throws ParseException
     *             if the schedule expression cannot be parsed
     * @throws ClassNotFoundException
     *             if the scheduled tasks' class cannot be found
     * @throws ScheduleException
     *             if the job cannot be scheduled
     */
    private void scheduleJobs(final Properties properties)
            throws ParseException, ClassNotFoundException, SchedulerException {
        final String separator = properties.getProperty("thinkparity.scheduler.joblist.separator", ",");
        final List<String> scheduledJobList = tokenize(properties, "thinkparity.scheduler.joblist", separator, "");
        String name, group, className, cron;
        Class<?> clasz;
        JobDetail jobDetail;
        Trigger trigger;
        Date date;
        for (final String scheduledJob : scheduledJobList) {
            name = properties.getProperty(MessageFormat.format("thinkparity.scheduler.job.{0}.name", scheduledJob));
            cron = properties.getProperty(MessageFormat.format("thinkparity.scheduler.job.{0}.cron", scheduledJob));
            group = properties.getProperty(MessageFormat.format("thinkparity.scheduler.job.{0}.group", scheduledJob));
            className = properties.getProperty(MessageFormat.format("thinkparity.scheduler.job.{0}.classname", scheduledJob));
            clasz = Class.forName(className);

            jobDetail = new JobDetail(name, group, clasz);
            trigger = new CronTrigger(
                    MessageFormat.format("{0}_CronTrigger", name),
                    MessageFormat.format("{0}_CronTrigger", group),
                    cron);
            date = scheduler.scheduleJob(jobDetail, trigger);
            logger.logInfo("Job {0} scheduled to run on:  {1}",
                    jobDetail.getName(), date);
        }
    }

    /**
     * Start the scheduler. Start a quartz scheduler and schedule all configured
     * jobs.
     * 
     * @param properties
     *            A set of <code>Properties</code>.
     * @throws IOException
     *             if the scheduler configuration cannot be loaded
     * @throws ParseException
     *             if the schedule expression cannot be parsed
     * @throws ClassNotFoundException
     *             if the scheduled tasks' class cannot be found
     * @throws ScheduleException
     *             if the job cannot be scheduled
     */
    private void startScheduler(final Properties properties)
            throws IOException, ParseException, SchedulerException,
            ClassNotFoundException {
        scheduler = newScheduler(properties);
        scheduleJobs(properties);
        scheduler.start();
    }
}
