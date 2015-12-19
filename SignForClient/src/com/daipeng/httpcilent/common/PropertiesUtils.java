package com.daipeng.httpcilent.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

public class PropertiesUtils {

	private PropertiesUtils() {}

	public static void init(Class<?> clazz){
		Logger logger = Logger.getLogger(PropertiesUtils.class);
		try{
			FileInputStream fis = new FileInputStream(new File(System.getProperty("confdir")+File.separator+"application.xml"));  
			Properties properties = new Properties();
			properties.loadFromXML(fis);  
	
			String className = clazz.getName().substring(clazz.getName().lastIndexOf(".") + 1);
			className = className.substring(0, 1).toLowerCase() + className.substring(1);
	
			Enumeration<Object> p = properties.keys();
	
			Field[] fields = clazz.getDeclaredFields();
	
			while(p.hasMoreElements()) {
				String key = (String) p.nextElement();
				String fieldName = key.toUpperCase().replace(".", "_");
	
				boolean success = false;
				for(Field f : fields) {
					f.setAccessible(true);
					if(f.getName().equals(fieldName.toString())) {
							if(f.getType().getName().equals(Long.class.getName())) {
								f.set(null, new Long(properties.getProperty(key)));
							} else {
								f.set(null, properties.getProperty(key));
							}
							success = true;
						break;
					}
				}
	
				if(success == false) {
					logger.warn(key + " did not defined.");
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error(e);
		}
	}	

}
