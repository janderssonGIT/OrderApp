package com.itintegration.orderapp.data.model;

import com.itintegration.orderapp.ui.assortmentprovider.AbstractDataProvider;

public final class ConcreteChildData extends AbstractDataProvider.ChildData {
    private boolean mPinned;
    private long mId;
    private final double mPrice;
    private final double mTotal;
    private String mComment;
    private int mAmount;

    public ConcreteChildData(long id, double price, double total, String comment, int amount) {
        mId = id;
        mPrice = price;
        mTotal = total;
        mComment = comment;
        mAmount = amount;
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
    public void setPinned(boolean pinned) {
        mPinned = pinned;
    }

    @Override
    public boolean isPinned() {
        return mPinned;
    }

}
