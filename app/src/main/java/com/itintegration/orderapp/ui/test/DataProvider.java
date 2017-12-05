package com.itintegration.orderapp.ui.test;

import android.support.v4.util.Pair;

import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.itintegration.orderapp.data.AppDataManager;
import com.itintegration.orderapp.data.model.ArticleSwe;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DataProvider extends AbstractDataProvider {
    private AppDataManager dataManager = new AppDataManager();
    private List<Pair<GroupData, ChildData>> mData;

    //TODO : #1 skapa egen linkedlist utav egen klass
    //TODO : #2 bygg linkedlist utav lokal db
    //TODO : #3 bygg linkedlist utav rest API


    public DataProvider() {
        mData = new LinkedList<>();
        List<ArticleSwe> list = dataManager.generateArticleList();

        for(int i  = 0; i < list.size(); i++) {
            final String groupText = list.get(i).getDescription();
            final String groupText2 = Long.toString(list.get(i).getId());
            final ConcreteGroupData group = new ConcreteGroupData(i, groupText, groupText2);
            final ConcreteChildData child = new ConcreteChildData(i, list.get(i).getPrice(),
                    list.get(i).getTotal(), list.get(i).getUnit());
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
        private final String mTextHeaderA;
        private final String mTextHeaderB;
        private boolean mPinned;

        ConcreteGroupData(long id, String h1, String h2) {
            mId = id;
            mTextHeaderA = h1;
            mTextHeaderB = h2;
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
        public String getHeaderTextA() {
            return mTextHeaderA;
        }

        @Override
        public String getHeaderTextB() {
            return mTextHeaderB;
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
        private final String mUnit;

        //TODO : Lägg till attr efter behov att spara user input.

        ConcreteChildData(long id, double price, double total, String unit) {
            mId = id;
            mPrice = price;
            mTotal = total;
            mUnit = unit;
        }

        @Override
        public String getUnit() {
            return mUnit;
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