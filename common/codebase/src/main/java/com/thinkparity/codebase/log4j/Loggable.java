/*
 * Dec 28, 2004
 */
package com.thinkparity.codebase.log4j;

/**
 * Loggable
 * @author raykroeker@gmail.com
 */
public interface Loggable {

	/**
	 * Common interface used by <code>LoggerFormatter</code> to output java
	 * objects.
	 * 
	 * @return <code>java.lang.StringBuffer</code>
	 */
    public StringBuffer logMe();
}
