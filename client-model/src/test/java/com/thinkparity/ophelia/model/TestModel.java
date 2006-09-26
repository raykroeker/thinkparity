/*
 * Created On: Sep 26, 2006 8:38:58 AM
 */
package com.thinkparity.ophelia.model;

import com.thinkparity.codebase.model.Context;


/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class TestModel extends AbstractModelImpl {

    public static Context getModelContext() {
        return new TestModel().getContext();
    }

    /** Create TestModel. */
    private TestModel() {
        super(null);
    }
}
