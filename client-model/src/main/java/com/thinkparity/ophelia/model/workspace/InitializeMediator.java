/*
 * Created On:  6-Apr-07 2:53:59 PM
 */
package com.thinkparity.ophelia.model.workspace;

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
     * Confirm restore on a premium account.
     * 
     * @return True if the user wishes to restore.
     */
    public Boolean confirmRestorePremium();

    /**
     * Confirm restore on a standard account.
     * 
     * @return True if the user wishes to restore.
     */
    public Boolean confirmRestoreStandard();
}
