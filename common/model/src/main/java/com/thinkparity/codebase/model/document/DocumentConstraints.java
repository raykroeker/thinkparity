/**
 * Created On: 1-Jul-07 12:09:56 PM
 * $Id$
 */
package com.thinkparity.codebase.model.document;

import com.thinkparity.codebase.constraint.StringConstraint;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class DocumentConstraints {

    /** A singleton instance of <code>DocumentConstraints</code>. */
    private static DocumentConstraints INSTANCE;

    /**
     * Obtain an instance of document constraints.
     * 
     * @return An instance of <code>DocumentConstraints</code>.
     */
    public static DocumentConstraints getInstance() {
        if (null == INSTANCE) {
            INSTANCE = new DocumentConstraints();
        }
        return INSTANCE;
    }

    /** A document name <code>StringConstraint</code>. */
    private final StringConstraint documentName;

    /**
     * Create DocumentConstraints.
     *
     */
    private DocumentConstraints() {
        super();
        this.documentName = new StringConstraint();
        this.documentName.setMaxLength(64);
        this.documentName.setMinLength(1);
        this.documentName.setName("Document name");
        this.documentName.setNullable(Boolean.FALSE);
    }

    /**
     * Obtain document name.
     * 
     * @return A <code>StringConstraint</code>.
     */
    public StringConstraint getDocumentName() {
        return documentName;
    }
}
