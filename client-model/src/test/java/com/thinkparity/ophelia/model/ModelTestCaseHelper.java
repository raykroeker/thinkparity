/*
 * Jul 1, 2005
 */
package com.thinkparity.ophelia.model;

import java.util.Collection;

import com.thinkparity.ophelia.model.document.Document;

/**
 * Provides singleton functionality for the model test suite.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.17
 */
public class ModelTestCaseHelper {

	/**
	 * Create a ModelTestCaseHelper
	 * 
	 */
	ModelTestCaseHelper() { super(); }

	/**
	 * Assert that the document list provided contains the document.
	 * 
	 * @param documentList
	 *            The document list to check.
	 * @param document
	 *            The document to validate.
	 */
	static void assertContains(final Collection<Document> documentList,
			Document document) {
		final StringBuffer actualIds = new StringBuffer("");
		Boolean didContain = Boolean.FALSE;
		int actualCounter = 0;
		for(Document actual : documentList) {
			if(actual.getId().equals(document.getId())) {
				didContain = Boolean.TRUE;
			}
			actualIds.append(actualCounter == 0 ? "" : ",");
			actualIds.append(actual.getId().toString());
		}
		ModelTestCase.assertTrue("expected:<" + document.getId() + " but was:<" + actualIds.toString(), didContain);
	}

	/**
	 * Assert that the document list provided doesn't contain the document.
	 * 
	 * @param documentList
	 *            The document list to check.
	 * @param document
	 *            The document to validate.
	 */
	static void assertNotContains(final Collection<Document> documentList,
			Document document) {
		final StringBuffer actualIds = new StringBuffer("");
		Boolean didContain = Boolean.FALSE;
		int actualCounter = 0;
		for(Document actual : documentList) {
			if(actual.getId().equals(document.getId())) {
				didContain = Boolean.TRUE;
			}
			actualIds.append(actualCounter++ == 0 ? "" : ",");
			actualIds.append(actual.getId().toString());
		}
		ModelTestCase.assertFalse("expected:<" + document.getId() + "> but was:<" + actualIds.toString() + ">", didContain);
	}
}
