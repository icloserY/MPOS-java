package model;

import java.util.*;

public class Popup {
	private static final String CONTENT = "";
	private static final String TYPE = "";
	private static final String ID = "static";
	private static final String DISMISS = "";
	
	public static Map<String, String> getPopup(String content, String type, String id, String dismiss){
		Map<String, String> popup = new HashMap<>();
		popup.put("Content", content == null ? CONTENT : content);
		popup.put("Type", type == null ? TYPE : type);
		popup.put("ID", id == null ? ID : id);
		popup.put("Dismiss", dismiss == null ? DISMISS : dismiss);
		
		return popup;
	}
}
