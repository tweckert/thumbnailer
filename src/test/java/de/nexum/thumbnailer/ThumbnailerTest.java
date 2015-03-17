package de.nexum.thumbnailer;

import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.nexum.test.AbstractTest;

/**
 * @author <a href="mailto:thomas.weckert@nexum.de">Thomas Weckert</a>
 */
public class ThumbnailerTest extends AbstractTest {
	
	private Thumbnailer thumbnailer;
	
	@Before
	public void before() {
		this.thumbnailer = new Thumbnailer();
	}

	@Test
	public void addPostfixTest() throws Exception {
		
		String fileName = this.thumbnailer.addPostfix("borussia_de_webseite_des_jahres_2014.jpg", "_", "160x160");
		Assert.assertTrue("borussia_de_webseite_des_jahres_2014_160x160.jpg".equals(fileName));
	}
	
	@Test
	public void scaleImageTest() throws Exception {
		
		File inputFile = new File(getTestDataPath(), "borussia_de_webseite_des_jahres_2014.jpg");
		File outputFile = new File(getTestDataPath(), "borussia_de_webseite_des_jahres_2014_230x230.jpg");
		
		if (outputFile.exists()) {
			outputFile.delete();
		}
		
		BufferedImage inputImage = this.thumbnailer.readImage(inputFile.getAbsolutePath());
		
		BufferedImage outputImage1 = this.thumbnailer.scaleImage(inputImage, 230, 230, RenderingHints.VALUE_INTERPOLATION_BICUBIC, RenderingHints.VALUE_RENDER_QUALITY, true);
		String outputFileName1 = this.thumbnailer.addPostfix("borussia_de_webseite_des_jahres_2014.jpg", "_", "230x230_bicubic");		
		this.thumbnailer.writeImage(outputImage1, new File(getTestDataPath(), outputFileName1).getAbsolutePath(), "jpg", 1.0f);
		
		BufferedImage outputImage2 = this.thumbnailer.scaleImage(inputImage, 230, 230, RenderingHints.VALUE_INTERPOLATION_BILINEAR, RenderingHints.VALUE_RENDER_QUALITY, true);
		String outputFileName2 = this.thumbnailer.addPostfix("borussia_de_webseite_des_jahres_2014.jpg", "_", "230x230_bilinear");		
		this.thumbnailer.writeImage(outputImage2, new File(getTestDataPath(), outputFileName2).getAbsolutePath(), "jpg", 1.0f);

		BufferedImage outputImage3 = this.thumbnailer.scaleImage(inputImage, 230, 230, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR, RenderingHints.VALUE_RENDER_QUALITY, true);
		String outputFileName3 = this.thumbnailer.addPostfix("borussia_de_webseite_des_jahres_2014.jpg", "_", "230x230_nearest_neighbour");		
		this.thumbnailer.writeImage(outputImage3, new File(getTestDataPath(), outputFileName3).getAbsolutePath(), "jpg", 1.0f);
	}
	
}
