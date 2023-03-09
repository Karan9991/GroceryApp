package com.grocerycustomer.model;

import com.google.gson.annotations.SerializedName;

public class HistryItem {

	@SerializedName("uid")
	private String uid;

	@SerializedName("amt")
	private String amt;

	@SerializedName("id")
	private String id;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private String status;

	public String getUid(){
		return uid;
	}

	public String getAmt(){
		return amt;
	}

	public String getId(){
		return id;
	}

	public String getMessage(){
		return message;
	}

	public String getStatus(){
		return status;
	}
}