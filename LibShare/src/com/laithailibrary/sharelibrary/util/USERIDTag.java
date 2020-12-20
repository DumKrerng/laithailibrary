package com.laithailibrary.sharelibrary.util;


import com.laithailibrary.sharelibrary.collection.GSet;
import exc.*;
import exc.ExceptionHandler;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: dumkrerng
 * Date: 5/13/12
 * Time: 3:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class USERIDTag {

	private static Set<String> setUSERIDTag = new GSet<String>();

	private USERIDTag() {}

	public static void addUSERIDTag(String strUSERIDTag) throws GException {
		try {
			if(strUSERIDTag.length() > 0) {
				if(setUSERIDTag.contains(strUSERIDTag)) {
					throw new GException("Duplicate USERIDTag!");

				} else {
					setUSERIDTag.add(strUSERIDTag);
				}
			}
		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

}
