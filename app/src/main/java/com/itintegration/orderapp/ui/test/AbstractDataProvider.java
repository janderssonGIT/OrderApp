package com.itintegration.orderapp.ui.test;

public abstract class AbstractDataProvider {
    public static abstract class BaseData {
        public abstract void setPinned(boolean pinned);
        public abstract boolean isPinned();
    }

    public static abstract class GroupData extends BaseData {
        public abstract boolean isSectionHeader();
        public abstract String getText();
        public abstract String getText2();
        public abstract long getGroupId();
    }

    public static abstract class ChildData extends BaseData {
        public abstract double getmTotal();
        public abstract long getChildId();
        public abstract double getPrice();
    }

    public abstract int getGroupCount();
    public abstract int getChildCount(int groupPosition);

    public abstract GroupData getGroupItem(int groupPosition);
    public abstract ChildData getChildItem(int groupPosition, int childPosition);

    public abstract void moveGroupItem(int fromGroupPosition, int toGroupPosition);
    public abstract void moveChildItem(int fromGroupPosition, int fromChildPosition, int toGroupPosition, int toChildPosition);

    public abstract void removeGroupItem(int groupPosition);
    public abstract void removeChildItem(int groupPosition, int childPosition);

    public abstract long undoLastRemoval();
}