
package com.grocerycustomer.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class RestResponse {

    @SerializedName("ResponseCode")
    private String mResponseCode;
    @SerializedName("ResponseMsg")
    private String mResponseMsg;
    @SerializedName("Result")
    private String mResult;
    @SerializedName("wallet")
    private String wallet;

    @SerializedName("code")
    private String code;

    @SerializedName("signupcredit")
    private String signupcredit;

    @SerializedName("refercredit")
    private String refercredit;

    public String getResponseCode() {
        return mResponseCode;
    }

    public void setResponseCode(String responseCode) {
        mResponseCode = responseCode;
    }

    public String getResponseMsg() {
        return mResponseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        mResponseMsg = responseMsg;
    }

    public String getResult() {
        return mResult;
    }

    public void setResult(String result) {
        mResult = result;
    }

    public String getWallet() {
        return wallet;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSignupcredit() {
        return signupcredit;
    }

    public void setSignupcredit(String signupcredit) {
        this.signupcredit = signupcredit;
    }

    public String getRefercredit() {
        return refercredit;
    }

    public void setRefercredit(String refercredit) {
        this.refercredit = refercredit;
    }
}
