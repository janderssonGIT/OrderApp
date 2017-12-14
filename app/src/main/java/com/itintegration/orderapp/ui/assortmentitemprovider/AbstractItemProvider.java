package com.itintegration.orderapp.ui.assortmentitemprovider;

import java.util.List;

public abstract class AbstractItemProvider {

    public static abstract class ItemData {
        public abstract long getItemId();
        public abstract String getProductName();
        public abstract String getProductBarcode();
        public abstract double getTotal();
        public abstract double getPrice();
        public abstract String getComment();
        public abstract void setComment(String comment);
        public abstract int getAmount();
        public abstract void setAmount(int amount);
        public abstract String getUnit();
        public abstract void setUnit(String unit);
    }

    public abstract int getGroupCount();
    public abstract AbstractItemProvider.ItemData getItem(int groupPosition);
    public abstract List<String> getUnitData();
}