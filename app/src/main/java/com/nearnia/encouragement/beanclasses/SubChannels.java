package com.nearnia.encouragement.beanclasses;

public class SubChannels {

	public String subChannelName;
	public String subChannelId;

	public boolean isSelected;

	public boolean box;
	public boolean ismasked;

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public boolean isIsmasked() {
		return ismasked;
	}

	public void setIsmasked(boolean ismasked) {
		this.ismasked = ismasked;
	}

	public boolean isBox() {
		return box;
	}

	public void setBox(boolean box) {
		this.box = box;
	}

	public String getSubChannelName() {
		return subChannelName;
	}

	public void setSubChannelName(String subChannelName) {
		this.subChannelName = subChannelName;
	}

	public String getSubChannelId() {
		return subChannelId;
	}

	public void setSubChannelId(String subChannelId) {
		this.subChannelId = subChannelId;
	}

}
