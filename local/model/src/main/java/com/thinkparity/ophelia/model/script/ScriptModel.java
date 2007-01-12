/*
 * Generated On: Oct 15 06 12:36:36 PM
 */
package com.thinkparity.ophelia.model.script;

import java.util.List;

import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.util.jta.TransactionType;

/**
 * <b>Title:</b>thinkParity Script Model<br>
 * <b>Description:</b><br>
 * 
 * @author CreateModel.groovy
 * @version 1.1.2.1
 */
@ThinkParityTransaction(TransactionType.REQUIRED)
public interface ScriptModel {

    /**
     * Execute a scripting scenario.
     * 
     * @param scripts
     *            A list of thinkParity <code>Script</code>s.
     */
    public void execute(final List<Script> scripts);
}
