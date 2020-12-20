package com.laithailibrary.sharelibrary.file.support;

import java.util.*;
import com.laithailibrary.sharelibrary.collection.*;
import exc.*;

public enum GEncodingCode {
	UTF_8("UTF-8", "UTF-8"),
	UTF_16("UTF-16", "UTF-16"),
	TIS_620("TIS-620", "TIS-620"),
	Windows_874("x-windows-874", "Windows-874"),
	ISO_8859_1("ISO-8859-1", "ISO-8859-1"),
	ISO_8859_11("x-iso-8859-11", "ISO-8859-11"),
	IBM_874("x-IBM874", "IBM874"),
	US_ASCII("US-ASCII", "US-ASCII");

	private String m_strCode = "";
	private String m_strLabel = "";

	private GEncodingCode(String p_strCode, String p_strLabel) {
		m_strCode = p_strCode;
		m_strLabel = p_strLabel;
	}

	public String getCode() {
		return m_strCode;
	}

	public String getLabel() {
		return m_strLabel;
	}

	public static GEncodingCode getGEncodingCode_ByCode(String p_strCode) throws GException {
		try {
			if(p_strCode.compareTo(UTF_8.getCode()) == 0) {
				return UTF_8;

			} else if(p_strCode.compareTo(UTF_16.getCode()) == 0) {
				return UTF_16;

			} else if(p_strCode.compareTo(TIS_620.getCode()) == 0) {
				return TIS_620;

			} else if(p_strCode.compareTo(Windows_874.getCode()) == 0) {
				return Windows_874;

			} else if(p_strCode.compareTo(ISO_8859_1.getCode()) == 0) {
				return ISO_8859_1;

			} else if(p_strCode.compareTo(ISO_8859_11.getCode()) == 0) {
				return ISO_8859_11;

			} else if(p_strCode.compareTo(IBM_874.getCode()) == 0) {
				return IBM_874;

			} else if(p_strCode.compareTo(US_ASCII.getCode()) == 0) {
				return US_ASCII;
			}

			throw new GException("Invalid Code(" + p_strCode + ").");

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public static GEncodingCode getGEncodingCode_ByLabel(String p_strLabel) throws GException {
		try {
			if(p_strLabel.compareTo(UTF_8.getLabel()) == 0) {
				return UTF_8;

			} else if(p_strLabel.compareTo(UTF_16.getLabel()) == 0) {
				return UTF_16;

			} else if(p_strLabel.compareTo(TIS_620.getLabel()) == 0) {
				return TIS_620;

			} else if(p_strLabel.compareTo(Windows_874.getLabel()) == 0) {
				return Windows_874;

			} else if(p_strLabel.compareTo(ISO_8859_1.getLabel()) == 0) {
				return ISO_8859_1;

			} else if(p_strLabel.compareTo(ISO_8859_11.getLabel()) == 0) {
				return ISO_8859_11;

			} else if(p_strLabel.compareTo(IBM_874.getLabel()) == 0) {
				return IBM_874;

			} else if(p_strLabel.compareTo(US_ASCII.getLabel()) == 0) {
				return US_ASCII;
			}

			throw new GException("Invalid Label(" + p_strLabel + ").");

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public static List<String> getGEncodingCodeLabels() throws GException {
		try {
			List<String> lsGEncodingCodeLabels = new GList<>();
			lsGEncodingCodeLabels.add(UTF_8.getLabel());
			lsGEncodingCodeLabels.add(UTF_16.getLabel());
			lsGEncodingCodeLabels.add(TIS_620.getLabel());
			lsGEncodingCodeLabels.add(Windows_874.getLabel());
			lsGEncodingCodeLabels.add(ISO_8859_1.getLabel());
			lsGEncodingCodeLabels.add(ISO_8859_11.getLabel());
			lsGEncodingCodeLabels.add(IBM_874.getLabel());
			lsGEncodingCodeLabels.add(US_ASCII.getLabel());

			return lsGEncodingCodeLabels;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}
}