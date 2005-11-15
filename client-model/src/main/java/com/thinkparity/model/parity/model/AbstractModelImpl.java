/*
 * Aug 6, 2005
 */
package com.thinkparity.model.parity.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.log4j.ModelLoggerFactory;
import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.api.ParityObject;
import com.thinkparity.model.parity.api.ParityObjectFlag;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.project.Project;
import com.thinkparity.model.parity.model.project.ProjectModel;
import com.thinkparity.model.parity.model.workspace.Preferences;
import com.thinkparity.model.parity.model.workspace.Workspace;


/**
 * AbstractModelImpl
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class AbstractModelImpl {

	/**
	 * Empty list of parity object flags.
	 */
	protected static final Collection<ParityObjectFlag> NO_FLAGS =
		Collections.unmodifiableCollection(new Vector<ParityObjectFlag>(0));

	/**
	 * Handle to an apache logger.
	 */
	protected final Logger logger =
		ModelLoggerFactory.getLogger(AbstractModelImpl.class);

	/**
	 * Handle to the parity model preferences.
	 */
	protected final Preferences preferences;

	/**
	 * Handle to the parity model workspace.
	 */
	protected final Workspace workspace;

	/**
	 * Create an AbstractModelImpl
	 * 
	 * @param workspace
	 *            Handle to an existing parity model workspace.
	 */
	protected AbstractModelImpl(final Workspace workspace) {
		super();
		this.workspace = workspace;
		this.preferences = (null == workspace ? null : workspace.getPreferences());
	}

	/**
	 * Assert that the calling method has not yet been implemented.
	 *
	 */
	protected void assertNYI() {
		Assert.assertNotYetImplemented("The calling method has not yet been implemented.");
	}

	/**
	 * Obtain a handle to the project model.
	 * 
	 * @return A handle to the project model.
	 */
	protected DocumentModel getDocumentModel() { return DocumentModel.getModel(); }

	/**
	 * Obtain a handle to the project model.
	 * 
	 * @return A handle to the project model.
	 */
	protected ProjectModel getProjectModel() { return ProjectModel.getModel(); }

	/**
	 * Remove the flag from the parity object. Also recursively remove the flag
	 * from the object's parent projects.
	 * 
	 * @param parityObject
	 *            The object to remove the flag from.
	 * @param flag
	 *            The flag to remove.
	 * @throws ParityException
	 */
	protected void removeFlag(final ParityObject parityObject,
			final ParityObjectFlag flag) throws ParityException {
		switch(parityObject.getType()) {
		case DOCUMENT:
			removeFlag((Document) parityObject, flag);
			break;
		case PROJECT:
			removeFlag((Project) parityObject, flag);
			break;
		default:
			throw Assert.createUnreachable(
					"removeFlag(ParityObject,ParityObjectFlag)");
		}
	}

	/**
	 * Remove a flag from the document; and it's parent proejcts recursively.
	 * 
	 * @param document
	 *            The document to remove the flag from.
	 * @param flag
	 *            The flag to remove.
	 * @throws ParityException
	 */
	private void removeFlag(final Document document, final ParityObjectFlag flag)
			throws ParityException {
		if(document.contains(flag)) {
			document.remove(flag);
			getDocumentModel().update(document);
		}
		final Project parent = getProjectModel().get(document.getParentId());
		if(null != parent) { removeFlag(parent, flag); }
	}

	/**
	 * Remove a flag from the project; and it's parent projects recursively.
	 * 
	 * @param project
	 *            The project to remove the flag from.
	 * @param flag
	 *            The flag to remove.
	 * @throws ParityException
	 */
	private void removeFlag(final Project project, final ParityObjectFlag flag)
			throws ParityException {
		final ProjectModel projectModel = getProjectModel();
		if(project.contains(flag)) {
			project.remove(flag);
			getProjectModel().update(project);
		}
		final Project parent = projectModel.get(project.getParentId());
		if(null != parent) { removeFlag(parent, flag); }
	}
}
