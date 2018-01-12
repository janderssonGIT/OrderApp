package com.itintegration.orderapp.ui.assortment;

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

import com.itintegration.orderapp.R;
import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.SimpleListDividerDecorator;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;
import com.itintegration.orderapp.data.DataManager;
import com.itintegration.orderapp.data.model.ArticleSwe;
import com.itintegration.orderapp.data.provider.SearchArticleProvider;
import com.itintegration.orderapp.di.component.ActivityComponent;
import com.itintegration.orderapp.ui.assortment.AssortmentAdapter.AssortmentAdapterCallback;

import javax.inject.Inject;

public class AssortmentFragment extends Fragment implements RecyclerViewExpandableItemManager.OnGroupCollapseListener,
        RecyclerViewExpandableItemManager.OnGroupExpandListener, AssortmentAdapterCallback {

    private static final String SAVED_STATE_EXPANDABLE_ITEM_MANAGER = "RecyclerViewExpandableItemManager";

    private RecyclerView mRecyclerView;
    private TextView mEmptyView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mWrappedAdapter;
    private RecyclerViewExpandableItemManager mRecyclerViewExpandableItemManager;
    private AssortmentAdapter assortmentAdapter;
    private AssortmentActivity mActivity;

    @Inject
    DataManager mDataManager;

    //dagger 2
    public ActivityComponent getActivityComponent() {
        if (mActivity != null) {
            return mActivity.getActivityComponent();
        }
        return null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AssortmentActivity) {
            AssortmentActivity activity = (AssortmentActivity) context;
            this.mActivity = activity;
            activity.onFragmentAttached();
        }
    }

    public AssortmentFragment() {
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
        return inflater.inflate(R.layout.assortment_fragment_recycler_listview, container, false);
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

        assortmentAdapter = new AssortmentAdapter(mRecyclerViewExpandableItemManager, this.getContext(), mDataManager, AssortmentFragment.this);
        mWrappedAdapter = mRecyclerViewExpandableItemManager.createWrappedAdapter(assortmentAdapter);
        final GeneralItemAnimator animator = new RefactoredDefaultItemAnimator();

        animator.setSupportsChangeAnimations(false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mWrappedAdapter);
        mRecyclerView.setItemAnimator(animator);
        mRecyclerView.setNestedScrollingEnabled(true);
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
        /*
            Note, I suspect this is the source for unintended bugs with the UI and may be the reason
            Viewholders get duplicated stable ID's, making the app crash. Commenting this may however result in loss
            of data whenever the user rotates the phone/emulator. Needs investigation!

        if (mRecyclerViewExpandableItemManager != null) {
            outState.putParcelable(SAVED_STATE_EXPANDABLE_ITEM_MANAGER,
                    mRecyclerViewExpandableItemManager.getSavedState());
        }
        */
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
        //TODO : Shouldn't save to SearchArticleProvider, but instead the OrderProvider, yet the article may not be added. Perhaps remove and save when adding to order instead?

        mDataManager.getSearchArticleProvider().getArticleItem(groupPosition).setUserComment(comment);
        mDataManager.getSearchArticleProvider().getArticleItem(groupPosition).setUserAmount(amount);
        mDataManager.getSearchArticleProvider().getArticleItem(groupPosition).setUserUnit(unit);

        mRecyclerViewExpandableItemManager.notifyGroupItemChanged(groupPosition);
        mRecyclerViewExpandableItemManager.notifyChildrenOfGroupItemChanged(groupPosition);
    }

    @Override
    public void addToOrder(ArticleSwe article, int groupPosition) {
        mDataManager.getOrderProvider().addToOrderList(article);
    }

    @Override
    public void removeFromOrder(int groupPosition) {
        ArticleSwe art = (ArticleSwe) mDataManager.getSearchArticleProvider().getArticleItem(groupPosition);
        String Id = art.getId();
        mDataManager.getOrderProvider().removeFromOrderList(Id);

        mRecyclerViewExpandableItemManager.notifyGroupItemChanged(groupPosition);
        mRecyclerViewExpandableItemManager.notifyChildrenOfGroupItemChanged(groupPosition);
    }

    /**
     * Activity -> Fragment actions.
     */

    public synchronized void updateProvider() {
        retrieveViewState();
        mRecyclerViewExpandableItemManager.collapseAll();
        assortmentAdapter.notifyDataSetChanged();
        mRecyclerView.refreshDrawableState();
    }
}