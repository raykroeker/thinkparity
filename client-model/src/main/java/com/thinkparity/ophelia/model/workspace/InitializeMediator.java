/*
 * Created On:  6-Apr-07 2:53:59 PM
 */
package com.thinkparity.ophelia.model.workspace;

import java.util.List;

import com.thinkparity.codebase.model.migrator.Feature;

/**
 * <b>Title:</b>thinkParity OpheliaModel Workspace Initialize Mediator<br>
 * <b>Description:</b>Decouples the workspace initialization client from the
 * initialization itself. It provides a callback mechanisim for confirming the
 * restoration of the user's database.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface InitializeMediator {

    /**
     * Confirm the restoration of a user's profile.
     * 
     * @param features
     *            A <code>List</code> of the <code>Feature</code>s a user
     *            has enabled.
     * @return True if the user wishes to restore.
     */
    public Boolean confirmRestore(final List<Feature> features);
}
