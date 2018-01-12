package com.itintegration.orderapp.data.provider;

import java.util.ArrayList;
import java.util.List;

public class SearchArticleProvider extends AbstractArticleProvider {
    private List<ArticleSwe> articleList;

    public SearchArticleProvider() {
    }

    public SearchArticleProvider(List<ArticleSwe> list) {
        articleList = list;
    }

    @Override
    public int getArticleListGroupCount() {
        if (articleList == null) {
            return 0;
        }
        return articleList.size();
    }

    @Override
    public ArticleSwe getArticleItem(int groupPosition) {
        if (groupPosition < 0 || groupPosition >= getArticleListGroupCount()) {
            throw new IndexOutOfBoundsException("groupPosition = " + groupPosition);
        }
        return articleList.get(groupPosition);
    }

    @Override
    //TODO : Get actual unitdata via a database request/API.
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

}
