/**
 * Created On: 27-Nov-06 8:42:47 PM
 * $Id$
 */
package com.thinkparity.codebase.swing.border;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.border.AbstractBorder;

/**
 * This is a very specific border suited to dialogs.
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
        if (null != screenCapture) {
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
        if (null != screenCapture) {
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
        if (null != screenCapture) {
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
     * @see javax.swing.border.AbstractBorder#getBorderInsets(java.awt.Component)
     * 
     */
    public Insets getBorderInsets(final Component c) {
        return new Insets(4, 4, 7, 7);
    }

    /**
     * @see javax.swing.border.AbstractBorder#getBorderInsets(java.awt.Component,
     *      java.awt.Insets)
     * 
     */
    public Insets getBorderInsets(final Component c, Insets insets) {
        insets.top = 4;
        insets.left = 4;
        insets.bottom = 7;
        insets.right = 7;
        
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
            
            // Draw the inner border the first time only.
            if (null == screenCapture) {
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
            }
            
            // Grab the screen behind the component the first time only
            if (null == screenCapture) {               
                screenCapture = robot.createScreenCapture(
                        new Rectangle(point.x, point.y, width, height));
                             
                final Graphics gMainImage = mainImage.createGraphics();
                try {
                    gMainImage.drawImage(screenCapture, point.x, point.y, null);
                }
                finally { gMainImage.dispose(); }
            }
                
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
    }
    
    /**
     * Force painting of the area underneath where the border shadows will be.
     * If this is not done then artifacts appear in the robot.createScreenCapture
     * because painting may not be complete yet.
     * This can't be called from paintBorder() above since paintAll() will cause
     * an infinite loop. Instead call it just before the dialog will be visible.
     */
    public void paintUnderneathBorder(final Component c, final int x, final int y, final int width, final int height) {
        Component deepestComponent = null;
        
        // Get the deepest component
        Component parent = c;
        while (null != parent) {
            deepestComponent = parent;
            parent = parent.getParent();
        }
        final int deepestComponentWidth = deepestComponent.getWidth();
        final int deepestComponentHeight = deepestComponent.getHeight();
        final Point point = SwingUtilities.convertPoint(c, x, y, deepestComponent);
        final Rectangle rect = new Rectangle();
        
        // If needed paint the dirty region under the vertical border.
        rect.x = point.x + width - 4;
        rect.y = point.y;
        rect.width = 4;
        rect.height = height;
        if ((rect.x + rect.width) > deepestComponentWidth) {
            rect.width = deepestComponentWidth - rect.x;
        }
        if ((rect.y + rect.height) > deepestComponentHeight) {
            rect.height = deepestComponentHeight - rect.y;
        }        
        paintUnderneathRectangle(deepestComponent, rect);
        
        // If needed paint the dirty region under the horizontal border.
        rect.x = point.x;
        rect.y = point.y + height - 4;
        rect.width = width - 4;
        rect.height = 4;  
        if ((rect.x + rect.width) > deepestComponentWidth) {
            rect.width = deepestComponentWidth - rect.x;
        }
        if ((rect.y + rect.height) > deepestComponentHeight) {
            rect.height = deepestComponentHeight - rect.y;
        }        
        paintUnderneathRectangle(deepestComponent, rect);
    }
    
    private void paintUnderneathRectangle(final Component component, final Rectangle rect) {
        if (!rect.isEmpty()) {
            final Graphics2D g2 = (Graphics2D)component.getGraphics();
            try { 
                g2.setClip(rect);
                if (component instanceof JComponent) {
                    JComponent jComponent = (JComponent) component;
                    boolean doubleBuffered = jComponent.isDoubleBuffered();
                    jComponent.setDoubleBuffered(false);
                    jComponent.paintAll(g2);
                    jComponent.setDoubleBuffered(doubleBuffered); 
                } else {
                    component.paintAll(g2);
                }
            } finally {
                g2.dispose();
            }
        }
    }
        
    private AlphaComposite makeComposite(float alpha) {
        int type = AlphaComposite.SRC_OVER;
        return(AlphaComposite.getInstance(type, alpha));
    }
}
