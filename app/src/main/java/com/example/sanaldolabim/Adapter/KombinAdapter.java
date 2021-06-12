package com.example.sanaldolabim.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.sanaldolabim.DB.FeedReaderDbHelper;
import com.example.sanaldolabim.KabinOdasi;
import com.example.sanaldolabim.Kiyafet;
import com.example.sanaldolabim.KiyafetSecme;
import com.example.sanaldolabim.Kombin;
import com.example.sanaldolabim.KombinSecme;
import com.example.sanaldolabim.Menu;
import com.example.sanaldolabim.R;

import java.util.List;


public class KombinAdapter extends RecyclerView.Adapter<KombinAdapter.ViewHolder> {

    private final List<Kombin> localDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView kombinAdi;
        Button kombinGoruntule,kombinSil,kombinSecme;
        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            kombinAdi=view.findViewById(R.id.kombinName);
            kombinGoruntule=view.findViewById(R.id.kombingoruntule);
            kombinSil=view.findViewById(R.id.kombinsil);
            kombinSecme=view.findViewById(R.id.kombinSecme);
        }

        public Button getKombinSecme() {
            return kombinSecme;
        }

        public TextView getKombinAdi() {
            return kombinAdi;
        }

        public Button getKombinGoruntule() {
            return kombinGoruntule;
        }

        public Button getKombinSil() {
            return kombinSil;
        }
    }

    public KombinAdapter(List<Kombin> dataSet) {
        localDataSet = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.kombin_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        if(viewHolder.getKombinAdi().getContext() instanceof KombinSecme){
            viewHolder.getKombinSecme().setVisibility(View.VISIBLE);
            viewHolder.getKombinSil().setVisibility(View.GONE);
            viewHolder.getKombinGoruntule().setVisibility(View.GONE);
        }

        viewHolder.getKombinAdi().setText("KOMBIN NO "+localDataSet.get(position).getID());

        viewHolder.getKombinSecme().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((KombinSecme)viewHolder.getKombinAdi().getContext()).ReturnKombin(String.valueOf(localDataSet.get(position).getID()));
            }
        });
        viewHolder.getKombinGoruntule().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Kombin k = localDataSet.get(position);

                ((KabinOdasi)viewHolder.getKombinAdi().getContext()).KombinDegistir(k);

            }
        });
        viewHolder.getKombinSil().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(viewHolder.getKombinAdi().getContext());
                builder1.setMessage("Kombini silmek istediğinize emin misiniz?");
                builder1.setCancelable(true);
                builder1.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //put your code that needed to be executed when okay is clicked
                                FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(viewHolder.getKombinAdi().getContext());
                                SQLiteDatabase db = dbHelper.getReadableDatabase();
                                dbHelper.DeleteKombin(db,String.valueOf(localDataSet.get(position).getID()));
                                //((KabinOdasi)viewHolder.getKombinAdi().getContext()).KombinDegistir();
                                dialog.cancel();
                                ((KabinOdasi)viewHolder.getKombinAdi().getContext()).RefreshList();

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