package com.app.shoptree.shoptree.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by lovishbajaj on 04/09/17.
 */

public class UserInfo {

    @SerializedName("UserID")
    @Expose
    private String userID;
    @SerializedName("CompanyID")
    @Expose
    private String companyID;
    @SerializedName("UserType")
    @Expose
    private String userType;
    @SerializedName("UserFirstName")
    @Expose
    private String userFirstName;
    @SerializedName("UserLastName")
    @Expose
    private String userLastName;
    @SerializedName("UserEmailID")
    @Expose
    private String userEmailID;
    @SerializedName("UserPhone")
    @Expose
    private String userPhone;
    @SerializedName("UserPasswordHash")
    @Expose
    private String userPasswordHash;
    @SerializedName("UserCPassword")
    @Expose
    private Object userCPassword;
    @SerializedName("UserPasswordSalt")
    @Expose
    private String userPasswordSalt;
    @SerializedName("UserEmailConfirm")
    @Expose
    private Integer userEmailConfirm;
    @SerializedName("UserPhoneConfirm")
    @Expose
    private Integer userPhoneConfirm;
    @SerializedName("UserLoginDate")
    @Expose
    private String userLoginDate;
    @SerializedName("UserLastLoginDate")
    @Expose
    private String userLastLoginDate;
    @SerializedName("UserAccessFailedCount")
    @Expose
    private Integer userAccessFailedCount;
    @SerializedName("UserLockoutEnable")
    @Expose
    private Integer userLockoutEnable;
    @SerializedName("UserRights")
    @Expose
    private String userRights;
    @SerializedName("UserPhoto")
    @Expose
    private String userPhoto;

    /**
     * No args constructor for use in serialization
     *
     */
    public UserInfo() {
    }

    /**
     *
     * @param userFirstName
     * @param userPhoto
     * @param userLockoutEnable
     * @param userPhoneConfirm
     * @param userType
     * @param userAccessFailedCount
     * @param userID
     * @param userLastLoginDate
     * @param userLoginDate
     * @param userEmailID
     * @param companyID
     * @param userPasswordHash
     * @param userEmailConfirm
     * @param userPhone
     * @param userLastName
     * @param userCPassword
     * @param userRights
     * @param userPasswordSalt
     */
    public UserInfo(String userID, String companyID, String userType, String userFirstName, String userLastName, String userEmailID, String userPhone, String userPasswordHash, Object userCPassword, String userPasswordSalt, Integer userEmailConfirm, Integer userPhoneConfirm, String userLoginDate, String userLastLoginDate, Integer userAccessFailedCount, Integer userLockoutEnable, String userRights, String userPhoto) {
        super();
        this.userID = userID;
        this.companyID = companyID;
        this.userType = userType;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userEmailID = userEmailID;
        this.userPhone = userPhone;
        this.userPasswordHash = userPasswordHash;
        this.userCPassword = userCPassword;
        this.userPasswordSalt = userPasswordSalt;
        this.userEmailConfirm = userEmailConfirm;
        this.userPhoneConfirm = userPhoneConfirm;
        this.userLoginDate = userLoginDate;
        this.userLastLoginDate = userLastLoginDate;
        this.userAccessFailedCount = userAccessFailedCount;
        this.userLockoutEnable = userLockoutEnable;
        this.userRights = userRights;
        this.userPhoto = userPhoto;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUserEmailID() {
        return userEmailID;
    }

    public void setUserEmailID(String userEmailID) {
        this.userEmailID = userEmailID;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserPasswordHash() {
        return userPasswordHash;
    }

    public void setUserPasswordHash(String userPasswordHash) {
        this.userPasswordHash = userPasswordHash;
    }

    public Object getUserCPassword() {
        return userCPassword;
    }

    public void setUserCPassword(Object userCPassword) {
        this.userCPassword = userCPassword;
    }

    public String getUserPasswordSalt() {
        return userPasswordSalt;
    }

    public void setUserPasswordSalt(String userPasswordSalt) {
        this.userPasswordSalt = userPasswordSalt;
    }

    public Integer getUserEmailConfirm() {
        return userEmailConfirm;
    }

    public void setUserEmailConfirm(Integer userEmailConfirm) {
        this.userEmailConfirm = userEmailConfirm;
    }

    public Integer getUserPhoneConfirm() {
        return userPhoneConfirm;
    }

    public void setUserPhoneConfirm(Integer userPhoneConfirm) {
        this.userPhoneConfirm = userPhoneConfirm;
    }

    public String getUserLoginDate() {
        return userLoginDate;
    }

    public void setUserLoginDate(String userLoginDate) {
        this.userLoginDate = userLoginDate;
    }

    public String getUserLastLoginDate() {
        return userLastLoginDate;
    }

    public void setUserLastLoginDate(String userLastLoginDate) {
        this.userLastLoginDate = userLastLoginDate;
    }

    public Integer getUserAccessFailedCount() {
        return userAccessFailedCount;
    }

    public void setUserAccessFailedCount(Integer userAccessFailedCount) {
        this.userAccessFailedCount = userAccessFailedCount;
    }

    public Integer getUserLockoutEnable() {
        return userLockoutEnable;
    }

    public void setUserLockoutEnable(Integer userLockoutEnable) {
        this.userLockoutEnable = userLockoutEnable;
    }

    public String getUserRights() {
        return userRights;
    }

    public void setUserRights(String userRights) {
        this.userRights = userRights;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

}
