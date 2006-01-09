/*
 * Jan 4, 2006
 */
package com.thinkparity.browser.javax.swing.plaf.parity.theme;

import org.jvnet.substance.theme.SubstanceTheme;

import com.thinkparity.browser.javax.swing.plaf.parity.color.iTunesColorScheme;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ParityTheme extends SubstanceTheme {

	/**
	 * Create a ParityTheme.
	 * 
	 */
	public ParityTheme() { super(new iTunesColorScheme(), "Parity", ThemeKind.COLD); }
}
