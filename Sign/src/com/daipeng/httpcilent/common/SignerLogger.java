package com.daipeng.httpcilent.common;

public class SignerLogger {
	
	public SignerLogger(String name) {
		
	}

	public static SignerLogger getDoSignLogger(String name){
		return new SignerLogger(name);
	}
	
	public static SignerLogger getDoSignLogger(Class<?> classParam){
		return new SignerLogger(classParam.getName());
	}
	
	public void info(String logMsg){
		System.out.println(logMsg);	
	}
	
	public void error(String logMsg,Exception e){
		System.err.println(logMsg);
		e.printStackTrace();
	}
	
	public void error(String logMsg){
		System.err.println(logMsg);
	}
	
	public void debug(String logMsg){
		System.out.println(logMsg);	
	}
}
