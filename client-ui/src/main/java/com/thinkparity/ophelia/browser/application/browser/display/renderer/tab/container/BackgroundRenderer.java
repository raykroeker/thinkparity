/*
 * Created On:  4-Dec-06 5:18:04 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
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
 * 5 smaller images provide the selection paradigm.<br>
 * The images are initially all scaled to the width of the primary desktop space
 * then clipped as needed to provide a "cached" (correct size for display)
 * image. This image is then displayed every time there is a request to paint a
 * background.
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class BackgroundRenderer {

    /** The underlying background <code>Image</code>. */
    private static final Image BACKGROUND;

    /** The east background <code>Image</code>. */
    private static final Image BACKGROUND_EAST;

    /** The container panel's expanded background <code>BufferedImage</code>. */ 
    private static BufferedImage containerImageExpanded;

    /** The container version panel's center background <code>BufferedImage</code>. */ 
    private static final BufferedImage[] VERSION_IMAGES_CENTER;

    /** The container version panel's western backgroudn <code>BufferedImage</code>s. */
    private static final Image[] VERSION_IMAGES_WEST;

    /** The container version panel's background <code>BufferedImage</code>. */ 
    private static BufferedImage versionImage;

    /** The container version panel's east background <code>BufferedImage</code>. */ 
    private static BufferedImage versionImageEast;

    /** The container version panel's west background <code>BufferedImage</code>s. */
    private static final BufferedImage[] versionImagesWest;

    static {
        Rectangle bounds = SwingUtil.getPrimaryDesktopBounds();
        BufferedImage buffer = ImageIOUtil.read("PanelBackground.png");
        BACKGROUND = buffer.getScaledInstance(bounds.width, buffer.getHeight(),
                Image.SCALE_SMOOTH);
        buffer = ImageIOUtil.read("PanelBackgroundEast.png");
        BACKGROUND_EAST = buffer.getScaledInstance(bounds.width,
                buffer.getHeight(), Image.SCALE_SMOOTH);
        VERSION_IMAGES_CENTER = new BufferedImage[] {
                ImageIOUtil.read("PanelBackgroundCenter0.png"),
                ImageIOUtil.read("PanelBackgroundCenter1.png"),
                ImageIOUtil.read("PanelBackgroundCenter2.png"),
                ImageIOUtil.read("PanelBackgroundCenter3.png"),
                ImageIOUtil.read("PanelBackgroundCenter4.png")
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
        versionImagesWest = new BufferedImage[VERSION_IMAGES_WEST.length];
        buffer = null;
        bounds = null;
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
     * Determine if the image destined for the component is dirty; ie needs to
     * be clipped before drawing.
     * 
     * @param component
     *            A <code>Component</code>.
     * @param image
     *            An <code>Image</code>.
     * @param imageSize
     *            The image size <code>Dimension</code>.
     * @return True if the image needs to be clipped before drawing.
     */
    private static boolean isDirty(final BufferedImage image, final int width,
            final int height) {
        return null == image || image.getWidth() != width;
    }

    /**
     * Create BackgroundRenderer.
     *
     */
    BackgroundRenderer() {
        super();
    }

    /**
     * Paint the background for the container panel.
     * 
     * @param g
     *            The panel <code>Graphics</code>.
     * @param panel
     *            A <code>ContainerPanel</code>.
     */
    void paintBackground(final Graphics g, final ContainerPanel panel) {
        /*
         * paint a background for a non-selected container panel - a simple
         * color fill
         */
        g.setColor(Colors.Browser.List.LIST_CONTAINERS_BACKGROUND);
        g.fillRect(0, 0, panel.getWidth(), panel.getHeight());
    }

    /**
     * Paint the background for the container version panel.
     * 
     * @param g
     *            The panel <code>Graphics</code>.
     * @param panel
     *            A <code>ContainerVersionPanel</code>.
     */
    void paintBackground(final Graphics g, final ContainerVersionPanel panel) {
        /*
         * paint the entire background with a top-to-bottom gradient that starts
         * in the container panel and extends to the bottom of the version
         * panel.
         * 
         * the number 23 is derived by taking the height of the container panel
         */
        if (isDirty(versionImage, panel.getWidth(), panel.getHeight())) {
            versionImage = clipImage(BACKGROUND, 0, 23, panel.getWidth(),
                    panel.getHeight(), panel);
        }
        g.drawImage(versionImage, 0, 0, panel);
    }

    /**
     * Paint the background for the container version panel.
     * 
     * @param g
     *            The panel <code>Graphics</code>.
     * @param panel
     *            A <code>ContainerVersionPanel</code>.
     */
    void paintBackgroundCenter(final Graphics g,
            final ContainerVersionPanel panel, final int selectionIndex) {
        /*
         * paint a vertial bar in the center of the panel based upon selection
         * 
         * the bar is not scaled
         */
        g.drawImage(VERSION_IMAGES_CENTER[selectionIndex], panel.getWidth() / 2
                - VERSION_IMAGES_CENTER[selectionIndex].getWidth() + 1, 0, panel);
    }

    /**
     * Paint the background for the container version panel.
     * 
     * @param g
     *            The panel <code>Graphics</code>.
     * @param panel
     *            A <code>ContainerVersionPanel</code>.
     */
    void paintBackgroundEast(final Graphics g, final ContainerVersionPanel panel) {
        // paint a solid gradient image on the eastern side of the version panel
        if (isDirty(versionImageEast, panel.getWidth() / 2, panel.getHeight() / 2)) {
            versionImageEast = clipImage(BACKGROUND_EAST, 0, 0, panel
                    .getWidth() / 2, panel.getHeight(), panel);
        }
        g.drawImage(versionImageEast, panel.getWidth() / 2 + 1, 0, panel);
    }

    /**
     * Paint the background for the container version panel.
     * 
     * @param g
     *            The panel <code>Graphics</code>.
     * @param panel
     *            A <code>ContainerVersionPanel</code>.
     * @param selectionIndex
     *            The selection index for the indices.
     */
    void paintBackgroundWest(final Graphics g,
            final ContainerVersionPanel panel, final int selectionIndex) {
        /*
         * from the finite set of selection images grab the one matching the
         * selection index; and paint it
         *
         * the number 26 is the height of the each selected row image
         * 
         * the number 24 is the offset at which to draw each selected row
         */
        if (isDirty(versionImagesWest[selectionIndex], panel.getWidth() / 2, panel.getHeight())) {
            versionImagesWest[selectionIndex] = clipImage(
                    VERSION_IMAGES_WEST[selectionIndex], 0, 0,
                    panel.getWidth() / 2, 26, panel);
        }
        g.drawImage(versionImagesWest[selectionIndex], 0, selectionIndex * 24, panel);
    }

    /**
     * Paint the expanded background for the container panel.
     * 
     * @param g
     *            The panel <code>Graphics</code>.
     * @param panel
     *            The <code>ContainerPanel</code>.
     */
    void paintExpandedBackground(final Graphics g, final ContainerPanel panel) {
        /*
         * paint the background for an expanded container - this involves simply
         * clipping the main background image at the top
         */
        if (isDirty(containerImageExpanded, panel.getWidth(), panel.getHeight())) {
            containerImageExpanded = clipImage(BACKGROUND, 0, 0, panel.getWidth(),
                    panel.getHeight(), panel);
        }
        g.drawImage(containerImageExpanded, 0, 0, panel);
    }
}
