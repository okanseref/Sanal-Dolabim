package com.example.sanaldolabim;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sanaldolabim.DB.FeedReaderContract;
import com.example.sanaldolabim.DB.FeedReaderDbHelper;
import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class EtkinlikEkle extends AppCompatActivity {
    EditText etkinlikadi,etkinlikturu,etkinliktarih,etkinlinlokasyon;
    TextView kombinAdi;
    Button kombinSec,etkinlikKaydet,lokasyonSec;
    String guncellemeMi;
    Etkinlik etkinlik;
    ActivityResultLauncher<Intent> someActivityResultLauncher;
    public static String SelectedKombin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etkinlik_ekle);
        guncellemeMi= (String) getIntent().getExtras().getSerializable("guncellemeMi");
        etkinlik= (Etkinlik) getIntent().getExtras().getSerializable("etkinlik");
        FillValues();
        if(guncellemeMi.equals("evet")){
            etkinlikadi.setText(etkinlik.getIsim());
            etkinlikturu.setText(etkinlik.getTuru());
            etkinliktarih.setText(etkinlik.getTarih());
            etkinlinlokasyon.setText(etkinlik.getLokasyon());
            kombinAdi.setText("Seçilen kombin : "+etkinlik.getKombinId());
        }
        etkinlikKaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(guncellemeMi.equals("evet")){
                    UpdateEtkinlik(etkinlik.getID());
                    MesajOlustur("Etkinlik başarıyla güncellendi.");
                }else{
                    InsertEtkinlik();
                    MesajOlustur("Etkinlik başarıyla kaydedildi.");
                }
            }
        });
        kombinSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EtkinlikEkle.this,KombinSecme.class);
                someActivityResultLauncher.launch(intent);            }
        });
        lokasyonSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickPointOnMap();
            }
        });
        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        kombinAdi.setText("Seçilen kombin : "+SelectedKombin);
                    }
                });
    }
    static final int PICK_MAP_POINT_REQUEST = 999;  // The request code
    private void pickPointOnMap() {
        Intent pickPointIntent = new Intent(this, MapsActivity.class);
        startActivityForResult(pickPointIntent, PICK_MAP_POINT_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_MAP_POINT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                LatLng latLng = (LatLng) data.getParcelableExtra("picked_point");
                etkinlinlokasyon.setText(latLng.latitude + " " + latLng.longitude);
                Toast.makeText(this, "Point Chosen: " + latLng.latitude + " " + latLng.longitude, Toast.LENGTH_LONG).show();
                ConvertLokasyon(latLng);
            }
        }
    }
    private void ConvertLokasyon(LatLng latitude){
        try {
            Geocoder geo = new Geocoder(this.getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geo.getFromLocation(latitude.latitude, latitude.longitude, 1);
            etkinlinlokasyon.setText(addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void MesajOlustur(String text) {
        AlertDialog alertDialog = new AlertDialog.Builder(EtkinlikEkle.this).create();
        alertDialog.setTitle("Mesaj");
        alertDialog.setMessage(text);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "TAMAM",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
        alertDialog.show();
    }
    private void FillValues(){
        etkinlikadi=findViewById(R.id.etkinlikismiedit);
        etkinlikturu=findViewById(R.id.etkinlikturuedit);
        etkinliktarih=findViewById(R.id.etkinliktarihedit);
        etkinlinlokasyon=findViewById(R.id.etkinliklokasyonedit);
        kombinAdi=findViewById(R.id.kombinAdi);
        lokasyonSec=findViewById(R.id.lokasyonSec);
        kombinSec=findViewById(R.id.kombinsec);
        etkinlikKaydet=findViewById(R.id.etkinlikkaydet);
    }
    private void InsertEtkinlik(){
        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.EtkinlikDB.isim, etkinlikadi.getText().toString());
        //values.put(FeedReaderContract.EtkinlikDB.kombin, etkinlikturu.getText().toString());
        values.put(FeedReaderContract.EtkinlikDB.lokasyon, etkinlinlokasyon.getText().toString());
        values.put(FeedReaderContract.EtkinlikDB.tarihi, etkinliktarih.getText().toString());
        values.put(FeedReaderContract.EtkinlikDB.turu, etkinlikturu.getText().toString());
        values.put(FeedReaderContract.EtkinlikDB.kombin, SelectedKombin);

        long newRowId = db.insert(FeedReaderContract.EtkinlikDB.TABLE_NAME, null, values);
        db.close();
        dbHelper.close();
    }
    private void UpdateEtkinlik(String ID){
        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.EtkinlikDB.isim, etkinlikadi.getText().toString());
        values.put(FeedReaderContract.EtkinlikDB.lokasyon, etkinlinlokasyon.getText().toString());
        values.put(FeedReaderContract.EtkinlikDB.tarihi, etkinliktarih.getText().toString());
        values.put(FeedReaderContract.EtkinlikDB.turu, etkinlikturu.getText().toString());
        values.put(FeedReaderContract.EtkinlikDB.kombin, SelectedKombin);

        long newRowId = db.update(FeedReaderContract.EtkinlikDB.TABLE_NAME,  values,"_ID='"+ID+"'",null);
        db.close();
        dbHelper.close();
    }
}