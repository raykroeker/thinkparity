/*
 * Jan 17, 2006
 */
package com.thinkparity.browser.javax.swing.session;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import com.thinkparity.browser.javax.swing.AbstractBrowserJPanel;
import com.thinkparity.browser.javax.swing.component.BrowserButtonFactory;
import com.thinkparity.browser.javax.swing.component.BrowserCheckBoxFactory;
import com.thinkparity.browser.javax.swing.component.BrowserLabelFactory;
import com.thinkparity.browser.javax.swing.component.BrowserTextFactory;

import com.thinkparity.model.parity.model.workspace.Preferences;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class LoginJPanel extends AbstractBrowserJPanel {

	private static final int INSET_LEFT = 25;

	/**
	 * @see java.io.Serializable
	 */
	private static final long serialVersionUID = 1;

	protected final Preferences preferences;

	/**
	 * Create a LoginJPanel.
	 * 
	 */
	public LoginJPanel() {
		super();
		this.preferences = getPreferences();
		
		setLayout(new GridBagLayout());

		// if the username is already set display a label
		add(createUsernameJLabel(), createUsernameJLabelConstraints());
		add(createRowFiller(), createRowFillerConstraints());

		if(preferences.isSetUsername()) {
			add(createUsernameJLabel2(), createUsernameJLabel2Constraints());
		}
		else {
			add(createUsernameJText(), createUsernameJTextConstraints());
		}
		add(createRowFiller(), createRowFillerConstraints());

		add(createPasswordJLabel(), createPasswordJLabelConstraints());
		add(createRowFiller(), createRowFillerConstraints());

		add(createPasswordJText(), createPasswordJTextConstraints());
		add(createRowFiller(), createRowFillerConstraints());

		add(createRememberPasswordJCheck(), createRememberPasswordJCheckConstraints());
		add(createRowFiller(), createRowFillerConstraints());

		add(createCancelJButton(), createCancelJButtonConstraints());
		add(createLoginJButton(), createLoginJButtonConstraints());
		add(createRowFiller(), createRowFillerConstraints());

		add(createFiller(), createFillerConstraints());
	}

	private Component createCancelJButton() {
		return BrowserButtonFactory.create("Cancel");
	}

	private Component createLoginJButton() {
		return BrowserButtonFactory.create("Login");
	}

	private Object createCancelJButtonConstraints() {
		return new GridBagConstraints(0, 5,
				1, 1,
				1.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0),
				0, 0);
	}

	private Object createLoginJButtonConstraints() {
		return new GridBagConstraints(1, 5,
				1, 1,
				0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0),
				0, 0);
	}

	private Component createRowFiller() { return createFiller(); }

	private Object createRowFillerConstraints() {
		return new GridBagConstraints(2, GridBagConstraints.RELATIVE,
				1, 1,
				1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0),
				0, 0);
	}

	private Component createFiller() { return BrowserLabelFactory.create("f"); }

	private Object createFillerConstraints() {
		return new GridBagConstraints(0, GridBagConstraints.RELATIVE,
				3, 1,
				1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0),
				0, 0);
	}

	private Component createPasswordJLabel() {
		return BrowserLabelFactory.create("Password:");
	}

	private Object createPasswordJLabelConstraints() {
		return new GridBagConstraints(0, 2,
				2, 1,
				1.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(0, INSET_LEFT, 2, 0),
				0, 0);
	}

	private Component createPasswordJText() {
		return BrowserTextFactory.create();
	}

	private Object createPasswordJTextConstraints() {
		return new GridBagConstraints(0, 3,
				2, 1,
				1.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(0, INSET_LEFT, 0, 0),
				0, 0);
	}

	private Component createRememberPasswordJCheck() {
		return BrowserCheckBoxFactory.create("Remember password");
	}

	private Object createRememberPasswordJCheckConstraints() {
		return new GridBagConstraints(0, 4,
				2, 1,
				1.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(0, INSET_LEFT, 0, 0),
				0, 0);
	}

	private Component createUsernameJLabel() {
		return BrowserLabelFactory.create("Parity username:");
	}

	private Component createUsernameJLabel2() {
		return BrowserLabelFactory.create(preferences.getUsername());
	}

	private Object createUsernameJLabel2Constraints() {
		return new GridBagConstraints(0, 1,
				2, 1,
				1.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(0, INSET_LEFT, 7, 0),
				0, 0);
	}

	private Object createUsernameJLabelConstraints() {
		return new GridBagConstraints(0, 0,
				2, 1,
				1.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(25, INSET_LEFT, 2, 0),
				0, 0);
	}
	
	private Component createUsernameJText() {
		return BrowserTextFactory.create();
	}
	
	private Object createUsernameJTextConstraints() {
		return new GridBagConstraints(0, 1,
				2, 1,
				1.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(0, INSET_LEFT, 0, 0),
				0, 0);
	}
}
