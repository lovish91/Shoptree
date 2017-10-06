package com.app.shoptree.shoptree.model;

import android.content.Intent;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by lovishbajaj on 05/10/17.
 */

public class TestModel{

    @SerializedName("SNo")
    @Expose
    private Integer sNo;
    @SerializedName("CartID")
    @Expose
    private String cartID;
    @SerializedName("CustomerID")
    @Expose
    private String customerID;
    @SerializedName("CompanyID")
    @Expose
    private String companyID;
    @SerializedName("PMID")
    @Expose
    private String pMID;
    @SerializedName("BrandCode")
    @Expose
    private String brandCode;
    @SerializedName("CategoryID")
    @Expose
    private String categoryID;
    @SerializedName("ProductID")
    @Expose
    private String productID;
    @SerializedName("ProName")
    @Expose
    private String proName;
    @SerializedName("ProDescription")
    @Expose
    private String proDescription;
    @SerializedName("ProPhotoMain")
    @Expose
    private String proPhotoMain;
    @SerializedName("MerchantID")
    @Expose
    private String merchantID;
    @SerializedName("CartQty")
    @Expose
    private Integer cartQty;
    @SerializedName("CartRate")
    @Expose
    private Integer cartRate;
    @SerializedName("CartAmount")
    @Expose
    private Integer cartAmount;
    @SerializedName("CartDate")
    @Expose
    private String cartDate;
    @SerializedName("TaxID")
    @Expose
    private Integer taxID;
    @SerializedName("DisID")
    @Expose
    private String disID;
    @SerializedName("UnitID")
    @Expose
    private Integer unitID;
    @SerializedName("OptionID")
    @Expose
    private Integer optionID;
    @SerializedName("OtherTax")
    @Expose
    private Integer otherTax;
    @SerializedName("DelCharges")
    @Expose
    private Integer delCharges;
    @SerializedName("Status")
    @Expose
    private Integer status;

    public TestModel() {
    }

    /**
     *
     * @param cartAmount
     * @param otherTax
     * @param sNo
     * @param status
     * @param categoryID
     * @param merchantID
     * @param cartDate
     * @param cartID
     * @param productID
     * @param proDescription
     * @param optionID
     * @param proName
     * @param pMID
     * @param brandCode
     * @param cartQty
     * @param taxID
     * @param cartRate
     * @param companyID
     * @param proPhotoMain
     * @param unitID
     * @param customerID
     * @param delCharges
     * @param disID
     */
    public TestModel(Integer sNo, String cartID, String customerID, String companyID, String pMID, String brandCode, String categoryID, String productID, String proName, String proDescription, String proPhotoMain, String merchantID, Integer cartQty, Integer cartRate, Integer cartAmount, String cartDate, Integer taxID, String disID, Integer unitID, Integer optionID, Integer otherTax, Integer delCharges, Integer status) {
        super();
        this.sNo = sNo;
        this.cartID = cartID;
        this.customerID = customerID;
        this.companyID = companyID;
        this.pMID = pMID;
        this.brandCode = brandCode;
        this.categoryID = categoryID;
        this.productID = productID;
        this.proName = proName;
        this.proDescription = proDescription;
        this.proPhotoMain = proPhotoMain;
        this.merchantID = merchantID;
        this.cartQty = cartQty;
        this.cartRate = cartRate;
        this.cartAmount = cartAmount;
        this.cartDate = cartDate;
        this.taxID = taxID;
        this.disID = disID;
        this.unitID = unitID;
        this.optionID = optionID;
        this.otherTax = otherTax;
        this.delCharges = delCharges;
        this.status = status;
    }

    public Integer getSNo() {
        return sNo;
    }

    public void setSNo(Integer sNo) {
        this.sNo = sNo;
    }

    public String getCartID() {
        return cartID;
    }

    public void setCartID(String cartID) {
        this.cartID = cartID;
    }

    public Object getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getPMID() {
        return pMID;
    }

    public void setPMID(String pMID) {
        this.pMID = pMID;
    }

    public String getBrandCode() {
        return brandCode;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getProDescription() {
        return proDescription;
    }

    public void setProDescription(String proDescription) {
        this.proDescription = proDescription;
    }

    public String getProPhotoMain() {
        return proPhotoMain;
    }

    public void setProPhotoMain(String proPhotoMain) {
        this.proPhotoMain = proPhotoMain;
    }

    public String getMerchantID() {
        return merchantID;
    }

    public void setMerchantID(String merchantID) {
        this.merchantID = merchantID;
    }

    public Integer getCartQty() {
        return cartQty;
    }

    public void setCartQty(Integer cartQty) {
        this.cartQty = cartQty;
    }

    public Integer getCartRate() {
        return cartRate;
    }

    public void setCartRate(Integer cartRate) {
        this.cartRate = cartRate;
    }

    public Integer getCartAmount() {
        return cartAmount;
    }

    public void setCartAmount(Integer cartAmount) {
        this.cartAmount = cartAmount;
    }

    public String getCartDate() {
        return cartDate;
    }

    public void setCartDate(String cartDate) {
        this.cartDate = cartDate;
    }

    public Integer getTaxID() {
        return taxID;
    }

    public void setTaxID(Integer taxID) {
        this.taxID = taxID;
    }

    public Object getDisID() {
        return disID;
    }

    public void setDisID(String disID) {
        this.disID = disID;
    }

    public Integer getUnitID() {
        return unitID;
    }

    public void setUnitID(Integer unitID) {
        this.unitID = unitID;
    }

    public Integer getOptionID() {
        return optionID;
    }

    public void setOptionID(Integer optionID) {
        this.optionID = optionID;
    }

    public Integer getOtherTax() {
        return otherTax;
    }

    public void setOtherTax(Integer otherTax) {
        this.otherTax = otherTax;
    }

    public Integer getDelCharges() {
        return delCharges;
    }

    public void setDelCharges(Integer delCharges) {
        this.delCharges = delCharges;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}