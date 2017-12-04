package com.itintegration.orderapp.ui.assortment.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder;

import com.itintegration.orderapp.R;
import com.itintegration.orderapp.ui.test.AbstractDataProvider;
import com.itintegration.orderapp.ui.test.DataProvider;
import com.itintegration.orderapp.ui.test.ExpandableItemIndicator;

class NewAssortmentAdapter extends AbstractExpandableItemAdapter<NewAssortmentAdapter.GroupViewHolder,
        NewAssortmentAdapter.ChildViewHolder> {

    private final Context mContext;
    private AbstractDataProvider mProvider;

    public NewAssortmentAdapter(Context context, AbstractDataProvider dataProvider) {
        mProvider = dataProvider;
        mContext = context;
        setHasStableIds(true);
    }

    private interface Expandable extends ExpandableItemConstants {
    }

    public static abstract class BaseViewHolder extends AbstractExpandableItemViewHolder {
        public FrameLayout mContainer;
        public TextView mTextView;

        public BaseViewHolder(View v) {
            super(v);
            mContainer = v.findViewById(R.id.container);
            mTextView = v.findViewById(android.R.id.text1);
        }
    }

    public static class GroupViewHolder extends BaseViewHolder {
        public ExpandableItemIndicator mIndicator;

        public GroupViewHolder(View v) {
            super(v);
            mIndicator = v.findViewById(R.id.indicator);
        }
    }

    public static class ChildViewHolder extends BaseViewHolder {
        public Button mButton;
        public ChildViewHolder(View v) {
            super(v);
            mButton = v.findViewById(R.id.childButton);
        }
    }

    @Override
    public boolean onCheckCanExpandOrCollapseGroup(GroupViewHolder holder, int groupPosition, int x, int y, boolean expand) {
        if (mProvider.getGroupItem(groupPosition).isPinned()) {
            return false;
        }
        if (!(holder.itemView.isEnabled() && holder.itemView.isClickable())) {
            return false;
        }
        return true;
    }

    @Override
    public int getGroupCount() {
        return mProvider.getGroupCount();
    }

    @Override
    public int getChildCount(int groupPosition) {
        return mProvider.getChildCount(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return mProvider.getGroupItem(groupPosition).getGroupId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return mProvider.getChildItem(groupPosition, childPosition).getChildId();
    }

    @Override
    public int getGroupItemViewType(int groupPosition) {
        return 0;
    }

    @Override
    public int getChildItemViewType(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public GroupViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.list_group_item, parent, false);
        return new GroupViewHolder(v);
    }

    @Override
    public ChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.list_item, parent, false);
        return new ChildViewHolder(v);
    }

    @Override
    public void onBindGroupViewHolder(GroupViewHolder holder, int groupPosition, int viewType) {
        final DataProvider.BaseData item = mProvider.getGroupItem(groupPosition);

        holder.mTextView.setText(item.getText());
        holder.itemView.setClickable(true);

        final int expandState = holder.getExpandStateFlags();

        if ((expandState & ExpandableItemConstants.STATE_FLAG_IS_UPDATED) != 0) {
            int bgResId;
            boolean isExpanded;
            boolean animateIndicator = ((expandState & Expandable.STATE_FLAG_HAS_EXPANDED_STATE_CHANGED) != 0);

            if ((expandState & Expandable.STATE_FLAG_IS_EXPANDED) != 0) {
                bgResId = R.drawable.bg_group_item_expanded_state;
                isExpanded = true;
            } else {
                bgResId = R.drawable.bg_group_item_normal_state;
                isExpanded = false;
            }
            holder.mContainer.setBackgroundResource(bgResId);
            holder.mIndicator.setExpandedState(isExpanded, animateIndicator);
        }
    }

    @Override
    public void onBindChildViewHolder(ChildViewHolder holder, int groupPosition, int childPosition, int viewType) {
        final DataProvider.ChildData item = mProvider.getChildItem(groupPosition, childPosition);
        int bgResId;
        bgResId = R.drawable.bg_item_normal_state;

        holder.mContainer.setBackgroundResource(bgResId);
        holder.mTextView.setText(item.getText());
        holder.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "This button works!", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
