/*
 * Created On: Feb 4, 2006
 * $Id$
 */
package com.thinkparity.browser.platform.application;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.thinkparity.browser.BrowserException;
import com.thinkparity.browser.platform.Platform;

import com.thinkparity.codebase.assertion.Assert;

/**
 * The browser's application factory.
 * 
 * @author raykroeker@gmail.com
 * @version $Revision$
 */
public class ApplicationFactory {

	/** The singleton application factory. */
	private static final ApplicationFactory SINGLETON;

	static { SINGLETON = new ApplicationFactory(); }

	/**
	 * Create an application.
	 * 
	 * @param id
	 *            The application id.
	 * @return The applcation.
	 */
	public static Application create(final Platform platform,
			final ApplicationId id) {
		return SINGLETON.doCreate(platform, id);
	}

	private static final StringBuffer getApiId(final String api) {
        return getLogId().append(" ").append(api);
    }

	private static final String getAssertionId(final String api,
            final String assertion) {
        return getApiId(api).append(" ").append(assertion).toString();
    }

	private static final String getErrorId(final String error) {
        return getLogId().append(" ").append(error).toString();
    }

    private static final StringBuffer getLogId() {
        return new StringBuffer("[LBROWSER] [PLATFORM] [APP FACTORY]");
    }

    /** The application registry. */
	private final ApplicationRegistry registry;

    /** Create ApplicationFactory. */
	private ApplicationFactory() {
		super();
		this.registry = new ApplicationRegistry();
	}

    /**
     * Create the application specified by the id.
     * 
     * @param id
     *            The application id.
     * @return The application.
     */
	private Application doCreate(final Platform platform, final ApplicationId id) {
		switch(id) {
		case BROWSER2: return doCreate(platform, "com.thinkparity.browser.application.browser.Browser");
        case SESSION: return doCreate(platform, "com.thinkparity.browser.application.session.SessionApplication");
		case SYS_APP: return doCreate(platform, "com.thinkparity.browser.application.system.SystemApplication");
		default: throw Assert.createUnreachable("Unknown application:  " + id);
		}
	}

    /**
     * Create an application; register it and add the platform as a listener.
     * 
     * @param platform
     *            The platform.
     * @param applicationName
     *            The fully qualified application name.
     * @return The application.
     */
    private Application doCreate(final Platform platform,
            final String applicationName) {
        Application application = null;
        try {
            final Class applicationClass = Class.forName(applicationName);
            final Constructor constructor = applicationClass.getConstructor(new Class[] {Platform.class});
            application = (Application) constructor.newInstance(new Object[] {platform});
        }
        catch(final ClassNotFoundException cnfx) {
            throw new BrowserException(
                    getErrorId("[DO CREATE] [CANNOT CREATE APPLICATION]"), cnfx);
        }
        catch(final IllegalAccessException iax) {
            throw new BrowserException(
                    getErrorId("[DO CREATE] [CANNOT CREATE APPLICATION]"), iax);
        }
        catch(final InstantiationException ix) {
            throw new BrowserException(
                    getErrorId("[DO CREATE] [CANNOT CREATE APPLICATION]"), ix);
        }
        catch(final InvocationTargetException itx) {
            throw new BrowserException(
                    getErrorId("[DO CREATE] [CANNOT CREATE APPLICATION]"), itx);
        }
        catch(final NoSuchMethodException nsmx) {
            throw new BrowserException(
                    getErrorId("[DO CREATE] [CANNOT CREATE APPLICATION]"), nsmx);
        }
        Assert.assertNotTrue(
                getAssertionId("[DO CREATE]", "[APPLICATION ALREADY REGISTERED]"),
                registry.contains(application.getId()));
        registry.put(application);
        application.addListener(platform);
        return application;
    }
}
