/*
 * Feb 6, 2006
 */
package com.thinkparity.migrator.io.hsqldb;

import com.thinkparity.migrator.io.IOFactory;
import com.thinkparity.migrator.io.handler.LibraryIOHandler;
import com.thinkparity.migrator.io.handler.ReleaseIOHandler;
import com.thinkparity.migrator.io.hsqldb.util.HypersonicValidator;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class HypersonicIOFactory extends IOFactory {

	/**
	 * Create a HypersonicIOFactory [Concrete Factory]
	 * 
	 */
	public HypersonicIOFactory() {
		super();
		new HypersonicValidator().validate();
	}

    /**
     * @see com.thinkparity.migrator.io.IOFactory#createLibraryHandler()
     * 
     */
    public LibraryIOHandler createLibraryHandler() {
        return new com.thinkparity.migrator.io.hsqldb.handler.LibraryIOHandler();
    }

	/**
	 * @see com.thinkparity.model.parity.model.io.IOFactory#createArtifactHandler()
	 * 
	 */
	public ReleaseIOHandler createReleaseHandler() {
		return new com.thinkparity.migrator.io.hsqldb.handler.ReleaseIOHandler();
	}

	/**
	 * @see com.thinkparity.model.parity.model.io.IOFactory#initialize()
	 * 
	 */
	public void initialize() {}
}
