/*
 * Apr 18, 2004
 */
package com.thinkparity.codebase;

import java.util.Vector;

/**
 * <b>Title:</b>  CommandLine
 * <br><b>Description:</b>  
 * @author raymond@raykroeker.com
 * @version x.x.x Build x
 */
public class CommandLine {

	private Vector<String> args = new Vector<String>(10);

	/**
	 * Create a new CommandLine
	 * args <code>java.lang.String[]</code>
	 */
	public CommandLine(String[] args) {
		super();
		processArgs(args);
	}
	
	public boolean contains(String arg) {
		if(null != arg) {
			return this.args.contains(arg);
		}
		return false;
	}

	private void processArgs(String[] args) {
		for(int i = 0; i < args.length; i++) {
			this.args.add(args[i]);
		}
	}

}
