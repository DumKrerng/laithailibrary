package com.laithailibrary.sharelibrary.file;

import javax.imageio.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.net.*;
import com.laithailibrary.sharelibrary.file.support.*;
import exc.*;

/**
 * Created by dumkrerng on 31/10/2560.
 */
public class GImage extends GFile {

	private static final long serialVersionUID = 3;

	public GImage() {}

	public GImage(String p_strFullName_WithoutType, ImageType p_imagetype) throws GException {
		super(p_strFullName_WithoutType.concat(".").concat(p_imagetype.getLabel()));
	}

	public GImage(String p_strFullName) throws GException {
		super(p_strFullName);
	}

	public GImage(String p_strFullName, boolean p_bolVerifyFileExist) throws GException {
		super(p_strFullName, p_bolVerifyFileExist);
	}

	public void setImage_ByURL(String p_strURL) throws GException {
		File outputtemp = null;

		try {
			if(p_strURL.length() > 0) {
				String strFileName = p_strURL;
				strFileName = strFileName.replace(":", "_");
				strFileName = strFileName.replace("/", "_");

				URL url = new URL(p_strURL);
				Image image = ImageIO.read(url.openStream());
				BufferedImage bufferedimage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
				Graphics bg = bufferedimage.getGraphics();
				bg.drawImage(image, 0, 0, null);
				bg.dispose();

				outputtemp = File.createTempFile(strFileName, ".tmp");

				GFilePostFix postfix;

				if(p_strURL.endsWith(GFilePostFix.GIF.getLabel())) {
					postfix = GFilePostFix.GIF;

				} else if(p_strURL.endsWith(GFilePostFix.PNG.getLabel())) {
					postfix = GFilePostFix.PNG;

				} else if(p_strURL.endsWith(GFilePostFix.JPEG.getLabel())) {
					postfix = GFilePostFix.JPEG;

				} else if(p_strURL.endsWith(GFilePostFix.JPG.getLabel())) {
					postfix = GFilePostFix.JPG;

				} else {
					throw new GException("Invalid Image Type!!!\nFile Name: " + p_strURL);
				}

				ImageIO.write(bufferedimage, postfix.getLabel(), outputtemp); // Write the Buffered Image into an output file

				setByteValues(outputtemp);
			}
		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);

		} finally {
			if(outputtemp != null) {
				outputtemp.delete();
			}
		}
	}

	public void setImage_ByURL(URL p_url) throws GException {
		File outputtemp = null;

		try {
			Image image = ImageIO.read(p_url.openStream());
			BufferedImage bufferedimage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
			Graphics bg = bufferedimage.getGraphics();
			bg.drawImage(image, 0, 0, null);
			bg.dispose();

			long timelong = System.currentTimeMillis();
			String strTempName = "TEMP_" + Long.toString(timelong);

			outputtemp = File.createTempFile(strTempName, ".tmp");

			String strFileName = p_url.getFile();

			GFilePostFix postfix;

			if(strFileName.endsWith(GFilePostFix.GIF.getLabel())) {
				postfix = GFilePostFix.GIF;

			} else if(strFileName.endsWith(GFilePostFix.PNG.getLabel())) {
				postfix = GFilePostFix.PNG;

			} else if(strFileName.endsWith(GFilePostFix.JPEG.getLabel())) {
				postfix = GFilePostFix.JPEG;

			} else if(strFileName.endsWith(GFilePostFix.JPG.getLabel())) {
				postfix = GFilePostFix.JPG;

			} else {
				throw new GException("Invalid Image Type!!!\nFile Name: " + strFileName);
			}

			ImageIO.write(bufferedimage, postfix.getLabel(), outputtemp); // Write the Buffered Image into an output file

			setByteValues(outputtemp);

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);

		} finally {
			if(outputtemp != null) {
				outputtemp.delete();
			}
		}
	}

	public BufferedImage toBufferedImage(int p_intMaxWidth, boolean p_bolFixedHeight) throws GException {
		try {
			if(isInvalid()) {
				return new BufferedImage(0, 0, Image.SCALE_FAST);
			}

			return toBufferedImage(ImageIO.read(new ByteArrayInputStream(super.getByteValues())), p_intMaxWidth, p_bolFixedHeight);

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public static BufferedImage toBufferedImage(String p_strURL, int p_intMaxWidth, boolean p_bolFixedHeight) throws GException {
		try {
			if(p_strURL.length() == 0) {
				return new BufferedImage(0, 0, Image.SCALE_FAST);
			}

			URL url = new URL(p_strURL);
			Image image = ImageIO.read(url.openStream());

			return toBufferedImage(image, p_intMaxWidth, p_bolFixedHeight);

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public static BufferedImage toBufferedImage(Image p_image, int p_intMaxWidth, boolean p_bolFixedHeight) throws GException {
		try {
			Dimension dimension = prepareDimensionPresent(p_image, p_intMaxWidth, p_bolFixedHeight);

			Image image = p_image.getScaledInstance(dimension.width, dimension.height, Image.SCALE_SMOOTH);

			BufferedImage resized = new BufferedImage(dimension.width, dimension.height, BufferedImage.TYPE_INT_ARGB);
			Graphics gph = resized.createGraphics();
			gph.drawImage(image, 0, 0, dimension.width, dimension.height, null);
			gph.dispose();

			return resized;

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public static Dimension prepareDimensionPresent(Image p_image, int p_intMaxWidth, boolean p_bolFixedHeight) throws GException {
		try {
			int intWidth = p_image.getWidth(null);
			int intHeight = p_image.getHeight(null);

			if(p_intMaxWidth > 0) {
				if(intWidth > intHeight) {

					float fRatio = (float)intWidth / (float)intHeight;

					if(intWidth > p_intMaxWidth) {
						intWidth = p_intMaxWidth;
						intHeight = Math.round(intWidth / fRatio);
					}
				} else {
					if(p_bolFixedHeight) {
						if(intHeight > p_intMaxWidth) {
							float fMaxWidth = p_intMaxWidth * 0.6F;
							p_intMaxWidth = Math.round(fMaxWidth);

							intWidth = p_intMaxWidth * intWidth / intHeight;
							intHeight = p_intMaxWidth;
						}
					} else {
						float fRatio = (float)intHeight/ (float)intWidth ;

						intWidth = p_intMaxWidth;
						intHeight = Math.round(intWidth * fRatio);
					}
				}
			}

			return new Dimension(intWidth, intHeight);

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public Image toImage(String p_strFullName_WithoutType, int p_intMaxWidth, ImageType p_imagetype) throws GException {
		File outputtemp = null;

		try {
			String strFullName_WithType = p_strFullName_WithoutType + '.' + p_imagetype.getLabel();
			outputtemp = File.createTempFile(strFullName_WithType, ".tmp");

			BufferedImage bufferedimage = toBufferedImage(p_intMaxWidth, false);
			ImageIO.write(bufferedimage, p_imagetype.getLabel(), outputtemp); // Write the Buffered Image into an output file
			Image image  = ImageIO.read(outputtemp);

			return image;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);

		} finally {
			if(outputtemp != null) {
				outputtemp.delete();
			}
		}
	}

	public Image toImage(int p_intMaxWidth, ImageType p_imagetype) throws GException {
		File outputtemp = null;

		try {
			String strFullName_WithType = Thread.currentThread().getId() + "_filetemp." + p_imagetype.getLabel() + ".tmp";
			outputtemp = File.createTempFile(strFullName_WithType, ".tmp");

			BufferedImage bufferedimage = toBufferedImage(p_intMaxWidth, false);
			ImageIO.write(bufferedimage, p_imagetype.getLabel(), outputtemp); // Write the Buffered Image into an output file
			Image image  = ImageIO.read(outputtemp);

			return image;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);

		} finally {
			if(outputtemp != null) {
				outputtemp.delete();
			}
		}
	}
}
