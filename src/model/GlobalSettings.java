package model;

import java.util.HashMap;
import java.util.Map;

public class GlobalSettings {
	private static final Map<String, String> settings = new HashMap<>();
	public static Map<String, String> getInstance() {
		return settings;
	}
}
