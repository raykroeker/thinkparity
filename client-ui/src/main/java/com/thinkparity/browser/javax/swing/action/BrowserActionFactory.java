/*
 * Jan 10, 2006
 */
package com.thinkparity.browser.javax.swing.action;

import java.util.Hashtable;
import java.util.Map;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class BrowserActionFactory {

	private static final BrowserActionFactory singleton;

	private static final Object singletonLock;

	static {
		singleton = new BrowserActionFactory();
		singletonLock = new Object();
	}

	/**
	 * Create a new named action.
	 * 
	 * @param name
	 *            The action name.
	 * @return The action.
	 */
	public static AbstractAction createAction(final Class clasz) {
		synchronized(singletonLock) { return singleton.doCreateAction(clasz); }
	}

	/**
	 * Cache of actions already created.
	 * 
	 */
	private final Map<Class,AbstractAction> cache;

	/**
	 * Create a BrowserActionFactory [Singleton, Factory]
	 * 
	 */
	private BrowserActionFactory() {
		super();
		this.cache = new Hashtable<Class,AbstractAction>(1, 1.0F);
	}

	/**
	 * Cache an action.
	 * 
	 * @param browserAction
	 *            The action to cache.
	 */
	private AbstractAction cache(final AbstractAction browserAction) {
		cache.put(browserAction.getClass(), browserAction);
		return browserAction;
	}

	/**
	 * Create a new instance of a named action.
	 * 
	 * @param name
	 *            The action to create.
	 * @return A new instance of the action.
	 */
	private AbstractAction doCreateAction(final Class clasz) {
		AbstractAction action = cache.get(clasz);
		if(null != action) { return action; }
		else {
			try { action = (AbstractAction) clasz.newInstance(); }
			catch(Exception x) { action = null; }
			return cache(action);
		}
	}
}
