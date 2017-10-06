package com.app.shoptree.shoptree.model;

/**
 * Created by lovishbajaj on 02/12/16.
 */

public class SugesstionM {
    private String SuggestionName;
   // private String SugesstionId;


    public SugesstionM(String SuggesionName ){
        this.SuggestionName = SuggesionName;
        //this.SugesstionId = SuggestionId;
    }

    public String getSuggestionName(){
        return this.SuggestionName;
    }
    public void setSuggestionName(String SuggestionName){
        this.SuggestionName=SuggestionName;
    }

   // public String getSugesstionId() {
     //   return this.SugesstionId;
   // }
    //public void setSugesstionId(String SugesstionId){
      //  this.SugesstionId =SugesstionId;
   // }

}
