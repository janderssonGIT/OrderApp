package com.itintegration.orderapp.unused;

import com.itintegration.orderapp.unused.assortmentprovider.AbstractDataProvider;

public final class ChildData extends AbstractDataProvider.ChildData {
    private long mId;
    private final double mPrice;
    private final double mTotal;
    private String mComment;
    private int mAmount;
    private String mUnit;

    public ChildData(long id, double price, double total, String comment, int amount) {
        mId = id;
        mPrice = price;
        mTotal = total;
        mComment = comment;
        mAmount = amount;
        mUnit = "";
    }

    @Override
    public double getTotal() {
        return mTotal;
    }

    @Override
    public long getChildId() {
        return mId;
    }

    @Override
    public double getPrice() {
        return mPrice;
    }

    @Override
    public String getComment() {
        return mComment;
    }

    @Override
    public void setComment(String comment) {
        mComment = comment;
    }

    @Override
    public int getAmount() {
        return mAmount;
    }

    @Override
    public void setAmount(int amount) {
        mAmount = amount;
    }

    @Override
    public String getUnit() {
        return mUnit;
    }

    @Override
    public void setUnit(String unit) {
        mUnit = unit;
    }

}
