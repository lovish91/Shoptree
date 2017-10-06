package com.miguelcatalan.materialsearchview;

/**
 * Created by lovishbajaj on 02/12/16.
 */

public class SugesstionM {
    private String articleNo;
    private String articleId;

    public SugesstionM(String ArticleNo , String ArticleId){
        this.articleNo = ArticleNo;
        this.articleId = ArticleId;
    }

    public String getarticleNo(){
        return this.articleNo;
    }
    public void setArticleNo(String ArticleNo){
        this.articleNo=ArticleNo;
    }

    public String getArticleId() {
        return this.articleId;
    }
    public void setPcs(String ArticleId){
        this.articleId=ArticleId;
    }

}
