package com.example.sanaldolabim.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.BaseColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.sanaldolabim.DB.FeedReaderContract;
import com.example.sanaldolabim.DB.FeedReaderDbHelper;
import com.example.sanaldolabim.Etkinlik;
import com.example.sanaldolabim.EtkinlikMenu;
import com.example.sanaldolabim.KabinOdasi;
import com.example.sanaldolabim.Kiyafet;
import com.example.sanaldolabim.Kombin;
import com.example.sanaldolabim.R;

import java.util.ArrayList;
import java.util.List;


public class EtkinlikAdapter extends RecyclerView.Adapter<EtkinlikAdapter.ViewHolder> {

    private final List<Etkinlik> localDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView etkinlikAdi,etkinlikTuru,etkinlikTarihi,etkinlikLokasyon;
        Button etkinlikGuncelle,etkinlikSil;
        ImageView basustu,surat,ustbeden,altbeden,ayak;
        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            etkinlikAdi=view.findViewById(R.id.etkinlikAdi);
            etkinlikTuru=view.findViewById(R.id.etkinlikTuru);
            etkinlikTarihi=view.findViewById(R.id.etkinlikTarihi);
            etkinlikLokasyon=view.findViewById(R.id.etkinlikLokasyon);
            basustu=view.findViewById(R.id.basustuetk);
            surat=view.findViewById(R.id.suratetk);
            ustbeden=view.findViewById(R.id.ustbedenetk);
            altbeden=view.findViewById(R.id.altbedenetk);
            ayak=view.findViewById(R.id.ayakkabietk);
            etkinlikGuncelle=view.findViewById(R.id.etkinlikGuncelle);
            etkinlikSil=view.findViewById(R.id.etkinlikSil);
        }

        public TextView getEtkinlikAdi() {
            return etkinlikAdi;
        }

        public TextView getEtkinlikTuru() {
            return etkinlikTuru;
        }

        public TextView getEtkinlikTarihi() {
            return etkinlikTarihi;
        }

        public TextView getEtkinlikLokasyon() {
            return etkinlikLokasyon;
        }

        public Button getEtkinlikGuncelle() {
            return etkinlikGuncelle;
        }

        public Button getEtkinlikSil() {
            return etkinlikSil;
        }

        public ImageView getBasustu() {
            return basustu;
        }

        public ImageView getSurat() {
            return surat;
        }

        public ImageView getUstbeden() {
            return ustbeden;
        }

        public ImageView getAltbeden() {
            return altbeden;
        }

        public ImageView getAyak() {
            return ayak;
        }
    }

    public EtkinlikAdapter(List<Etkinlik> dataSet) {
        localDataSet = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.etkinlik_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Kombin k = GetKombin(localDataSet.get(position).getKombinId(),viewHolder.getEtkinlikAdi().getContext());
        if(k!=null){
            viewHolder.getBasustu().setImageBitmap(GetKiyafetImageFromID(k.getBasustu(),viewHolder.getEtkinlikAdi().getContext()));
            viewHolder.getSurat().setImageBitmap(GetKiyafetImageFromID(k.getSurat(),viewHolder.getEtkinlikAdi().getContext()));
            viewHolder.getUstbeden().setImageBitmap(GetKiyafetImageFromID(k.getUstbeden(),viewHolder.getEtkinlikAdi().getContext()));
            viewHolder.getAltbeden().setImageBitmap(GetKiyafetImageFromID(k.getAltbeden(),viewHolder.getEtkinlikAdi().getContext()));
            viewHolder.getAyak().setImageBitmap(GetKiyafetImageFromID(k.getAyakkabi(),viewHolder.getEtkinlikAdi().getContext()));
        }


        viewHolder.getEtkinlikAdi().setText(localDataSet.get(position).getIsim());
        viewHolder.getEtkinlikTuru().setText(localDataSet.get(position).getTuru());
        viewHolder.getEtkinlikTarihi().setText(localDataSet.get(position).getTarih());
        viewHolder.getEtkinlikLokasyon().setText(localDataSet.get(position).getLokasyon());

        viewHolder.getEtkinlikGuncelle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((EtkinlikMenu)viewHolder.getAyak().getContext()).EtkinlikEkle("evet",localDataSet.get(position));
            }
        });
        viewHolder.getEtkinlikSil().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(viewHolder.getEtkinlikAdi().getContext());
                builder1.setMessage("Etkinliği silmek istediğinize emin misiniz?");
                builder1.setCancelable(true);
                builder1.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(viewHolder.getEtkinlikAdi().getContext());
                                SQLiteDatabase db = dbHelper.getReadableDatabase();
                                dbHelper.DeleteEtkinlik(db,String.valueOf(localDataSet.get(position).getID()));
                                dialog.cancel();
                                ((EtkinlikMenu)viewHolder.getEtkinlikAdi().getContext()).RefreshList();
                            }
                        });
                builder1.setNegativeButton("İptal",
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
    private Kombin GetKombin(String ID,Context context){

        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(context);
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

        // Filter results WHERE "title" = 'My Title'
        String selection = FeedReaderContract.KombinDB._ID + " = ?";
        String[] selectionArgs = { ID };

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
        Kombin q;
        if(cursor.getCount()==0){
            dbHelper.DeleteEtkinlik(db,ID);
            q=null;
        }else{
            cursor.moveToNext();
            q = new Kombin(cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.KombinDB.basustu)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.KombinDB.surat)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.KombinDB.ustbeden)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.KombinDB.altbeden)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.KombinDB.ayakkabi))
            );
            q.setID(cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.KombinDB._ID)));
        }


        cursor.close();
        db.close();
        dbHelper.close();
        return q;
    }
    public Bitmap GetKiyafetImageFromID(String ID, Context context){
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

        String selection = FeedReaderContract.KiyafetDB._ID + " = ?";
        System.out.println("NULL DİYO BUNA:"+ID);
        String[] selectionArgs = {ID};

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

        cursor.moveToNext();
        byte[] bp= cursor.getBlob(cursor.getColumnIndexOrThrow(FeedReaderContract.KiyafetDB.foto));

        cursor.close();
        db.close();
        dbHelper.close();

        return BitmapFactory.decodeByteArray(bp, 0,bp.length );

    }
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

}