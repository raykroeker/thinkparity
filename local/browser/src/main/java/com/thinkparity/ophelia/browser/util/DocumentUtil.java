/*
 * Nov 9, 2005
 */
package com.thinkparity.ophelia.browser.util;

import java.io.File;
import java.util.List;

import com.thinkparity.codebase.model.document.Document;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class DocumentUtil {

	/** A singleton instance of <code>DocumentUtil</code>. */
	private static final DocumentUtil INSTANCE;

	static {
        INSTANCE = new DocumentUtil();
	}

	/**
     * Obtain the document's filename extension.
     * 
     * @param document
     *            A thinkParity document.
     * @return The document's filename extension.
     */
	public static DocumentUtil getInstance() {
        return INSTANCE;
	}

	/**
     * Create DocumentUtil.
     * 
     */
	private DocumentUtil() { super(); }

    /**
     * Determine if the list of documents contains the file by using an exact
     * name comparison.
     * 
     * @param documents
     *            A <code>List</code> of <code>Document</code>s.
     * @param file
     *            A <code>File</code>.
     * @return True if a match is found; false otherwise.
     */
    public boolean contains(final List<Document> documents, final File file) {
        return -1 < indexOf(documents, file);
    }

    /**
     * Obtain the index of a file within a list of documents by using a
     * non-case-sensitive name comparison.
     * 
     * @param documents
     *            A <code>List</code> of <code>Document</code>s.
     * @param file
     *            A <code>File</code>.
     * @return The index of the document in the list matching the file; or -1 if
     *         no match is found.
     */
	public int indexOf(final List<Document> documents, final File file) {
        for (int i = 0; i < documents.size(); i++)
            if (documents.get(i).getName().equalsIgnoreCase(file.getName()))
                return i;
        return -1;
    }
}
