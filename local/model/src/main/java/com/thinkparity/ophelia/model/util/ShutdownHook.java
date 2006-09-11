/*
 * Created On: Aug 10, 2006 11:41:59 AM
 */
package com.thinkparity.ophelia.model.util;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class ShutdownHook implements Runnable, Comparable<ShutdownHook> {

    /**
     * Create ShutdownHook.
     */
    protected ShutdownHook() { super(); }

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(final ShutdownHook o) {
        return getPriority().compareTo(o.getPriority());
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if(null != obj && obj instanceof ShutdownHook) {
            return ((ShutdownHook) obj).getName().equals(getName());
        }
        return false;
    }

    /**
     * Obtain the shutdown hook description.
     * 
     * @return The shut down hook description.
     */
    public abstract String getDescription();

    /**
     * Obtain the shutdown hook name.
     * 
     * @return The shutdown hook name.
     */
    public abstract String getName();

    /**
     * Obtain the shutdown hook priority.
     * 
     * @return The shutdown hook priority.
     */
    public abstract Integer getPriority();

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() { return getName().hashCode(); }

    /**
     * @see java.lang.Runnable#run()
     * 
     */
    public abstract void run();

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new StringBuffer(getClass().getName()).append("//")
                .append(getName()).append("/")
                .append(getPriority())
                .toString();
    }
}
