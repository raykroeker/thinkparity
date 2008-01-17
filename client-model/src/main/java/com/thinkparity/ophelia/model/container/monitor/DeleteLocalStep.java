/*
 * Created On:  2-Mar-07 2:35:27 PM
 */
package com.thinkparity.ophelia.model.container.monitor;

import com.thinkparity.ophelia.model.util.Step;

/**
 * <b>Title:</b>thinkParity Ophelia Model Container Delete Local Monitor Step<br>
 * <b>Description:</b>Discrete progress steps used when deleting all local data.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public enum DeleteLocalStep implements Step {
    DELETE_CONTAINER, DELETE_CONTAINERS
}
