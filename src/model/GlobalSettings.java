package model;

import java.util.*;

public class GlobalSettings {
	private static final Map<String, String> settings = new HashMap<>();
	public static Map<String, String> getInstance() {
		return settings;
	}
	
	public static String get(String key){
		return settings.get(key);
	}

}
