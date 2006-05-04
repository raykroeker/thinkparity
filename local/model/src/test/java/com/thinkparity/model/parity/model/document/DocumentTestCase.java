/*
 * Created On: Feb 14, 2006
 * $Id$
 */
package com.thinkparity.model.parity.model.document;

import java.io.File;

import com.thinkparity.codebase.DateUtil;
import com.thinkparity.codebase.DateUtil.DateImage;
import com.thinkparity.codebase.FileUtil;

import com.thinkparity.model.parity.model.ModelTestCase;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class DocumentTestCase extends ModelTestCase {

	/**
	 * Create a DocumentTestCase.
	 * 
	 * @param name
	 *            The test name.
	 */
	public DocumentTestCase(final String name) { super(name); }

	/**
	 * @see com.thinkparity.model.parity.model.ModelTestCase#setUp()
	 * 
	 */
	protected void setUp() throws Exception {
		super.setUp();

		login();
	}

	/**
	 * @see com.thinkparity.model.parity.model.ModelTestCase#tearDown()
	 * 
	 */
	protected void tearDown() throws Exception {
		logout();

		super.tearDown();
	}

    protected void modifyDocument(final Long documentId) throws Exception {
        final String prefix = DateUtil.format(DateUtil.getInstance(), DateImage.FileSafeDateTime);
        final String suffix = DateUtil.format(DateUtil.getInstance(), DateImage.FileSafeDateTime);
        final File tempFile = File.createTempFile(prefix, suffix);
        tempFile.deleteOnExit();

        FileUtil.writeBytes(tempFile,
                ("jUnit Test MOD " +
                DateUtil.format(DateUtil.getInstance(), DateImage.ISO)).getBytes());
        getDocumentModel().updateWorkingVersion(documentId, tempFile);
    }
}
