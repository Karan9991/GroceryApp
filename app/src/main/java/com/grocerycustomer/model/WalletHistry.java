package com.grocerycustomer.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WalletHistry{

	@SerializedName("ResponseCode")
	private String responseCode;

	@SerializedName("data")
	private List<HistryItem> data;

	@SerializedName("ResponseMsg")
	private String responseMsg;

	@SerializedName("Result")
	private String result;

	public String getResponseCode(){
		return responseCode;
	}

	public List<HistryItem> getData(){
		return data;
	}

	public String getResponseMsg(){
		return responseMsg;
	}

	public String getResult(){
		return result;
	}
}