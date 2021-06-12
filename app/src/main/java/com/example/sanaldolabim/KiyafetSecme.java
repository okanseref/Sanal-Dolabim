package com.example.sanaldolabim;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;

import com.example.sanaldolabim.Adapter.CekmeceAdapter;
import com.example.sanaldolabim.Adapter.KiyafetAdapter;
import com.example.sanaldolabim.DB.FeedReaderContract;
import com.example.sanaldolabim.DB.FeedReaderDbHelper;

import java.util.ArrayList;
import java.util.List;

public class KiyafetSecme extends AppCompatActivity {
    RecyclerView kiyafetSecme;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kiyafet_secme);

        kiyafetSecme=findViewById(R.id.kiyafetSecmeRec);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        kiyafetSecme.setLayoutManager(layoutManager);
        RecyclerView.Adapter adapter = new KiyafetAdapter(GetKiyafetler());
        kiyafetSecme.setAdapter(adapter);

    }

    public void ReturnKiyafet(String ID){
        KabinOdasi.SelectedID=ID;
        finish();
    }
    private List<Kiyafet> GetKiyafetler(){
        List<Kiyafet> questions = new ArrayList<>();

        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        dbHelper.createCekmeceler(db);
        String[] projection = {
                BaseColumns._ID,
                FeedReaderContract.KiyafetDB.desen,
                FeedReaderContract.KiyafetDB.fiyat,
                FeedReaderContract.KiyafetDB.foto,
                FeedReaderContract.KiyafetDB.renk,
                FeedReaderContract.KiyafetDB.tarih,
                FeedReaderContract.KiyafetDB.turu,
                FeedReaderContract.KiyafetDB.cekmeceAdi,
                FeedReaderContract.KiyafetDB._ID
        };

        // Filter results WHERE "title" = 'My Title'

        String selection = "";
        String[] selectionArgs = {};

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
            Kiyafet q = new Kiyafet(cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.KiyafetDB.turu)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.KiyafetDB.renk)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.KiyafetDB.desen)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.KiyafetDB.tarih)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.KiyafetDB.fiyat)),
                    cursor.getBlob(cursor.getColumnIndexOrThrow(FeedReaderContract.KiyafetDB.foto)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.KiyafetDB.cekmeceAdi)));

            q.setID(cursor.getInt(cursor.getColumnIndexOrThrow(FeedReaderContract.KiyafetDB._ID)));
            questions.add(q);
        }
        cursor.close();
        db.close();
        dbHelper.close();

        return questions;
    }
}