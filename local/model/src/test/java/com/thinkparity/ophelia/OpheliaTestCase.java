/*
 * CreatedOn: Feb 13, 2006
 * $Id$
 */
package com.thinkparity.ophelia;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.DateUtil;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.junitx.TestCase;
import com.thinkparity.codebase.junitx.TestSession;

import com.thinkparity.ophelia.model.artifact.ArtifactModel;
import com.thinkparity.ophelia.model.contact.ContactModel;
import com.thinkparity.ophelia.model.container.ContainerModel;
import com.thinkparity.ophelia.model.document.DocumentModel;
import com.thinkparity.ophelia.model.index.IndexModel;
import com.thinkparity.ophelia.model.workspace.Preferences;
import com.thinkparity.ophelia.model.workspace.WorkspaceModel;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class OpheliaTestCase extends TestCase {

    /** The JUnit eXtension test session. */
	private static final TestSession testSession;

    static final String TEST_SERVERHOST = "thinkparity.dyndns.org";

    static final Integer TEST_SERVERPORT = 5224;

	static {
		// set non ssl mode
		System.setProperty("parity.insecure", "true");
        System.setProperty("parity.serverhost", TEST_SERVERHOST);
        System.setProperty("parity.serverport", TEST_SERVERPORT.toString());

        testSession = TestCase.getTestSession();
		final OpheliaTestUser modelTestUser = OpheliaTestUser.getJUnit();
		testSession.setData("modelTestUser", modelTestUser);
        // init archive
		initParityArchive(testSession.getSessionDirectory());
		// init install
		initParityInstall(testSession.getSessionDirectory());
		// init workspace
		initParityWorkspace(testSession.getSessionDirectory());
        // init preferences
		final Preferences preferences = WorkspaceModel.getModel().getWorkspace().getPreferences();
		preferences.setUsername(modelTestUser.getUsername());
	}

    /**
     * Assert that two byte arrays are equal.
     * 
     * @param assertion
     *            The assertion.
     * @param expected
     *            The expected byte array.
     * @param actual
     *            The actual byte array.
     */
    protected static void assertEquals(final String assertion,
            final byte[] expected, final byte[] actual) {
        assertEquals(new StringBuffer(assertion).append(" [BYTE ARRAY LENGTH DOES NOT MATCH EXPECTATION]").toString(), expected.length, actual.length);
        for(int i = 0; i < expected.length; i++) {
            assertEquals(new StringBuffer(assertion).append(" [BYTE AT POSITION ").append(i).append(" DOES NOT MATCH EXPECTATION]").toString(), expected[i], actual[i]);
        }
    }

    /**
     * Obtain the current date\time.
     * 
     * @return A calendar.
     */
    protected static Calendar currentDateTime() { return DateUtil.getInstance(); }

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

	/** An apache logger. */
    protected final Logger logger;

	private ContactModel contactModel;

	private ContainerModel containerModel;

    private DocumentModel documentModel;

	private IndexModel indexModel;

    /**
	 * Create a ModelTestCase.
	 * 
	 * @param name
	 *            The test name
	 */
	protected OpheliaTestCase(final String name) {
		super(name);
		this.logger = Logger.getLogger(getClass());
	}

    protected ArtifactModel getArtifactModel() {
        return ArtifactModel.getModel();
    }

    protected ContactModel getContactModel() {
        if(null == contactModel) {
            contactModel = ContactModel.getModel();
        }
        return contactModel;
    }

    protected ContainerModel getContainerModel() {
        if(null == containerModel) {
            containerModel = ContainerModel.getModel();
        }
        return containerModel;
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
	 * @see com.thinkparity.codebase.junitx.TestCase#getInputFiles()
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
	protected OpheliaTestUser getModelTestUser() {
		return (OpheliaTestUser) testSession.getData("modelTestUser");
	}
}
