/*
 * Jan 19, 2006
 */
package com.thinkparity.browser.platform;

import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.thinkparity.codebase.NetworkUtil;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.config.ConfigFactory;

import com.thinkparity.browser.Version;
import com.thinkparity.browser.platform.util.model.ModelFactory;
import com.thinkparity.browser.profile.Profile;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class BrowserPlatformInitializer {

    /**
     * Initialize the logging based upon the application's mode.
     * 
     */
    private static void initLogging() {
        final Properties log4j = ConfigFactory.newInstance("log4j.properties");
        switch(Version.getMode()) {
        case DEVELOPMENT:
            if(isNetworkTargetReachable(log4j)) {
                log4j.setProperty("log4j.logger.com.thinkparity", "DEBUG,CONSOLE,FILE,NETWORK");
            } else {
                log4j.setProperty("log4j.logger.com.thinkparity", "DEBUG,CONSOLE,FILE");
            }
            break;
        case PRODUCTION:
            log4j.setProperty("log4j.logger.com.thinkparity", "WARN,FILE");
            break;
        case TESTING:
            if(isNetworkTargetReachable(log4j)) {
                log4j.setProperty("log4j.logger.com.thinkparity", "INFO,FILE,NETWORK");
            } else {
                log4j.setProperty("log4j.logger.com.thinkparity", "INFO,FILE");
            }
            break;
        default:
            Assert.assertUnreachable("");
        }
        LogManager.resetConfiguration();
        PropertyConfigurator.configure(log4j);
        Logger.getLogger(BrowserPlatformInitializer.class).info("PLATFORM INIT");
    }

    /**
     * Determine if the network target for logging is reachable.
     * 
     * @param log4j
     *            The log4j configuration.
     * @return True if the network host is reachable on the given port.
     */
    private static Boolean isNetworkTargetReachable(final Properties log4j) {
        return NetworkUtil.isTargetReachable(
                log4j.getProperty("log4j.appender.NETWORK.RemoteHost"),
                Integer.parseInt(log4j.getProperty("log4j.appender.NETWORK.Port")));
    }

    private static void initParityServerHost() {
        final String serverHost = System.getProperty("parity.serverhost");
        if(null == serverHost) { System.setProperty("parity.serverhost", "thinkparity.dyndns.org"); }
    }

	private static void initParityServerPort() {
        final Integer serverPort= Integer.getInteger("parity.serverport", null);
        if(null == serverPort) { System.setProperty("parity.serverport", "5222"); }
    }

	/** A profile. */
    private final Profile profile;

    /**
     * Create BrowserPlatformInitializer.
     * 
     * @param profile
     *            A browser profile to initialize.
     */
    BrowserPlatformInitializer(final Profile profile) {
        this.profile = profile;
    }

    /**
	 * Initialize the browser2 platform.
	 *
	 */
	void initialize() {
		System.setProperty("parity.insecure", "true");
        initializePropertyParityArchive();
        initializePropertyParityWorkspace();
        initParityServerHost();
        initParityServerPort();
        initLogging();

        // init model
        ModelFactory.getInstance().initialize();

        final Properties properties = System.getProperties();
        for(final Object key : properties.keySet()) {
            Logger.getLogger(BrowserPlatformInitializer.class)
                    .debug(new StringBuffer()
                        .append(key).append(":")
                        .append(properties.get(key)));
        }
	}

    private void initializePropertyParityArchive() {
        System.setProperty("parity.archive.directory", profile.getParityArchive());
    }

    /** Initialize the system property for the parity workspace. */
    private void initializePropertyParityWorkspace() {
        System.setProperty("parity.workspace", profile.getParityWorkspace());
    }
}
