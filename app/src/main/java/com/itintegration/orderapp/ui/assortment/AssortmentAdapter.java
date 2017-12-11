package com.itintegration.orderapp.ui.assortment;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.itintegration.orderapp.data.AppDataManager;
import com.itintegration.orderapp.ui.assortmentprovider.AbstractDataProvider;
import com.itintegration.orderapp.ui.assortmentprovider.DataProvider;
import com.itintegration.orderapp.util.ExpandableItemIndicator;

import java.util.List;
import java.util.Locale;

class AssortmentAdapter extends AbstractExpandableItemAdapter<AssortmentAdapter.GroupViewHolder,
        AssortmentAdapter.ChildViewHolder> {

    private AbstractDataProvider mProvider;
    private List<String> unitList;
    private Context mContext;
    private AssortmentAdapterCallback callback;

    void updateProvider(String comments, int groupPosition) {
        mProvider.getChildItem(groupPosition).setComment(comments);
    }

    public interface AssortmentAdapterCallback {
        void saveComment(String comment, int groupPositionS);
    }

    AssortmentAdapter(Context context, AbstractDataProvider dataProvider, AssortmentAdapterCallback cb) {
        mProvider = dataProvider;
        mContext = context;
        unitList = new AppDataManager().generateUnitList();
        callback = cb;

        setHasStableIds(true);
    }

    private interface Expandable extends ExpandableItemConstants {

    }

    static abstract class BaseViewHolder extends AbstractExpandableItemViewHolder {
        FrameLayout mContainer;
        LinearLayout mInnerLeft;
        LinearLayout mInnerRight;

        BaseViewHolder(View v) {
            super(v);
            mContainer = v.findViewById(R.id.container);
            mInnerLeft = v.findViewById(R.id.innerLeftLayout);
            mInnerRight = v.findViewById(R.id.innerRightLayout);
        }
    }

    static class GroupViewHolder extends BaseViewHolder {
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

    static class ChildViewHolder extends BaseViewHolder {
        private TextView mTextViewPrice;
        private TextView mTextViewPrelimLager;
        private EditText mEditAmount;
        private EditText mEditComment;
        private Spinner mUnitSpinner;
        private ToggleButton mAddToOrderButton;
        private Button mSaveButton;

        ChildViewHolder(View v) {
            super(v);
            mTextViewPrice = v.findViewById(R.id.textViewPrice);
            mTextViewPrelimLager = v.findViewById(R.id.textViewPrelimLager);
            mEditAmount = v.findViewById(R.id.editAmount);
            mEditComment = v.findViewById(R.id.editComment);
            mUnitSpinner = v.findViewById(R.id.unitSpinner);
            mAddToOrderButton = v.findViewById(R.id.addToOrderButton);
            mSaveButton = v.findViewById(R.id.saveButton);
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
        return 1;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return mProvider.getGroupItem(groupPosition).getGroupId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return mProvider.getChildItem(groupPosition).getChildId();
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
        final DataProvider.GroupData data = mProvider.getGroupItem(groupPosition);
        holder.mTextViewHeaderA.setText(data.getProductName());
        holder.mTextViewHeaderB.setText(data.getProductBarcode());
        holder.itemView.setClickable(true);

        final int expandState = holder.getExpandStateFlags();

        if ((expandState & ExpandableItemConstants.STATE_FLAG_IS_UPDATED) != 0) {
            int bgResId;
            int strResId;
            boolean isExpanded;
            boolean animateIndicator = ((expandState & Expandable.STATE_FLAG_HAS_EXPANDED_STATE_CHANGED) != 0);

            if ((expandState & Expandable.STATE_FLAG_IS_EXPANDED) != 0) {
                bgResId = R.color.group_expanded_pressed_state;
                strResId = holder.mContainer.getResources().getColor(R.color.group_expanded_white_text);
                isExpanded = true;
            } else {
                bgResId = R.color.group_normal_state;
                strResId = holder.mContainer.getResources().getColor(R.color.group_expanded_black_text);
                isExpanded = false;
            }
            holder.mContainer.setBackgroundResource(bgResId);
            holder.mIndicator.setExpandedState(isExpanded, animateIndicator);
            holder.mTextViewHeaderA.setTextColor(strResId);
            holder.mTextViewHeaderB.setTextColor(strResId);
        }
    }

    @Override
    public void onBindChildViewHolder(final ChildViewHolder holder, final int groupPosition, int childPosition, int viewType) {
        String stringDouble;
        final DataProvider.ChildData item = mProvider.getChildItem(groupPosition);

            stringDouble = Double.toString(item.getTotal());
        holder.mTextViewPrelimLager.setText(stringDouble);
            stringDouble = Integer.toString(item.getAmount());
        holder.mEditAmount.setText(stringDouble);

        holder.mEditComment.setText(item.getComment().toString());
        holder.mTextViewPrice.setText((String.format(Locale.getDefault(),"%.2f",item.getPrice())));

        setupSpinner(holder); //TODO : Not dynamic
        setupSaveButton(holder, groupPosition);
        setupOrderButton(holder, groupPosition);
    }

    private void setupSaveButton(final ChildViewHolder holder, final int groupPosition) {
        holder.mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cmt = holder.mEditComment.getText().toString();
                callback.saveComment(cmt, groupPosition);
            }
        });
    }

    private void setupSpinner(final ChildViewHolder holder) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, unitList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.mUnitSpinner.setAdapter(adapter);
    }

    private void setupOrderButton(final ChildViewHolder holder, final int groupPosition) {
        holder.mAddToOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if(!markedToOrder.get(groupPosition)){
//                    holder.mInnerLeft.setBackgroundResource(R.color.item_added_to_order);
//                    holder.mInnerRight.setBackgroundResource(R.color.item_added_to_order);
//                    holder.mContainer.setBackgroundResource(R.color.group_added_to_order);
//                    markedToOrder.append(groupPosition, true);
//                } else {
//                    holder.mInnerLeft.setBackgroundResource(R.color.item_background_blue);
//                    holder.mInnerRight.setBackgroundResource(R.color.item_background_blue);
//                    holder.mContainer.setBackgroundResource(R.color.group_expanded_pressed_state);
//                    markedToOrder.append(groupPosition, false);
//                }
            }
        });
    }
































    /**
     *
     *
     * ### Supporting methods ###
     *
     *
     */

//    private void initializeSelectionsMap() {
//        Selections selections = new Selections();
//        userSelections = selections.createEmptyHashMap(mProvider.getGroupCount());
//    }


//    protected void postAndNotifyAdapter(final Handler handler, final RecyclerView recyclerView, final RecyclerView.Adapter adapter) {
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                if (!recyclerView.isComputingLayout()) {
//                    adapter.notifyDataSetChanged();
//                } else {
//                    postAndNotifyAdapter(handler, recyclerView, adapter);
//                }
//            }
//        });
//    }


}
