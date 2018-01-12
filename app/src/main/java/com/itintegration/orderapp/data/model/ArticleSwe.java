package com.itintegration.orderapp.data.model;

import com.itintegration.orderapp.data.provider.AbstractArticleProvider;

public class ArticleSwe extends AbstractArticleProvider.ArticleSwe{

    private String Id;
    private int itemId;
    private String description;
    private double total;
    private double disponible;
    private String unit;
    private String barcode;
    private byte outgoing;
    private byte inactive;
    private double price;
    private long codingCode;
    private String storageLocation;
    private byte articleMigration;
    private byte sCodeUpdate;
    private byte storageMerchandise;
    private String artGroup;
    private String alternativeDescription;
    private byte packageArticle;
    private String comment;
    private int amount;
    private String userUnit;
    private boolean addedToCart;

    public ArticleSwe() {}

    public String getId() {
        return Id;
    }

    public void setId(String mId) {
        this.Id = mId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getDisponible() {
        return disponible;
    }

    public void setDisponible(double disponible) {
        this.disponible = disponible;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public byte getOutgoing() {
        return outgoing;
    }

    public void setOutgoing(byte outgoing) {
        this.outgoing = outgoing;
    }

    public byte getInactive() {
        return inactive;
    }

    public void setInactive(byte inactive) {
        this.inactive = inactive;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getCodingCode() {
        return codingCode;
    }

    public void setCodingCode(long codingCode) {
        this.codingCode = codingCode;
    }

    public String getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }

    public byte getArticleMigration() {
        return articleMigration;
    }

    public void setArticleMigration(byte articleMigration) {
        this.articleMigration = articleMigration;
    }

    public byte getSCodeUpdate() {
        return sCodeUpdate;
    }

    public void setSCodeUpdate(byte sCodeUpdate) {
        this.sCodeUpdate = sCodeUpdate;
    }

    public byte getStorageMerchandise() {
        return storageMerchandise;
    }

    public void setStorageMerchandise(byte storageMerchandise) {
        this.storageMerchandise = storageMerchandise;
    }

    public String getArtGroup() {
        return artGroup;
    }

    public void setArtGroup(String artGroup) {
        this.artGroup = artGroup;
    }

    public String getAlternativeDescription() {
        return alternativeDescription;
    }

    public void setAlternativeDescription(String alternativeDescription) {
        this.alternativeDescription = alternativeDescription;
    }

    public byte getPackageArticle() {
        return packageArticle;
    }

    @Override
    public String getUserComment() {
        return comment;
    }

    @Override
    public void setUserComment(String comment) {
        this.comment = comment;
    }

    @Override
    public int getUserAmount() {
        return amount;
    }

    @Override
    public void setUserAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String getUserUnit() {
        return userUnit;
    }

    @Override
    public void setUserUnit(String unit) {
        userUnit = unit;
    }

    public void setPackageArticle(byte packageArticle) {
        this.packageArticle = packageArticle;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public boolean isMarkedForOrder() {
        return addedToCart;
    }

    public void setMarkedForOrder(boolean mAddedToCart) {
        this.addedToCart = mAddedToCart;
    }
}
