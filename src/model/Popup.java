package model;

import java.util.*;

public class Popup {
	private static final String CONTENT = "";
	private static final String TYPE = "";
	private static final String ID = "static";
	private static final String DISMISS = "";//default "" if the change true insert "yes"
	
	/*
	 * content : popup content
	 * type : alert alert-danger, alert alert-info, alert alert-warning, alert alert-success
	 * id : static
	 * dissmiss : null or yes
	 */
	public static Map<String, String> getPopup(String content, String type, String id, String dismiss){
		Map<String, String> popup = new HashMap<>();
		popup.put("Content", content == null ? CONTENT : content);
		popup.put("Type", type == null ? TYPE : type);
		popup.put("ID", id == null ? ID : id);
		popup.put("Dismiss", dismiss == null ? DISMISS : dismiss);
		
		return popup;
	}
}
