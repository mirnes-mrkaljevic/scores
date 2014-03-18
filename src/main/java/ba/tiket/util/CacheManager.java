package ba.tiket.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CacheManager {
	
	 	private static CacheManager instance=null;
	    private static Object monitor = new Object();
	    private Map<String, Object> cache = Collections.synchronizedMap(new HashMap<String, Object>());

	    private CacheManager() {
	    }

	    public void put(String cacheKey, Object value) {
	        cache.put(cacheKey, value);
	    }

	    public Object get(String cacheKey) {
	        return cache.get(cacheKey);
	    }

	    public void clear(String cacheKey) {
	        //cache.put(cacheKey, null);
	        cache.remove(cacheKey);
	    }

	    public void clear() {
	        cache.clear();
	    }

	    public static CacheManager getInstance() {
	        if (instance == null) {
	            synchronized (monitor) {
	                if (instance == null) {
	                    instance = new CacheManager();
	                }
	            }
	        }
	        return instance;
	    }  

}
