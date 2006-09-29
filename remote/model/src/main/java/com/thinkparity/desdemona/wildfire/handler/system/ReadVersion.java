/*
 * Created On: Sep 28, 2006 8:41:26 AM
 */
package com.thinkparity.desdemona.wildfire.handler.system;

import java.text.MessageFormat;

import com.thinkparity.desdemona.model.Version;
import com.thinkparity.desdemona.wildfire.handler.AbstractHandler;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ReadVersion extends AbstractHandler {

    /** Create ReadVersion. */
    public ReadVersion() {
        super("system:readversion");
    }

    /**
     * @see com.thinkparity.codebase.wildfire.handler.AbstractHandler#service()
     */
    @Override
    public void service() {
        logApiId();
        writeString("version", MessageFormat.format("{0} - {1} - {2}",
                Version.getName(), Version.getMode(), Version.getBuildId()));
    }
}
