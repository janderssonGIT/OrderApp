package com.itintegration.orderapp.ui.settings;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.itintegration.orderapp.data.test.MsSQLConnection;
import com.itintegration.orderapp.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class SettingsActivity extends AppCompatActivity {

    private Context mContext;
    private MsSQLConnection connectionClass;
    private EditText edtuserid, edtpass;
    private Button btnlogin;
    private ProgressBar pbbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mContext = getApplicationContext();

        enableToolbar();
        setupConnectionUI();
    }

    private void setupConnectionUI() {
        connectionClass = new MsSQLConnection(mContext);
        edtuserid = findViewById(R.id.edtuserid);
        edtpass = findViewById(R.id.edtpass);
        btnlogin = findViewById(R.id.btnlogin);
        pbbar = findViewById(R.id.pbbar);
        pbbar.setVisibility(View.GONE);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoLogin doLogin = new DoLogin();
                doLogin.execute("");
            }
        });

    }

    private void enableToolbar() {
        Toolbar bar = findViewById(R.id.toolbar_admin);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class DoLogin extends AsyncTask<String,String,String> {
        String z = "";
        Boolean isSuccess = false;
        String userid = edtuserid.getText().toString();
        String password = edtpass.getText().toString();

        @Override
        protected void onPreExecute () {
        pbbar.setVisibility(View.VISIBLE);
    }

        @Override
        protected void onPostExecute (String r){
        pbbar.setVisibility(View.GONE);
            CharSequence fail = "Failed to connect!";
            CharSequence succ = z;
            if (isSuccess) {
                Toast.makeText(mContext, succ, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(mContext, fail, Toast.LENGTH_LONG).show();
            }
    }

        @Override
        protected String doInBackground (String...params){
        if (userid.trim().equals("") || password.trim().equals(""))
            z = "Please enter User Id and Password";
        else {
            try {
                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {
                    String query = "select * from Users"; // where Name='" + userid + "' and Password='" + password + "'
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    if (rs.next()) {
                        z = "Login successful";
                        isSuccess = true;
                    } else {
                        z = "Invalid Credentials";
                        isSuccess = false;
                    }
                }
            } catch (Exception ex) {
                isSuccess = false;
                z = "Exceptions";
            }
        }
        return z;
    }

    }
}
