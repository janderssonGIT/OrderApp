package com.itintegration.orderapp.unused.assortmentprovider;

import java.util.List;

public abstract class AbstractDataProvider {

    public static abstract class GroupData {
        public abstract String getProductName();
        public abstract String getProductBarcode();
        public abstract long getGroupId();
    }

    public static abstract class ChildData {
        public abstract double getTotal();
        public abstract long getChildId();
        public abstract double getPrice();
        public abstract String getComment();
        public abstract void setComment(String comment);
        public abstract int getAmount();
        public abstract void setAmount(int amount);
        public abstract String getUnit();
        public abstract void setUnit(String unit);
    }

    public abstract int getGroupCount();
    public abstract GroupData getGroupItem(int groupPosition);
    public abstract ChildData getChildItem(int groupPosition);
    public abstract List<String> getUnitData();
}
