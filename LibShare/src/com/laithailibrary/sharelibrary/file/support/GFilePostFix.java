package com.laithailibrary.sharelibrary.file.support;

import exc.*;

/**
 * Created by dumkrerng on 30/4/2561.
 */
public enum GFilePostFix {
	NONE(""),
	TEXT("txt"),
	GIF("gif"),
	JPG("jpg"),
	JPEG("jpeg"),
	PNG("png");

	private String m_strLabel = "";

	private GFilePostFix(String p_strLabel) {
		m_strLabel = p_strLabel;
	}

	public String getLabel() {
		return m_strLabel;
	}

	public static GFilePostFix getFileType(String p_strFullName) throws GException {
		try {
			if(p_strFullName.endsWith(TEXT.getLabel())) {
				return TEXT;

			} else if(p_strFullName.endsWith(GIF.getLabel())) {
				return GIF;

			} else if(p_strFullName.endsWith(JPG.getLabel())) {
				return JPG;

			} else if(p_strFullName.endsWith(JPEG.getLabel())) {
				return JPEG;

			} else if(p_strFullName.endsWith(PNG.getLabel())) {
				return PNG;
			}

			return NONE;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}
}
