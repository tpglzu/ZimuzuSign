package com.daipeng.httpcilent.common;

public class Application {
	public static String URL_DOLOGIN;
	public static String URL_SIGNPAGE;
	public static String URL_DOSIGN;
	
	public static String LOGIN_USER_NAME_KEY;
	public static String LOGIN_USER_NAME_VALUE;
	public static String LOGIN_USER_PASSWORD_KEY;
	public static String LOGIN_USER_PASSWORD_VALUE;
	
	static {
		PropertiesUtils.init(Application.class);
	}
}
