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
        datum.scriptModel.execute(new GroovyScripts("AddDocumentTest.groovy"));
    }

    public void testCreate() {
        datum.scriptModel.execute(new GroovyScripts("CreateTest.groovy"));
    }

    public void testFind() {
        datum.scriptModel.execute(new GroovyScripts("FindTest.groovy"));
    }
    
    public void testFindAddDocument() {
        datum.scriptModel.execute(new GroovyScripts("FindAddDocumentTest.groovy"));
    }

    public void testHelloWorld() {
        datum.scriptModel.execute(new GroovyScripts("HelloWorld.groovy"));
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

    private class GroovyScripts extends ArrayList<Script> {
        private GroovyScripts(final String scriptName) {
            super();
            add(new Script() {
                public String getName() {
                    return scriptName;
                }
                public InputStream open() {
                    return ResourceUtil.getInputStream("script/" + scriptName);
                }
                public InputStream openResource(String name) {
                    return ResourceUtil.getInputStream("junitx-files/" + name);
                }
            });
        }
    }
}
