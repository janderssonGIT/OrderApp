package com.itintegration.orderapp.data.model;


public class Article {

    private String ProductName;
    private int ArticleNumber;
    private String Description;
    private int Stock;

    public Article (String ProductName, int ArticleNumber, String Description, int Stock) {
        this.ProductName = ProductName;
        this.ArticleNumber = ArticleNumber;
        this.Description = Description;
        this.Stock = Stock;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public int getArticleNumber() {
        return ArticleNumber;
    }

    public void setArticleNumber(int articleNumber) {
        ArticleNumber = articleNumber;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getStock() {
        return Stock;
    }

    public void setStock(int stock) {
        Stock = stock;
    }
}