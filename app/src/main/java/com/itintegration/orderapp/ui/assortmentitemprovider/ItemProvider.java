package com.itintegration.orderapp.ui.assortmentitemprovider;

import com.itintegration.orderapp.data.test.GenerateArticleData;
import com.itintegration.orderapp.data.test.ArticleSwe;

import java.util.LinkedList;
import java.util.List;

public class ItemProvider extends AbstractItemProvider {

    private List<ItemData> mData;

    public ItemProvider() {
        mData = new LinkedList<>();
        List<ArticleSwe> artList = new GenerateArticleData().generateArticleList();

        for(int i  = 0; i < artList.size(); i++) {
            final String descrText = artList.get(i).getDescription();
            final String barCode = Long.toString(artList.get(i).getBarcode());
            final double price = artList.get(i).getPrice();
            final double total = artList.get(i).getTotal();

            final com.itintegration.orderapp.data.model.ItemData data =
                    new com.itintegration.orderapp.data.model.ItemData(i, descrText, barCode,
                            price, total, "", 0, "st");
            mData.add(data);
        }
    }

    @Override
    public int getGroupCount() {
        return mData.size();
    }

    @Override
    public ItemData getItem(int groupPosition) {
        if (groupPosition < 0 || groupPosition >= getGroupCount()) {
            throw new IndexOutOfBoundsException("groupPosition = " + groupPosition);
        }
        return mData.get(groupPosition);
    }

    @Override
    public List<String> getUnitData() {
        return new GenerateArticleData().generateUnitList();
    }
}