package com.itintegration.orderapp.data.provider;

import android.support.v4.util.Pair;

import com.itintegration.orderapp.data.AppDataManager;
import com.itintegration.orderapp.data.model.ArticleSwe;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DataProvider extends AbstractDataProvider {

    private AppDataManager dataManager = new AppDataManager();
    private List<Pair<GroupData, ChildData>> mData;

    public DataProvider() {
        mData = new LinkedList<>();
        List<ArticleSwe> artList = dataManager.generateArticleList();

        for(int i  = 0; i < artList.size(); i++) {
            final String groupText = artList.get(i).getDescription();
            final String groupText2 = Long.toString(artList.get(i).getBarcode());
            final ConcreteGroupData group = new ConcreteGroupData(i,
                    groupText,
                    groupText2);
            final ConcreteChildData child = new ConcreteChildData(i,
                    artList.get(i).getPrice(),
                    artList.get(i).getTotal());
            mData.add(new Pair<GroupData, ChildData>(group, child));
        }
    }

    @Override
    public int getGroupCount() {
        return mData.size();
    }

    @Override
    public GroupData getGroupItem(int groupPosition) {
        if (groupPosition < 0 || groupPosition >= getGroupCount()) {
            throw new IndexOutOfBoundsException("groupPosition = " + groupPosition);
        }
        return mData.get(groupPosition).first;
    }

    @Override
    public ChildData getChildItem(int groupPosition) {
        return mData.get(groupPosition).second;
    }

    /**
     * UNUSED
     *
    @Override
    public void moveGroupItem(int fromGroupPosition, int toGroupPosition) {
        if (fromGroupPosition == toGroupPosition) {
            return;
        }

        final Pair<GroupData, ChildData> item = mData.remove(fromGroupPosition);
        mData.add(toGroupPosition, item);
    }

    @Override
    public void moveChildItem(int fromGroupPosition, int fromChildPosition, int toGroupPosition, int toChildPosition) {
        if ((fromGroupPosition == toGroupPosition) && (fromChildPosition == toChildPosition)) {
            return;
        }

        final Pair<GroupData, List<ChildData>> fromGroup = mData.get(fromGroupPosition);
        final Pair<GroupData, List<ChildData>> toGroup = mData.get(toGroupPosition);
        final ConcreteChildData item = (ConcreteChildData) fromGroup.second.remove(fromChildPosition);

        if (toGroupPosition != fromGroupPosition) {
            final long newId = ((ConcreteGroupData) toGroup.first).generateNewChildId();
            item.setChildId(newId);
        }
        toGroup.second.add(toChildPosition, item);
    }

    @Override
    public void removeGroupItem(int groupPosition) {
        mLastRemovedGroup = mData.remove(groupPosition);
        mLastRemovedGroupPosition = groupPosition;
        mLastRemovedChild = null;
        mLastRemovedChildParentGroupId = -1;
        mLastRemovedChildPosition = -1;
    }

    @Override
    public void removeChildItem(int groupPosition, int childPosition) {
        mLastRemovedChild = mData.get(groupPosition).second.remove(childPosition);
        mLastRemovedChildParentGroupId = mData.get(groupPosition).first.getGroupId();
        mLastRemovedChildPosition = childPosition;
        mLastRemovedGroup = null;
        mLastRemovedGroupPosition = -1;
    }

    @Override
    public long undoLastRemoval() {
        if (mLastRemovedGroup != null) {
            return undoGroupRemoval();
        } else if (mLastRemovedChild != null) {
            return undoChildRemoval();
        } else {
            return RecyclerViewExpandableItemManager.NO_EXPANDABLE_POSITION;
        }
    }

    private long undoGroupRemoval() {
        int insertedPosition;
        if (mLastRemovedGroupPosition >= 0 && mLastRemovedGroupPosition < mData.size()) {
            insertedPosition = mLastRemovedGroupPosition;
        } else {
            insertedPosition = mData.size();
        }
        mData.add(insertedPosition, mLastRemovedGroup);
        mLastRemovedGroup = null;
        mLastRemovedGroupPosition = -1;

        return RecyclerViewExpandableItemManager.getPackedPositionForGroup(insertedPosition);
    }

    private long undoChildRemoval() {
        Pair<GroupData, List<ChildData>> group = null;
        int groupPosition = -1;

        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).first.getGroupId() == mLastRemovedChildParentGroupId) {
                group = mData.get(i);
                groupPosition = i;
                break;
            }
        }

        if (group == null) {
            return RecyclerViewExpandableItemManager.NO_EXPANDABLE_POSITION;
        }

        int insertedPosition;
        if (mLastRemovedChildPosition >= 0 && mLastRemovedChildPosition < group.second.size()) {
            insertedPosition = mLastRemovedChildPosition;
        } else {
            insertedPosition = group.second.size();
        }

        group.second.add(insertedPosition, mLastRemovedChild);

        mLastRemovedChildParentGroupId = -1;
        mLastRemovedChildPosition = -1;
        mLastRemovedChild = null;

        return RecyclerViewExpandableItemManager.getPackedPositionForChild(groupPosition, insertedPosition);
    }
     **/

    public static final class ConcreteGroupData extends GroupData {
        private final long mId;
        private final String mProductName;
        private final String mBarcode;
        private boolean mPinned;

        ConcreteGroupData(long id, String h1, String h2) {
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

    public static final class ConcreteChildData extends AbstractDataProvider.ChildData {
        private boolean mPinned;
        private long mId;
        private final double mPrice;
        private final double mTotal;

        ConcreteChildData(long id, double price, double total) {
            mId = id;
            mPrice = price;
            mTotal = total;
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
        public void setPinned(boolean pinned) {
            mPinned = pinned;
        }

        @Override
        public boolean isPinned() {
            return mPinned;
        }

    }
}