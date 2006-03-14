/*
 * Feb 11, 2006
 */
package com.thinkparity.browser.util;

import org.apache.log4j.Logger;

import com.thinkparity.browser.application.browser.BrowserWindow;
import com.thinkparity.browser.application.browser.window.HistoryWindow;
import com.thinkparity.browser.application.gadget.GadgetWindow;
import com.thinkparity.browser.platform.application.window.Window;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.log4j.ModelLoggerFactory;

import com.l2fprod.gui.nativeskin.NativeConstants;
import com.l2fprod.gui.nativeskin.NativeSkin;
import com.l2fprod.gui.region.Region;
import com.l2fprod.gui.region.RegionBuilder;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class NativeSkinUtil {

	/**
	 * Singleton instance.
	 * 
	 */
	private static final NativeSkinUtil singleton;

	static { singleton = new NativeSkinUtil(); }

	/**
	 * Apply the native skin to the gadget window.
	 * 
	 * @param gadgetWindow
	 *            The gadget window.
	 */
	public static void applyNativeSkin(final GadgetWindow gadgetWindow) {
		singleton.doApplyNativeSkin(gadgetWindow);
	}

	/**
	 * Apply a native skin to a parity window.
	 * 
	 * @param window
	 *            The parity window.
	 */
	public static void applyNativeSkin(final HistoryWindow historyWindow) {
		singleton.doApplyNativeSkin(historyWindow);
	}

	public static void applyNativeSkin(final Window window) {}

	/**
	 * Apply the native skin to the browser window.
	 * 
	 * @param browserWindow
	 *            The browser window.
	 */
	public static void applyNativeSkin(final BrowserWindow browserWindow) {
		singleton.doApplyNativeSkin(browserWindow);
	}

	/**
	 * An apache logger.
	 * 
	 */
	private final Logger logger;

	/**
	 * The native skin.
	 * 
	 */
	private final NativeSkin nativeSkin;

	/**
	 * Create a NativeSkinUtil [Singleton]
	 * 
	 */
	private NativeSkinUtil() {
		super();
		this.logger = ModelLoggerFactory.getLogger(getClass());
		this.nativeSkin = NativeSkin.getInstance();
		logger.info("[VERSION] " + NativeConstants.VERSION);
	}

	/**
	 * Apply the native skin to the gadget window.
	 * 
	 * @param gadgetWindow
	 *            The gadget window.
	 */
	private void doApplyNativeSkin(final GadgetWindow gadgetWindow) {
//		final Region region =
//			new RegionBuilder().createRegion(GadgetWindow.getImage());
//		nativeSkin.setWindowRegion(gadgetWindow, region, true);
	}

	/**
	 * Apply the native skin to the browser window.
	 * 
	 * @param browserWindow
	 *            The browser window.
	 */
	private void doApplyNativeSkin(final BrowserWindow browserWindow) {
//		final Dimension browserWindowSize = BrowserWindow.getMainWindowSize();
//		final RegionBuilder regionBuilder = new RegionBuilder();
//		final Region region =
//			regionBuilder.createRoundRectangleRegion(
//					0, 0, browserWindowSize.width + 1, BrowserConstants.TitlePaneHeight * 2,
//					BrowserConstants.TitlePaneCurvature,
//					BrowserConstants.TitlePaneCurvature);
//		final Region region2 = regionBuilder.createRectangleRegion(0,
//				BrowserConstants.TitlePaneHeight, browserWindowSize.width, browserWindowSize.height);
//		final Region region3 = regionBuilder.combineRegions(region, region2,
//				NativeConstants.REGION_OR);
//		NativeSkin.getInstance().setWindowRegion(browserWindow, region3, true);
	}

	/**
	 * Apply a native skin to a parity window.
	 * 
	 * @param window
	 *            The parity window.
	 */
	private void doApplyNativeSkin(final Window window) {
	}

	/**
	 * Apply a native skin to the parity history window.
	 * 
	 * @param historyWindow
	 *            The parity history window.
	 */
	private void doApplyNativeSkin(final HistoryWindow historyWindow) {
		final Region region =
			new RegionBuilder().createRegion(HistoryWindow.getImage());
		nativeSkin.setWindowRegion(historyWindow, region, true);
	}
}
