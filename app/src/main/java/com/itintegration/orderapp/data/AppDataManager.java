package com.itintegration.orderapp.data;


import com.itintegration.orderapp.data.model.ArticleSwe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class AppDataManager implements DataManager {

    public AppDataManager() {
    }

    //TESTLIST
    @Override
    public List<ArticleSwe> generateArticleList() {
        List<ArticleSwe> articleSweList = new ArrayList<>();

        for(int i = 0; i < 20; i++) {
            int id = 300 + (i*2);
            String description = "Neat description" + i;
            double total = 10.0000 + i%2.0000;
            double disponible = 10.0000 + i%2.0000;
            String unit = "" + (10 + i) + "";
            long barcode = 7330000000000L + (i*(1000000 + i*150));
            byte outgoing = 0;
            byte inactive = (byte) ThreadLocalRandom.current().nextInt(0, 1);
            Random r = new Random();
            double price = 575.00 + (14200.00 - 575.00) * r.nextDouble();
            int codingCode = 0;
            int storageLocation = 0;
            byte articleMigration = 0;
            byte sCodeUpdate = 0;
            byte storageMerchandise = 1;
            String artGroup = "Text";
            String alternativeDescription = "";
            byte packageArticle = 0;

            ArticleSwe article = new ArticleSwe(id, description, total, disponible,
                    unit, barcode, outgoing, inactive, price, codingCode, storageLocation,
                    articleMigration, sCodeUpdate, storageMerchandise, artGroup, alternativeDescription,
                    packageArticle);
            articleSweList.add(article);
        }
        return articleSweList;
    }
}
