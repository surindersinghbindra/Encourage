package com.nearnia.encouragement;

public class UniversalSingleton {
	private static UniversalSingleton mInstance = null;

	Object object;



	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	private UniversalSingleton() {
		object = null;
	}

	public static UniversalSingleton getInstance() {
		if (mInstance == null) {
			mInstance = new UniversalSingleton();
		}
		return mInstance;
	}
	
}