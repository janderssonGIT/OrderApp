package com.itintegration.orderapp.ui.assortment;

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
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder;

import com.itintegration.orderapp.R;
import com.itintegration.orderapp.data.DataManager;
import com.itintegration.orderapp.data.model.ArticleSwe;
import com.itintegration.orderapp.data.provider.SearchArticleProvider;
import com.itintegration.orderapp.util.ExpandableItemIndicator;

import java.util.List;
import java.util.Locale;

class AssortmentAdapter extends AbstractExpandableItemAdapter<AssortmentAdapter.GroupViewHolder,
        AssortmentAdapter.ChildViewHolder> {

    private Context mContext;
    private AssortmentAdapterCallback callback;
    private RecyclerViewExpandableItemManager mExpandableItemManager;
    private DataManager mDataManager;

    public interface AssortmentAdapterCallback {
        void saveUserChangesOfGroup(String comment, int amount, String unit, int groupPositionS);
        void addToOrder(ArticleSwe article, int groupPosition);
        void removeFromOrder(int groupPosition);
    }

    AssortmentAdapter(RecyclerViewExpandableItemManager expandableItemManager, Context context, DataManager dataManager, AssortmentAdapterCallback cb) {
        mExpandableItemManager = expandableItemManager;
        mContext = context;
        callback = cb;
        mDataManager = dataManager;
        setHasStableIds(true);
    }

    private interface Expandable extends ExpandableItemConstants {}

    @Override
    public boolean onCheckCanExpandOrCollapseGroup(GroupViewHolder holder, int groupPosition, int x, int y, boolean expand) {
        if (!(holder.itemView.isEnabled() && holder.itemView.isClickable())) {
            return false;
        }
        return true;
    }

    @Override
    public int getGroupCount() {
        return mDataManager.getSearchArticleProvider().getArticleListGroupCount();
    }

    @Override
    public int getChildCount(int groupPosition) {
        return 1;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groupPosition;
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

        BaseViewHolder(View v) {
            super(v);
            mContainerGroup = v.findViewById(R.id.container_group);
        }
    }

    static class GroupViewHolder extends BaseViewHolder {
        private ExpandableItemIndicator mIndicator;
        private TextView mTextViewHeaderA;
        private TextView mTextViewHeaderB;

        GroupViewHolder(View v) {
            super(v);
            mIndicator = v.findViewById(R.id.indicator);
            mTextViewHeaderA = v.findViewById(R.id.textHeaderA);
            mTextViewHeaderB = v.findViewById(R.id.textHeaderB);
        }
    }

    static class ChildViewHolder extends BaseViewHolder {
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
        private ToggleButton mAddToOrderButton;

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
            mAddToOrderButton = v.findViewById(R.id.addToOrderButton);
        }
    }

    @Override
    public GroupViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.assortment_list_group_item, parent, false);
        return new GroupViewHolder(v);
    }

    @Override
    public ChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.assortment_list_item, parent, false);
        return new ChildViewHolder(v);
    }

    @Override
    public void onBindGroupViewHolder(GroupViewHolder holder, int groupPosition, int viewType) {
        final SearchArticleProvider.ArticleSwe data = mDataManager.getSearchArticleProvider().getArticleItem(groupPosition);
        holder.mTextViewHeaderA.setText(data.getDescription());
        holder.mTextViewHeaderB.setText(data.getBarcode());
        holder.itemView.setClickable(true);
        final int expandState = holder.getExpandStateFlags();
        applyGroupItemColorState(holder, groupPosition, expandState);
    }

    @Override
    public void onBindChildViewHolder(final ChildViewHolder holder, final int groupPosition, int childPosition, int viewType) {
        String stringDouble;
        final SearchArticleProvider.ArticleSwe item = mDataManager.getSearchArticleProvider().getArticleItem(groupPosition);
        stringDouble = Double.toString(item.getTotal());
        holder.mTextViewPrelimLager.setText(stringDouble);
        stringDouble = Integer.toString(item.getUserAmount());
        holder.mEditAmount.setText(stringDouble);
        holder.mEditComment.setText(item.getUserComment());
        holder.mTextViewPrice.setText((String.format(Locale.getDefault(),"%.2f",item.getPrice())));

        //TODO : Not implemented yet, but we must check if item exists in orderlist, if so, mark item accordingly in provider, and color flag as usual.
        //mDataManager.getOrderProvider().checkIfItemsExistsInOrderList(groupPosition)

        applyChildItemColorState(holder, groupPosition);

        //TODO : Declaring listeners in onBind is bad practice, as onBind is called many times in recycleView implementations.
        setupSpinner(holder, groupPosition);
        setupSaveButton(holder, groupPosition);
        setupAddToOrderButton(holder, groupPosition);
    }

    private void setupSaveButton(final ChildViewHolder holder, final int groupPosition) {
        holder.mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserSelections(holder, groupPosition);
            }
        });
    }

    private void setupSpinner(final ChildViewHolder holder, int groupPosition) {
        ArrayAdapter<String> adapter;
        List<String> unitData = mDataManager.getSearchArticleProvider().getUnitData();

        //populate spinner
        adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, unitData);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.mUnitSpinner.setAdapter(adapter);

        //set default position of spinner if user selection exists.
        if (!mDataManager.getSearchArticleProvider().getArticleItem(groupPosition).getUnit().equals("")) {
            for (int i = 0; i < unitData.size(); i++) {
                if(mDataManager.getSearchArticleProvider().getArticleItem(groupPosition).getUnit().equals(unitData.get(i))) {
                    holder.mUnitSpinner.setSelection(i);
                }
            }
        }
    }

    private void setupAddToOrderButton(final ChildViewHolder holder, final int groupPosition) {
        holder.mAddToOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDataManager.getSearchArticleProvider().getArticleItem(groupPosition).isMarkedForOrder()) {
                    mDataManager.getSearchArticleProvider().getArticleItem(groupPosition).setMarkedForOrder(false);
                    callback.removeFromOrder(groupPosition);
                } else {
                    mDataManager.getSearchArticleProvider().getArticleItem(groupPosition).setMarkedForOrder(true);
                    callback.addToOrder((ArticleSwe) mDataManager.getSearchArticleProvider().getArticleItem(groupPosition), groupPosition);
                    saveUserSelections(holder, groupPosition);
                }
            }
        });
    }

    /**
     * Actions.
     */

    private void saveUserSelections(ChildViewHolder holder, int groupPosition) {
        String cmt = holder.mEditComment.getText().toString();
        int amt = Integer.parseInt(holder.mEditAmount.getText().toString());
        String unit = holder.mUnitSpinner.getSelectedItem().toString();
        callback.saveUserChangesOfGroup(cmt, amt, unit, groupPosition);
    }

    private void applyGroupItemColorState(GroupViewHolder holder, int groupPosition, int expandState) {
         applyExpandStateColor(holder, expandState);
         if (mDataManager.getSearchArticleProvider().getArticleItem(groupPosition).isMarkedForOrder()) {
            holder.mContainerGroup.setBackgroundResource(R.color.group_added_to_order);
            holder.mTextViewHeaderA.setTextColor((holder.mContainerGroup.getResources().getColor(R.color.group_expanded_black_text)));
            holder.mTextViewHeaderB.setTextColor((holder.mContainerGroup.getResources().getColor(R.color.group_expanded_black_text)));
        } else if ((expandState & Expandable.STATE_FLAG_IS_EXPANDED) != 0){
            holder.mContainerGroup.setBackgroundResource(R.color.group_expanded_pressed_state);
            holder.mTextViewHeaderA.setTextColor((holder.mContainerGroup.getResources().getColor(R.color.group_expanded_white_text)));
            holder.mTextViewHeaderB.setTextColor((holder.mContainerGroup.getResources().getColor(R.color.group_expanded_white_text)));
        }
    }

    private void applyChildItemColorState(ChildViewHolder holder, int groupPosition) {
        if (mDataManager.getSearchArticleProvider().getArticleItem(groupPosition).isMarkedForOrder()) {
            holder.mInnerLeft.setBackgroundResource(R.color.item_added_to_order);
            holder.mInnerRight.setBackgroundResource(R.color.item_added_to_order);
            holder.mInnerRightBottom.setBackgroundResource(R.color.item_added_to_order_bright);
            holder.mInnerRightTop.setBackgroundResource(R.color.item_added_to_order_bright);
            holder.mEditComment.setBackgroundResource(R.color.item_added_to_order_bright);
            holder.mAddToOrderButton.setChecked(true);
        } else {
            holder.mInnerLeft.setBackgroundResource(R.color.item_background_blue);
            holder.mInnerRight.setBackgroundResource(R.color.item_background_blue);
            holder.mInnerRightBottom.setBackgroundResource(R.color.item_background_bright_blue);
            holder.mInnerRightTop.setBackgroundResource(R.color.item_background_bright_blue);
            holder.mEditComment.setBackgroundResource(R.color.item_background_bright_blue);
            holder.mAddToOrderButton.setChecked(false);
        }
    }

    private void applyExpandStateColor(GroupViewHolder holder, int expandState) {
        if ((expandState & ExpandableItemConstants.STATE_FLAG_IS_UPDATED) != 0) {
            int bgResId;
            int strResId;
            boolean isExpanded;
            boolean animateIndicator = ((expandState & Expandable.STATE_FLAG_HAS_EXPANDED_STATE_CHANGED) != 0);

            if ((expandState & Expandable.STATE_FLAG_IS_EXPANDED) != 0) {
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
