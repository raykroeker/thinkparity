/*
 * Created On:  23-May-07 4:21:29 PM
 */
package com.thinkparity.ophelia.model.help;

import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.util.jta.TransactionType;

/**
 * <b>Title:</b>thinkParity OpheliaModel Internal Help Model<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
@ThinkParityTransaction(TransactionType.REQUIRED)
public interface InternalHelpModel extends HelpModel {

    /**
     * Build the help index.
     *
     */
    public void index();
}
