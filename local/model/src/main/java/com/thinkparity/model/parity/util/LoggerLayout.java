/*
 * May 15, 2005
 */
package com.thinkparity.model.parity.util;

import org.apache.log4j.PatternLayout;

class LoggerLayout extends PatternLayout {

	/**
	 * Create a LoggerLayout
	 * Uses the same pattern as specified in TTCCLayout.
	 */
	LoggerLayout() { super("%n[%d{yyyy.MM.dd HH.mm.ss}] [%t] [%p] %m%n"); }
}
