package com.itintegration.orderapp.data.test;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import com.itintegration.orderapp.data.DbHelper;
import com.itintegration.orderapp.data.SharedPrefsHelper;
import com.itintegration.orderapp.di.ApplicationContext;
import com.itintegration.orderapp.ui.assortmentitemprovider.ItemProvider;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Jim Andersson on 2017-11-08.
 *
 */

public class MsSQLConnection {
    private Context mContext;

    @Inject
    public MsSQLConnection(@ApplicationContext Context context) {
        mContext = context;
    }

    @SuppressWarnings("all")
    private String IP = "172.18.40.196:50085/SQLEXPRESS";
//  private String IP = "TESTSRV/SQLEXPRESS";
    private String CLASS = "net.sourceforge.jtds.jdbc.Driver";
    private String DB = "Flerlagerpluis";
    private String UN = "sa";
    private String PASSWORD = "Need4speed";

    @SuppressLint("NewApi")
    public Connection CONN() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        String ConnURL;
        try {

            Class.forName(CLASS);
            ConnURL = "jdbc:jtds:sqlserver://" + IP + ";"
                    + "databaseName=" + DB + ";user=" + UN + ";PASSWORD="
                    + PASSWORD + ";";
            conn = DriverManager.getConnection(ConnURL);
        } catch (SQLException | ClassNotFoundException se) {
            Log.e("ERROR", se.getMessage());
        }
        return conn;
    }
}