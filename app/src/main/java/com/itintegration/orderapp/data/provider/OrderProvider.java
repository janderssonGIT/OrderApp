package com.itintegration.orderapp.data.provider;

import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class OrderProvider extends AbstractArticleProvider {
    private List<ArticleSwe> orderList;

    public OrderProvider() {
        orderList = new ArrayList<>();
    }

    @Override
    public int getArticleListGroupCount() {
        if (orderList == null) {
            return 0;
        }
        return orderList.size();
    }

    @Override
    public ArticleSwe getArticleItem(int groupPosition) {
        if (groupPosition < 0 || groupPosition >= getArticleListGroupCount()) {
            throw new IndexOutOfBoundsException("groupPosition = " + groupPosition);
        }
        return orderList.get(groupPosition);
    }

    @Override
    //TODO : Get actual unitdata via a database request/API that is called and stored through DataManager
    public List<String> getUnitData() {
        List<String> list = new ArrayList<>();
        list.add("kg");
        list.add("10");
        list.add("st");
        return list;
    }

    @Override
    public boolean isEmpty() {
        return getArticleListGroupCount() == 0;
    }

    public void addToOrderList(ArticleSwe article) {
        article.setItemId(orderList.size());
        orderList.add(article);
    }

    public void removeFromOrderList(String id) {
        Iterator iterator = orderList.iterator();
        while(iterator.hasNext()) {
            ArticleSwe art = (ArticleSwe) iterator.next();
            if (art.getId().equals(id)) {
                iterator.remove();
            }
        }
    }
}