/*
 * May 14, 2005
 */
package com.thinkparity.model.xmpp.user;

import com.thinkparity.codebase.log4j.Loggable;

public class User implements Loggable {

	public enum State {
		ACCEPTED, DECLINED, PENDING;
	}

	private final String name;
	private final State state;
	private final String username;

	public User(final String name, final String username, final State state) {
		super();
		this.name = name;
		this.username = username;
		this.state = state;
	}

	/**
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new User(name, username, state);
	}

	public String getName() { return name; }

	/**
	 * Obtain state.
	 * @return String
	 */
	public State getState() { return state; }

	public String getUsername() { return username; }

	public StringBuffer logMe() {
		return new StringBuffer("<user>")
			.append("<name>").append(name).append("</name>")
			.append("<state>").append(state).append("</state>")
			.append("<username>").append(username).append("</username>")
			.append("</user>");
	}
}
