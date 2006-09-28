/*
 * Created On: Sep 17, 2006 10:54:42 AM
 */
package com.thinkparity.codebase.model;


/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class TestContext {

    public static Context CONTEXT;

    static {
        CONTEXT = new Context(new TestModel());
    }

    private static class TestModel extends AbstractModel<TestModelImpl> {

        private TestModel() {
            super(new TestModelImpl());
        }

        /**
         * @see com.thinkparity.codebase.model.AbstractModel#getImplLock()
         */
        @Override
        protected Object getImplLock() {
            return this;
        }
    }

    private static class TestModelImpl extends AbstractModelImpl {

        private TestModelImpl() {
            super();
        }
    }
        
}
