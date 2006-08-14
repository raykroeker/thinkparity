/*
 * Created On: Feb 4, 2006
 * $Id$
 */
package com.thinkparity.browser.platform.application;

import java.lang.reflect.Constructor;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.browser.BrowserException;
import com.thinkparity.browser.platform.Platform;

/**
 * The browser's application factory.
 * 
 * @author raykroeker@gmail.com
 * @version $Revision$
 */
public class ApplicationFactory {

	/** The singleton instance. */
	private static ApplicationFactory SINGLETON;

    /**
     * Obtain an instance of an application factory.
     * 
     * @param platform
     *            A platform.
     * @return An application factory.
     */
	public static ApplicationFactory getInstance(final Platform platform) {
        if (null == SINGLETON) {
            SINGLETON = new ApplicationFactory(platform);
        }
        return SINGLETON;
    }

	/** A thinkParity platform. */
    private final Platform platform;

    /** An application registry. */
	private final ApplicationRegistry registry;

    /**
     * Create ApplicationFactory.
     * 
     * @param platform
     *            An instance of the thinkParity platform.
     */
	private ApplicationFactory(final Platform platform) {
		super();
        this.platform = platform;
		this.registry = new ApplicationRegistry();
	}

    /**
	 * Create an application.
	 * 
	 * @param id
	 *            The application id.
	 * @return The applcation.
	 */
	public Application create(final ApplicationId id) {
		return SINGLETON.doCreate(id);
	}

    /**
     * Create the application specified by the id.
     * 
     * @param id
     *            The application id.
     * @return The application.
     */
	private Application doCreate(final ApplicationId id) {
		final Application application;
        switch(id) {
		case BROWSER:
            application = doCreate(platform, "com.thinkparity.browser.application.browser.Browser");
            break;
        case SESSION:
            application = doCreate(platform, "com.thinkparity.browser.application.session.SessionApplication");
            break;
		case SYSTEM:
            application =  doCreate(platform, "com.thinkparity.browser.application.system.SystemApplication");
            break;
		default:
            throw Assert.createUnreachable("UNKNOWN APPLICATION");
		}
        register(application);
        application.addListener(platform);
        application.setProfile(platform.getModelFactory().getProfileModel(getClass()).read());
        return application;
	}

    /**
     * Create an application.
     * 
     * @param platform
     *            The platform.
     * @param applicationName
     *            The fully qualified application name.
     * @return The application.
     */
    private Application doCreate(final Platform platform,
            final String applicationName) {
        try {
            final Class applicationClass = Class.forName(applicationName);
            final Constructor constructor = applicationClass.getConstructor(new Class[] {Platform.class});
            return (Application) constructor.newInstance(new Object[] {platform});
        }
        catch(final Throwable t) {
            throw new BrowserException("CANNOT CREATE APPLICATION", t);
        }
    }

    /**
     * Register the application.
     * 
     * @param application
     *            An application.
     */
    private void register(final Application application) {
        Assert.assertNotTrue(
                "APPLICATION ALREADY REGISTERED",
                registry.contains(application.getId()));
        registry.put(application);
    }
}
