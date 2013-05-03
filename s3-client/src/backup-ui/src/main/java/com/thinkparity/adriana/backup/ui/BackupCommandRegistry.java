/*
 * Created On:  29-Sep-07 4:02:36 PM
 */
package com.thinkparity.adriana.backup.ui;

import java.util.HashMap;
import java.util.Map;

/**
 * <b>Title:</b>thinkParity Adriana Backup Command Registry<br>
 * <b>Description:</b>A registry of all commands available.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class BackupCommandRegistry {

    /** The command registry. */
    private static final Map<String, String> registry;

    static {
        registry = new HashMap<String, String>(1, 1.0F);
        registry.put("backup", "com.thinkparity.adriana.backup.ui.command.Backup");
        registry.put("usage", "com.thinkparity.adriana.backup.ui.command.Usage");
        registry.put("restore", "com.thinkparity.adriana.backup.ui.command.Restore");
    }

    /**
     * Create BackupCommandRegistry.
     *
     */
    BackupCommandRegistry() {
        super();
    }

    /**
     * Instantiate a fully-qualified-class-name for the command.
     * 
     * @param command
     *            A <code>String</code>.
     * @return A <code>String</code>.
     */
    String newClassName(final String command) {
        return registry.get(command);
    }

    /**
     * Instantiate a fully-qualified-class-name for the command. If the command
     * is not registered use a default.
     * 
     * @param command
     *            A <code>String</code>.
     * @param defaultCommand
     *            A <code>String</code>.
     * @return A <code>String</code>.
     */
    String newClassName(final String command, final String defaultCommand) {
        if (registry.containsKey(command)) {
            return registry.get(command);
        } else {
            return registry.get(defaultCommand);
        }
    }
}
