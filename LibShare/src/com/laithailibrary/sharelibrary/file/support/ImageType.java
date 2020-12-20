package com.laithailibrary.sharelibrary.file.support;

import exc.*;

/**
 * Created by dumkrerng on 31/10/2560.
 */
public enum ImageType {
	GIF("gif"),
	JPEG("jpeg"),
	PNG("png");

	private String m_strLabel = "";

	private ImageType(String p_strLabel) {
		m_strLabel = p_strLabel;
	}

	public String getLabel() {
		return m_strLabel;
	}

	public static ImageType getHTMLImageType_ImageName(String p_strImageName) throws GException {
		try {
			if(p_strImageName.endsWith(GIF.getLabel())) {
				return GIF;

			} else if(p_strImageName.endsWith(JPEG.getLabel())
				|| p_strImageName.endsWith("jpg")) {

				return JPEG;

			} else if(p_strImageName.endsWith(PNG.getLabel())) {
				return PNG;
			}

			throw new GException("Not support image " + p_strImageName + "!!!");

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}
}
