/*
 * Feb 6, 2006
 */
package com.thinkparity.model.parity.model.io;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.model.io.db.hsqldb.HypersonicIOFactory;
import com.thinkparity.model.parity.model.io.handler.AuditIOHandler;
import com.thinkparity.model.parity.model.io.handler.DocumentIOHandler;
import com.thinkparity.model.parity.model.io.handler.SystemMessageIOHandler;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.parity.model.workspace.WorkspaceModel;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class IOFactory {

	private static IOFactory ioFactory;

	public static IOFactory getDefault() {
		if(null == ioFactory) {
			final Workspace workspace = getWorkspace();
			final IOLayerId ioLayerId = IOLayerId.DB;
			switch(ioLayerId) {
			case DB:
				ioFactory = new HypersonicIOFactory(workspace);
				break;
			default: Assert.assertUnreachable("");
			}
			ioFactory.initialize();
		}
		return ioFactory;
	}

	private static Workspace getWorkspace() {
		return WorkspaceModel.getModel().getWorkspace();
	}

	/**
	 * The parity workspace.
	 * 
	 */
	protected final Workspace workspace;

	/**
	 * Create an IOFactory [Abstract Factory]
	 * 
	 */
	protected IOFactory(final Workspace workspace) {
		super();
		this.workspace = workspace;
	}

	/**
	 * Create an audit io handler.
	 * 
	 * @return The audit io handler.
	 */
	public abstract AuditIOHandler createAuditHandler();

	/**
	 * Create a document io handler.
	 * 
	 * @return The document io handler.
	 */
	public abstract DocumentIOHandler createDocumentHandler();

	/**
	 * Create a system message io handler.
	 * 
	 * @return The system message io handler.
	 */
	public abstract SystemMessageIOHandler createSystemMessageHandler();

	/**
	 * Initialize the io layer. Any directory\file creation should be done here.
	 * 
	 */
	public abstract void initialize();
}
