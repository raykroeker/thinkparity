/*
 * Feb 13, 2006
 */
package com.thinkparity.model;

import java.io.File;
import java.io.IOException;

import com.thinkparity.codebase.Mode;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.model.artifact.ArtifactModel;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.index.IndexModel;
import com.thinkparity.model.parity.model.workspace.Preferences;
import com.thinkparity.model.parity.model.workspace.WorkspaceModel;

import com.raykroeker.junitx.TestCase;
import com.raykroeker.junitx.TestSession;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class ModelTestCase extends TestCase {

	/**
	 * The JUnit eXtension test session.
	 * 
	 */
	private static final TestSession testSession;

    static {
		// set non ssl mode
		System.setProperty("parity.insecure", "true");
		// set staging system
        if(Mode.DEVELOPMENT == Version.getMode()) {
            System.setProperty("parity.serverhost", "rkutil.raykroeker.com");
        }
        else if(Mode.TESTING == Version.getMode()) {
            System.setProperty("parity.serverhost", "rkutil.raykroeker.com");
        }
        else if(Mode.PRODUCTION == Version.getMode()) {
            System.setProperty("parity.serverhost", "rkutil.raykroeker.com");
        }
        else { Assert.assertUnreachable("[RMODEL] [TEST INIT] [UNKNOWN OP MODE]"); }

		testSession = TestCase.getTestSession();
		final ModelTestUser modelTestUser = ModelTestUser.getJUnit();
		testSession.setData("modelTestUser", modelTestUser);
		// init workspace
		initParityWorkspace(testSession.getSessionDirectory());
        // init preferences
		final Preferences preferences = WorkspaceModel.getModel().getWorkspace().getPreferences();
		preferences.setUsername(modelTestUser.getUsername());
        // init archive
        initParityArchive(testSession.getSessionDirectory());
        // init install
        initParityInstall(testSession.getSessionDirectory());
	}

    /**
     * Initialize the parity archive directory for a test run.
     * 
     * @param parent
     *            The parent within which to create the archive dir.
     */
    private static void initParityArchive(final File parent) {
        final File archive = new File(parent, "parity.archive");
        Assert.assertTrue("[LMODEL] [TEST INIT] [INIT ARCHIVE]", archive.mkdir());

        System.setProperty("parity.archive.directory", archive.getAbsolutePath());
    }

    /**
     * Initialize the parity install directory for a test run.
     * 
     * @param parent
     *            The parent within which to create the install dir.
     */
    private static void initParityInstall(final File parent) {
        final File install = new File(parent, "parity.install");
        Assert.assertTrue("[LMODEL] [TEST INIT] [INIT INSTALL]", install.mkdir());
        Assert.assertTrue("[LMODEL] [TEST INIT] [INIT INSTALL CORE]", new File(install, "core").mkdir());
        final File lib = new File(install, "lib");
        Assert.assertTrue("[LMODEL] [TEST INIT] [INIT INSTALL LIB]", lib.mkdir());
        Assert.assertTrue("[LMODEL] [TEST INIT] [INIT INSTALL LIB NATIVE]", new File(lib, "win32").mkdir());

        System.setProperty("parity.install", install.getAbsolutePath());
    }

	/**
     * Initialize the parity workspace directory for a test run.
     * 
     * @param parent
     *            The parent within which to create the workspace dir.
     */
    private static void initParityWorkspace(final File parent) {
        final File workspace = new File(parent, "parity.workspace");
        Assert.assertTrue("[LMODEL] [TEST INIT] [INIT WORKSPACE]", workspace.mkdir());

        System.setProperty("parity.workspace", workspace.getAbsolutePath());
    }

	private DocumentModel documentModel;

	private IndexModel indexModel;

	/**
	 * Create a ModelTestCase.
	 * 
	 * @param name
	 *            The test name
	 */
	protected ModelTestCase(final String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

    protected ArtifactModel getArtifactModel() {
        return ArtifactModel.getModel();
    }

	protected DocumentModel getDocumentModel() {
		if(null == documentModel) {
			documentModel = DocumentModel.getModel();
		}
		return documentModel;
	}

	protected IndexModel getIndexModel() {
		if(null == indexModel) {
			indexModel = IndexModel.getModel();
		}
		return indexModel;
	}

	/**
	 * @see com.raykroeker.junitx.TestCase#getInputFiles()
	 */
	protected File[] getInputFiles() throws IOException {
		final File[] inputFiles = new File[5];
		System.arraycopy(super.getInputFiles(), 0, inputFiles, 0, 5);
		return inputFiles;
	
	}

	/**
	 * Obtain the junit test user.
	 * 
	 * @return The junit test user.
	 */
	protected ModelTestUser getModelTestUser() {
		return (ModelTestUser) testSession.getData("modelTestUser");
	}
}
