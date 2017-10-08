package com.app.shoptree.shoptree.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.Settings;
import android.util.Log;

/**
 * Created by lovishbajaj on 19/09/17.
 */

public class MyDb {
    final static String DATABASE_NAME = "mycart";
    final static int DATABASE_VERSION = 1;
    final static String DATABASE_CREATE = "create table product_list(id integer primary key autoincrement,"
            + "productid text,pmid text,productname text,productimage text,productprice text,productourprice text,productquantity text);";
    SQLiteDatabase db;
    final DatabaseHelper dbHelper;
    final Context context;

    public MyDb(Context con) {
        this.context = con;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        db.close();
    }

    public long insertData(String productid, String pmid, String productname, String productimage, String productprice, Integer productquantity) {
        ContentValues values = new ContentValues();

        values.put("productid", productid);
        values.put("pmid",pmid);
        values.put("productname", productname);
        values.put("productimage", productimage);
        values.put("productprice",productprice);
        values.put("productquantity", productquantity);

        return db.insert("product_list", null, values);

    }

    public void updateQuantity(String pmid,Integer quantity){
        ContentValues cv = new ContentValues();
        cv.put("productquantity",quantity); //These Fields should be your String values of actual column names
        db.update("product_list", cv, "pmid="+pmid, null);
    }

    public Cursor getAllData() {
        return db.query(true, "product_list", new String[]{"id", "productid", "productname", "productimage",
                "productprice","productourprice","productquantity"
        }, null, null, null, null, "productname"+" ASC", null);
    }

    public void deleteAllData() {
        // SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("product_list", null, null);
        //db.close();
    }
    public int getQuantity(String pmid){
        int qty = 0;
        String sql = "SELECT * FROM product_list WHERE pmid='"+pmid+"'";
        Cursor cursor = db.rawQuery(sql,null);
            if (cursor.moveToNext()){
               qty = Integer.parseInt(cursor.getString(cursor.getColumnIndex("productquantity")));
                Log.d("not",String.valueOf(qty));
            }
         //qty = cursor.getString(cursor.);
        return qty;
    }
    public int checkAvailable(String pmid){
        Cursor cursor = null;
        String sql ="SELECT * FROM product_list WHERE pmid='"+pmid+"'";
        cursor= db.rawQuery(sql,null);


        if(cursor.getCount()>0){
            //PID Found
            cursor.close();
            return 1;
        }else{
            //PID Not Found
            cursor.close();
            return 0;
        }
    }

    public boolean deleteproduct(String pmid)
    {
        return db.delete("product_list", "pmid" + "='" + pmid+"'", null) > 0;
    }

    public long countproduct(){

        long count  = DatabaseUtils.queryNumEntries(db, "product_list");

        return count;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase database) {
            try {
                database.execSQL(DATABASE_CREATE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS mycart");

            onCreate(db);
        }
    }
}
