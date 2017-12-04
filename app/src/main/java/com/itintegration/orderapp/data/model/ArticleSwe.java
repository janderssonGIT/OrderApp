package com.itintegration.orderapp.data.model;


public class ArticleSwe {

    private int id;
    private String description;
    private double total;
    private double disponible;
    private String unit;
    private long barcode;
    private byte outgoing;
    private byte inactive;
    private double price;
    private int codingCode;
    private int storageLocation;
    private byte articleMigration;
    private byte sCodeUpdate;
    private byte storageMerchandise;
    private String artGroup;
    private String alternativeDescription;
    private byte packageArticle;

    public ArticleSwe(int id, String description, double total, double disponible, String unit,
                      long barcode, byte outgoing, byte inactive, double price, int codingCode,
                      int storageLocation, byte articleMigration, byte sCodeUpdate, byte storageMerchandise,
                      String artGroup, String alternativeDescription, byte packageArticle) {
        this.id = id;
        this.description = description;
        this.total = total;
        this.disponible = disponible;
        this.unit = unit;
        this.barcode = barcode;
        this.outgoing = outgoing;
        this.inactive = inactive;
        this.price = price;
        this.codingCode = codingCode;
        this.storageLocation = storageLocation;
        this.articleMigration = articleMigration;
        this.sCodeUpdate = sCodeUpdate;
        this.storageMerchandise = storageMerchandise;
        this.artGroup = artGroup;
        this.alternativeDescription = alternativeDescription;
        this.packageArticle = packageArticle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public long getBarcode() {
        return barcode;
    }

    public void setBarcode(long barcode) {
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

    public int getCodingCode() {
        return codingCode;
    }

    public void setCodingCode(int codingCode) {
        this.codingCode = codingCode;
    }

    public int getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(int storageLocation) {
        this.storageLocation = storageLocation;
    }

    public byte getArticleMigration() {
        return articleMigration;
    }

    public void setArticleMigration(byte articleMigration) {
        this.articleMigration = articleMigration;
    }

    public byte getsCodeUpdate() {
        return sCodeUpdate;
    }

    public void setsCodeUpdate(byte sCodeUpdate) {
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

    public void setPackageArticle(byte packageArticle) {
        this.packageArticle = packageArticle;
    }


}