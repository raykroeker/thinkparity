/*
 * Dec 23, 2003
 */
package com.thinkparity.codebase;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.swing.ImageIcon;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * <b>Title:</b>  ImageUtil
 * <br><b>Description:</b>  Creates thumbnails of JPEG files.  If byte[] provided
 * does not represent a JPEG, the byte[] result will not be recognizable.  Please
 * note that in order for the image manipulation to occur, an X server must be 
 * running.  In a win32 environment, this is not an issue, but in any kind of *nix
 * platform, a minimal X server must be runnint.
 * @author raykroeker@gmail.com
 * @version 1.0.0
 */
public abstract class ImageUtil {

	/**
	 * Create a new ImageUtil [Singleton]
	 */
    private ImageUtil() {super();}

    /**
     * Creates a thumbnail for the specified file.
     * @param originalImage <code>byte[]</code>
     * @param maximumDimension <code>int</code>
     * @return <code>java.io.File</code>
     */
	public static synchronized byte[] createThumbnail(
		byte[] originalImage,
		int maximumDimension)
		throws IOException {
		return createJPEGThumbnail(originalImage, maximumDimension);
    }

    /**
     * Generates a thumbnail based upon originalImage and maximumDimensions.
     * @param originalImage <code>byte[]</code>
     * @param maximumDimensions <code>int</code>
     * @return <code>byte[]</code>
     */
	private static synchronized byte[] createJPEGThumbnail(
		byte[] originalImage,
		int maximumDimensions)
		throws IOException {
        // GET THE IMAGE FROM A FILE
        Image inImage = new ImageIcon(originalImage).getImage();

        // DETERMINE THE SCALE
        double scale =
        	(double) maximumDimensions / (double) inImage.getHeight(null);

        if(inImage.getWidth(null) > inImage.getHeight(null))
            scale = (double) maximumDimensions / (double) inImage.getWidth( null );

        // DETERMINE SIZE OF NEW IMAGE.
        // ONE SHOULD EQUAL MAXDIM
        int scaledW = (int) (scale * inImage.getWidth(null));
        int scaledH = (int) (scale * inImage.getHeight(null));

        // CREATE AN IMAGE BUFFER IN WHICH TO PAINT ON
        BufferedImage outImage =
        	new BufferedImage(scaledW, scaledH, BufferedImage.TYPE_INT_RGB);
        
        // SET THE SCALE
        AffineTransform tx = new AffineTransform();

        // IF THE IMAGE IS SMLALLER THAN THE DESIRED IMAGE SIZE DON'T
        // BOTHER SCALING
        if(scale < 1.0d)
            tx.scale(scale, scale);

        // PAINT IMAGE
        Graphics2D g2d = outImage.createGraphics();
        g2d.drawImage(inImage, tx, null);
        g2d.dispose();

        // JPEG-ENCODE THE IMAGE AND WRITE TO A FILE
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(os);
        encoder.encode(outImage);
        os.flush();
        os.close();
        
        // CREATE AND RETURN THE THUMBNAILED FILE
		byte[] thumbnail = os.toByteArray();
        return thumbnail;
     }

}
