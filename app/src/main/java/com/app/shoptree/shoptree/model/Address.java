package com.app.shoptree.shoptree.model;

/**
 * Created by lovishbajaj on 21/08/17.
 */

public class Address {
    String UserAddressID;
    String UserID;
    String UserEmailID;
    String UserMobile;
    String UserLocationName;
    String UserAddreddLine1;
    String UserAddressLine2;
    String UserAddressLine3;
    String UserCity;
    String UserState;
    String UserPin;
    int Status;

    public Address(){
        super();
    }
    public Address(String UserAddressID,String UserID,String UserEmailID,String UserMobile,String UserLocationName,
                   String UserAddreddLine1,String UserAddressLine2,String UserAddressLine3,String UserCity,String UserState,
                   String UserPin,int Status) {

        this.UserAddressID = UserAddressID;
        this.UserID = UserID;
        this.UserEmailID = UserEmailID;
        this.UserMobile = UserMobile;
        this.UserLocationName = UserLocationName;
        this.UserAddreddLine1 = UserAddreddLine1;
        this.UserAddressLine2 = UserAddressLine2;
        this.UserAddressLine3 = UserAddressLine3;
        this.UserCity = UserCity;
        this.UserState = UserState;
        this.UserPin = UserPin;
        this.Status = Status;
    }

    public String getUserAddressID(){
        return UserAddressID;
    }
    public void setUserAddressID(String userAddressID){
        this.UserAddressID = userAddressID;
    }
    public String getUserID(){
        return UserID;
    }
    public void setUserID(String userID){
        this.UserID = userID;
    }
    public String getUserEmailID(){
        return UserEmailID;
    }
    public void setUserEmailID(String userEmailID){
        this.UserEmailID = userEmailID;
    }
    public String getUserMobile(){
        return UserMobile;
    }
    public void setUserMobile(String userMobile){
        this.UserMobile = userMobile;
    }
    public String getUserLocationName(){
        return UserLocationName;
    }
    public void setUserLocationName(String userLocationName){
        this.UserLocationName = userLocationName;
    }
    public String getUserAddreddLine1(){
        return UserAddreddLine1;
    }
    public void setUserAddreddLine1(String userAddreddLine1){
        this.UserAddreddLine1 = userAddreddLine1;
    }
    public String getUserAddressLine2(){
        return UserAddressLine2;
    }
    public void setUserAddressLine2(String userAddressLine2){
        this.UserAddressLine2 = userAddressLine2;
    }
    public String getUserAddressLine3(){
        return UserAddressLine3;
    }
    public void setUserAddressLine3(String userAddressLine3){
        this.UserAddressLine3 = userAddressLine3;
    }
    public String getUserCity (){
        return UserCity;
    }
    public void setUserCity(String userCity){
        this.UserCity = userCity;
    }
    public String getUserState(){
        return UserState;
    }
    public void setUserState(String userState){
        this.UserState = userState;
    }
    public String getUserPin(){
        return UserPin;
    }
    public void setUserPin(String userPin){
        this.UserPin = userPin;
    }
    public int getStatus(){
        return Status;
    }
    public void setStatus(int status){
        this.Status = status;
    }
}
