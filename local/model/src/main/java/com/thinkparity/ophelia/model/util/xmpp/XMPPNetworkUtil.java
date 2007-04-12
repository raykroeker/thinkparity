/*
 * Created On:  2007-04-11 12:38 -0700 
 */
package com.thinkparity.ophelia.model.util.xmpp;

import com.thinkparity.ophelia.model.io.xmpp.XMPPMethod;

/**
 * <b>Title:</b>thinkParity OpheliaModel XMPP Method Network Utiltity<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class XMPPNetworkUtil {

	private static final long DEFAULT_EXECUTION_TIME;

	private static final long DEFAULT_SERIALIZATION_TIME;

	private static final long MINIMUM_EXECUTION_TIME;

	private static final long MINIMUM_SERIALIZATION_TIME;

	static {
		DEFAULT_EXECUTION_TIME = 11250L;
		DEFAULT_SERIALIZATION_TIME = 75L;
		MINIMUM_EXECUTION_TIME = 7500L;
		MINIMUM_SERIALIZATION_TIME = 50L;
	}

	/** The total method exection time. */
	private long executionTime;

	/** The method parameter xml serialization time. */
	private long serializationTime;

	/** The <code>XMPPCore</code>. */
	private final XMPPCore xmppCore;

	/**
	 * Create XMPPMethodNetworkUtil.
	 * 
	 */
	XMPPNetworkUtil(final XMPPCore xmppCore) {
		super();
		this.xmppCore = xmppCore;
	}

	/**
	 * Calculate an appropriate timeout in order to execute the method. The
	 * timeout is calculated based upon an initial baseline execution along with
	 * the number of parameters involved in the method invocation.
	 * 
	 * @param method
	 *            An <code>XMPPMethod</code>.
	 * @return A timeout number of milliseconds <code>Long</code>.
	 */
	public Long calculateTimeout(final XMPPMethod method) {
		// the baseline execution duration
		long timeout = executionTime - serializationTime;
		// the serialization of parameters
		timeout += (serializationTime * method.getParameterSize());
		// fudge 25%
		return Long.valueOf((long) (timeout * 1.25));
	}

	/**
	 * Initialize a baseline for xmpp network timeouts by excuting a basic
	 * remote method. This timeout will include parameter xml serialization the
	 * remote call and result xml serialization.
	 * 
	 */
	void initializeBaseline() {
		final XMPPMethod readDateTime = new XMPPMethod("system:readdatetime");
		readDateTime.setParameter("userId", xmppCore.getUserId());
		xmppCore.execute(readDateTime, Boolean.TRUE);
		executionTime = readDateTime.getExecutionTime().longValue();
		if (executionTime < MINIMUM_EXECUTION_TIME)
			executionTime = DEFAULT_EXECUTION_TIME;
		serializationTime = readDateTime.getSerializationTime();
		if (serializationTime < MINIMUM_SERIALIZATION_TIME)
			serializationTime = DEFAULT_SERIALIZATION_TIME;
	}
}
