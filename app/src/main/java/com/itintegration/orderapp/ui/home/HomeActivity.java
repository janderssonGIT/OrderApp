package com.itintegration.orderapp.ui.home;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.support.v7.widget.AppCompatImageButton;
import android.widget.Toast;

import com.itintegration.orderapp.R;
import com.itintegration.orderapp.ui.assortment.AssortmentActivity;
import com.itintegration.orderapp.ui.assortment.test.NewAssortmentActivity;
import com.itintegration.orderapp.ui.settings.SettingsActivity;
import com.itintegration.orderapp.ui.signin.SignInActivity;


/**
 // TODO : Dölj "Logout" när användare ej är inloggad.
 */


public class HomeActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mContext = getApplicationContext();

        setupToolbar();
        setupButtons();
    }

    private void setupButtons() {
        Button orderButton = findViewById(R.id.order_btn);
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, NewAssortmentActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });
    }

    private void setupToolbar() {
        Toolbar bar = findViewById(R.id.toolbar_home);
        setSupportActionBar(bar);
        getSupportActionBar().setTitle(R.string.home_title);

        AppCompatImageButton settings_btn = findViewById(R.id.settingsButton);
        settings_btn.setVisibility(View.VISIBLE);
        settings_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        popup.setOnMenuItemClickListener(this);
        inflater.inflate(R.menu.toolbar_menu, popup.getMenu());
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.popupSettings:
                intent = new Intent(mContext, SettingsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                return true;
            case R.id.popupLogout:
                intent = new Intent(mContext, SignInActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
