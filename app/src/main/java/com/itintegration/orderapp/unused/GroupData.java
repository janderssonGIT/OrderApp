package com.itintegration.orderapp.unused;

import com.itintegration.orderapp.unused.assortmentprovider.AbstractDataProvider;

public final class GroupData extends AbstractDataProvider.GroupData {
    private final long mId;
    private final String mProductName;
    private final String mBarcode;

    public GroupData(long id, String productName, String barcode) {
        mId = id;
        mProductName = productName;
        mBarcode = barcode;
    }

    @Override
    public long getGroupId() {
        return mId;
    }

    @Override
    public String getProductName() {
        return mProductName;
    }

    @Override
    public String getProductBarcode() {
        return mBarcode;
    }

}
