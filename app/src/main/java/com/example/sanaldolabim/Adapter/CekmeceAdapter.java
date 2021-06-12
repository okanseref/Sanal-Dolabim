package com.example.sanaldolabim.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.provider.BaseColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sanaldolabim.Cekmece;
import com.example.sanaldolabim.DB.FeedReaderContract;
import com.example.sanaldolabim.DB.FeedReaderDbHelper;
import com.example.sanaldolabim.Kiyafet;
import com.example.sanaldolabim.KiyafetEkle;
import com.example.sanaldolabim.Menu;
import com.example.sanaldolabim.R;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class CekmeceAdapter extends RecyclerView.Adapter<CekmeceAdapter.ViewHolder> {

    private final List<Cekmece> localDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView cekmeceAdi;
        Button kiyafetEkle,cekmeceSil;
        RecyclerView kiyafetListe;
        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
           cekmeceSil=view.findViewById(R.id.cekmeceSil);
           kiyafetEkle=view.findViewById(R.id.kiyafetEkle);
            cekmeceAdi=view.findViewById(R.id.cekmeceAdi);
            kiyafetListe=view.findViewById(R.id.kiyafetList);
        }

        public RecyclerView getKiyafetListe() {
            return kiyafetListe;
        }

        public TextView getCekmeceAdi() {
            return cekmeceAdi;
        }

        public Button getKiyafetEkle() {
            return kiyafetEkle;
        }

        public Button getCekmeceSil() {
            return cekmeceSil;
        }
    }

    public CekmeceAdapter(List<Cekmece> dataSet) {
        localDataSet = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cekmece_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(viewHolder.getCekmeceAdi().getContext());
        viewHolder.getKiyafetListe().setLayoutManager(layoutManager);
        RecyclerView.Adapter adapter = new KiyafetAdapter(GetKiyafetler(localDataSet.get(position).getCekmeceAdi(),viewHolder.getCekmeceAdi().getContext()));
        viewHolder.getKiyafetListe().setAdapter(adapter);
        viewHolder.getKiyafetListe().setNestedScrollingEnabled(false);

        viewHolder.getCekmeceAdi().setText(localDataSet.get(position).getCekmeceAdi());
        viewHolder.getKiyafetEkle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Menu)viewHolder.getCekmeceAdi().getContext()).KiyafetEkle(localDataSet.get(position).getCekmeceAdi(),"hayir",null);
            }
        });
        viewHolder.getCekmeceSil().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(viewHolder.getCekmeceAdi().getContext());
                builder1.setMessage("Çekmeceyi silmek istediğinize emin misiniz?");
                builder1.setCancelable(true);
                builder1.setPositiveButton("TAMAM",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //put your code that needed to be executed when okay is clicked
                                FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(viewHolder.getCekmeceAdi().getContext());
                                SQLiteDatabase db = dbHelper.getReadableDatabase();
                                dbHelper.DeleteCekmece(db,String.valueOf(localDataSet.get(position).getID()));
                                ((Menu)viewHolder.getCekmeceAdi().getContext()).RefreshMenuList();
                                dialog.cancel();
                            }
                        });
                builder1.setNegativeButton("İPTAL",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();

            }
        });

    }
    private List<Kiyafet> GetKiyafetler(String cekmeceAdi,Context context){
        List<Kiyafet> questions = new ArrayList<>();

        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(context);
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

        String selection = FeedReaderContract.KiyafetDB.cekmeceAdi + " = ?";
        String[] selectionArgs = {cekmeceAdi};

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
            System.out.println(q.getID()+" kiyafet ID BU");

        }
        cursor.close();
        db.close();
        dbHelper.close();

        return questions;
    }
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

}