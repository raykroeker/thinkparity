/**
 * 
 */
package com.thinkparity.ophelia.browser.mode.demo;

import com.thinkparity.ophelia.model.script.Script;

/**
 * @author raymond
 *
 */
interface ExecutionMonitor {
    public void notifyScriptError(final Script script, final Throwable error);
}
