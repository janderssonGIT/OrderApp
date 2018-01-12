package com.itintegration.orderapp.ui.order;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder;
import com.itintegration.orderapp.R;
import com.itintegration.orderapp.data.DataManager;
import com.itintegration.orderapp.data.provider.SearchArticleProvider;
import com.itintegration.orderapp.data.provider.OrderProvider;
import com.itintegration.orderapp.util.ExpandableItemIndicator;

import java.util.List;
import java.util.Locale;

public class OrderAdapter extends AbstractExpandableItemAdapter<OrderAdapter.GroupViewHolder,
        OrderAdapter.ChildViewHolder> {

    private DataManager mDataManager;
    private Context mContext;
    private OrderAdapterCallback callback;

    public interface OrderAdapterCallback {
        void saveUserChangesOfGroup(String comment, int amount, String unit, int groupPositionS);
    }

    OrderAdapter(Context context, DataManager dataManager, OrderAdapterCallback cb) {
        mContext = context;
        mDataManager = dataManager;
        callback = cb;
        setHasStableIds(true);
    }

    private interface Expandable extends ExpandableItemConstants {}

    @Override
    public boolean onCheckCanExpandOrCollapseGroup(OrderAdapter.GroupViewHolder holder, int groupPosition, int x, int y, boolean expand) {
        return true;
    }

    @Override
    public int getGroupCount() {
        return mDataManager.getOrderProvider().getArticleListGroupCount();
    }

    @Override
    public int getChildCount(int groupPosition) {
        return 1;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return mDataManager.getOrderProvider().getArticleItem(groupPosition).getItemId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return mDataManager.getOrderProvider().getArticleItem(groupPosition).getItemId();
    }

    @Override
    public int getGroupItemViewType(int groupPosition) {
        return 0;
    }

    @Override
    public int getChildItemViewType(int groupPosition, int childPosition) {
        return 0;
    }

    static abstract class BaseViewHolder extends AbstractExpandableItemViewHolder {
        FrameLayout mContainerGroup;
        FrameLayout mContainerChild;

        BaseViewHolder(View v) {
            super(v);
            mContainerGroup = v.findViewById(R.id.container_group);
            mContainerChild = v.findViewById(R.id.child_container);
        }
    }

    static class GroupViewHolder extends OrderAdapter.BaseViewHolder {
        ExpandableItemIndicator mIndicator;
        TextView mTextViewHeaderA;
        TextView mTextViewHeaderB;

        GroupViewHolder(View v) {
            super(v);
            mIndicator = v.findViewById(R.id.indicator);
            mTextViewHeaderA = v.findViewById(R.id.textHeaderA);
            mTextViewHeaderB = v.findViewById(R.id.textHeaderB);
        }
    }

    static class ChildViewHolder extends OrderAdapter.BaseViewHolder {
        private TextView mTextViewPrice;
        private TextView mTextViewPrelimLager;
        private EditText mEditAmount;
        private EditText mEditComment;
        private Spinner mUnitSpinner;
        private Button mSaveButton;
        private LinearLayout mInnerLeft;
        private LinearLayout mInnerRight;
        private LinearLayout mInnerRightTop;
        private LinearLayout mInnerRightBottom;

        ChildViewHolder(View v) {
            super(v);
            mTextViewPrice = v.findViewById(R.id.textViewPrice);
            mTextViewPrelimLager = v.findViewById(R.id.textViewPrelimLager);
            mEditAmount = v.findViewById(R.id.editAmount);
            mEditComment = v.findViewById(R.id.editComment);
            mUnitSpinner = v.findViewById(R.id.unitSpinner);
            mSaveButton = v.findViewById(R.id.saveButton);
            mInnerLeft = v.findViewById(R.id.innerLeftLayout);
            mInnerRight = v.findViewById(R.id.innerRightLayout);
            mInnerRightTop = v.findViewById(R.id.innerRightTop);
            mInnerRightBottom = v.findViewById(R.id.innerRightBottom);
        }
    }

    @Override
    public OrderAdapter.GroupViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.order_list_group_item, parent, false);
        return new OrderAdapter.GroupViewHolder(v);
    }

    @Override
    public OrderAdapter.ChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.order_list_item, parent, false);
        return new OrderAdapter.ChildViewHolder(v);
    }

    @Override
    public void onBindGroupViewHolder(OrderAdapter.GroupViewHolder holder, int groupPosition, int viewType) {
        final SearchArticleProvider.ArticleSwe data = mDataManager.getOrderProvider().getArticleItem(groupPosition);
        holder.mTextViewHeaderA.setText(data.getDescription());
        holder.mTextViewHeaderB.setText(data.getBarcode());
        holder.itemView.setClickable(true);

        final int expandState = holder.getExpandStateFlags();
        applyGroupItemColorState(holder, groupPosition, expandState);
    }

    @Override
    public void onBindChildViewHolder(final OrderAdapter.ChildViewHolder holder, final int groupPosition, int childPosition, int viewType) {
        String stringDouble;
        final SearchArticleProvider.ArticleSwe item = mDataManager.getOrderProvider().getArticleItem(groupPosition);

        stringDouble = Double.toString(item.getTotal());
        holder.mTextViewPrelimLager.setText(stringDouble);
        stringDouble = Integer.toString(item.getUserAmount());
        holder.mEditAmount.setText(stringDouble);

        holder.mEditComment.setText(item.getUserComment());
        holder.mTextViewPrice.setText((String.format(Locale.getDefault(),"%.2f",item.getPrice())));

        applyChildItemColorState(holder, groupPosition);

        setupSpinner(holder, groupPosition);
        setupSaveButton(holder, groupPosition);
    }

    private void setupSaveButton(final OrderAdapter.ChildViewHolder holder, final int groupPosition) {
        holder.mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSelections(holder, groupPosition);
            }
        });
    }

    private void setupSpinner(final OrderAdapter.ChildViewHolder holder, int groupPosition) {
        ArrayAdapter<String> adapter;
        List<String> unitData = mDataManager.getOrderProvider().getUnitData();

        adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, unitData);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.mUnitSpinner.setAdapter(adapter);

        //set default position of spinner if user selection exists.
        if (!mDataManager.getOrderProvider().getArticleItem(groupPosition).getUnit().equals("")) {
            for (int i = 0; i < unitData.size(); i++) {
                if(mDataManager.getOrderProvider().getArticleItem(groupPosition).getUnit().equals(unitData.get(i))) {
                    holder.mUnitSpinner.setSelection(i);
                }
            }
        }
    }

    /**
     * Actions.
     */

    private void saveSelections(OrderAdapter.ChildViewHolder holder, int groupPosition) {
        String cmt = holder.mEditComment.getText().toString();
        int amt = Integer.parseInt(holder.mEditAmount.getText().toString());
        String unit = holder.mUnitSpinner.getSelectedItem().toString();
        callback.saveUserChangesOfGroup(cmt, amt, unit, groupPosition);
    }

    private void applyGroupItemColorState(OrderAdapter.GroupViewHolder holder, int groupPosition, int expandState) {
        applyExpandStateColor(holder, expandState);
        if (mDataManager.getOrderProvider().getArticleItem(groupPosition).isMarkedForOrder()) {
            holder.mContainerGroup.setBackgroundResource(R.color.group_added_to_order);
            holder.mTextViewHeaderA.setTextColor((holder.mContainerGroup.getResources().getColor(R.color.group_expanded_black_text)));
            holder.mTextViewHeaderB.setTextColor((holder.mContainerGroup.getResources().getColor(R.color.group_expanded_black_text)));
        } else if ((expandState & OrderAdapter.Expandable.STATE_FLAG_IS_EXPANDED) != 0){
            holder.mContainerGroup.setBackgroundResource(R.color.group_expanded_pressed_state);
            holder.mTextViewHeaderA.setTextColor((holder.mContainerGroup.getResources().getColor(R.color.group_expanded_white_text)));
            holder.mTextViewHeaderB.setTextColor((holder.mContainerGroup.getResources().getColor(R.color.group_expanded_white_text)));
        }
    }

    private void applyChildItemColorState(OrderAdapter.ChildViewHolder holder, int groupPosition) {
        if (mDataManager.getOrderProvider().getArticleItem(groupPosition).isMarkedForOrder()) {
            holder.mInnerLeft.setBackgroundResource(R.color.item_added_to_order);
            holder.mInnerRight.setBackgroundResource(R.color.item_added_to_order);
            holder.mInnerRightBottom.setBackgroundResource(R.color.item_added_to_order_bright);
            holder.mInnerRightTop.setBackgroundResource(R.color.item_added_to_order_bright);
            holder.mEditComment.setBackgroundResource(R.color.item_added_to_order_bright);
        } else {
            holder.mInnerLeft.setBackgroundResource(R.color.item_background_blue);
            holder.mInnerRight.setBackgroundResource(R.color.item_background_blue);
            holder.mInnerRightBottom.setBackgroundResource(R.color.item_background_bright_blue);
            holder.mInnerRightTop.setBackgroundResource(R.color.item_background_bright_blue);
            holder.mEditComment.setBackgroundResource(R.color.item_background_bright_blue);
        }
    }

    private void applyExpandStateColor(OrderAdapter.GroupViewHolder holder, int expandState) {
        if ((expandState & ExpandableItemConstants.STATE_FLAG_IS_UPDATED) != 0) {
            int bgResId;
            int strResId;
            boolean isExpanded;
            boolean animateIndicator = ((expandState & OrderAdapter.Expandable.STATE_FLAG_HAS_EXPANDED_STATE_CHANGED) != 0);

            if ((expandState & OrderAdapter.Expandable.STATE_FLAG_IS_EXPANDED) != 0) {
                bgResId = R.color.group_expanded_pressed_state;
                strResId = holder.mContainerGroup.getResources().getColor(R.color.group_expanded_white_text);
                isExpanded = true;
            } else {
                bgResId = R.color.group_normal_state;
                strResId = holder.mContainerGroup.getResources().getColor(R.color.group_expanded_black_text);
                isExpanded = false;
            }
            holder.mIndicator.setExpandedState(isExpanded, animateIndicator);
            holder.mContainerGroup.setBackgroundResource(bgResId);
            holder.mTextViewHeaderA.setTextColor(strResId);
            holder.mTextViewHeaderB.setTextColor(strResId);
        }
    }
}
