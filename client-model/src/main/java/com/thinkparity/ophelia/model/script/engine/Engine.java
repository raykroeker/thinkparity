/*
 * Created On: Oct 15, 2006 12:45:50 PM
 */
package com.thinkparity.ophelia.model.script.engine;

import java.io.IOException;

import com.thinkparity.ophelia.model.script.Script;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface Engine {
    public void execute(final Script script) throws IOException;
    public void initialize(final Environment environment);
}
