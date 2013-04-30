package com.kos.cvut;

public class Constants {
	private static final String username = "kosandr",password = "nAtras9UpruCRuza";
	private static final String BASE = "https://kosapi.feld.cvut.cz/api/3b/";
	private static final String rss_news = "http://www.fel.cvut.cz/aktuality/rss.xml";

	public static String getUsername() {
		return username;
	}

	public static String getPassword() {
		return password;
	}

	public static String getBase() {
		return BASE;
	}

	public static String getRssNews() {
		return rss_news;
	}
	
}
