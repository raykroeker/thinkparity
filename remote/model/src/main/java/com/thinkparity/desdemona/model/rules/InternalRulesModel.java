/*
 * Created On:  28-Feb-07 8:24:03 PM
 */
package com.thinkparity.desdemona.model.rules;

import com.thinkparity.codebase.model.Context;

import com.thinkparity.desdemona.model.session.Session;

/**
 * <b>Title:</b>thinkParity DesdemonaModel Internal Rules Interface<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class InternalRulesModel extends RulesModel {

    /**
     * Create InternalRulesModel.
     *
     * @param impl
     */
    InternalRulesModel(final Context context, final Session session) {
        super(session);
    }
}
