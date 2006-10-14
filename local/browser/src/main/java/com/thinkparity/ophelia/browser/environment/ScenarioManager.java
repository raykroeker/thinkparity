/*
 * Created On: 13-Oct-06 5:13:36 PM
 */
package com.thinkparity.ophelia.browser.environment;


/**
 * <b>Title:</b>thinkParity Scenario Manager<br>
 * <b>Description:</b>A scenario manager is responsible for selecting a demo
 * scenario; then creating necessary profiles and data to execute the scenario.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class ScenarioManager {

    /**
     * Create ScenarioManager.
     *
     */
    ScenarioManager() {
        super();
    }

    Scenario select() {
        return new Scenario();
    }
}
