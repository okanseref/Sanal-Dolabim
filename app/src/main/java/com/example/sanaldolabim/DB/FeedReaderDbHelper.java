package com.example.sanaldolabim.DB;

import android.app.DownloadManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.example.sanaldolabim.Cekmece;
import com.example.sanaldolabim.Kiyafet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FeedReaderDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";

    private static final String SQL_CREATE_CEKMECE =
            "CREATE TABLE IF NOT EXISTS " + FeedReaderContract.CekmeceDB.TABLE_NAME + " (" +
                    FeedReaderContract.CekmeceDB._ID + " INTEGER PRIMARY KEY," +
                    FeedReaderContract.CekmeceDB.CEKMECEADI + " TEXT)";

    private static final String SQL_CREATE_KIYAFET =
            "CREATE TABLE IF NOT EXISTS " + FeedReaderContract.KiyafetDB.TABLE_NAME + " (" +
                    FeedReaderContract.KiyafetDB._ID + " INTEGER PRIMARY KEY," +
                    FeedReaderContract.KiyafetDB.TABLE_NAME+ " TEXT," +
                    FeedReaderContract.KiyafetDB.turu + " TEXT," +
                    FeedReaderContract.KiyafetDB.desen + " TEXT," +
                    FeedReaderContract.KiyafetDB.fiyat + " TEXT," +
                    FeedReaderContract.KiyafetDB.foto + " BLOB," +
                    FeedReaderContract.KiyafetDB.tarih + " TEXT," +
                    FeedReaderContract.KiyafetDB.cekmeceAdi + " TEXT," +
                    FeedReaderContract.KiyafetDB.renk + " TEXT)";
    private static final String SQL_CREATE_KOMBIN =
            "CREATE TABLE IF NOT EXISTS " + FeedReaderContract.KombinDB.TABLE_NAME + " (" +
                    FeedReaderContract.KombinDB._ID + " INTEGER PRIMARY KEY," +
                    FeedReaderContract.KombinDB.TABLE_NAME+ " TEXT," +
                    FeedReaderContract.KombinDB.basustu + " INTEGER," +
                    FeedReaderContract.KombinDB.surat + " INTEGER," +
                    FeedReaderContract.KombinDB.ustbeden + " INTEGER," +
                    FeedReaderContract.KombinDB.altbeden + " INTEGER," +
                    FeedReaderContract.KombinDB.ayakkabi + " INTEGER)";
    private static final String SQL_CREATE_ETKINLIK =
            "CREATE TABLE IF NOT EXISTS " + FeedReaderContract.EtkinlikDB.TABLE_NAME + " (" +
                    FeedReaderContract.EtkinlikDB._ID + " INTEGER PRIMARY KEY," +
                    FeedReaderContract.EtkinlikDB.TABLE_NAME+ " TEXT," +
                    FeedReaderContract.EtkinlikDB.isim + " TEXT," +
                    FeedReaderContract.EtkinlikDB.kombin + " TEXT," +
                    FeedReaderContract.EtkinlikDB.lokasyon + " TEXT," +
                    FeedReaderContract.EtkinlikDB.tarihi + " TEXT," +
                    FeedReaderContract.EtkinlikDB.turu + " TEXT)";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + FeedReaderContract.CekmeceDB._ID;
    public FeedReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {

    }
    public void createKiyafetler(SQLiteDatabase db){
        System.out.println("Db created"+FeedReaderContract.KiyafetDB.TABLE_NAME);
        db.execSQL(SQL_CREATE_KIYAFET);
    }
    public void createKombinler(SQLiteDatabase db){
        db.execSQL(SQL_CREATE_KOMBIN);
    }
    public void createEtkinlikler(SQLiteDatabase db){
        db.execSQL(SQL_CREATE_ETKINLIK);
    }
    public void createCekmeceler(SQLiteDatabase db){
        db.execSQL(SQL_CREATE_CEKMECE);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
    public  void DeleteKiyafet(SQLiteDatabase db,String id){
        db.execSQL("DELETE FROM kiyafetler WHERE _ID='"+id+"'");
        db.execSQL("DELETE FROM kombinler WHERE basustu='"+id+"'");
        db.execSQL("DELETE FROM kombinler WHERE surat='"+id+"'");
        db.execSQL("DELETE FROM kombinler WHERE ustbeden='"+id+"'");
        db.execSQL("DELETE FROM kombinler WHERE altbeden='"+id+"'");
        db.execSQL("DELETE FROM kombinler WHERE ayakkabi='"+id+"'");

    }
    public  void DeleteCekmece(SQLiteDatabase db,String id){
        db.execSQL("DELETE FROM cekmeceler WHERE _ID='"+id+"'");

        DeleteKiyafet(db,id);
        ArrayList<String> deleteList = new ArrayList<>();
        String[] projection = {
                BaseColumns._ID,
                FeedReaderContract.KiyafetDB.cekmeceAdi,
                FeedReaderContract.KiyafetDB._ID
        };
        String selection =FeedReaderContract.KiyafetDB.cekmeceAdi + " = ?";
        String[] selectionArgs = { id };
        String sortOrder =
                FeedReaderContract.KiyafetDB._ID + " DESC";

        Cursor cursor = db.query(
                FeedReaderContract.KiyafetDB.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        while(cursor.moveToNext()) {
            deleteList.add(cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.KiyafetDB._ID)));
        }
        for(int i=0;i<deleteList.size();i++){
            DeleteKiyafet(db,deleteList.get(i));
        }
    }
    public  void DeleteKombin(SQLiteDatabase db,String id){
        db.execSQL("DELETE FROM kombinler WHERE _ID='"+id+"'");
        db.execSQL("DELETE FROM etkinlikler WHERE kombin='"+id+"'");
    }
    public  void DeleteEtkinlik(SQLiteDatabase db,String id){
        db.execSQL("DELETE FROM etkinlikler WHERE _ID='"+id+"'");
    }


}
