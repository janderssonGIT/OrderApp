package com.itintegration.orderapp.unused.assortmentprovider;

import android.support.v4.util.Pair;

import com.itintegration.orderapp.data.test.GenerateArticleData;
import com.itintegration.orderapp.data.test.ArticleSwe;

import java.util.LinkedList;
import java.util.List;

public class DataProvider extends AbstractDataProvider {

    private List<Pair<GroupData, ChildData>> mData;

    DataProvider() {

        //TODO : Fetch data from repository.
        mData = new LinkedList<>();
        List<ArticleSwe> artList = new GenerateArticleData().generateArticleList();






        for(int i  = 0; i < artList.size(); i++) {
            final String groupText = artList.get(i).getDescription();
            final String groupText2 = Long.toString(artList.get(i).getBarcode());
            final com.itintegration.orderapp.unused.GroupData group = new com.itintegration.orderapp.unused.GroupData(i,
                    groupText,
                    groupText2);
            final com.itintegration.orderapp.unused.ChildData child = new com.itintegration.orderapp.unused.ChildData(i,
                    artList.get(i).getPrice(),
                    artList.get(i).getTotal(), "", 0);
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

    @Override
    public List<String> getUnitData() {
        return new GenerateArticleData().generateUnitList();
    }
}