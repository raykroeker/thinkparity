/*
 * Created On:  Nov 19, 2007 9:53:03 AM
 */
package com.thinkparity.ophelia.support.ui.action;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import com.thinkparity.ophelia.support.ui.Input;


/**
 * <b>Title:</b>thinkParity Support UI Action Factory<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ActionFactory {

    /** A map of ids to their classes. */
    private static final Map<String, Class<?>> ID_CLASS_MAP;

    /** A message format pattern for the illegal state exception. */
    private static final String ILLEGAL_STATE_INSTANTIATED_PATTERN;

    /** A singleton instance. */
    private static final ActionFactory SINGLETON;

    static {
        ID_CLASS_MAP = new HashMap<String, Class<?>>();
        ID_CLASS_MAP.put("/application/data/delete", com.thinkparity.ophelia.support.ui.action.application.data.Delete.class);
        ID_CLASS_MAP.put("/application/process/terminate", com.thinkparity.ophelia.support.ui.action.application.process.Terminate.class);
        ID_CLASS_MAP.put("/application/selecttab", com.thinkparity.ophelia.support.ui.action.application.SelectTab.class);
        ID_CLASS_MAP.put("/network/reloadtab", com.thinkparity.ophelia.support.ui.action.network.ReloadTab.class);
        ID_CLASS_MAP.put("/network/selecttab", com.thinkparity.ophelia.support.ui.action.network.SelectTab.class);
        ID_CLASS_MAP.put("/network/test", com.thinkparity.ophelia.support.ui.action.network.Test.class);
        ID_CLASS_MAP.put("/main/quit", com.thinkparity.ophelia.support.ui.action.main.Quit.class);
        ID_CLASS_MAP.put("/main/start", com.thinkparity.ophelia.support.ui.action.main.Start.class);

        ILLEGAL_STATE_INSTANTIATED_PATTERN = "Action \"{0}\" has already been instantiated.";

        SINGLETON = new ActionFactory();
    }

    /**
     * Obtain an action factory instance.
     * 
     * @return An <code>ActionFactory</code>.
     */
    public static ActionFactory getInstance() {
        return SINGLETON;
    }

    /**
     * Instantiate an illegal state exception for an already instantiated
     * action.
     * 
     * @param id
     *            A <code>String</code>.
     * @return An <code>IllegalStateException</code>.
     */
    private static IllegalStateException newIllegalStateInstantiated(final String id) {
        return new IllegalStateException(MessageFormat.format(
                ILLEGAL_STATE_INSTANTIATED_PATTERN, id));
    }

    /** An action registry. */
    private final ActionRegistry registry;

    /**
     * Create ActionFactory.
     *
     */
    private ActionFactory() {
        super();
        this.registry = new ActionRegistry();
    }

    /**
     * Instantiate a new action runner.
     * 
     * @param id
     *            A <code>String</code>.
     * @return An <code>ActionRunner</code>.
     */
    public ActionRunner newRunner(final String id) {
        final Action action;
        if (registry.isRegistered(id)) {
            action = registry.lookup(id);
        } else {
            action = newAction(id);
        }
        return new ActionRunner() {
            /**
             * @see com.thinkparity.ophelia.support.ui.action.ActionRunner#run()
             *
             */
            @Override
            public void run() {
                run(Input.emptyInput());
            }
            /**
             * @see com.thinkparity.ophelia.support.ui.action.ActionRunner#run(com.thinkparity.ophelia.support.ui.Input)
             *
             */
            @Override
            public void run(final Input input) {
                action.invoke(input);
            }
        };
    }

    /** 
    private static final Map<String, Class<? extends Action>> ID_CLASS_MAP;

    /**
     * Instantiate an action.
     * 
     * @param <T>
     *            An action <code>Type</code>.
     * @param actionClass
     *            A <code>Class<T></code>.
     * @return A <code>T</code>.
     */
    private Action newAction(final Class<?> actionClass) {
        try {
            final Action action = (Action) actionClass.newInstance();

            registry.register(action.getId(), action);
            return action;
        } catch (final IllegalAccessException iax) {
            throw new NoSuchActionException(actionClass);
        } catch (final InstantiationException ix) {
            throw new NoSuchActionException(actionClass);
        }
    }

    /**
     * Instantiate an action.
     * 
     * @param <T>
     *            An action <code>Type</code>.
     * @param actionClass
     *            A <code>Class<T></code>.
     * @return A <code>T</code>.
     */
    private Action newAction(final String id) {
        if (registry.isRegistered(id)) {
            throw newIllegalStateInstantiated(id);
        }

        return newAction(ID_CLASS_MAP.get(id));
    }
}
