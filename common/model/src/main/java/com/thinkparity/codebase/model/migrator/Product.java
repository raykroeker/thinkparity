/*
 * Created On:  23-Jan-07 4:18:46 PM
 */
package com.thinkparity.codebase.model.migrator;

import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.artifact.ArtifactType;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class Product extends Artifact {

    public static final Product THIS;

    static {
        THIS = new Product();
    }

    /**
     * Create Product.
     *
     */
    public Product() {
        super();
    }

    /**
     * @see com.thinkparity.codebase.model.artifact.Artifact#getType()
     *
     */
    @Override
    public ArtifactType getType() {
        return ArtifactType.PRODUCT;
    }
}
