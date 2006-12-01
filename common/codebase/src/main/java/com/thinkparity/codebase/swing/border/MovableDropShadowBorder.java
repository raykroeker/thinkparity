/**
 * Created On: 27-Nov-06 8:42:47 PM
 * $Id$
 */
package com.thinkparity.codebase.swing.border;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

import javax.swing.SwingUtilities;
import javax.swing.border.AbstractBorder;

import com.thinkparity.ophelia.browser.Constants.Images;

/**
 * This is a very specific border suited to dialogs.
 * 
 * In addition to calling the constructor make sure to call
 * initialize(), settingLocation(), settingSize(), and
 * settingBounds().
 * 
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class MovableDropShadowBorder extends AbstractBorder {
    
    /** A screen-size image for storing screen captures. */
    private static BufferedImage mainImage;
    
    /** Dimensions of the shadow border. */
    private static int SHADOW_TOP;
    private static int SHADOW_LEFT;
    private static int SHADOW_BOTTOM;
    private static int SHADOW_RIGHT;
    
    static {
        final Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        mainImage = new BufferedImage((int) screenSize.getWidth(),
                (int) screenSize.getHeight(), BufferedImage.TYPE_INT_RGB);
        
        SHADOW_TOP = 3;
        SHADOW_LEFT = 3;
        SHADOW_BOTTOM = 6;
        SHADOW_RIGHT = 6;
    }
    
    /** The scaled up background image on the right. */
    private BufferedImage scaledShadowImageTop = null;
    
    /** A screen capture of behind the component <code>BufferedImage</code>.*/
    private BufferedImage screenCapture = null;
    
    /** The component. */
    private final Component component;
    
    /** The inner border top colour. */
    private final Color topColor;
    
    /** The inner border bottom colour. */
    private final Color bottomColor;
    
    /** The top colour of the gradient border on the left. */
    private final Color topLeftColor;
    
    /** The bottom colour of the gradient border on the left. */
    private final Color bottomLeftColor;
    
    /** The top colour of the gradient border on the right. */
    private Color topRightColor = null;
    
    /** The bottom colour of the gradient border on the right. */
    private Color bottomRightColor = null; 

    /** The swing <code>Robot</code>. */
    private final Robot robot;
    
    /** Left image. */
    private BufferedImage leftImage;
    
    /** Right image. */
    private BufferedImage rightImage;
    
    /** Top image. */
    private BufferedImage topImage;
    
    /** Bottom image. */
    private BufferedImage bottomImage;
    
    /** Corner images. */
    private BufferedImage topLeftImage;
    private BufferedImage topRightImage;
    private BufferedImage bottomLeftImage;
    private BufferedImage bottomRightImage;
    
    /** Previous rectangle */
    private Rectangle previousRectangle = null;
    
    /** Flag to indicate if paintBorder called. */
    private Boolean isPaintedBorder = Boolean.FALSE;
    
    
    /**
     * Create a MovableDropShadowBorder.
     */
    public MovableDropShadowBorder(final Component component,
            final Color topColor, final Color bottomColor,
            final Color topLeftColor, final Color bottomLeftColor,
            final Color topRightColor, final Color bottomRightColor) throws AWTException {
        super();
        this.component = component;
        this.topColor = topColor;
        this.bottomColor = bottomColor;
        this.topLeftColor = topLeftColor;
        this.bottomLeftColor = bottomLeftColor;
        this.topRightColor = topRightColor;
        this.bottomRightColor = bottomRightColor;
        this.robot = new Robot();
    }
    
    /**
     * Initialize the border. This should be called just before the
     * component becomes visible.
     */
    public void initialize() {
        // Grab the screen behind the component the first time only
        if (null == screenCapture) { 
            final Point point = new Point(0, 0); 
            SwingUtilities.convertPointToScreen(point, component);
            screenCapture = robot.createScreenCapture(
                    new Rectangle(point.x, point.y, component.getWidth(), component.getHeight()));
                         
            final Graphics gMainImage = mainImage.createGraphics();
            try {
                gMainImage.drawImage(screenCapture, point.x, point.y, null);
            }
            finally { gMainImage.dispose(); }
        }
        
        //prepareShadowImages(component.getWidth(), component.getHeight());
    }
    
    /**
     * Prepare for moving the dialog.
     * This method should be called from an overridden setLocation() method
     * on the component that has this border.
     * 
     * @param x
     *      New x location of the component.
     * @param y
     *      New y location of the component.   
     */
    public void settingLocation(final int x, final int y) {
        if (isPaintedBorder) {
            final Rectangle r = new Rectangle(x, y, component.getWidth(), component.getHeight());
            settingBounds(r);
        }
    }
    
    /**
     * Prepare for resizing the dialog.
     * This method should be called from an overridden setSize() method
     * on the component that has this border.
     * 
     * @param width
     *      New width of the component.
     * @param height
     *      New height of the component.   
     */
    public void settingSize(final int width, final int height) {
        if (isPaintedBorder) {
            final Rectangle r = new Rectangle(component.getLocation().x, component.getLocation().y, width, height);
            settingBounds(r);
        }
    }
    
    /**
     * Prepare for moving and resizing the dialog.
     * This method should be called from overridden setBounds() method
     * on the component that has this border.
     * 
     * @param r
     *      Rectangle representing the new size and location of the component.
     */
    public void settingBounds(final Rectangle r) {
        if (isPaintedBorder) {
            if ((null == previousRectangle) || (!previousRectangle.equals(r))) {
                previousRectangle = r;
                final int x = r.x;
                final int y = r.y;
                final Point currentLocation = component.getLocation();
                final int newCompWidth = r.width;
                final int newCompHeight = r.height;
                final int currentCompWidth = component.getWidth();
                final int currentCompHeight = component.getHeight();
                final int shiftLeft = (currentLocation.x > x ? currentLocation.x - x : 0);
                int shiftRight = x - currentLocation.x + newCompWidth - currentCompWidth;
                if (shiftRight < 0) {
                    shiftRight = 0;
                }
                final int shiftUp = (currentLocation.y > y ? currentLocation.y - y : 0);
                int shiftDown = y - currentLocation.y + newCompHeight - currentCompHeight;
                if (shiftDown < 0) {
                    shiftDown = 0;
                }
                if (shiftLeft > 0) {
                    final int width = shiftLeft;
                    final int height = currentCompHeight + shiftUp + shiftDown;
                    final Point location = new Point(x, currentLocation.y - shiftUp);
                    captureScreen(location, width, height);
                }
                if (shiftRight > 0) {
                    final int width = shiftRight;
                    final int height = currentCompHeight + shiftUp + shiftDown;
                    final Point location = new Point(currentLocation.x + currentCompWidth, currentLocation.y - shiftUp);
                    captureScreen(location, width, height);
                }
                if (shiftUp > 0) {
                    final int width = currentCompWidth + shiftLeft + shiftRight;
                    final int height = shiftUp;
                    final Point location = new Point(currentLocation.x - shiftLeft, y);  
                    captureScreen(location, width, height);           
                }
                if (shiftDown > 0) {
                    final int width = currentCompWidth + shiftLeft + shiftRight;
                    final int height = shiftDown;
                    final Point location = new Point(currentLocation.x - shiftLeft, currentLocation.y + currentCompHeight);
                    captureScreen(location, width, height);  
                }
                if ((shiftLeft>0) || (shiftRight>0) || (shiftUp>0) || (shiftDown>0)) {   
                    component.repaint();
                }
            }
        }
    }
    
    /**
     * Capture the screen and store at the specified location.
     * 
     * @param point
     *      Location in screen coordinates
     * @param width
     *      Width
     * @param height
     *      Height
     */
    private void captureScreen(final Point point, final int width, final int height) {
        screenCapture = robot.createScreenCapture(new Rectangle(point.x, point.y, width, height));
        final Graphics gMainImage = mainImage.createGraphics();
        try {
            gMainImage.drawImage(screenCapture, point.x, point.y, null);
        }
        finally { gMainImage.dispose(); }     
    }
    
    /**
     * Prepare shadow images.
     * 
     * @param width
     *          Width of component
     * @param height
     *          Height of component
     */
    private void prepareShadowImages(final int width, final int height) {
        if ((null == scaledShadowImageTop) || scaledShadowImageTop.getWidth() < width) {
            if (width <= Images.BrowserTitle.DIALOG_SHADOW_TOP.getWidth()) {
                scaledShadowImageTop = Images.BrowserTitle.DIALOG_SHADOW_TOP;
            } else {
                final int imageHeight = Images.BrowserTitle.DIALOG_SHADOW_TOP.getHeight();
                final Image image = Images.BrowserTitle.DIALOG_SHADOW_TOP.getScaledInstance(
                        width, imageHeight, Image.SCALE_FAST);
                scaledShadowImageTop = new BufferedImage(width, imageHeight, BufferedImage.TYPE_INT_RGB);
                final Graphics g2 = scaledShadowImageTop.createGraphics();
                try {
                    g2.drawImage(image, 0, 0, null);
                }
                finally { g2.dispose(); }
            }
        }
    }

    /**
     * @see javax.swing.border.AbstractBorder#getBorderInsets(java.awt.Component)
     * 
     */
    public Insets getBorderInsets(final Component c) {
        return new Insets(SHADOW_TOP + 1, SHADOW_LEFT + 1, SHADOW_BOTTOM + 1, SHADOW_RIGHT + 1);
    }

    /**
     * @see javax.swing.border.AbstractBorder#getBorderInsets(java.awt.Component,
     *      java.awt.Insets)
     * 
     */
    public Insets getBorderInsets(final Component c, Insets insets) {
        insets.top = SHADOW_TOP + 1;
        insets.left = SHADOW_LEFT + 1;
        insets.bottom = SHADOW_BOTTOM + 1;
        insets.right = SHADOW_RIGHT + 1;
        
        return insets;
    }

    /**
     * @see javax.swing.border.AbstractBorder#getInteriorRectangle(java.awt.Component,
     *      int, int, int, int)
     * 
     */
    public Rectangle getInteriorRectangle(final Component c, final int x, final int y, final int width,
            final int height) {
        return super.getInteriorRectangle(c, x, y, width, height);
    }

    /**
     * @see javax.swing.border.AbstractBorder#isBorderOpaque()
     * 
     */
    public boolean isBorderOpaque() { return true; }

    /**
     * @see javax.swing.border.AbstractBorder#paintBorder(java.awt.Component,
     *      java.awt.Graphics, int, int, int, int)
     * 
     */
    public void paintBorder(final Component c, final Graphics g, final int x, final int y, final int width, final int height) {               
        final Graphics2D g2 = (Graphics2D) g.create();
        try {       
            final int outerTop = SHADOW_TOP;
            final int outerLeft = SHADOW_LEFT;
            final int outerRight = SHADOW_RIGHT;
            final int outerBottom = SHADOW_BOTTOM;
            final Point point = new Point(x, y); 
            SwingUtilities.convertPointToScreen(point, c);
            
            // Grab the screen behind the component the first time only
            if (!isPaintedBorder) {
                initialize();
            }
            
            // Inner border top line
            // Leave 2 pixels extra on either side to account for rounded corners.
            g2.setColor(topColor);
            g2.drawLine(x + outerLeft + 2, y + outerTop, x + width - outerRight - 3, y + outerTop);
           
            // Inner border bottom line
            // Leave 2 pixels extra on either side to account for rounded corners.
            g2.setColor(bottomColor);
            g2.drawLine(x + outerLeft + 2, y + height - outerBottom - 1,
                        x + width - outerRight - 3, y + height - outerBottom - 1);
            
            // Left gradient inner border line
            // Leave 2 pixels extra on either side to account for rounded corners.
            final Paint gPaintLeft =
                new GradientPaint(x + outerLeft, y + outerTop + 2, topLeftColor,
                                  x + outerLeft, y + height - outerBottom - 3, bottomLeftColor);
            g2.setPaint(gPaintLeft);
            g2.fillRect(x + outerLeft, y + outerTop + 2, 1, y + height - outerBottom - outerTop - 4);
            
            // Right gradient inner border line
            // Leave 2 pixels extra on either side to account for rounded corners.
            final Paint gPaintRight =
                new GradientPaint(x + width - outerRight - 1, y + outerTop + 2, topRightColor,
                                  x + width - outerRight - 1, y + height - outerBottom - 3, bottomRightColor);
            g2.setPaint(gPaintRight);
            g2.fillRect(x + width - outerRight - 1, y + outerTop + 2, 1, y + height - outerBottom - outerTop - 4);
                
            // Prepare subimages for the edges
            leftImage = mainImage.getSubimage(point.x, point.y, outerLeft, height);
            rightImage = mainImage.getSubimage(
                    point.x + width - outerRight, point.y, outerRight, height);
            topImage = mainImage.getSubimage(point.x + outerLeft, point.y,
                    width - outerLeft - outerRight, outerTop);
            bottomImage = mainImage.getSubimage(point.x + outerLeft,
                    point.y + height - outerBottom, width - outerLeft - outerRight, outerBottom);

            // Prepare subimages for the rounded corners
            topLeftImage = mainImage.getSubimage(point.x + outerLeft,
                    point.y + outerTop, 2, 2);
            topRightImage = mainImage.getSubimage(point.x + width
                    - outerRight - 3, point.y + outerTop, 2, 2);
            bottomLeftImage = mainImage.getSubimage(point.x + outerLeft,
                    point.y + height - outerBottom - 3, 2, 2);
            bottomRightImage = mainImage.getSubimage(point.x + width
                    - outerRight - 3, point.y + height - outerBottom - 3, 2, 2);
            
            // Draw the background images to the border.
            g2.drawImage(leftImage, null, x, y);
            g2.drawImage(rightImage, null, x + width - outerRight, y);
            g2.drawImage(topImage, null, x + outerLeft, y);
            g2.drawImage(bottomImage, null, x + outerLeft, y + height - outerBottom);
            
            g2.drawImage(topLeftImage, null, outerLeft, outerTop);
            g2.drawImage(topRightImage, null, width - outerRight - 2, outerTop);
            g2.drawImage(bottomLeftImage, null, outerLeft, height - outerBottom - 2);
            g2.drawImage(bottomRightImage, null, width - outerRight - 2, height - outerBottom - 2);
            
            // Create composites
            final AlphaComposite[] composites = new AlphaComposite[] {
                    makeComposite(0.05f), makeComposite(0.1f),
                    makeComposite(0.15f), makeComposite(0.2f),
                    makeComposite(0.25f), makeComposite(0.3f)
            };
            
            // Draw shadow gradient, bottom left
            g2.setColor(new Color(0, 0, 0, 255));
            drawAlphaGradient(g2, x+SHADOW_LEFT,   y+height-SHADOW_BOTTOM,   1, 0, 6, composites, 6);
            drawAlphaGradient(g2, x+SHADOW_LEFT+1, y+height-SHADOW_BOTTOM+1, 1, 0, 5, composites, 6);
            drawAlphaGradient(g2, x+SHADOW_LEFT+2, y+height-SHADOW_BOTTOM+2, 1, 0, 4, composites, 6);
            drawAlphaGradient(g2, x+SHADOW_LEFT+3, y+height-SHADOW_BOTTOM+3, 1, 0, 3, composites, 6);
            drawAlphaGradient(g2, x+SHADOW_LEFT+4, y+height-SHADOW_BOTTOM+4, 1, 0, 2, composites, 6);
            drawAlphaGradient(g2, x+SHADOW_LEFT+5, y+height-SHADOW_BOTTOM+5, 1, 0, 1, composites, 6);
            drawAlphaGradient(g2, x+SHADOW_LEFT-1, y+height-SHADOW_BOTTOM-1, 0, -1, 3, composites, 6);
            drawAlphaGradient(g2, x+SHADOW_LEFT-2, y+height-SHADOW_BOTTOM-2, 0, -1, 2, composites, 6);
            drawAlphaGradient(g2, x+SHADOW_LEFT-3, y+height-SHADOW_BOTTOM-3, 0, -1, 1, composites, 6);
            
            // Draw shadow gradient, top right
            drawAlphaGradient(g2, x+width-SHADOW_RIGHT,   y+SHADOW_TOP,   0, 1, 6, composites, 6);
            drawAlphaGradient(g2, x+width-SHADOW_RIGHT+1, y+SHADOW_TOP+1, 0, 1, 5, composites, 6);
            drawAlphaGradient(g2, x+width-SHADOW_RIGHT+2, y+SHADOW_TOP+2, 0, 1, 4, composites, 6);
            drawAlphaGradient(g2, x+width-SHADOW_RIGHT+3, y+SHADOW_TOP+3, 0, 1, 3, composites, 6);
            drawAlphaGradient(g2, x+width-SHADOW_RIGHT+4, y+SHADOW_TOP+4, 0, 1, 2, composites, 6);
            drawAlphaGradient(g2, x+width-SHADOW_RIGHT+5, y+SHADOW_TOP+5, 0, 1, 1, composites, 6);
            drawAlphaGradient(g2, x+width-SHADOW_RIGHT-1, y+SHADOW_TOP-1, -1, 0, 3, composites, 6);
            drawAlphaGradient(g2, x+width-SHADOW_RIGHT-2, y+SHADOW_TOP-2, -1, 0, 2, composites, 6);
            drawAlphaGradient(g2, x+width-SHADOW_RIGHT-3, y+SHADOW_TOP-3, -1, 0, 1, composites, 6);
            
            // Draw shadow, bottom and right
            g2.setComposite(composites[5]);
            g2.draw(new Line2D.Double(x+SHADOW_LEFT+6, y+height-SHADOW_BOTTOM, x+width-SHADOW_RIGHT-1, y+height-SHADOW_BOTTOM));
            g2.draw(new Line2D.Double(x+width-SHADOW_RIGHT, y+SHADOW_TOP+6, x+width-SHADOW_RIGHT, y+height-SHADOW_BOTTOM-1));
            g2.draw(new Line2D.Double(x+width-8, y+height-7, x+width-7, y+height-7));
            g2.draw(new Line2D.Double(x+width-7, y+height-8, x+width-7, y+height-8));
            
            g2.setComposite(composites[4]);
            g2.draw(new Line2D.Double(x+SHADOW_LEFT+6, y+height-SHADOW_BOTTOM+1, x+width-SHADOW_RIGHT, y+height-SHADOW_BOTTOM+1));
            g2.draw(new Line2D.Double(x+width-SHADOW_RIGHT+1, y+SHADOW_TOP+6, x+width-SHADOW_RIGHT+1, y+height-SHADOW_BOTTOM));
            g2.draw(new Line2D.Double(x+width-6, y+height-6, x+width-6, y+height-6));
            
            g2.setComposite(composites[3]);
            g2.draw(new Line2D.Double(x+SHADOW_LEFT+6, y+height-SHADOW_BOTTOM+2, x+width-SHADOW_RIGHT+1, y+height-SHADOW_BOTTOM+2));
            g2.draw(new Line2D.Double(x+width-SHADOW_RIGHT+2, y+SHADOW_TOP+6, x+width-SHADOW_RIGHT+2, y+height-SHADOW_BOTTOM+1));
            g2.draw(new Line2D.Double(x+width-5, y+height-5, x+width-5, y+height-5));
            
            g2.setComposite(composites[2]);
            g2.draw(new Line2D.Double(x+SHADOW_LEFT+6, y+height-SHADOW_BOTTOM+3, x+width-SHADOW_RIGHT+2, y+height-SHADOW_BOTTOM+3));
            g2.draw(new Line2D.Double(x+width-SHADOW_RIGHT+3, y+SHADOW_TOP+6, x+width-SHADOW_RIGHT+3, y+height-SHADOW_BOTTOM+2));
            g2.draw(new Line2D.Double(x+width-4, y+height-4, x+width-4, y+height-4));
            
            g2.setComposite(composites[1]);
            g2.draw(new Line2D.Double(x+SHADOW_LEFT+6, y+height-SHADOW_BOTTOM+4, x+width-SHADOW_RIGHT+3, y+height-SHADOW_BOTTOM+4));
            g2.draw(new Line2D.Double(x+width-SHADOW_RIGHT+4, y+SHADOW_TOP+6, x+width-SHADOW_RIGHT+4, y+height-SHADOW_BOTTOM+3));
            g2.draw(new Line2D.Double(x+width-3, y+height-3, x+width-3, y+height-3));
            
            g2.setComposite(composites[0]);
            g2.draw(new Line2D.Double(x+SHADOW_LEFT+6, y+height-SHADOW_BOTTOM+5, x+width-SHADOW_RIGHT+3, y+height-SHADOW_BOTTOM+5));
            g2.draw(new Line2D.Double(x+width-SHADOW_RIGHT+5, y+SHADOW_TOP+6, x+width-SHADOW_RIGHT+5, y+height-SHADOW_BOTTOM+3));
            g2.draw(new Line2D.Double(x+width-2, y+height-2, x+width-2, y+height-2));
            
            // Draw shadow, top and left
            g2.setComposite(composites[2]);
            g2.draw(new Line2D.Double(x+SHADOW_LEFT, y+SHADOW_TOP-1, x+width-SHADOW_RIGHT-4, y+SHADOW_TOP-1));
            g2.draw(new Line2D.Double(x+SHADOW_LEFT-1, y+SHADOW_TOP, x+SHADOW_LEFT-1, y+height-SHADOW_BOTTOM-4));
            g2.draw(new Line2D.Double(x+3, y+3, x+4, y+3));
            g2.draw(new Line2D.Double(x+3, y+4, x+3, y+4));
            g2.draw(new Line2D.Double(x+width-SHADOW_RIGHT-2, y+SHADOW_TOP, x+width-SHADOW_RIGHT-2, y+SHADOW_TOP));
            g2.draw(new Line2D.Double(x+width-SHADOW_RIGHT-1, y+SHADOW_TOP+1, x+width-SHADOW_RIGHT-1, y+SHADOW_TOP+1));            
            g2.draw(new Line2D.Double(x+SHADOW_LEFT, y+height-SHADOW_BOTTOM-2, x+SHADOW_LEFT, y+height-SHADOW_BOTTOM-2));
            g2.draw(new Line2D.Double(x+SHADOW_LEFT+1, y+height-SHADOW_BOTTOM-1, x+SHADOW_LEFT+1, y+height-SHADOW_BOTTOM-1));
            
            g2.setComposite(composites[1]);
            g2.draw(new Line2D.Double(x+SHADOW_LEFT-1, y+SHADOW_TOP-2, x+width-SHADOW_RIGHT-4, y+SHADOW_TOP-2));
            g2.draw(new Line2D.Double(x+SHADOW_LEFT-2, y+SHADOW_TOP-1, x+SHADOW_LEFT-2, y+height-SHADOW_BOTTOM-4));
            g2.draw(new Line2D.Double(x+2, y+2, x+2, y+2));
            g2.draw(new Line2D.Double(x+width-SHADOW_RIGHT-1, y+SHADOW_TOP, x+width-SHADOW_RIGHT-1, y+SHADOW_TOP));
            g2.draw(new Line2D.Double(x+SHADOW_LEFT, y+height-SHADOW_BOTTOM-1, x+SHADOW_LEFT, y+height-SHADOW_BOTTOM-1));
            
            g2.setComposite(composites[0]);
            g2.draw(new Line2D.Double(x+SHADOW_LEFT-1, y+SHADOW_TOP-3, x+width-SHADOW_RIGHT-4, y+SHADOW_TOP-3));
            g2.draw(new Line2D.Double(x+SHADOW_LEFT-3, y+SHADOW_TOP-1, x+SHADOW_LEFT-3, y+height-SHADOW_BOTTOM-4));
            g2.draw(new Line2D.Double(x+1, y+1, x+1, y+1));
            g2.draw(new Line2D.Double(x+width-SHADOW_RIGHT, y+SHADOW_TOP-1, x+width-SHADOW_RIGHT+4, y+SHADOW_TOP+3));
            g2.draw(new Line2D.Double(x+SHADOW_LEFT-1, y+height-SHADOW_BOTTOM, x+SHADOW_LEFT+3, y+height-SHADOW_BOTTOM+4));
            
            
            
/*            g2.setComposite(makeComposite(0.05f));
            g2.draw(new Line2D.Double(x, y, x + width, y));
            g2.draw(new Line2D.Double(x, y, x, y + height));
            
            g2.setComposite(makeComposite(0.1f));
            g2.draw(new Line2D.Double(x, y+1, x + width, y+1));
            g2.draw(new Line2D.Double(x+1, y, x+1, y + height));
            
            g2.setComposite(makeComposite(0.15f));
            g2.draw(new Line2D.Double(x, y+2, x + width, y+2));
            g2.draw(new Line2D.Double(x+2, y, x+2, y + height));*/
            
            // Draw the shadow, four lines, from inner to outer.
/*            g2.setColor(new Color(0, 0, 0, 255));
            
            g2.setComposite(makeComposite(0.6f));
            g2.draw(new Line2D.Double(x + width - 4, y + 7, x + width - 4, y + height - 5));   // right
            g2.draw(new Line2D.Double(x + 7, y + height - 4, x + width - 5, y + height - 4));  // bottom
            
            g2.setComposite(makeComposite(0.4f));
            g2.draw(new Line2D.Double(x + width - 3, y + 7, x + width - 3, y + height - 4));   // right
            g2.draw(new Line2D.Double(x + width - 4, y + 6, x + width - 4, y + 6));            // soften top right
            g2.draw(new Line2D.Double(x + 7, y + height - 3, x + width - 4, y + height - 3));  // bottom
            g2.draw(new Line2D.Double(x + 6, y + height - 4, x + 6, y + height - 4 ));         // soften bottom left
            
            g2.setComposite(makeComposite(0.2f));
            g2.draw(new Line2D.Double(x + width - 2, y + 7, x + width - 2, y + height - 3));   // right
            g2.draw(new Line2D.Double(x + width - 4, y + 5, x + width - 3, y + 6));            // soften top right
            g2.draw(new Line2D.Double(x + 7, y + height - 2, x + width - 3, y + height - 2));  // bottom
            g2.draw(new Line2D.Double(x + 5, y + height - 4, x + 6, y + height - 3 ));         // soften bottom left 
            
            g2.setComposite(makeComposite(0.1f));
            g2.draw(new Line2D.Double(x + width - 1, y + 7, x + width - 1, y + height - 2));   // right
            g2.draw(new Line2D.Double(x + width - 4, y + 4, x + width - 2, y + 6));            // soften top right
            g2.draw(new Line2D.Double(x + 7, y + height - 1, x + width - 2, y + height - 1));  // bottom
            g2.draw(new Line2D.Double(x + 4, y + height - 4, x + 6, y + height - 2 ));         // soften bottom left
            
            // Soften the shadow at the bottom right corner
            g2.setComposite(makeComposite(0.5f));
            g2.draw(new Line2D.Double(x + width - 4, y + height - 4, x + width - 4, y + height - 4));
            g2.setComposite(makeComposite(0.3f));
            g2.draw(new Line2D.Double(x + width - 3, y + height - 3, x + width - 3, y + height - 3));
            g2.setComposite(makeComposite(0.15f));
            g2.draw(new Line2D.Double(x + width - 2, y + height - 2, x + width - 2, y + height - 2));*/
        } finally {
            g2.dispose();
        }
        
        isPaintedBorder = Boolean.TRUE;
    }
        
    private AlphaComposite makeComposite(float alpha) {
        int type = AlphaComposite.SRC_OVER;
        return(AlphaComposite.getInstance(type, alpha));
    }
    
    private void drawAlphaGradient(final Graphics2D g2, final int x1,
            final int y1, final int deltaX, final int deltaY, final int length,
            final AlphaComposite[] composites, final int numComposites) {
        final Line2D.Double line = new Line2D.Double();
        for (int pixel = 0; (pixel < length) && (pixel < numComposites); pixel++) {
            g2.setComposite(composites[pixel]);
            line.setLine(x1+pixel*deltaX, y1+pixel*deltaY, x1+pixel*deltaX, y1+pixel*deltaY);
            g2.draw(line);            
        }
        for (int pixel = numComposites; pixel < length; pixel++) {
            line.setLine(x1+pixel*deltaX, y1+pixel*deltaY, x1+pixel*deltaX, y1+pixel*deltaY);
            g2.draw(line);  
        }
    }
}
