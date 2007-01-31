/*
 * Created On:  4-Dec-06 5:18:04 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.ophelia.browser.Constants.Colors;
import com.thinkparity.ophelia.browser.util.ImageIOUtil;

/**
 * <b>Title:</b>thinkParity Tab Panel Background Renderer<br>
 * <b>Description:</b>The background renderer is used within the panel's
 * overridden paintComponent methods to provide the visual imagery across to
 * panels.<br>
 * There are twelve separate images used to create the background. A primary
 * background provides backdrop on top of which all other images are drawn.
 * Another larger image provides the eastern half of the panel; and two sets of
 * smaller images (one for each visible row) provide the selection paradigm.<br>
 * The images are initially all scaled to the width of the primary desktop space
 * then clipped as needed to provide a "cached" (correct size for display)
 * image. This image is then displayed every time there is a request to paint a
 * background.
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class TabRenderer {

    /** The underlying background <code>Image</code>. */
    private static Image BACKGROUND;

    /** The east background <code>Image</code>. */
    private static Image BACKGROUND_EAST;

    /** The east background when row 0 is selected <code>Image</code>. */
    private static Image BACKGROUND_EAST_ROW0_SELECTED;

    /** The fields background, center <code>Image</code>. */
    private static Image BACKGROUND_FIELDS_CENTER;

    /** The fields background, east <code>Image</code>. */
    private static Image BACKGROUND_FIELDS_EAST;

    /** The fields background, west <code>Image</code>. */
    private static Image BACKGROUND_FIELDS_WEST;

    /** The fields background, center <code>Image</code>. */
    private static BufferedImage backgroundFieldsCenter;

    /** The version panel's center background <code>BufferedImage</code>. */ 
    private static BufferedImage[] VERSION_IMAGES_CENTER;

    /** The version panel's west background <code>BufferedImage</code>s. */
    private static Image[] VERSION_IMAGES_WEST;

    /** The version panel's west background <code>BufferedImage</code>s. */
    private static BufferedImage[] versionImagesWest;

    static {
        initialize();
    }

    /**
     * Clip an image and return the clipped portion. An offscreen image is
     * created and the source is drawn onto it. The x and y coordinates are the
     * location within the source image that is clipped; as well the width and
     * height are dimensions within the source that are clipped.
     * 
     * @param image
     *            An <code>Image</code>.
     * @param x
     *            An x coordinate <code>int</code>.
     * @param y
     *            A y coordinate <code>int</code>.
     * @param width
     *            An width <code>int</code>.
     * @param height
     *            A height <code>int</code>.
     * @param observer
     *            An <code>ImageObserver</code>.
     * @return The clipped <code>BufferedImage</code>.
     */
    private static BufferedImage clipImage(final Image image, final int x,
            final int y, final int width, final int height,
            final ImageObserver observer) {
        final BufferedImage clippedImage = SwingUtil.createImage(width, height);
        final Graphics gImage = clippedImage.getGraphics();
        try {
            gImage.drawImage(image, 0, 0, width, height, x, y, x + width, y
                    + height, observer);
        } finally {
            gImage.dispose();
        }
        return clippedImage;
    }

    /**
     * Initialize the background renderer. This will load the appropriate images
     * the first time.
     * 
     */
    private static void initialize() {
        Rectangle bounds = SwingUtil.getPrimaryDesktopBounds();
        BufferedImage buffer = ImageIOUtil.read("PanelBackground.png");
        BACKGROUND = buffer.getScaledInstance(bounds.width, buffer.getHeight(),
                Image.SCALE_SMOOTH);
        buffer = ImageIOUtil.read("PanelBackgroundEast.png");
        BACKGROUND_EAST = buffer.getScaledInstance(bounds.width,
                buffer.getHeight(), Image.SCALE_SMOOTH);
        buffer = ImageIOUtil.read("PanelBackgroundEast_0Selected.png");
        BACKGROUND_EAST_ROW0_SELECTED = buffer.getScaledInstance(bounds.width,
                buffer.getHeight(), Image.SCALE_SMOOTH);
        buffer = ImageIOUtil.read("PanelBackgroundFieldsCenter.png");        
        BACKGROUND_FIELDS_CENTER = buffer.getScaledInstance(bounds.width,
                buffer.getHeight(), Image.SCALE_SMOOTH);
        BACKGROUND_FIELDS_EAST = ImageIOUtil.read("PanelBackgroundFieldsEast.png");
        BACKGROUND_FIELDS_WEST = ImageIOUtil.read("PanelBackgroundFieldsWest.png");
        VERSION_IMAGES_CENTER = new BufferedImage[] {
                ImageIOUtil.read("PanelBackgroundCenter0.png"),
                ImageIOUtil.read("PanelBackgroundCenter1.png"),
                ImageIOUtil.read("PanelBackgroundCenter2.png"),
                ImageIOUtil.read("PanelBackgroundCenter3.png"),
                ImageIOUtil.read("PanelBackgroundCenter4.png"),
                ImageIOUtil.read("PanelBackgroundCenter5.png")
        };
        VERSION_IMAGES_WEST = new Image[VERSION_IMAGES_CENTER.length];
        buffer = ImageIOUtil.read("PanelBackgroundWest0.png");
        VERSION_IMAGES_WEST[0] = buffer.getScaledInstance(bounds.width, buffer
                .getHeight(), Image.SCALE_SMOOTH);
        buffer = ImageIOUtil.read("PanelBackgroundWest1.png");
        VERSION_IMAGES_WEST[1] = buffer.getScaledInstance(bounds.width, buffer
                .getHeight(), Image.SCALE_SMOOTH);
        buffer = ImageIOUtil.read("PanelBackgroundWest2.png");
        VERSION_IMAGES_WEST[2] = buffer.getScaledInstance(bounds.width, buffer
                .getHeight(), Image.SCALE_SMOOTH);
        buffer = ImageIOUtil.read("PanelBackgroundWest3.png");
        VERSION_IMAGES_WEST[3] = buffer.getScaledInstance(bounds.width, buffer
                .getHeight(), Image.SCALE_SMOOTH);
        buffer = ImageIOUtil.read("PanelBackgroundWest4.png");
        VERSION_IMAGES_WEST[4] = buffer.getScaledInstance(bounds.width, buffer
                .getHeight(), Image.SCALE_SMOOTH);
        buffer = ImageIOUtil.read("PanelBackgroundWest5.png");
        VERSION_IMAGES_WEST[5] = buffer.getScaledInstance(bounds.width, buffer
                .getHeight(), Image.SCALE_SMOOTH);
        versionImagesWest = new BufferedImage[VERSION_IMAGES_WEST.length];
        buffer = null;
        bounds = null;
    }

    /**
     * Determine if the image destined for the component is dirty; ie needs to
     * be clipped before drawing.
     * 
     * @param image
     *            A <code>BufferedImage</code>.
     * @param width
     *            The image <code>int</code> width.
     * @param height
     *            The image <code>int</code> height.
     * @return True if the image needs to be clipped before drawing.
     */
    private static boolean isDirty(final BufferedImage image, final int width,
            final int height) {
        return null == image || image.getWidth() != width;
    }

    /**
     * Create TabRenderer.
     *
     */
    public TabRenderer() {
        super();
    }

    /**
     * Paint the background for the collapsed panel.
     * 
     * @param g
     *            A <code>Graphics</code> context.
     * @param width
     *            A width <code>int</code>.
     * @param height
     *            A height <code>int</code>.
     * @param selected
     *            A selected <code>Boolean</code>.            
     */
    public void paintBackground(final Graphics g, final int width, final int height, final Boolean selected) {
        final Graphics2D g2 = (Graphics2D)g.create();
        try {
            // Paint the background for a collapsed panel.
            // This is a simple color fill.
            g2.setColor(Colors.Browser.Panel.PANEL_COLLAPSED_BACKGROUND);
            g2.fillRect(0, 0, width, height);
            
            // For selection, draw a dashed line around the perimeter.
            if (selected) {
                final Path2D path = getSelectionPath(width, height);
                final float dashes[] = {1};
                g2.setStroke( new BasicStroke( 1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 10.0f, dashes, 0));
                g2.setColor(Colors.Browser.Panel.PANEL_COLLAPSED_SELECTION_LINE); 
                g2.draw(path);
            }
        }
        finally { g2.dispose(); }
    }

    /**
     * Paint the expanded background for the panel.
     * 
     * @param g
     *            A <code>Graphics</code> context.
     * @param observer
     *            An <code>ImageObserver</code>.
     */
    public void paintExpandedBackground(final Graphics g, final ImageObserver observer) {
        /*
         * paint the background for an expanded panel - this involves simply
         * painting the main background image at the top
         */
        g.drawImage(BACKGROUND, 0, 0, observer);
    }

    /**
     * Paint the background for the version panel.
     * 
     * @param g
     *            The panel <code>Graphics</code>.
     * @param width
     *            A width <code>int</code>.
     * @param height
     *            A height <code>int</code>.
     * @param selectionIndex
     *            The <code>int</code> selection index.
     * @param observer
     *            An <code>ImageObserver</code>.              
     */
    public void paintExpandedBackgroundCenter(final Graphics g,
            final int width, final int height, final int selectionIndex,
            final ImageObserver observer) {
        /*
         * paint a vertial bar in the center of the panel based upon selection
         * 
         * the bar is not scaled
         */
        g.drawImage(VERSION_IMAGES_CENTER[selectionIndex], width
                - VERSION_IMAGES_CENTER[selectionIndex].getWidth(), 24,
                observer);
    }

    /**
     * Paint the eastern background.
     * 
     * @param g
     *            The <code>Graphics</code>.
     * @param x
     *            The <code>int</code> x coordinate.
     * @param height
     *            A height <code>int</code>.
     * @param selectionIndex
     *            The <code>int</code> selection index.
     * @param observer
     *            An <code>ImageObserver</code>.    
     */
    public void paintExpandedBackgroundEast(final Graphics g, final int x,
            final int height, final int selectionIndex,
            final ImageObserver observer) {
        // paint a solid gradient image on the eastern side of the version panel
        if (selectionIndex==0) {
            g.drawImage(BACKGROUND_EAST_ROW0_SELECTED, x, 0, observer);
        } else {
            g.drawImage(BACKGROUND_EAST, x, 0, observer);
        }
    }

    /**
     * Paint the background for a field.
     * 
     * @param g
     *            The <code>Graphics</code>.
     * @param x
     *            The <code>int</code> x coordinate.
     * @param width
     *            A width <code>int</code>.
     * @param height
     *            A height <code>int</code>.
     * @param observer
     *            The <code>ImageObserver</code>.
     */
    public void paintExpandedBackgroundFields(final Graphics g,
            final int x, final int width, final int height, final ImageObserver observer) {
        /*
         * The west and east images are each 3 pixels wide. They are not scaled.
         * The centre image is scaled in the X direction to fill the desired width.
         * The width is adjusted so it does not paint all the way to the right.
         */
        final int adjustedWidth = width - 140;
        g.drawImage(BACKGROUND_FIELDS_WEST, x, 0, observer);
        if (isDirty(backgroundFieldsCenter, adjustedWidth-6, height)) {
            backgroundFieldsCenter = clipImage(
                    BACKGROUND_FIELDS_CENTER, 0, 0, adjustedWidth-6, height,
                    observer);
        }
        g.drawImage(backgroundFieldsCenter, x + 3, 0, observer);     
        g.drawImage(BACKGROUND_FIELDS_EAST, x + adjustedWidth - 3, 0, observer);
    }

    /**
     * Paint the background for the version panel.
     * 
     * @param g
     *            A <code>Graphics</code> context.
     * @param width
     *            A width <code>int</code>.
     * @param height
     *            A height <code>int</code>.
     * @param selectionIndex
     *            The <code>int</code> selection index.
     * @param observer
     *            An <code>ImageObserver</code>.      
     */
    public void paintExpandedBackgroundWest(final Graphics g, final int width,
            final int height, final int selectionIndex,
            final ImageObserver observer) {
        /*
         * from the finite set of selection images grab the one matching the
         * selection index; and paint it
         *
         * the number 26 is the height of the each selected row image (ie. row
         * height of 24 plus 2 extra pixels).
         * 
         * the number 24 is the offset at which to draw each selected row
         */
        final int rowHeight;
        final int rowOffset;
        rowHeight = 26;
        rowOffset = selectionIndex * 24;
        if (isDirty(versionImagesWest[selectionIndex], width, height)) {
            versionImagesWest[selectionIndex] = clipImage(
                    VERSION_IMAGES_WEST[selectionIndex], 0, 0, width, rowHeight,
                    observer);
        }
        g.drawImage(versionImagesWest[selectionIndex], 0, rowOffset, observer);
    }

    /**
     * Get a path for the selection line.
     * 
     * @param width
     *            A width <code>int</code>.
     * @param height
     *            A height <code>int</code>.
     * @return A <code>Path2D</code>.
     */
    private Path2D getSelectionPath(final int width, final int height) {
        final Path2D path = new Path2D.Double();           
        final int insetX = 6;   // Inset from the left, right
        final int insetY = 2;   // Inset from the top, bottom
        final int radius = 2;   // Radius of the curves on the corners
        final int quad = 0;     // Inset to the control point for defining curves
        final int minX = insetX;
        final int maxX = width - insetX - 1;
        final int minY = insetY;
        final int maxY = height - insetY - 1;
        path.moveTo(minX + radius, minY);
        path.lineTo(maxX - radius, minY);
        path.quadTo(maxX - quad, minY + quad, maxX, minY + radius);
        path.lineTo(maxX, maxY - radius);
        path.quadTo(maxX - quad, maxY - quad, maxX - radius, maxY);
        path.lineTo(minX + radius, maxY);
        path.quadTo(minX + quad, maxY - quad, minX, maxY - radius);
        path.lineTo(minX, minY + radius);
        path.quadTo(minX + quad, minY + quad, minX + radius, minY);
        return path;
    }
}
