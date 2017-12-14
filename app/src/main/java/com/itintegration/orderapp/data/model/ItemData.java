package com.itintegration.orderapp.data.model;


import com.itintegration.orderapp.ui.assortmentitemprovider.AbstractItemProvider;

public final class ItemData extends AbstractItemProvider.ItemData{
    private final long mId;
    private final String mProductName;
    private final String mBarcode;
    private final double mPrice;
    private final double mTotal;
    private String mComment;
    private int mAmount;
    private String mUnit;

    public ItemData(long id, String productName, String barcode, double price, double total, String comment, int amount, String unit) {
        mId = id;
        mProductName = productName;
        mBarcode = barcode;
        mPrice = price;
        mTotal = total;
        mComment = comment;
        mAmount = amount;
        mUnit = unit;
    }

    public long getItemId() {
        return mId;
    }

    public String getProductName() {
        return mProductName;
    }

    public String getProductBarcode() {
        return mBarcode;
    }

    public double getPrice() {
        return mPrice;
    }

    public double getTotal() {
        return mTotal;
    }

    public String getComment() {
        return mComment;
    }

    public void setComment(String mComment) {
        this.mComment = mComment;
    }

    public int getAmount() {
        return mAmount;
    }

    public void setAmount(int mAmount) {
        this.mAmount = mAmount;
    }

    public String getUnit() {
        return mUnit;
    }

    public void setUnit(String mUnit) {
        this.mUnit = mUnit;
    }
}
