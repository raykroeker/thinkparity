/*
 * Jan 2, 2006
 */
package com.thinkparity.browser.javax.swing.plaf.parity;

import javax.swing.UIDefaults;

import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.painter.SpecularGradientPainter;
import org.jvnet.substance.theme.SubstanceTheme;
import org.jvnet.substance.watermark.SubstanceNullWatermark;

import com.thinkparity.codebase.OS;
import com.thinkparity.codebase.OSUtil;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ParityLookAndFeel extends SubstanceLookAndFeel {

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
	public ParityLookAndFeel(final SubstanceTheme substanceTheme) {
		super();
		this.os = OSUtil.getOS();

		setCurrentGradientPainter(new SpecularGradientPainter());
		setCurrentTheme(substanceTheme);
		setCurrentWatermark(new SubstanceNullWatermark());
	}

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

		final String browserSubstancePackageName =
			"com.thinkparity.browser.javax.swing.plaf.parity.";
		final Object[] browserSubstanceDefaults = {
			"RootPaneUI", browserSubstancePackageName + "ParityRootPaneUI"
		};

		table.putDefaults(browserSubstanceDefaults);
	}
}
