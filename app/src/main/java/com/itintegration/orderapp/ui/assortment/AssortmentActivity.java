package com.itintegration.orderapp.ui.assortment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.itintegration.orderapp.R;
import com.itintegration.orderapp.data.model.Article;
import com.itintegration.orderapp.data.model.Hero;

import java.util.ArrayList;
import java.util.List;

public class AssortmentActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    AssortmentAdapter assortmentAdapter;
    List<Article> articleList;

    //TEST CODE
    final String URL_GET_DATA = "https://simplifiedcoding.net/demos/marvel/";
    HeroAdapter adapter;
    List<Hero> heroList;
    //END TEST CODE


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assortment);

        setupToolbar();
        setupDataArticles();

    }

    private void setupDataArticles() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        articleList = new ArrayList<>();
        loadArticles();
    }

    private void loadArticles() {
        Article article1 = new Article("Roquefort", 1000400021, "Roquefort är en blåmögelost, " +
                "uppkallad efter staden Roquefort i Frankrike. Roquefort görs av fårmjölk och lagras i grottor med väggar belagda med mögelsvampen " +
                "Penicillium roqueforti.", 20);

        Article article2 = new Article("Parmesan", 1000400022, "Parmigiano-Reggiano är en klassisk italiensk hårdost " +
                "gjord på komjölk, som fått sitt namn på grund av att osten först började tillverkas i trakten mellan städerna Parma och Reggio nell'Emilia i " +
                "Emilia-Romagna i norra Italien. Den är kanske den mest berömda av många italienska hårdostar.", 120);

        Article article3 = new Article("Gorgonzola", 1000400023, "Gorgonzola är en italiensk grönmögelost uppkallad efter en " +
                "by utanför Milano med samma namn. Osten lagras vanligen i 2-4 månader, dock högst ett halvår.[1] Osten görs av komjölk. Tillverkning i byn Gorgonzola " +
                "är känd sedan medeltiden.", 1);

        Article article4 = new Article("Stilton", 1000400024, "Stilton är en engelsk blåmögelost gjord på komjölk och uppkallad " +
                "efter orten Stilton och får endast tillverkas i grevskapen Derbyshire, Leicestershire och Nottinghamshire för att kunna kallas stilton." +
                 " Osten är cylinderformad med en tjock skorpa och säljs inte förrän den lagrats i minst tre månader", 30);

        articleList.add(article1);
        articleList.add(article2);
        articleList.add(article3);
        articleList.add(article4);

        assortmentAdapter = new AssortmentAdapter(articleList, getApplicationContext());
        recyclerView.setAdapter(assortmentAdapter);
    }

//    private void setupDataHero() {
//        recyclerView = findViewById(R.id.recyclerView);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        heroList = new ArrayList<>();
//        loadHeroes();
//    }

    private void setupToolbar() {
        Toolbar bar = findViewById(R.id.toolbar_home);
        setSupportActionBar(bar);
        getSupportActionBar().setTitle(R.string.assortment_title);

        ImageButton btn = bar.findViewById(R.id.settingsButton);
        btn.setVisibility(View.GONE);
        btn.setActivated(false);

        SearchView sv = findViewById(R.id.toolbar_searchView);
        sv.setVisibility(View.VISIBLE);
        sv.setMaxWidth(Integer.MAX_VALUE);
    }

    //TEST CODE
//    private void loadHeroes() {
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_GET_DATA,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONArray jsonArray = new JSONArray(response);
//
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject obj = jsonArray.getJSONObject(i);
//
//                                Hero hero = new Hero(
//                                        obj.getString("name"),
//                                        obj.getString("realname"),
//                                        obj.getString("team"),
//                                        obj.getString("firstappearance"),
//                                        obj.getString("createdby"),
//                                        obj.getString("publisher"),
//                                        obj.getString("imageurl"),
//                                        obj.getString("bio")
//                                );
//                                heroList.add(hero);
//                            }
//
//                            adapter = new HeroAdapter(heroList, getApplicationContext());
//                            recyclerView.setAdapter(adapter);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                    }
//                });
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(stringRequest);
//    }
    //END TEST CODE
}
