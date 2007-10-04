/*
 * Created On:  23-Jan-07 5:33:57 PM
 */
package com.thinkparity.desdemona.model.migrator;

import java.util.List;

import com.thinkparity.codebase.filter.Filter;

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
     * Read a list of features.
     * 
     * @param product
     *            A <code>Product</code>.
     * @param filter
     *            A <code>Filter<? super Feature></code>.
     * @return A <code>List<Feature></code>.
     */
    List<Feature> readFeatures(Product product, Filter<? super Feature> filter);

    /**
     * Read a list of fees associated with the feature list
     * 
     * @param featureList
     *            A <code>List<Feature></code>.
     * 
     * @return A <code>List<Fee></code>.
     */
    List<Fee> readFees(final List<Feature> featureList);

    /**
     * Determine whether or not payment is required for the feature.
     * 
     * @param feature
     *            A <code>Feature</code>.
     * @return True if payment is required.
     */
    Boolean readIsPaymentRequired(Feature feature);

    /**
     * Read a product feature.
     * 
     * @param product
     *            A <code>Product</code>.
     * @param name
     *            A product feature name <code>String</code>.
     * @return A <code>Feature</code>.
     */
    Feature readProductFeature(Product product, String name);
}
