package com.example.sanaldolabim;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.View;
import android.widget.Button;

import com.example.sanaldolabim.Adapter.EtkinlikAdapter;
import com.example.sanaldolabim.Adapter.KombinAdapter;
import com.example.sanaldolabim.DB.FeedReaderContract;
import com.example.sanaldolabim.DB.FeedReaderDbHelper;

import java.util.ArrayList;
import java.util.List;

public class EtkinlikMenu extends AppCompatActivity {
    RecyclerView etkinlikRec;
    Button etkinlikEkle;
    ActivityResultLauncher<Intent> someActivityResultLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etkinlik_menu);
        FillValues();
        etkinlikEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EtkinlikEkle("hayir",null);
            }
        });
        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        RefreshList();
                    }
                });
    }
    public void EtkinlikEkle(String guncellemeMi,Etkinlik etkinlik){

        Intent intent = new Intent(getApplicationContext(), EtkinlikEkle.class);
        intent.putExtra("etkinlik", etkinlik);
        intent.putExtra("guncellemeMi", guncellemeMi);
        someActivityResultLauncher.launch(intent);
    }
    private void FillValues(){
        etkinlikRec=findViewById(R.id.etkinlinRecView);
        etkinlikEkle=findViewById(R.id.etkinlikEkle);


        RefreshList();
    }
    public void RefreshList(){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        etkinlikRec.setLayoutManager(layoutManager);
        RecyclerView.Adapter adapter = new EtkinlikAdapter(GetEtkinlikler());
        etkinlikRec.setAdapter(adapter);
    }

    private List<Etkinlik> GetEtkinlikler(){
        List<Etkinlik> etkinliks = new ArrayList<>();

        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        dbHelper.createCekmeceler(db);
        String[] projection = {
                BaseColumns._ID,
                FeedReaderContract.EtkinlikDB.turu,
                FeedReaderContract.EtkinlikDB._ID,
                FeedReaderContract.EtkinlikDB.tarihi,
                FeedReaderContract.EtkinlikDB.lokasyon,
                FeedReaderContract.EtkinlikDB.kombin,
                FeedReaderContract.EtkinlikDB.isim
        };

        // Filter results WHERE "title" = 'My Title'
        String selection = "";//FeedReaderContract.Question.OWNER + " = ?";
        String[] selectionArgs = {  };

        String sortOrder =
                FeedReaderContract.EtkinlikDB._ID + " DESC";

        Cursor cursor = db.query(
                FeedReaderContract.EtkinlikDB.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        while(cursor.moveToNext()) {

            Etkinlik q = new Etkinlik(cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.EtkinlikDB.isim)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.EtkinlikDB.turu)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.EtkinlikDB.tarihi)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.EtkinlikDB.lokasyon)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.EtkinlikDB.kombin))
            );

            q.setID(cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.EtkinlikDB._ID)));
            etkinliks.add(q);
            System.out.println(q.getID()+" KOMBİN ID BU");

        }
        cursor.close();
        db.close();
        dbHelper.close();

        return etkinliks;
    }
}