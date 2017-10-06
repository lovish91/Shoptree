package com.app.shoptree.shoptree.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by lovishbajaj on 04/10/17.
 */

public class LoginModel {


    private String userEmailID;
    private String userPasswordHash;

    public LoginModel(String userEmailID,String userPasswordHash){
        this.userEmailID = userEmailID;
        this.userPasswordHash = userPasswordHash;
    }

    public String getUserEmailID() {
        return userEmailID;
    }

    public void setUserEmailID(String userEmailID) {
        this.userEmailID = userEmailID;
    }

    public String getUserPasswordHash() {
        return userPasswordHash;
    }

    public void setUserPasswordHash(String userPasswordHash) {
        this.userPasswordHash = userPasswordHash;
    }

}
