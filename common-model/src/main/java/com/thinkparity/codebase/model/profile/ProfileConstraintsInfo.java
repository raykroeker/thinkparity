/*
 * Created On:  21-Dec-07 9:41:47 AM
 */
package com.thinkparity.codebase.model.profile;

import java.util.List;

import com.thinkparity.codebase.model.migrator.Feature;

/**
 * <b>Title:</b>thinkParity Common Profile Profile Constraints Info<br>
 * <b>Description:</b>Provides suplementary information for the constraints.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface ProfileConstraintsInfo {

    /**
     * Obtain a list of features.
     * 
     * @return A <code>List<Feature></code>.
     */
    List<Feature> getFeatures();
}
