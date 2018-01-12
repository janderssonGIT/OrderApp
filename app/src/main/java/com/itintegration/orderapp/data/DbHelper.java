package com.itintegration.orderapp.data;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.itintegration.orderapp.data.model.ArticleSwe;
import com.itintegration.orderapp.data.model.User;
import com.itintegration.orderapp.data.provider.AbstractArticleProvider;
import com.itintegration.orderapp.data.provider.SearchArticleProvider;
import com.itintegration.orderapp.data.dbconnection.MsSQLConnection;
import com.itintegration.orderapp.di.ApplicationContext;
import com.itintegration.orderapp.di.DatabaseInfo;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class DbHelper extends SQLiteOpenHelper {

    /**
     * Sqlite methods are for local app storage only and does not access outside sources.
     * The sqlite methods are currently not in use and is derived from example projects.
     */

    //USER TABLE
    public static final String USER_TABLE_NAME = "users";
    public static final String USER_COLUMN_USER_ID = "id";
    public static final String USER_COLUMN_USER_NAME = "usr_name";
    public static final String USER_COLUMN_USER_EMAIL = "usr_email";
    public static final String USER_COLUMN_USER_CREATED_AT = "created_at";
    public static final String USER_COLUMN_USER_UPDATED_AT = "updated_at";

    private MsSQLConnection connection;
    private int searchIdScrambler = 100;

    @Inject
    public DbHelper(@ApplicationContext Context context,
                    @DatabaseInfo String dbName,
                    @DatabaseInfo Integer version,
                    MsSQLConnection connection) {
        super(context, dbName, null, version);
        this.connection = connection;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        tableCreateStatements(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
        onCreate(db);
    }

    private void tableCreateStatements(SQLiteDatabase db) {
        try {
            db.execSQL(
                    "CREATE TABLE IF NOT EXISTS "
                            + USER_TABLE_NAME + "("
                            + USER_COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + USER_COLUMN_USER_NAME + " VARCHAR(20), "
                            + USER_COLUMN_USER_EMAIL + " VARCHAR(50), "
                            + USER_COLUMN_USER_CREATED_AT + " VARCHAR(10) DEFAULT " + getCurrentTimeStamp() + ", "
                            + USER_COLUMN_USER_UPDATED_AT + " VARCHAR(10) DEFAULT " + getCurrentTimeStamp() + ")"
            );

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected User getUser(Long userId) throws Resources.NotFoundException, NullPointerException {
        Cursor cursor = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            cursor = db.rawQuery(
                    "SELECT * FROM "
                            + USER_TABLE_NAME
                            + " WHERE "
                            + USER_COLUMN_USER_ID
                            + " = ? ",
                    new String[]{userId + ""});

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                User user = new User();
                user.setId(cursor.getLong(cursor.getColumnIndex(USER_COLUMN_USER_ID)));
                user.setName(cursor.getString(cursor.getColumnIndex(USER_COLUMN_USER_NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(USER_COLUMN_USER_EMAIL)));
                user.setCreatedAt(cursor.getString(cursor.getColumnIndex(USER_COLUMN_USER_CREATED_AT)));
                user.setUpdatedAt(cursor.getString(cursor.getColumnIndex(USER_COLUMN_USER_UPDATED_AT)));
                return user;
            } else {
                throw new Resources.NotFoundException("User with id " + userId + " does not exists");
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    protected Long insertUser(User user) throws Exception {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(USER_COLUMN_USER_NAME, user.getName());
            contentValues.put(USER_COLUMN_USER_EMAIL, user.getEmail());
            return db.insert(USER_TABLE_NAME, null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private String getCurrentTimeStamp() {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }


    public SearchArticleProvider getArticlesBySearchString(String term) {

        try {
            String query = "select * FROM Flerlagerpluis.dbo.ArtiklarSwe";
            query += " WHERE Benämning LIKE ";
            query += "'" + term + "%'";

            Statement stmt = connection.CONN().createStatement();
            ResultSet rs = stmt.executeQuery(query);

            return convertResultSetToList(rs);

        } catch (java.sql.SQLException e) {
            Log.d("RS-Convert", e.getMessage());
        }
        return null;
    }

    @SuppressLint("UseSparseArrays")
    public SearchArticleProvider convertResultSetToList(ResultSet rs) throws java.sql.SQLException {
        List<AbstractArticleProvider.ArticleSwe> list = new ArrayList<>();
        int i = 0;
        while (rs.next()) {
            ArticleSwe articleSwe = new ArticleSwe();
            articleSwe.setId(rs.getString("ID"));
            articleSwe.setItemId(i + searchIdScrambler);
            articleSwe.setDescription(rs.getString("Benämning"));
            articleSwe.setTotal(rs.getDouble("Total"));
            articleSwe.setDisponible(rs.getDouble("Disponible"));
            articleSwe.setUnit(rs.getString("Enhet"));
            articleSwe.setBarcode(rs.getString("Streckkod"));
            articleSwe.setOutgoing(rs.getByte("Utgående"));
            articleSwe.setInactive(rs.getByte("Inaktiv"));
            articleSwe.setPrice(rs.getDouble("Pris"));
            articleSwe.setCodingCode(rs.getLong("Konteringskod"));
            articleSwe.setStorageLocation(rs.getString("LagerPlats"));
            articleSwe.setArticleMigration(rs.getByte("ArtikelFlytt"));
            articleSwe.setSCodeUpdate(rs.getByte("ScodeUpdate"));
            articleSwe.setStorageMerchandise(rs.getByte("Lagervara"));
            articleSwe.setArtGroup(rs.getString("ArtGroup"));
            articleSwe.setAlternativeDescription(rs.getString("AnnanBenäm"));
            articleSwe.setPackageArticle(rs.getByte("PaketArt"));
            list.add(articleSwe);
            i++;
        }
        SearchArticleProvider provider = new SearchArticleProvider(list);
        searchIdScrambler += 100;
        return provider;
    }

}
