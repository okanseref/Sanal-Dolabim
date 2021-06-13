package com.example.sanaldolabim;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.sanaldolabim.Adapter.CekmeceAdapter;
import com.example.sanaldolabim.DB.FeedReaderContract;
import com.example.sanaldolabim.DB.FeedReaderDbHelper;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.Inflater;

public class Menu extends AppCompatActivity {
    Button cekmeceEkle,kabinGir,etkinlikButton;
    RecyclerView cekmeceListe;
    ActivityResultLauncher<Intent> someActivityResultLauncher;
    BroadcastReceiver br;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FillValues();
        kabinGir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Menu.this,KabinOdasi.class));
            }
        });
        cekmeceEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CekmeceEkle();
            }
        });
        etkinlikButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Menu.this,EtkinlikMenu.class));
            }
        });
        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        RefreshMenuList();
                    }

                });

        Broadcast();
    }
    private void FillValues(){
        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        dbHelper.createCekmeceler(db);
        dbHelper.createKiyafetler(db);
        dbHelper.createKombinler(db);
        dbHelper.createEtkinlikler(db);
        db.close();

        cekmeceEkle=findViewById(R.id.cekmece_ekle);
        kabinGir=findViewById(R.id.Kabin);
        etkinlikButton=findViewById(R.id.etkinlikButton);
        cekmeceListe=findViewById(R.id.cekmeceListe);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        cekmeceListe.setLayoutManager(layoutManager);
        RecyclerView.Adapter adapter = new CekmeceAdapter(GetCekmeceler());
        cekmeceListe.setAdapter(adapter);


    }
    private List<Cekmece> GetCekmeceler(){
        List<Cekmece> questions = new ArrayList<>();

        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                FeedReaderContract.CekmeceDB.CEKMECEADI,
                FeedReaderContract.CekmeceDB._ID
        };
        String selection = "";//FeedReaderContract.Question.OWNER + " = ?";
        String[] selectionArgs = {  };
        String sortOrder =
                FeedReaderContract.CekmeceDB._ID + " DESC";

        Cursor cursor = db.query(
                FeedReaderContract.CekmeceDB.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        while(cursor.moveToNext()) {

            Cekmece q = new Cekmece(cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.CekmeceDB.CEKMECEADI)));

            q.setID(cursor.getInt(cursor.getColumnIndexOrThrow(FeedReaderContract.CekmeceDB._ID)));
            questions.add(q);
            System.out.println(q.getID()+" cekmece ID BU");

        }
        cursor.close();
        db.close();
        return questions;
    }

    private void CekmeceEkle() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Menu.this);

        alertDialog.setTitle("Çekmece adını girin");
        final EditText input = new EditText(Menu.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        alertDialog.setPositiveButton("KAYDET",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        Toast.makeText(getApplicationContext(), "Cekmece eklendi", Toast.LENGTH_SHORT).show();
                        InsertCekmece(new Cekmece(input.getText().toString()));
                    }
                });
        alertDialog.setNegativeButton("İPTAL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }
    public void MesajOlustur(String text) {
        AlertDialog alertDialog = new AlertDialog.Builder(Menu.this).create();
        alertDialog.setTitle("Mesaj");
        alertDialog.setMessage(text);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "TAMAM",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
    public void RefreshMenuList(){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        cekmeceListe.setLayoutManager(layoutManager);
        RecyclerView.Adapter adapter = new CekmeceAdapter(GetCekmeceler());
        cekmeceListe.setAdapter(adapter);
    }
    private void InsertCekmece(Cekmece cekmece){
        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.CekmeceDB.CEKMECEADI, cekmece.getCekmeceAdi());

        long newRowId = db.insert(FeedReaderContract.CekmeceDB.TABLE_NAME, null, values);
        db.close();
        dbHelper.close();

        RefreshMenuList();
        MesajOlustur("Çekmece başarıyla eklendi.");
    }
    public void KiyafetEkle(String cekmeceAdi,String guncellemeMi,Kiyafet kiyafet){

        Intent intent = new Intent(getApplicationContext(), KiyafetEkle.class);
        intent.putExtra("cekmeceAdi", cekmeceAdi);
        intent.putExtra("kiyafet", kiyafet);
        intent.putExtra("guncellemeMi", guncellemeMi);
        someActivityResultLauncher.launch(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("Unregistered");

        this.unregisterReceiver(br); //Geri kapatıyoruz leak olmasın diye
    }
    private void Broadcast(){

        MyBroadcastReceiver.kiyafetList = GetKiyafetler();
        br = new MyBroadcastReceiver();

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        this.registerReceiver(br, filter);

        System.out.println("Registered");

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