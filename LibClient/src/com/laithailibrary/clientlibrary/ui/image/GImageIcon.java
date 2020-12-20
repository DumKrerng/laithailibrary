package com.laithailibrary.clientlibrary.ui.image;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: dumkrerng
 * Date: 6/6/12
 * Time: 10:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class GImageIcon extends ImageIcon {

	private static final String PATH_ICON_NEW = "images/Actions-new-icon.png";
	private static final String PATH_ICON_EDIT = "images/Actions-edit-icon.png";
	private static final String PATH_ICON_DELETE = "images/Actions-delete-icon.png";
	private static final String PATH_ICON_RELOAD = "images/Actions-reload-icon.png";
	private static final String PATH_ICON_PROCESS = "images/progress.gif";

	private static final String PATH_ICON_SELECTALL = "images/Select-Actions-selectall-icon.png";
	private static final String PATH_ICON_SELECT = "images/Select-Actions-select-icon.png";
	private static final String PATH_ICON_UNSELECTALL = "images/Select-Actions-unselectall-icon.png";
	private static final String PATH_ICON_UNSELECT = "images/Select-Actions-unselect-icon.png";

	private static Icon m_iconNew = new ImageIcon(ClassLoader.getSystemResource(PATH_ICON_NEW));
	private static Icon m_iconEdit = new ImageIcon(ClassLoader.getSystemResource(PATH_ICON_EDIT));
	private static Icon m_iconDelete = new ImageIcon(ClassLoader.getSystemResource(PATH_ICON_DELETE));
	private static Icon m_iconReload = new ImageIcon(ClassLoader.getSystemResource(PATH_ICON_RELOAD));
	private static Icon m_iconProcess = new ImageIcon(ClassLoader.getSystemResource(PATH_ICON_PROCESS));

	private static Icon m_iconSelectAll = new ImageIcon(ClassLoader.getSystemResource(PATH_ICON_SELECTALL));
	private static Icon m_iconSelect = new ImageIcon(ClassLoader.getSystemResource(PATH_ICON_SELECT));
	private static Icon m_iconUnselectAll = new ImageIcon(ClassLoader.getSystemResource(PATH_ICON_UNSELECTALL));
	private static Icon m_iconUnselect = new ImageIcon(ClassLoader.getSystemResource(PATH_ICON_UNSELECT));

	private static final long serialVersionUID = 975622776147L;

	private GImageIcon() {}

	public static Icon getIconNew() {
		return m_iconNew;
	}

	public static Icon getIconEdit() {
		return m_iconEdit;
	}

	public static Icon getIconDelete() {
		return m_iconDelete;
	}

	public static Icon getIconReload() {
		return m_iconReload;
	}

	public static Icon getIconProcess() {
		return m_iconProcess;
	}

	public static Icon getIconSelectAll() {
		return m_iconSelectAll;
	}

	public static Icon getIconSelect() {
		return m_iconSelect;
	}

	public static Icon getIconUnselectAll() {
		return m_iconUnselectAll;
	}

	public static Icon getIconUnselect() {
		return m_iconUnselect;
	}
}
