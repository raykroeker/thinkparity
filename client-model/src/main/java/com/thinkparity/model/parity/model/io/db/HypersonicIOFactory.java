/*
 * Feb 6, 2006
 */
package com.thinkparity.model.parity.model.io.db;

import com.thinkparity.model.parity.model.io.IOFactory;
import com.thinkparity.model.parity.model.io.db.hsqldb.util.HypersonicValidator;
import com.thinkparity.model.parity.model.io.handler.DocumentIOHandler;
import com.thinkparity.model.parity.model.workspace.Workspace;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class HypersonicIOFactory extends IOFactory {

	/**
	 * Create a HypersonicIOFactory [Concrete Factory]
	 * 
	 */
	public HypersonicIOFactory(final Workspace workspace) {
		super(workspace);
		new HypersonicValidator(workspace).validate();
	}

	/**
	 * @see com.thinkparity.model.parity.model.io.IOFactory#createDocumentHandler()
	 * 
	 */
	public DocumentIOHandler createDocumentHandler() {
		return new com.thinkparity.model.parity.model.io.db.hsqldb.handler.DocumentIOHandler();
	}

	/**
	 * @see com.thinkparity.model.parity.model.io.IOFactory#initialize()
	 * 
	 */
	public void initialize() {}
}
