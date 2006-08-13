/*
 * Created On: Jul 30, 2006 12:05:39 PM
 */
package com.thinkparity.codebase.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class GradientPainter {

    private static final GradientPainter SINGLETON;

    static { SINGLETON = new GradientPainter(); }

    public static void paintVertical(final Graphics jPanelGraphics,
            final Dimension jPanelSize, final Color start, final Color finish) {
        SINGLETON.doPaintVertical((Graphics2D) jPanelGraphics, jPanelSize, start, finish);
    }

    private GradientPainter() { super(); }

    private void doPaintVertical(final Graphics2D jPanelGraphics,
            final Dimension jPanelSize, final Color start, final Color finish) {
        final Paint gPaint =
            new GradientPaint(0, 0, start, 0, jPanelSize.height, finish);
        jPanelGraphics.setPaint(gPaint);
        jPanelGraphics.fillRect(0, 0, jPanelSize.width, jPanelSize.height);
    }
}
