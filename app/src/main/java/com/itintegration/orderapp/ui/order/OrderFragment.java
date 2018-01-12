package com.itintegration.orderapp.ui.order;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.SimpleListDividerDecorator;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;
import com.itintegration.orderapp.R;
import com.itintegration.orderapp.data.DataManager;
import com.itintegration.orderapp.di.component.ActivityComponent;

import javax.inject.Inject;

public class OrderFragment extends Fragment implements RecyclerViewExpandableItemManager.OnGroupCollapseListener,
        RecyclerViewExpandableItemManager.OnGroupExpandListener, OrderAdapter.OrderAdapterCallback {

    private static final String SAVED_STATE_EXPANDABLE_ITEM_MANAGER = "RecyclerViewExpandableItemManager";

    private RecyclerView mRecyclerView;
    private TextView mEmptyView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mWrappedAdapter;
    private RecyclerViewExpandableItemManager mRecyclerViewExpandableItemManager;
    private OrderAdapter orderAdapter;
    private OrderActivity mActivity;

    @Inject
    DataManager mDataManager;

    //dagger 2
    public ActivityComponent getActivityComponent() {
        if (mActivity != null) {
            return mActivity.getActivityComponent();
        }
        return null;
    }

    public interface Callback {
        void onFragmentAttached();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OrderActivity) {
            OrderActivity activity = (OrderActivity) context;
            this.mActivity = activity;
            activity.onFragmentAttached();
        }
    }

    public OrderFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.order_fragment_recycler_listview, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = getView().findViewById(R.id.recycler_view);
        mEmptyView = getView().findViewById(R.id.empty_view);
        mLayoutManager = new LinearLayoutManager(getContext());

        retrieveViewState();

        final Parcelable eimSavedState = (savedInstanceState != null) ? savedInstanceState.getParcelable(SAVED_STATE_EXPANDABLE_ITEM_MANAGER) : null;
        mRecyclerViewExpandableItemManager = new RecyclerViewExpandableItemManager(eimSavedState);
        mRecyclerViewExpandableItemManager.setOnGroupExpandListener(this);
        mRecyclerViewExpandableItemManager.setOnGroupCollapseListener(this);

        orderAdapter = new OrderAdapter(this.getContext(), mDataManager, OrderFragment.this);
        mWrappedAdapter = mRecyclerViewExpandableItemManager.createWrappedAdapter(orderAdapter);
        final GeneralItemAnimator animator = new RefactoredDefaultItemAnimator();

        animator.setSupportsChangeAnimations(false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mWrappedAdapter);
        mRecyclerView.setItemAnimator(animator);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.addItemDecoration(new SimpleListDividerDecorator(ContextCompat.getDrawable(getContext(), R.drawable.list_divider_h), true));
        mRecyclerViewExpandableItemManager.attachRecyclerView(mRecyclerView);
    }

    private void retrieveViewState() {
        if (mDataManager.getSearchArticleProvider().isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mRecyclerViewExpandableItemManager != null) {
            outState.putParcelable(SAVED_STATE_EXPANDABLE_ITEM_MANAGER,
                    mRecyclerViewExpandableItemManager.getSavedState());
        }
    }

    @Override
    public void onDestroyView() {
        if (mRecyclerViewExpandableItemManager != null) {
            mRecyclerViewExpandableItemManager.release();
            mRecyclerViewExpandableItemManager = null;
        }

        if (mRecyclerView != null) {
            mRecyclerView.setItemAnimator(null);
            mRecyclerView.setAdapter(null);
            mRecyclerView = null;
        }

        if (mWrappedAdapter != null) {
            WrapperAdapterUtils.releaseAll(mWrappedAdapter);
            mWrappedAdapter = null;
        }
        mLayoutManager = null;

        super.onDestroyView();
    }

    @Override
    public void onGroupCollapse(int groupPosition, boolean fromUser, Object payload) {

    }

    @Override
    public void onGroupExpand(int groupPosition, boolean fromUser, Object payload) {
        if (fromUser) {
            adjustScrollPositionOnGroupExpanded(groupPosition);
        }
    }

    private void adjustScrollPositionOnGroupExpanded(int groupPosition) {
        int childItemHeight = getActivity().getResources().getDimensionPixelSize(R.dimen.list_item_height);
        int topMargin = (int) (getActivity().getResources().getDisplayMetrics().density * 16);
        int bottomMargin = topMargin;
        mRecyclerViewExpandableItemManager.scrollToGroup(groupPosition, childItemHeight, topMargin, bottomMargin);
    }

    /**
     * Adapter <-> Fragment actions.
     */

    @Override
    public void saveUserChangesOfGroup(String comment, int amount, String unit, int groupPosition) {
        mDataManager.getOrderProvider().getArticleItem(groupPosition).setUserComment(comment);
        mDataManager.getOrderProvider().getArticleItem(groupPosition).setUserAmount(amount);
        mDataManager.getOrderProvider().getArticleItem(groupPosition).setUserUnit(unit);

        mRecyclerViewExpandableItemManager.notifyGroupItemChanged(groupPosition);
        mRecyclerViewExpandableItemManager.notifyChildrenOfGroupItemChanged(groupPosition);
    }
}
