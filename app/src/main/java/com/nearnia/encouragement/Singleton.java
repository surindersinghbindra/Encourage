package com.nearnia.encouragement;

import com.nearnia.encouragement.beanclasses.Channels;

public class Singleton {
	private static Singleton mInstance = null;

	Channels object;



	public Channels getObject() {
		return object;
	}

	public void setObject(Channels object) {
		this.object = object;
	}

	private Singleton() {
		object = null;
	}

	public static Singleton getInstance() {
		if (mInstance == null) {
			mInstance = new Singleton();
		}
		return mInstance;
	}
	
}