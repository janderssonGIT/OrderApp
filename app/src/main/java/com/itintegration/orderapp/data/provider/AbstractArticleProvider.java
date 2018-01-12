package com.itintegration.orderapp.data.provider;

import java.util.List;

public abstract class AbstractArticleProvider {

    public static abstract class ArticleSwe {
        public abstract String getId();
        public abstract String getDescription();
        public abstract double getTotal();
        public abstract double getDisponible();
        public abstract String getUnit();
        public abstract String getBarcode();
        public abstract byte getOutgoing();
        public abstract byte getInactive();
        public abstract double getPrice();
        public abstract long getCodingCode();
        public abstract String getStorageLocation();
        public abstract byte getArticleMigration();
        public abstract byte getSCodeUpdate();
        public abstract byte getStorageMerchandise();
        public abstract String getArtGroup();
        public abstract String getAlternativeDescription();
        public abstract byte getPackageArticle();

        public abstract void setItemId(int id);
        public abstract int getItemId();

        //User selections
        public abstract String getUserComment();
        public abstract void setUserComment(String comment);
        public abstract int getUserAmount();
        public abstract void setUserAmount(int amount);
        public abstract String getUserUnit();
        public abstract void setUserUnit(String unit);
        public abstract boolean isMarkedForOrder();
        public abstract void setMarkedForOrder(boolean b);
    }

    public abstract int getArticleListGroupCount();
    public abstract AbstractArticleProvider.ArticleSwe getArticleItem(int groupPosition);
    public abstract List<String> getUnitData();
    public abstract boolean isEmpty();
}
