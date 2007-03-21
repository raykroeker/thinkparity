/*
 * Created On: Jul 21, 2006 2:43:25 PM
 */
package com.thinkparity.codebase;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public enum Application {

    DESDEMONA("Desdemona - thinkParity XMPP Server", new ApplicationNature[] {ApplicationNature.XMPP}),

    OPHELIA("Ophelia - thinkParity Client", new ApplicationNature[] {ApplicationNature.SWING, ApplicationNature.HTTP});

    /** The application name. */
    private final String name;

    /** The nature of an application. */
    private final ApplicationNature[] natures;

    /**
     * Create an application.
     * 
     * @param name
     *            An application name.
     * @param natures
     *            A list of application natures.
     */
    private Application(final String name, final ApplicationNature[] natures) {
        this.name = name;
        this.natures = natures;
    }

    /**
     * Obtain the application name.
     * 
     * @return The application name.
     */
    public String getName() { return name; }

    /**
     * Obtain the natures
     *
     * @return An array of natures.
     */
    public ApplicationNature[] getNatures() { return natures; }

    /**
     * Determine if the application is of the specified nature.
     * 
     * @param nature
     *            An application nature.
     * @return True if the application is of the nature; false otherwise.
     */
    public Boolean isNatured(final ApplicationNature nature) {
        for(int i = 0; i < natures.length; i++) {
            if(natures[i] == nature) { return Boolean.TRUE; }
        }
        return Boolean.FALSE;
    }
}
