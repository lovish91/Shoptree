package com.app.shoptree.shoptree.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by lovishbajaj on 08/06/17.
 */

public class CategoryModel {
    @SerializedName("CategoryID")
    @Expose
    private String CategoryId;
    @SerializedName("CatName")
    @Expose
    private String CatName;
    @SerializedName("CatDescription")
    @Expose
    private String CatDescription;
    @SerializedName("CatImage")
    @Expose
    private String CatImage;


    public CategoryModel (){
        super();
    }
    public CategoryModel(String CategoryId, String CatName, String CatDescription, String CatImage){
        super();
        this.CategoryId = CategoryId;
        this.CatName = CatName;
        this.CatDescription = CatDescription;
        this.CatImage = CatImage;

    }
    public String getCategoryId (){
        return CategoryId;
    }
    public void setCategoryId(String CategortyId){
        this.CategoryId = CategortyId;
    }
    public String getCatName(){
        return CatName;
    }
    public void setCatName(String CatName){
        this.CatImage = CatName;
    }
    public String getCatDescription(){
        return CatDescription;
    }
    public void setCatDescription(String CatDescription){
        this.CatDescription = CatDescription;
    }
    public String getCatImage (){
        return CatImage;
    }
    public void setCatImage(String CatImage){
        this.CatImage = CatImage;
    }
}
