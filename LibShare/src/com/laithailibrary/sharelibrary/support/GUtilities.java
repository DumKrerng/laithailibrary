package com.laithailibrary.sharelibrary.support;

/**
 * Created with IntelliJ IDEA.
 * User: dumkrerng
 * Date: 9/3/12
 * Time: 12:00 AM
 * To change this template use File | Settings | File Templates.
 */
public class GUtilities {

	private static String ProName = "";
	private static String ProVersion = "";

	private static AppType ApplicationType = AppType.Server;
	private static SingleClient singleclient = SingleClient.No;

	private GUtilities() {}

	public static void setProName(String p_strProName) {
		ProName = p_strProName;
	}

	public static String getProName() {
		return ProName;
	}

	public static void setProVersion(String p_strProVersion) {
		ProVersion = p_strProVersion;
	}

	public static String getProVersion() {
		return ProVersion;
	}

	public static void setApplicationType(AppType p_apptype) {
		ApplicationType = p_apptype;
	}

	public static AppType getApplicationType() {
		return ApplicationType;
	}

	public static void setSingleClient(SingleClient p_singleclient) {
		singleclient = p_singleclient;
	}

	public static SingleClient getSingleClient() {
		return singleclient;
	}

	public static boolean isServer() {
		if(ApplicationType == AppType.Server) {
			return true;
		}

		return false;
	}

	public static boolean isServiceClient() {
		if(ApplicationType == AppType.Client) {
			return true;
		}

		return false;
	}

	public enum AppType {
		Client,
		Server
	}
}
