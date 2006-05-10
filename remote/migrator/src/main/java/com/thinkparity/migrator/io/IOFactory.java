/*
 * Created On: May 9, 2006 11:50:02 AM
 * $Id$
 */
package com.thinkparity.migrator.io;

import com.thinkparity.migrator.io.handler.LibraryIOHandler;
import com.thinkparity.migrator.io.handler.ReleaseIOHandler;

/** 
 * @author raymond@thinkparity.com
 * @version 1.1
 */
public abstract class IOFactory {

	/**
     * Obtain the hypersonic db io factory.
     * 
     * @return The hypersonic db io factory.
     */
    public static IOFactory getHypersonic() {
        return new com.thinkparity.migrator.io.hsqldb.HypersonicIOFactory();
    }

    /** Create IOFactory. [Abstract Factory] */
	protected IOFactory() { super(); }

	/**
     * Obtain the library io handler.
     * 
     * @return The library io handler.
     */
    public abstract LibraryIOHandler createLibraryHandler();

    /**
     * Obtain the release io handler.
     * 
     * @return The release io handler.
     */
    public abstract ReleaseIOHandler createReleaseHandler();

    /**
	 * Initialize the io layer. Any directory\file creation should be done here.
	 * 
	 */
	public abstract void initialize();
}
