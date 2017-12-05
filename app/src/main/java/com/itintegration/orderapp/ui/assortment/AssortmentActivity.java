package com.itintegration.orderapp.ui.assortment;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.itintegration.orderapp.R;
import com.itintegration.orderapp.data.model.ArticleSwe;

import java.util.ArrayList;
import java.util.List;

public class AssortmentActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    AssortmentAdapter assortmentAdapter;
    List<ArticleSwe> articleSweList;
    private Menu mOptionsMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assortment);

        setupToolbar();
        setupDataArticles();
    }

    private void setupToolbar() {
        Toolbar bar = findViewById(R.id.toolbar_home);
        setSupportActionBar(bar);
        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.assortment_title);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mOptionsMenu = menu;
        getMenuInflater().inflate(R.menu.toolbar_assortment, mOptionsMenu);
        SearchView sv = (SearchView) menu.findItem(R.id.action_search).getActionView();
        sv.setMaxWidth(Integer.MAX_VALUE);
        return true;
    }

    /**
     * Vid knapptryck på "retur" knappen, kolla om sökfält innehåller sökfras.
     * Vid 'TRUE'; rensa sökfält, om FALSE; utför RETUR till föregående vy.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                SearchView sv = (SearchView) mOptionsMenu.findItem(R.id.action_search).getActionView();
                if (sv.getQuery().length() > 0) {
                    sv.setQuery("", true);
                    return false;
                } else {
                    onBackPressed();
                    overridePendingTransition(R.anim.left_in, R.anim.right_out);
                    return true;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDataArticles() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        articleSweList = new ArrayList<>();
        //loadArticles();
    }

    //TEST DATA - SKA TAS BORT.
//    private void loadArticles() {
//        ArticleSwe articleSwe1 = new ArticleSwe("Roquefort", 1000400021, "Roquefort är en blåmögelost, " +
//                "uppkallad efter staden Roquefort i Frankrike. Roquefort görs av fårmjölk och lagras i grottor med väggar belagda med mögelsvampen " +
//                "Penicillium roqueforti.", 20);
//
//        ArticleSwe articleSwe2 = new ArticleSwe("Parmesan", 1000400022, "Parmigiano-Reggiano är en klassisk italiensk hårdost " +
//                "gjord på komjölk, som fått sitt namn på grund av att osten först började tillverkas i trakten mellan städerna Parma och Reggio nell'Emilia i " +
//                "Emilia-Romagna i norra Italien. Den är kanske den mest berömda av många italienska hårdostar.", 120);
//
//        ArticleSwe articleSwe3 = new ArticleSwe("Gorgonzola", 1000400023, "Gorgonzola är en italiensk grönmögelost uppkallad efter en " +
//                "by utanför Milano med samma namn. Osten lagras vanligen i 2-4 månader, dock högst ett halvår.[1] Osten görs av komjölk. Tillverkning i byn Gorgonzola " +
//                "är känd sedan medeltiden.", 1);
//
//        ArticleSwe articleSwe4 = new ArticleSwe("Stilton", 1000400024, "Stilton är en engelsk blåmögelost gjord på komjölk och uppkallad " +
//                "efter orten Stilton och får endast tillverkas i grevskapen Derbyshire, Leicestershire och Nottinghamshire för att kunna kallas stilton." +
//                 " Osten är cylinderformad med en tjock skorpa och säljs inte förrän den lagrats i minst tre månader", 30);
//
//        ArticleSwe articleSwe5 = new ArticleSwe("Roquefort", 1000400021, "Roquefort är en blåmögelost, " +
//                "uppkallad efter staden Roquefort i Frankrike. Roquefort görs av fårmjölk och lagras i grottor med väggar belagda med mögelsvampen " +
//                "Penicillium roqueforti.", 20);
//
//        ArticleSwe articleSwe6 = new ArticleSwe("Parmesan", 1000400022, "Parmigiano-Reggiano är en klassisk italiensk hårdost " +
//                "gjord på komjölk, som fått sitt namn på grund av att osten först började tillverkas i trakten mellan städerna Parma och Reggio nell'Emilia i " +
//                "Emilia-Romagna i norra Italien. Den är kanske den mest berömda av många italienska hårdostar.", 120);
//
//        ArticleSwe articleSwe7 = new ArticleSwe("Gorgonzola", 1000400023, "Gorgonzola är en italiensk grönmögelost uppkallad efter en " +
//                "by utanför Milano med samma namn. Osten lagras vanligen i 2-4 månader, dock högst ett halvår.[1] Osten görs av komjölk. Tillverkning i byn Gorgonzola " +
//                "är känd sedan medeltiden.", 1);
//
//        ArticleSwe articleSwe8 = new ArticleSwe("Stilton", 1000400024, "Stilton är en engelsk blåmögelost gjord på komjölk och uppkallad " +
//                "efter orten Stilton och får endast tillverkas i grevskapen Derbyshire, Leicestershire och Nottinghamshire för att kunna kallas stilton." +
//                " Osten är cylinderformad med en tjock skorpa och säljs inte förrän den lagrats i minst tre månader", 30);
//
//        ArticleSwe articleSwe9 = new ArticleSwe("Roquefort", 1000400021, "Roquefort är en blåmögelost, " +
//                "uppkallad efter staden Roquefort i Frankrike. Roquefort görs av fårmjölk och lagras i grottor med väggar belagda med mögelsvampen " +
//                "Penicillium roqueforti.", 20);
//
//        ArticleSwe articleSwe10 = new ArticleSwe("Parmesan", 1000400022, "Parmigiano-Reggiano är en klassisk italiensk hårdost " +
//                "gjord på komjölk, som fått sitt namn på grund av att osten först började tillverkas i trakten mellan städerna Parma och Reggio nell'Emilia i " +
//                "Emilia-Romagna i norra Italien. Den är kanske den mest berömda av många italienska hårdostar.", 120);
//
//        ArticleSwe articleSwe11 = new ArticleSwe("Gorgonzola", 1000400023, "Gorgonzola är en italiensk grönmögelost uppkallad efter en " +
//                "by utanför Milano med samma namn. Osten lagras vanligen i 2-4 månader, dock högst ett halvår.[1] Osten görs av komjölk. Tillverkning i byn Gorgonzola " +
//                "är känd sedan medeltiden.", 1);
//
//        ArticleSwe articleSwe12 = new ArticleSwe("Stilton", 1000400024, "Stilton är en engelsk blåmögelost gjord på komjölk och uppkallad " +
//                "efter orten Stilton och får endast tillverkas i grevskapen Derbyshire, Leicestershire och Nottinghamshire för att kunna kallas stilton." +
//                " Osten är cylinderformad med en tjock skorpa och säljs inte förrän den lagrats i minst tre månader", 30);
//
//        articleSweList.add(articleSwe1);
//        articleSweList.add(articleSwe2);
//        articleSweList.add(articleSwe3);
//        articleSweList.add(articleSwe4);
//        articleSweList.add(articleSwe5);
//        articleSweList.add(articleSwe6);
//        articleSweList.add(articleSwe7);
//        articleSweList.add(articleSwe8);
//        articleSweList.add(articleSwe9);
//        articleSweList.add(articleSwe10);
//        articleSweList.add(articleSwe11);
//        articleSweList.add(articleSwe12);
//
//        assortmentAdapter = new AssortmentAdapter(articleSweList, getApplicationContext());
//        recyclerView.setAdapter(assortmentAdapter);
//    }
}
