package com.itintegration.orderapp.data;


import com.itintegration.orderapp.data.model.ArticleSwe;

import java.util.List;

interface DataManager {
    List<ArticleSwe> generateArticleList();
    List<String> generateUnitList();
}
