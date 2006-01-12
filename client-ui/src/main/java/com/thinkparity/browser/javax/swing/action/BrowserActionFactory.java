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
	public static BrowserAction createAction(final Class clasz) {
		synchronized(singletonLock) { return singleton.doCreateAction(clasz); }
	}

	/**
	 * Cache of actions already created.
	 * 
	 */
	private final Map<Class,BrowserAction> cache;

	/**
	 * Create a BrowserActionFactory [Singleton, Factory]
	 * 
	 */
	private BrowserActionFactory() {
		super();
		this.cache = new Hashtable<Class,BrowserAction>(1, 1.0F);
	}

	/**
	 * Cache an action.
	 * 
	 * @param browserAction
	 *            The action to cache.
	 */
	private BrowserAction cache(final BrowserAction browserAction) {
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
	private BrowserAction doCreateAction(final Class clasz) {
		BrowserAction action = cache.get(clasz);
		if(null != action) { return action; }
		else {
			try { action = (BrowserAction) clasz.newInstance(); }
			catch(Exception x) { action = null; }
			return cache(action);
		}
	}
}
