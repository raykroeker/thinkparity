/*
 * Created On:  25-Mar-07 1:54:19 PM
 */
package com.thinkparity.ophelia.model.document;

import java.util.List;

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
     * Find a document's matching version. If there exists more than one
     * matching version; the first one is returned. If there exists no matching
     * version; null is returned.
     * 
     * @param document
     *            A <code>Document</code>.
     * @param versionList
     *            A <code>List<DocumentVersion></code>.
     * @return A <code>DocumentVersion</code>.
     */
    public DocumentVersion findVersion(final Document document,
            final List<? extends DocumentVersion> versionList) {
        for (final DocumentVersion version : versionList) {
            if (version.getArtifactId().equals(document.getId())) {
                return version;
            }
        }
        return null;
    }

    /**
     * Obtain a document's name extension.
     * 
     * @param document
     *            A <code>Document</code>.
     * @return The document's name extension <code>String</code>.
     */
    public String getNameExtension(final Document document) {
        return FileUtil.getExtension(document.getName());
    }

    /**
     * Obtain a document version's name extension.
     * 
     * @param version
     *            A <code>DocumentVersion</code>.
     * @return A version's name extension <code>String</code>.
     */
    public String getNameExtension(final DocumentVersion version) {
        return FileUtil.getExtension(version.getArtifactName());
    }
}
