/*
 * Created On:  25-Mar-07 1:54:19 PM
 */
package com.thinkparity.ophelia.model.document;

import com.thinkparity.codebase.FileUtil;

import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class DocumentUtil {

    /** An instance of <code>DocumentUtil</code>. */
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

    /**
     * Create DocumentUtil.
     *
     */
    private DocumentUtil() {
        super();
    }

    /**
     * Obtain a document's name extension.
     * 
     * @param document
     *            A <code>Document</code>.
     * @return The document's name extension <code>String</code>.
     */
    public String getNameExtension(final Document document) {
        final String name = document.getName();
        return FileUtil.getExtension(name);
    }

    /**
     * Obtain a document version's name extension.
     * 
     * @param version
     *            A <code>DocumentVersion</code>.
     * @return A version's name extension <code>String</code>.
     */
    public String getNameExtension(final DocumentVersion version) {
        final String name = version.getName();
        return FileUtil.getExtension(name);
    }
}
