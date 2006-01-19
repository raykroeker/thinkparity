/*
 * Jan 19, 2006
 */
package com.thinkparity.browser.javax.swing.browser;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;

import com.thinkparity.browser.javax.swing.AbstractJPanel;
import com.thinkparity.browser.javax.swing.component.BrowserLabelFactory;
import com.thinkparity.browser.model.ModelFactory;
import com.thinkparity.browser.model.NetworkStatus;

import com.thinkparity.model.parity.model.session.SessionModel;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class StatusJPanel extends AbstractJPanel {

	/**
	 * @see java.io.Serializable
	 */
	private static final long serialVersionUID = 1;

	private Component networkStatusJLabel;

	/**
	 * Create a StatusJPanel.
	 */
	public StatusJPanel() {
		super("StatusJPanel");

		setLayout(new GridBagLayout());
		addNetworkStatusJLabel();
		set(getCurrentNetworkStatus());
	}

	public void set(final NetworkStatus networkStatus) {
		((JLabel) networkStatusJLabel).setText(getString(networkStatus));
	}

	private void addNetworkStatusJLabel() {
		if(null == networkStatusJLabel) {
			networkStatusJLabel = createNetworkStatusJLabel();
		}
		add(networkStatusJLabel, createNetworkStatusJLabelConstraints());
	}

	private Component createNetworkStatusJLabel() {
		return BrowserLabelFactory.create();
	}

	private Object createNetworkStatusJLabelConstraints() {
		return new GridBagConstraints(0, 0,
				1, 1,
				1.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0),
				0, 0);
	}

	private NetworkStatus getCurrentNetworkStatus() {
		final SessionModel sessionModel = ModelFactory.getInstance().getSessionModel(getClass());
		if(sessionModel.isLoggedIn()) { return NetworkStatus.ONLINE; }
		else { return NetworkStatus.OFFLINE; }
	}

	private String getString(final NetworkStatus networkStatus) {
		return getString(networkStatus.toString());
	}
}
