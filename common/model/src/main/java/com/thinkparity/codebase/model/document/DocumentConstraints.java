/**
 * Created On: 1-Jul-07 12:09:56 PM
 * $Id$
 */
package com.thinkparity.codebase.model.document;

import com.thinkparity.codebase.constraint.IntegerConstraint;
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

    /** A document name constraint. */
    private final StringConstraint name;

    /** A document size constraint. */
    private final IntegerConstraint size;

    /**
     * Create DocumentConstraints.
     *
     */
    private DocumentConstraints() {
        super();
        this.name = new StringConstraint();
        this.name.setMaxLength(64);
        this.name.setMinLength(1);
        this.name.setName("Document name");
        this.name.setNullable(Boolean.FALSE);

        this.size = new IntegerConstraint();
        this.size.setMaxValue(Integer.MAX_VALUE);
        this.size.setMinValue(1);
        this.size.setName("Document size");
        this.size.setNullable(Boolean.FALSE);
    }

    /**
     * Obtain document name.
     * 
     * @return A <code>StringConstraint</code>.
     */
    public StringConstraint getDocumentName() {
        return name;
    }

    /**
     * Obtain the size constraint.
     * 
     * @return A <code>IntegerConstraint</code>.
     */
    public IntegerConstraint getSize() {
        return size;
    }
}
