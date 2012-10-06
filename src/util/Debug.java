package util;

import java.util.HashMap;
import java.util.Map;

public class Debug {
	private static Debug instance = new Debug();
	private Map<String,Object> data;
	
	private Debug() {
		data = new HashMap<String,Object>();
	}
	
	public static final Debug getInstance() {
		return instance;
	}
	
	public void setData(String key, Object value) {
		data.put(key, value);
	}
	
	public Object getData(String key) {
		return data.get(key);
	}
}
