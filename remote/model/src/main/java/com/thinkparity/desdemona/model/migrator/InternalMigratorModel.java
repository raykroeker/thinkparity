/*
 * Created On:  23-Jan-07 5:33:57 PM
 */
package com.thinkparity.desdemona.model.migrator;

import com.thinkparity.codebase.model.migrator.Feature;
import com.thinkparity.codebase.model.migrator.Product;


/**
 * <b>Title:</b>thinkParity Migrator Model<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface InternalMigratorModel extends MigratorModel {
   
    /**
     * Read a product feature.
     * 
     * @param product
     *            A <code>Product</code>.
     * @param name
     *            A product feature name <code>String</code>.
     * @return A <code>Feature</code>.
     */
    public Feature readProductFeature(final Product product, final String name);
}
