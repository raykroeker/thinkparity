/*
 * Nov 9, 2005
 */
package com.thinkparity.ophelia.browser.util;

import java.io.File;
import java.util.List;

import com.thinkparity.codebase.sort.StringComparator;

import com.thinkparity.codebase.model.document.Document;

/**
 * <b>Title:</b>thinkParity OpheliaUI Document Util<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.3
 */
public class DocumentUtil {

	/** A singleton instance of <code>DocumentUtil</code>. */
	private static DocumentUtil INSTANCE;

	/**
     * Obtain an instance of document util.
     * 
     * @return An instance of <code>DocumentUtil</code>.
     */
	public static DocumentUtil getInstance() {
        if (null == INSTANCE) {
            INSTANCE = new DocumentUtil();
        }
        return INSTANCE;
	}

    /** A <code>StringComparator</code>. */
    private final StringComparator stringComparator;

	/**
     * Create DocumentUtil.
     * 
     */
	private DocumentUtil() {
        super();
        this.stringComparator = new StringComparator(Boolean.TRUE);
	}

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
     * Obtain the index of a file within a list of documents by using a platform
     * specific name comparison.
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
            if (0 == stringComparator.compare(documents.get(i).getName(),
                    file.getName()))
                return i;
        return -1;
    }
}
