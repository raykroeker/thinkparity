/*
 * Jan 2, 2006
 */
package com.thinkparity.browser.javax.swing.plaf.parity;

import javax.swing.UIDefaults;
import javax.swing.plaf.metal.MetalLookAndFeel;

import com.thinkparity.browser.ui.UIConstants;

import com.thinkparity.codebase.OS;
import com.thinkparity.codebase.OSUtil;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ParityLookAndFeel extends MetalLookAndFeel {

	private static final long serialVersionUID = 1;

	/**
	 * Current operating system.
	 * 
	 * @see OSUtil#getOS()
	 */
	private final OS os;

	/**
	 * Create a ParityLookAndFeel.
	 */
	public ParityLookAndFeel() {
		super();
		this.os = OSUtil.getOS();
	}

	/**
	 * @see javax.swing.LookAndFeel#getDescription()
	 * 
	 */
	public String getDescription() { return UIConstants.LookAndFeelDescription; }

	/**
	 * @see javax.swing.LookAndFeel#getID()
	 * 
	 */
	public String getID() { return UIConstants.LookAndFeelId; }

	/**
	 * @see javax.swing.LookAndFeel#getName()
	 * 
	 */
	public String getName() { return UIConstants.LookAndFeelName; }

	/**
	 * @see javax.swing.LookAndFeel#isNativeLookAndFeel()
	 * 
	 */
	public boolean isNativeLookAndFeel() { return Boolean.TRUE; }

	/**
	 * @see javax.swing.LookAndFeel#isNativeLookAndFeel()
	 * 
	 */
	public boolean isSupportedLookAndFeel() {
		if(os == OS.WINDOWS_XP) { return Boolean.TRUE; }
		else { return Boolean.FALSE; }
	}

	/**
	 * @see org.jvnet.substance.SubstanceLookAndFeel#initClassDefaults(javax.swing.UIDefaults)
	 */
	protected void initClassDefaults(UIDefaults table) {
		super.initClassDefaults(table);

		final String plafPackageName =
			"com.thinkparity.browser.javax.swing.plaf.parity.";
		final Object[] browserSubstanceDefaults = {
			"RootPaneUI", plafPackageName + "ParityRootPaneUI"
		};

		table.putDefaults(browserSubstanceDefaults);
	}
}
