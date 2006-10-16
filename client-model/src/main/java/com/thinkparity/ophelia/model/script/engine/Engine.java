/*
 * Created On: Oct 15, 2006 12:45:50 PM
 */
package com.thinkparity.ophelia.model.script.engine;

import java.io.IOException;
import java.util.List;

import com.thinkparity.ophelia.model.script.Script;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface Engine {
    public void execute(final List<Script> scripts) throws IOException;
    public void initialize(final Environment environment);
}
