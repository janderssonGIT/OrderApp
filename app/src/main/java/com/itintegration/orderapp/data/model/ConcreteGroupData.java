package com.itintegration.orderapp.data.model;

import com.itintegration.orderapp.ui.assortmentprovider.AbstractDataProvider;

public final class ConcreteGroupData extends AbstractDataProvider.GroupData {
    private final long mId;
    private final String mProductName;
    private final String mBarcode;
    private boolean mPinned;

    public ConcreteGroupData(long id, String h1, String h2) {
        mId = id;
        mProductName = h1;
        mBarcode = h2;
    }

    @Override
    public long getGroupId() {
        return mId;
    }

    @Override
    public boolean isSectionHeader() {
        return false;
    }

    @Override
    public String getProductName() {
        return mProductName;
    }

    @Override
    public String getProductBarcode() {
        return mBarcode;
    }

    @Override
    public void setPinned(boolean pinnedToSwipeLeft) {
        mPinned = pinnedToSwipeLeft;
    }

    @Override
    public boolean isPinned() {
        return mPinned;
    }
}
