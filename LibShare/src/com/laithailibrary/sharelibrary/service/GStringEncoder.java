package com.laithailibrary.sharelibrary.service;

import java.util.*;
import exc.*;

/**
 * Created by dumkrerng on 14/8/2559.
 */
public class GStringEncoder {

	private GStringEncoder() {}

	public static String encode(String p_strKey, String p_strString) throws GException {
		try {
			if(p_strKey.length() <= 0
				|| p_strString.length() <= 0) {

				return p_strString;
			}

			int intLength = p_strString.length();
			int intCenter = intLength / 2;

			String strString01 = p_strString.substring(0, intCenter);
			String strString02 = p_strString.replace(strString01, "");
			p_strString = strString01 + '$' + p_strKey + '$' + strString02;

			// Encode using basic encoder
			String strEncoded = Base64.getEncoder().encodeToString(p_strString.getBytes("utf-8"));

			return strEncoded;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public static String decode(String p_strKey, String p_strString_Encoded) throws GException {
		try {
			if(p_strKey.length() <= 0
				|| p_strString_Encoded.length() <= 0) {

				return p_strString_Encoded;
			}

			p_strString_Encoded = p_strString_Encoded.replace('$' + p_strKey + '$', "");

			// Decode
			byte[] base64decodedBytes = Base64.getDecoder().decode(p_strString_Encoded);
			String strString_Decoded = new String(base64decodedBytes, "utf-8");

			return strString_Decoded;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}
}
