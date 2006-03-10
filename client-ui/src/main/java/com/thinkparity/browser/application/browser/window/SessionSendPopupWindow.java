/*
 * Mar 9, 2006
 */
package com.thinkparity.browser.application.browser.window;

import com.thinkparity.browser.javax.swing.AbstractJDialog;
import com.thinkparity.browser.javax.swing.AbstractJFrame;
import com.thinkparity.browser.platform.application.window.Window;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class SessionSendPopupWindow extends Window {

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	public SessionSendPopupWindow(final AbstractJFrame owner) {
		super(owner, Boolean.TRUE, "SessionSendPopupWindow");
		initComponents();
	}


	private void initComponents() {}

	/**
	 * @see com.thinkparity.browser.platform.application.window.Window#getId()
	 */
	@Override
	public WindowId getId() {
		// TODO Auto-generated method stub
		return null;
	}

}
