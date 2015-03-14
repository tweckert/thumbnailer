package de.nexum.thumbnailer;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.io.IOUtils;

public class Thumbnailer {
	
	public void writeImage(BufferedImage image, String fileType, String path) throws IOException {

		File outputfile = new File(path);
		ImageIO.write(image, fileType, outputfile);
	}
	
	public BufferedImage readImage(String path) throws IOException {
		
		BufferedImage img = null;
		ImageInputStream iis = null;
		
		try {
		
			byte[] rawData = readBytesFromFile(path); 
			iis = ImageIO.createImageInputStream(new ByteArrayInputStream(rawData));
			img = ImageIO.read(iis);
		} finally {
			IOUtils.closeQuietly(iis);
		}
		
		return img;
	}
	
	public byte[] readBytesFromFile(String path) throws FileNotFoundException, IOException {

		byte[] image = null;
		FileInputStream fileInputStream = null;
		
		try {
			
			File file = new File(path);
			image = new byte[(int) file.length()];

			fileInputStream = new FileInputStream(file);
			fileInputStream.read(image);	
		} finally {
			IOUtils.closeQuietly(fileInputStream);
		}

		return image;
	}

    /**
     * Scales a specified BufferedImage to a maximum width and height.
     * 
     * @param maxWidth the max. width in pixels
     * @param maxHeight the max. height in pixels
     * @param image the BufferedImage to be scaled
     */
	public BufferedImage scaleImageToSize(int maxWidth, int maxHeight, BufferedImage image) {
		
        double scaleX = (double) maxWidth / (double) image.getWidth();
        double scaleY = (double) maxHeight / (double) image.getHeight();
        double scaleFactor = Math.min(scaleX, scaleY);

        return scaleImage(scaleFactor, image);
    }

    /**
     * Scales a BufferedImage by a specified factor.
     * 
     * @param factor the factor to scale the image
     * @param image the BufferedImage to be scaled
     */
	public BufferedImage scaleImage(double scaleFactor, BufferedImage image) {
		
        AffineTransformOp transformation = null;

        if (scaleFactor == 1) {
            return image;
        }

        transformation = new AffineTransformOp(AffineTransform.getScaleInstance(scaleFactor, scaleFactor), AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return transformation.filter(image, null);
    }
	
	public static void main(String[] argv) {
		
		try {
			
			Thumbnailer thumbnailer = new Thumbnailer();
			BufferedImage image = thumbnailer.readImage("/Users/tweckert/Downloads/Borussia_de_webseite_des_jahres_2014.jpg");
			BufferedImage scaledImage = thumbnailer.scaleImageToSize(240, 240, image);
			thumbnailer.writeImage(scaledImage, "jpg", "/Users/tweckert/Downloads/Borussia_de_webseite_des_jahres_2014_scaled.jpg");
		} catch (Throwable t) {
			
		}
	}

}