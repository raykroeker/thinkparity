/*
 * Created On:  4-Dec-06 5:18:04 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab;

import java.awt.Graphics;
import java.awt.Graphics2D;
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

    /** The fields background, center <code>Image</code>. */
    private static Image BACKGROUND_FIELDS_CENTER;

    /** The fields background, east <code>Image</code>. */
    private static Image BACKGROUND_FIELDS_EAST;

    /** The fields background, west <code>Image</code>. */
    private static Image BACKGROUND_FIELDS_WEST;

    /** The fields background, center <code>Image</code>. */
    private static BufferedImage backgroundFieldsCenter;

    /** The height of image BACKGROUND_FIELDS_CENTER. */
    private static int backgroundFieldsCenterHeight;

    /** The version panel's center background <code>BufferedImage</code>. */ 
    private static BufferedImage[] VERSION_IMAGES_CENTER;

    /** The version panel's west background <code>BufferedImage</code>s. */
    private static Image[] VERSION_IMAGES_WEST;

    /** The version panel's west edge background <code>BufferedImage</code>s. */
    private static BufferedImage[] VERSION_IMAGES_WEST_EDGE;

    /** The version panel's west underline background <code>BufferedImage</code>s. */
    private static BufferedImage[] VERSION_IMAGES_WEST_UNDERLINE;

    /** The version panel's west background <code>BufferedImage</code>s. */
    private static BufferedImage[] versionImagesWest;

    /** The version panel's west underline background <code>BufferedImage</code>s. */
    private static Image[] versionImagesWestUnderline;

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
        buffer = ImageIOUtil.read("PanelBackgroundFieldsCenter.png"); 
        backgroundFieldsCenterHeight = buffer.getHeight();
        BACKGROUND_FIELDS_CENTER = buffer.getScaledInstance(bounds.width,
                buffer.getHeight(), Image.SCALE_SMOOTH);
        BACKGROUND_FIELDS_EAST = ImageIOUtil.read("PanelBackgroundFieldsEast.png");
        BACKGROUND_FIELDS_WEST = ImageIOUtil.read("PanelBackgroundFieldsWest.png");
        VERSION_IMAGES_CENTER = new BufferedImage[] {
                ImageIOUtil.read("PanelBackgroundCenter1.png"),
                ImageIOUtil.read("PanelBackgroundCenter2.png"),
                ImageIOUtil.read("PanelBackgroundCenter3.png"),
                ImageIOUtil.read("PanelBackgroundCenter4.png"),
                ImageIOUtil.read("PanelBackgroundCenter5.png")
        };
        VERSION_IMAGES_WEST_EDGE = new BufferedImage[] {
                ImageIOUtil.read("PanelBackgroundWestEdge1.png"),
                ImageIOUtil.read("PanelBackgroundWestEdge2.png"),
                ImageIOUtil.read("PanelBackgroundWestEdge3.png"),
                ImageIOUtil.read("PanelBackgroundWestEdge4.png"),
                ImageIOUtil.read("PanelBackgroundWestEdge5.png")
        };
        VERSION_IMAGES_WEST_UNDERLINE = new BufferedImage[] {
                ImageIOUtil.read("PanelBackgroundWestUnderline1.png"),
                ImageIOUtil.read("PanelBackgroundWestUnderline2.png"),
                ImageIOUtil.read("PanelBackgroundWestUnderline3.png"),
                ImageIOUtil.read("PanelBackgroundWestUnderline4.png"),
                ImageIOUtil.read("PanelBackgroundWestUnderline5.png")
        };
        versionImagesWestUnderline = new Image[VERSION_IMAGES_WEST_UNDERLINE.length];
        VERSION_IMAGES_WEST = new Image[VERSION_IMAGES_CENTER.length];
        buffer = ImageIOUtil.read("PanelBackgroundWest1.png");
        VERSION_IMAGES_WEST[0] = buffer.getScaledInstance(bounds.width, buffer
                .getHeight(), Image.SCALE_SMOOTH);
        buffer = ImageIOUtil.read("PanelBackgroundWest2.png");
        VERSION_IMAGES_WEST[1] = buffer.getScaledInstance(bounds.width, buffer
                .getHeight(), Image.SCALE_SMOOTH);
        buffer = ImageIOUtil.read("PanelBackgroundWest3.png");
        VERSION_IMAGES_WEST[2] = buffer.getScaledInstance(bounds.width, buffer
                .getHeight(), Image.SCALE_SMOOTH);
        buffer = ImageIOUtil.read("PanelBackgroundWest4.png");
        VERSION_IMAGES_WEST[3] = buffer.getScaledInstance(bounds.width, buffer
                .getHeight(), Image.SCALE_SMOOTH);
        buffer = ImageIOUtil.read("PanelBackgroundWest5.png");
        VERSION_IMAGES_WEST[4] = buffer.getScaledInstance(bounds.width, buffer
                .getHeight(), Image.SCALE_SMOOTH);
        versionImagesWest = new BufferedImage[VERSION_IMAGES_WEST.length];
        buffer = null;
        bounds = null;
    }

    /**
     * Determine if the image destined for the component is dirty; ie needs to
     * be clipped or scaled before drawing.
     * 
     * @param image
     *            A <code>BufferedImage</code>.
     * @param width
     *            The image <code>int</code> width.
     * @param height
     *            The image <code>int</code> height.
     * @return True if the image needs to be clipped or scaled before drawing.
     */
    private static boolean isDirty(final BufferedImage image, final int width,
            final int height) {
        return null == image || image.getWidth() != width;
    }

    /**
     * Determine if the image destined for the component is dirty; ie needs to
     * be clipped or scaled before drawing.
     * 
     * @param image
     *            An <code>Image</code>.
     * @param width
     *            The image <code>int</code> width.
     * @param height
     *            The image <code>int</code> height.
     * @param observer
     *            An <code>ImageObserver</code>.  
     * @return True if the image needs to be clipped or scaled before drawing.
     */
    private static boolean isDirty(final Image image, final int width,
            final int height, final ImageObserver observer) {
        return null == image || image.getWidth(observer) != width;
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
    public void paintBackground(final Graphics g, final int width,
            final int height, final Boolean selected) {
        final Graphics2D g2 = (Graphics2D)g.create();
        try {
            if (selected) {
                g2.setColor(Colors.Browser.Panel.PANEL_COLLAPSED_SELECTED_BORDER);
                g2.drawLine(0, 0, width-1, 0);
                g2.drawLine(0, height-1, width-1, height-1);
                g2.setColor(Colors.Browser.Panel.PANEL_COLLAPSED_SELECTED_BACKGROUND);
                g2.fillRect(0, 1, width, height-2);
            } else {
                g2.setColor(Colors.Browser.Panel.PANEL_COLLAPSED_BACKGROUND);
                g2.fillRect(0, 0, width, height);
            }
        }
        finally { g2.dispose(); }
    }

    /**
     * Paint the background for the expanded panel.
     * The main background images is painted at the top.
     * 
     * @param g
     *            A <code>Graphics</code> context.
     * @param observer
     *            An <code>ImageObserver</code>.
     */
    public void paintExpandedBackground(final Graphics g, final ImageObserver observer) {
        g.drawImage(BACKGROUND, 0, 0, observer);
    }

    /**
     * Paint a vertial bar in the center of the panel based upon selection.
     * The bar is not scaled.
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
        if (selectionIndex > 0) {
            final int bufferIndex = selectionIndex - 1;
            g.drawImage(VERSION_IMAGES_CENTER[bufferIndex], width
                    - VERSION_IMAGES_CENTER[bufferIndex].getWidth(), 24,
                    observer);
        }
    }

    /**
     * Paint the eastern background.
     * 
     * Paint a solid gradient image on the eastern side of the version panel.
     * 
     * @param g
     *            The <code>Graphics</code>.
     * @param x
     *            The <code>int</code> x coordinate.
     * @param width
     *            A width <code>int</code>.
     * @param height
     *            A height <code>int</code>.
     * @param selectionIndex
     *            The <code>int</code> selection index.
     * @param observer
     *            An <code>ImageObserver</code>.    
     */
    public void paintExpandedBackgroundEast(final Graphics g, final int x,
            final int width, final int height, final int selectionIndex,
            final ImageObserver observer) {
        if (selectionIndex > 0) {
            g.drawImage(BACKGROUND_EAST, x, 0, observer);
        }
    }

    /**
     * Paint the eastern background selection.
     * 
     * East selection is painted here (rather than in the EastCellRenderer) because
     * the selection color extends past the end of the cell.
     * 
     * @param g
     *            The <code>Graphics</code>.
     * @param x
     *            The <code>int</code> x coordinate.
     * @param width
     *            A width <code>int</code>.
     * @param eastSelectionIndex
     *            The <code>int</code> east selection index.
     * @param observer
     *            An <code>ImageObserver</code>.    
     */
    public void paintExpandedBackgroundEastSelection(final Graphics g, final int x,
            final int width, final int eastSelectionIndex, final ImageObserver observer) {
        final Graphics2D g2 = (Graphics2D)g.create();
        try {
            if (eastSelectionIndex > 0) {
                final int rowHeight;
                final int rowOffset;
                rowHeight = 22;
                rowOffset = eastSelectionIndex * 24 + 1;
                g2.setColor(Colors.Browser.Panel.PANEL_EAST_SELECTION_BACKGROUND[eastSelectionIndex-1]);
                g2.fillRect(x, rowOffset, width, rowHeight);
            }
        }
        finally { g2.dispose(); }
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
            final int x, final int width, final ImageObserver observer) {
        /*
         * The west and east images are each 3 pixels wide. They are not scaled.
         * The centre image is scaled in the X direction to fill the desired width.
         * The width is adjusted so it does not paint all the way to the right.
         */
        final int adjustedWidth = width - 140;
        g.drawImage(BACKGROUND_FIELDS_WEST, x, 0, observer);
        if (isDirty(backgroundFieldsCenter, adjustedWidth-6, backgroundFieldsCenterHeight)) {
            backgroundFieldsCenter = clipImage(
                    BACKGROUND_FIELDS_CENTER, 0, 0, adjustedWidth-6, backgroundFieldsCenterHeight,
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
        if (selectionIndex > 0) {
            final int bufferIndex = selectionIndex - 1;
            final int rowHeight = 26;;
            final int rowOffset = selectionIndex * 24;
            final int westEdgeWidth = VERSION_IMAGES_WEST_EDGE[bufferIndex].getWidth();
            final int remainingWidth = width - westEdgeWidth;
            final int underlineWidth = (int)(remainingWidth * 0.9);
            final int underlineHeight = VERSION_IMAGES_WEST_UNDERLINE[bufferIndex].getHeight();
            if (isDirty(versionImagesWest[bufferIndex], remainingWidth, rowHeight)) {
                versionImagesWest[bufferIndex] = clipImage(
                        VERSION_IMAGES_WEST[bufferIndex], 0, 0, remainingWidth, rowHeight,
                        observer);
            }
            if (isDirty(versionImagesWestUnderline[bufferIndex], underlineWidth, underlineHeight, observer)) {
                versionImagesWestUnderline[bufferIndex] = VERSION_IMAGES_WEST_UNDERLINE[bufferIndex]
                        .getScaledInstance(underlineWidth, underlineHeight, Image.SCALE_SMOOTH);
            }
            g.drawImage(VERSION_IMAGES_WEST_EDGE[bufferIndex], 0, rowOffset, observer);
            g.drawImage(versionImagesWest[bufferIndex], westEdgeWidth, rowOffset, observer);
            g.drawImage(versionImagesWestUnderline[bufferIndex], westEdgeWidth,
                    rowOffset + rowHeight - underlineHeight, observer);
        }
    }
}
