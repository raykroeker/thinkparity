/**
 * 
 */
package com.thinkparity.cordelia;

import com.thinkparity.codebase.Mode;
import com.thinkparity.codebase.swing.Swing;

import com.thinkparity.codebase.model.session.Environment;

import com.thinkparity.cordelia.environment.EnvironmentManager;
import com.thinkparity.cordelia.mode.ModeManager;
import com.thinkparity.cordelia.ui.CordeliaPlatform;

/**
 * @author raymond
 *
 */
public class Cordelia {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Swing.init();
        final Mode mode = new ModeManager().select();
        final Environment environment = new EnvironmentManager().select();
        if (null != mode && null != environment) {
            CordeliaPlatform.createPlatform(mode, environment).start();
        }
    }

    /** Create Cordelia */
    private Cordelia() {
        super();
    }
}
