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
    private Pair<GroupData, ChildData> mLastRemovedGroup;
    private int mLastRemovedGroupPosition = -1;

    private ChildData mLastRemovedChild;
    private long mLastRemovedChildParentGroupId = -1;
    private int mLastRemovedChildPosition = -1;

    //TODO : #1 skapa egen linkedlist utav egen klass
    //TODO : #2 bygg linkedlist utav lokal db
    //TODO : #3 bygg linkedlist utav rest API

    public DataProvider() {

        final String groupItems = "AB";
        final String childItems = "1";
        mData = new LinkedList<>();

        for (int i = 0; i < groupItems.length(); i++) {
            final long groupId = i;
            final String groupText = Character.toString(groupItems.charAt(i));
            final ConcreteGroupData group = new ConcreteGroupData(groupId, groupText);
            final List<ChildData> children = new ArrayList<>();

            for (int j = 0; j < childItems.length(); j++) {
                final long childId = group.generateNewChildId();
                final String childText = Character.toString(childItems.charAt(j));

                children.add(new ConcreteChildData(childId, childText));
            }
            mData.add(new Pair<GroupData, List<ChildData>>(group, children));
        }
    }

    public void DataProviderTwo() {
        List<ArticleSwe> list = dataManager.generateArticleList();
        for(int i  = 0; i < list.size(); i++) {
            final String groupText = list.get(i).getDescription();
            final String groupText2 = Long.toString(list.get(i).getId());
            final ConcreteGroupData group = new ConcreteGroupData(i, groupText, groupText2);
            final ConcreteChildData child = new ConcreteChildData(i, list.get(i).getPrice(),
                    list.get(i).getTotal());
            mData.add(new Pair<GroupData, ChildData>(group, child));
        }
    }

    @Override
    public int getGroupCount() {
        return mData.size();
    }

    @Override
    public int getChildCount(int groupPosition) {
        return mData.get(groupPosition).second.size();
    }

    @Override
    public GroupData getGroupItem(int groupPosition) {
        if (groupPosition < 0 || groupPosition >= getGroupCount()) {
            throw new IndexOutOfBoundsException("groupPosition = " + groupPosition);
        }
        return mData.get(groupPosition).first;
    }

    @Override
    public ChildData getChildItem(int groupPosition, int childPosition) {
        final List<ChildData> children = mData.get(groupPosition).second;

        if (groupPosition < 0 || groupPosition >= getGroupCount()) {
            throw new IndexOutOfBoundsException("groupPosition = " + groupPosition);
        }
        if (childPosition < 0 || childPosition >= children.size()) {
            throw new IndexOutOfBoundsException("childPosition = " + childPosition);
        }
        return children.get(childPosition);
    }

    @Override
    public void moveGroupItem(int fromGroupPosition, int toGroupPosition) {
        if (fromGroupPosition == toGroupPosition) {
            return;
        }

        final Pair<GroupData, List<ChildData>> item = mData.remove(fromGroupPosition);
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

    public static final class ConcreteGroupData extends GroupData {
        private final long mId;
        private final String mTextHeader1;
        private final String mTextHeader2;
        private boolean mPinned;
        private long mNextChildId;

        ConcreteGroupData(long id, String text) {
            mId = id;
            mTextHeader1 = text;
            mTextHeader2 = null;
            mNextChildId = 0;
        }

        ConcreteGroupData(long id, String text1, String text2) {
            mId = id;
            mTextHeader1 = text1;
            mTextHeader2 = text2;
            mNextChildId = 0;
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
        public String getText() {
            return mTextHeader1;
        }

        @Override
        public String getText2() {
            return mTextHeader2;
        }

        @Override
        public void setPinned(boolean pinnedToSwipeLeft) {
            mPinned = pinnedToSwipeLeft;
        }

        @Override
        public boolean isPinned() {
            return mPinned;
        }

        public long generateNewChildId() {
            final long id = mNextChildId;
            mNextChildId += 1;
            return id;
        }
    }

    public static final class ConcreteChildData extends AbstractDataProvider.ChildData {
        private boolean mPinned;
        private long mId;
        private final double mPrice;
        private final double mTotal;

        //Lägg till attr efter behov att spara user input.

        ConcreteChildData(long id, double price, double total) {
            mId = id;
            mPrice = price;
            mTotal = total;
        }

        @Override
        public double getmTotal() {
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

        public void setChildId(long id) {
            this.mId = id;
        }
    }
}