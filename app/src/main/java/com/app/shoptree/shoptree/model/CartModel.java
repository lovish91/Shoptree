package com.app.shoptree.shoptree.model;

/**
 * Created by lovishbajaj on 29/07/17.
 */

public class CartModel  {
    String productId;
    String pmId;
    String productName;
    String productdicount;
    String cartQuantity;
    Double cartRate;
    Double cartAmount;
    String productImg;
    String unit;

    public CartModel(){
        super();
    }

    public CartModel(String productId,String pmId,String productName,String productdicount,String cartQuantity,Double cartRate,Double cartAmount,String productImg,String unit){
        this.productId = productId;
        this.pmId = pmId;
        this.productName = productName;
        this.productdicount = productdicount;
        this.cartQuantity = cartQuantity;
        this.cartAmount = cartAmount;
        this.cartRate = cartRate;
        this.productImg = productImg;
        this.unit = unit;
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
    public String getCartQuantity(){
        return cartQuantity;
    }
    public void setCartQuantity(String cartQuantity){
        this.cartQuantity = cartQuantity;
    }
    public Double getCartRate(){
        return cartRate;
    }
    public void setCartRate(Double cartRate){
        this.cartRate=cartRate;
    }
    public Double getCartAmount(){
        return cartAmount;
    }
    public void setCartAmount(Double cartAmount){
        this.cartAmount = cartAmount;
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
}

