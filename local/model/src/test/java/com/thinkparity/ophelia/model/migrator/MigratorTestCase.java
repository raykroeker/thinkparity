/*
 * Created On:  24-Jan-07 5:31:21 PM
 */
package com.thinkparity.ophelia.model.migrator;

import com.thinkparity.codebase.model.migrator.Product;

import com.thinkparity.ophelia.model.ModelTestCase;

/**
 * <b>Title:</b>thinkParity Migrator Test Case<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class MigratorTestCase extends ModelTestCase {

    protected static void assertNotNull(final Product product) {
        assertNotNull("Product reference is null.", (Object) product);
        assertNotNull("Product created by reference is null.", product.getCreatedBy());
        assertNotNull("Product created on reference is null.", product.getCreatedOn());
        assertNotNull("Product name reference is null.", product.getName());
        assertNotNull("Product type reference is null.", product.getType());
        assertNotNull("Product unique id reference is null.", product.getUniqueId());
        assertNotNull("Product updated by reference is null.", product.getUpdatedBy());
        assertNotNull("Product updated on reference is null.", product.getUpdatedOn());
    }

    /**
     * Create MigratorTestCase.
     * 
     * @param name
     *            A test name <code>String</code>.
     */
    public MigratorTestCase(final String name) {
        super(name);
    }

    protected abstract class Fixture extends ModelTestCase.Fixture {}
}
