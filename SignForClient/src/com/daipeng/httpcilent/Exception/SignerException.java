package com.daipeng.httpcilent.Exception;

public class SignerException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7077427716641144951L;
	
	public SignerException() {
	}

	public SignerException(String paramString) {
		super(paramString);
	}

	public SignerException(String paramString, Throwable paramThrowable) {
		super(paramString, paramThrowable);
	}

	public SignerException(Throwable paramThrowable) {
		super(paramThrowable);
	}

	protected SignerException(String paramString, Throwable paramThrowable,
			boolean paramBoolean1, boolean paramBoolean2) {
		super(paramString, paramThrowable, paramBoolean1, paramBoolean2);
	}

}
