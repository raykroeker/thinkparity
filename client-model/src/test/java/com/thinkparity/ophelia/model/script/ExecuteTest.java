/*
 * Created On: Oct 15, 2006 3:14:24 PM
 */
package com.thinkparity.ophelia.model.script;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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

    public void testExecute() {
        datum.scriptModel.execute(datum.scripts);
    }

    /**
     * @see com.thinkparity.ophelia.model.script.ScriptTestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        final InternalScriptModel scriptModel = getScriptModel(OpheliaTestUser.JUNIT);
        final List<Script> scripts = new ArrayList<Script>();
        scripts.add(new GroovyScriptWrapper("HelloWorld.groovy"));
        datum = new Fixture(scriptModel, scripts);
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
        private final List<Script> scripts;
        private Fixture(final ScriptModel scriptModel,
                final List<Script> scripts) {
            this.scriptModel = scriptModel;
            this.scripts = scripts;
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
        public InputStream openStream() {
            return ResourceUtil.getInputStream(
                    new StringBuffer("script/").append(name).toString());
        }
    }
}
