package javax.thumbnailer;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;

import org.apache.commons.io.IOUtils;

/**
 * @author Thomas Weckert
 */
public class Thumbnailer {
	
	public BufferedImage scaleImage(BufferedImage image, int targetWidth, int targetHeight, Object interpolation, Object rendering, boolean highQuality) {
		
		int type = (image.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
		BufferedImage scaledImage = (BufferedImage) image;
		int width, height;
		
		if (highQuality) {			
			// start with original size, then scale down in multiple passes
			width = image.getWidth();
			height = image.getHeight();
		} else {			
			// scale directly to target size
			width = targetWidth;
			height = targetHeight;
		}

		do {
			
			if (highQuality && width > targetWidth) {
				width /= 2;
				if (width < targetWidth) {
					width = targetWidth;
				}
			}

			if (highQuality && height > targetHeight) {
				height /= 2;
				if (height < targetHeight) {
					height = targetHeight;
				}
			}

			BufferedImage tmpImage = new BufferedImage(width, height, type);
			Graphics2D g2 = tmpImage.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, interpolation);
			g2.setRenderingHint(RenderingHints.KEY_RENDERING, rendering);
			g2.drawImage(scaledImage, 0, 0, width, height, null);
			g2.dispose();

			scaledImage = tmpImage;
		} while (width != targetWidth || height != targetHeight);

		return scaledImage;
	}	
	
	public void writeImage(BufferedImage bufferedImage, String path, String formatName, float quality) throws IOException {
		
		Iterator<ImageWriter> iterator = ImageIO.getImageWritersByFormatName(formatName);
		ImageWriter imageWriter = iterator.next();
		
		ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam();
		imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		imageWriteParam.setCompressionQuality(quality);
		
		OutputStream outputStream = new FileOutputStream(path);		
		ImageOutputStream imageOutputStream = new MemoryCacheImageOutputStream(outputStream);
		imageWriter.setOutput(imageOutputStream);
		
		IIOImage iioimage = new IIOImage(bufferedImage, null, null);
		imageWriter.write(null, iioimage, imageWriteParam);
		
		imageOutputStream.flush();
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
	
	public String addPostfix(String fileNameWithExtension, String separator, String postfix) throws Exception {
		
		int lastDotIndex = -1;
		if ((lastDotIndex = fileNameWithExtension.lastIndexOf(".")) == -1) {
			throw new Exception("Unable to append postfix, filename '" + String.valueOf(fileNameWithExtension) + "' does not contain a dot '.'!");
		}
		
		String fileName = fileNameWithExtension.substring(0, lastDotIndex);
		String extension = fileNameWithExtension.substring(lastDotIndex + 1, fileNameWithExtension.length());
		
		return new StringBuffer().append(fileName).append(separator).append(postfix).append(".").append(extension).toString();
	}

}