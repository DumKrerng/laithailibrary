package com.laithailibrary.clientlibrary.ui.ucv;

import exc.GException;

/**
 * Created with IntelliJ IDEA.
 * User: dumkrerng
 * Date: 5/24/12
 * Time: 11:03 PM
 * To change this template use File | Settings | File Templates.
 */
public interface I_UIDataView {
	public void reloadDataView() throws GException;
	public void reloadDataChildView() throws GException;
	public void reloadDataChildView_NotReloadImageView() throws GException;
	public void clearDataView() throws GException;
}
