package com.itintegration.orderapp.ui.assortment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.itintegration.orderapp.R;
import com.itintegration.orderapp.data.model.Hero;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AssortmentActivity extends AppCompatActivity {

    //TEST CODE
    final String URL_GET_DATA = "https://simplifiedcoding.net/demos/marvel/";
    RecyclerView recyclerView;
    HeroAdapter adapter;
    List<Hero> heroList;
    //END TEST CODE

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assortment);

        setupToolbar();

        //TEST CODE
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        heroList = new ArrayList<>();

        loadHeroes();
        //END TEST CODE
    }

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
    private void loadHeroes() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_GET_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);

                                Hero hero = new Hero(
                                        obj.getString("name"),
                                        obj.getString("realname"),
                                        obj.getString("team"),
                                        obj.getString("firstappearance"),
                                        obj.getString("createdby"),
                                        obj.getString("publisher"),
                                        obj.getString("imageurl"),
                                        obj.getString("bio")
                                );
                                heroList.add(hero);
                            }

                            adapter = new HeroAdapter(heroList, getApplicationContext());
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    //END TEST CODE
}
