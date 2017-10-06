package com.app.shoptree.shoptree.model;

/**
 * Created by lovishbajaj on 04/09/17.
 */

public class UserInfo {
    String UserId;
    String UserName;
    String PhoneNo;

    public UserInfo(){
        super();
    }

    public String getUserId(){
        return UserId;
    }
    public void setUserId(String userId){
        this.UserId=userId;
    }
    public String getUserName(){
        return UserName;
    }
    public void setUserName(String userName){
        this.UserName=userName;
    }
    public String getPhoneNo(){
        return PhoneNo;
    }
    public void setPhoneNo(String phoneNo){
        this.PhoneNo=phoneNo;
    }
}

