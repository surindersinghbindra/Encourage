package com.nearnia.encouragement.beanclasses;

import java.io.Serializable;

import org.json.JSONArray;

public class Channels implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String channelName, image;

	String objectId;
	String relationObjectId;
//	ParseRelation<ParseObject> relationObject;
	String coverImageUrl;
	String iconImageUrl;
	JSONArray jsonArray;

	public String getIconImageUrl() {
		return iconImageUrl;
	}

	public void setIconImageUrl(String iconImageUrl) {
		this.iconImageUrl = iconImageUrl;
	}

	public JSONArray getJsonArray() {
		return jsonArray;
	}

	public void setJsonArray(JSONArray jsonArray) {
		this.jsonArray = jsonArray;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getCoverImageUrl() {
		return coverImageUrl;
	}

	public void setCoverImageUrl(String coverImageUrl) {
		this.coverImageUrl = coverImageUrl;
	}

//	public ParseRelation<ParseObject> getRelationObject() {
//		return relationObject;
//	}
//
//	public void setRelationObject(ParseRelation<ParseObject> relationObject) {
//		this.relationObject = relationObject;
//	}

	public String getRelationObjectId() {
		return relationObjectId;
	}

	public void setRelationObjectId(String relationObjectId) {
		this.relationObjectId = relationObjectId;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

}
