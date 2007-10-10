/*
 * Created On:  29-Sep-07 6:32:30 PM
 */
package com.thinkparity.adriana.backup.model.command;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Result {

    /** A pass indicator. */
    private Boolean pass;

    /**
     * Create CommandResult.
     *
     */
    public Result() {
        super();
    }

    /**
     * Obtain the pass.
     *
     * @return A <code>Boolean</code>.
     */
    public Boolean isPass() {
        return pass;
    }

    /**
     * Set the pass.
     *
     * @param pass
     *		A <code>Boolean</code>.
     */
    public void setPass(final Boolean pass) {
        this.pass = pass;
    }
}
