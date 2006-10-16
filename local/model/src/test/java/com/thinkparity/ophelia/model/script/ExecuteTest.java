/*
 * Created On: Oct 15, 2006 3:14:24 PM
 */
package com.thinkparity.ophelia.model.script;

import java.io.InputStream;
import java.util.ArrayList;

import com.thinkparity.codebase.ResourceUtil;
import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ExecuteTest extends ScriptTestCase {

    private static final String NAME = "Execute Test";

    private Fixture datum;

    /** Create ExecuteTest. */
    public ExecuteTest() {
        super(NAME);
    }

    public void testAddDocument() {
        datum.scriptModel.execute(new GroovyScriptList("AddDocumentTest.groovy"));
    }

    public void testCreate() {
        datum.scriptModel.execute(new GroovyScriptList("CreateTest.groovy"));
    }

    public void testFindAddDocument() {
        datum.scriptModel.execute(new GroovyScriptList("FindAddDocumentTest.groovy"));
    }
    
    public void testFind() {
        datum.scriptModel.execute(new GroovyScriptList("FindTest.groovy"));
    }

    public void testHelloWorld() {
        datum.scriptModel.execute(new GroovyScriptList("HelloWorld.groovy"));
    }

    /**
     * @see com.thinkparity.ophelia.model.script.ScriptTestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        final InternalScriptModel scriptModel = getScriptModel(OpheliaTestUser.JUNIT);
        datum = new Fixture(scriptModel);
    }

    /**
     * @see com.thinkparity.ophelia.model.script.ScriptTestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        datum = null;
        super.tearDown();
    }

    private class Fixture extends ScriptTestCase.Fixture {
        private final ScriptModel scriptModel;
        private Fixture(final ScriptModel scriptModel) {
            this.scriptModel = scriptModel;
        }
    }

    private class GroovyScriptList extends ArrayList<Script> {
        private GroovyScriptList(final String name) {
            super();
            add(new GroovyScriptWrapper(name));
        }
    }
    /**
     * A groovy script wrapper.
     * 
     */
    private class GroovyScriptWrapper implements Script {
        private final String name;
        private GroovyScriptWrapper(final String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }
        public InputStream openStream() {
            return ResourceUtil.getInputStream(
                    new StringBuffer("script/").append(name).toString());
        }
    }
}
