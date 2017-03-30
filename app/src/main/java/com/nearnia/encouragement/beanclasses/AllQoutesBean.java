package com.nearnia.encouragement.beanclasses;

import android.os.Parcel;
import android.os.Parcelable;

public class AllQoutesBean{

	private String quoteid, quote, picture, amount, fullname, categoryid, subcategoryid, userid;
	private int heartGrwoth;

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getQuoteid() {
		return quoteid;
	}

	public void setQuoteid(String quoteid) {
		this.quoteid = quoteid;
	}

	public String getQuote() {
		return quote;
	}

	public void setQuote(String quote) {
		this.quote = quote;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getCategoryid() {
		return categoryid;
	}

	public void setCategoryid(String categoryid) {
		this.categoryid = categoryid;
	}

	public String getSubcategoryid() {
		return subcategoryid;
	}

	public void setSubcategoryid(String subcategoryid) {
		this.subcategoryid = subcategoryid;
	}

	public int getHeartGrowth() {
		return heartGrwoth;
	}

	public void setHeartGrowth(int heartGrwoth) {
		this.heartGrwoth = heartGrwoth;
	}

}
