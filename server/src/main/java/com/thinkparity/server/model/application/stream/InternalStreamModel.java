/*
 * Created On:  9-Nov-06 10:00:49 AM
 */
package com.thinkparity.desdemona.model.stream;

import com.thinkparity.codebase.model.Context;

import com.thinkparity.desdemona.model.session.Session;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class InternalStreamModel extends StreamModel {

    /**
     * Create InternalStreamModel.
     *
     * @param session
     */
    InternalStreamModel(final Context context, final Session session) {
        super(session);
    }
}
