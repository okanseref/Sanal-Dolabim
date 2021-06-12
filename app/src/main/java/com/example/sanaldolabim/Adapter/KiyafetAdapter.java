package com.example.sanaldolabim.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.provider.BaseColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sanaldolabim.Cekmece;
import com.example.sanaldolabim.DB.FeedReaderContract;
import com.example.sanaldolabim.DB.FeedReaderDbHelper;
import com.example.sanaldolabim.KabinOdasi;
import com.example.sanaldolabim.Kiyafet;
import com.example.sanaldolabim.KiyafetSecme;
import com.example.sanaldolabim.Menu;
import com.example.sanaldolabim.R;

import java.util.ArrayList;
import java.util.List;


public class KiyafetAdapter extends RecyclerView.Adapter<KiyafetAdapter.ViewHolder> {

    private final List<Kiyafet> localDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView turu,renk,desen,tarih,fiyat;
        ImageView foto;
        Button kiyafetGuncelle,kiyafetSil,kiyafetSec;
        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            kiyafetGuncelle=view.findViewById(R.id.kiyafetGuncelle);
            kiyafetSil=view.findViewById(R.id.kiyafetSil);
            kiyafetSec=view.findViewById(R.id.kiyafetSec);
            turu=view.findViewById(R.id.kiyafetTuruT);
            renk=view.findViewById(R.id.kiyafetRenkT);
            desen=view.findViewById(R.id.kiyafetDesenT);
            tarih=view.findViewById(R.id.kiyafetTarhiT);
            fiyat=view.findViewById(R.id.kiyafetFiyatT);
            foto=view.findViewById(R.id.kiyafetFotoView);
        }

        public Button getKiyafetSec() {
            return kiyafetSec;
        }

        public ImageView getFoto() {
            return foto;
        }

        public TextView getTuru() {
            return turu;
        }

        public TextView getRenk() {
            return renk;
        }

        public TextView getDesen() {
            return desen;
        }

        public TextView getTarih() {
            return tarih;
        }

        public TextView getFiyat() {
            return fiyat;
        }

        public Button getKiyafetGuncelle() {
            return kiyafetGuncelle;
        }

        public Button getKiyafetSil() {
            return kiyafetSil;
        }
    }

    public KiyafetAdapter(List<Kiyafet> dataSet) {
        localDataSet = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.kiyafet_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        System.out.println("HAAAAAAAAAA"+viewHolder.getTuru().getContext());
        if(viewHolder.getTuru().getContext() instanceof KiyafetSecme){
            viewHolder.getKiyafetGuncelle().setVisibility(View.GONE);
            viewHolder.getKiyafetSil().setVisibility(View.GONE);
            viewHolder.getKiyafetSec().setVisibility(View.VISIBLE);
        }

        viewHolder.getTuru().setText(localDataSet.get(position).getKiyafetTuru());
        viewHolder.getRenk().setText(localDataSet.get(position).getRenk());
        viewHolder.getDesen().setText(localDataSet.get(position).getDesen());
        viewHolder.getTarih().setText(localDataSet.get(position).getTarih());
        viewHolder.getFiyat().setText(localDataSet.get(position).getFiyat());
        viewHolder.getFoto().setImageBitmap(BitmapFactory.decodeByteArray(localDataSet.get(position).getFoto(), 0,localDataSet.get(position).getFoto().length ));
        viewHolder.getKiyafetSec().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((KiyafetSecme)viewHolder.getFoto().getContext()).ReturnKiyafet(String.valueOf(localDataSet.get(position).getID()));
            }
        });
        viewHolder.getKiyafetGuncelle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Menu)viewHolder.getFoto().getContext()).KiyafetEkle(localDataSet.get(position).getCekmeceAdi(),"evet",localDataSet.get(position));

            }
        });
        viewHolder.getKiyafetSil().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(viewHolder.getFiyat().getContext());
                builder1.setMessage("Question will be deleted. Are you sure?");
                builder1.setCancelable(true);
                builder1.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //put your code that needed to be executed when okay is clicked
                                FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(viewHolder.getFiyat().getContext());
                                SQLiteDatabase db = dbHelper.getReadableDatabase();
                                dbHelper.DeleteKiyafet(db,String.valueOf(localDataSet.get(position).getID()));
                                ((Menu)viewHolder.getFiyat().getContext()).RefreshMenuList();
                                dialog.cancel();
                            }
                        });
                builder1.setNegativeButton("Cancel",
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

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

}