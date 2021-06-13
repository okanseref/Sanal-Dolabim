package com.example.sanaldolabim;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;

import com.example.sanaldolabim.Adapter.KiyafetAdapter;
import com.example.sanaldolabim.Adapter.KombinAdapter;
import com.example.sanaldolabim.DB.FeedReaderContract;
import com.example.sanaldolabim.DB.FeedReaderDbHelper;

import java.util.ArrayList;
import java.util.List;

public class KombinSecme extends AppCompatActivity {
    RecyclerView kombinRec;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kombin_secme);

        kombinRec=findViewById(R.id.kombinSecmeRec);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        kombinRec.setLayoutManager(layoutManager);
        RecyclerView.Adapter adapter = new KombinAdapter(GetKombinler());
        kombinRec.setAdapter(adapter);

    }
    public void ReturnKombin(String ID){
        EtkinlikEkle.SelectedKombin=ID;
        finish();
    }
    private List<Kombin> GetKombinler(){
        List<Kombin> kombins = new ArrayList<>();

        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        dbHelper.createCekmeceler(db);
        String[] projection = {
                BaseColumns._ID,
                FeedReaderContract.KombinDB.altbeden,
                FeedReaderContract.KombinDB._ID,
                FeedReaderContract.KombinDB.basustu,
                FeedReaderContract.KombinDB.surat,
                FeedReaderContract.KombinDB.ustbeden,
                FeedReaderContract.KombinDB.ayakkabi
        };

        String selection = "";
        String[] selectionArgs = {  };

        String sortOrder =
                FeedReaderContract.KombinDB._ID + " DESC";

        Cursor cursor = db.query(
                FeedReaderContract.KombinDB.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        while(cursor.moveToNext()) {

            Kombin q = new Kombin(cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.KombinDB.basustu)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.KombinDB.surat)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.KombinDB.ustbeden)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.KombinDB.altbeden)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.KombinDB.ayakkabi))
            );

            q.setID(cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.KombinDB._ID)));
            kombins.add(q);
            System.out.println(q.getID()+" KOMBİN ID BU");

        }
        cursor.close();
        db.close();
        dbHelper.close();

        return kombins;
    }
}