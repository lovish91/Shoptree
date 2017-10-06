package com.app.shoptree.shoptree.model;

/**
 * Created by lovishbajaj on 02/06/17.
 */

public class Product {
    String productId;
    String pmId;
    String productName;
    String productdicount;
    String productQuantity;
    Double productOldPrice;
    Double productNewPrice;
    String productImg;
    String unit;
    String description;

    public Product(){
        super();
    }

    public Product(String productId,String pmId,String productName,String productdicount,String productQuantity,Double productOldPrice,Double productNewPrice,String productImg,String unit,String description){
        this.productId = productId;
        this.pmId = pmId;
        this.productName = productName;
        this.productdicount = productdicount;
        this.productQuantity = productQuantity;
        this.productOldPrice = productOldPrice;
        this.productNewPrice = productNewPrice;
        this.productImg = productImg;
        this.unit = unit;
        this.description = description;
    }
    public String getProductId(){
        return productId;
    }
    public void setProductId(String productId){
        this.productId=productId;
    }
    public String getPmId(){
        return pmId;
    }
    public void setPmId(String pmId){
        this.pmId=pmId;
    }
    public String getProductName(){
        return productName;
    }
    public void setProductName(String productName){
        this.productName=productName;
    }
    public String getProductdicount(){
        return productdicount;
    }
    public void setProductdicount(String productdicount){
        this.productdicount = productdicount;
    }
    public String getProductQuantity(){
        return productQuantity;
    }
    public void setProductQuantity(String productQuantity){
        this.productQuantity = productQuantity;
    }
    public Double getProductOldPrice(){
        return productOldPrice;
    }
    public void setProductOldPrice(Double productOldPrice){
        this.productOldPrice=productOldPrice;
    }
    public Double getProductNewPrice(){
        return productNewPrice;
    }
    public void setProductNewPrice(Double productNewPrice){
        this.productNewPrice = productNewPrice;
    }
    public String getProductImg(){
        return productImg;
    }
    public void setProductImg(String productImg){
        this.productImg = productImg;
    }
    public String getUnit(){
        return unit;
    }
    public void setUnit(String unit){
        this.unit=unit;
    }
    public String getDescription(){
        return description;
    }
    public void setDescription(String description){
        this.description = description;
    }
}
