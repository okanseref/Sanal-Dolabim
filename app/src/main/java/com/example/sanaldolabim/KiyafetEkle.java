package com.example.sanaldolabim;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.sanaldolabim.Adapter.KiyafetAdapter;
import com.example.sanaldolabim.DB.FeedReaderContract;
import com.example.sanaldolabim.DB.FeedReaderDbHelper;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KiyafetEkle extends AppCompatActivity {
    EditText turu,renk,desen,tarih,fiyat;
    ImageButton foto;
    Button save;
    String cekmeceAdi,guncellemeMi;
    Kiyafet kiyafet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kiyafet_ekle);
        cekmeceAdi= (String) getIntent().getExtras().getSerializable("cekmeceAdi");
        guncellemeMi= (String) getIntent().getExtras().getSerializable("guncellemeMi");
        kiyafet= (Kiyafet) getIntent().getExtras().getSerializable("kiyafet");
        FillValues();
        if(guncellemeMi.equals("evet")){
            turu.setText(kiyafet.getKiyafetTuru());
            renk.setText(kiyafet.getRenk());
            desen.setText(kiyafet.getDesen());
            tarih.setText(kiyafet.getTarih());
            fiyat.setText(kiyafet.getFiyat());
            foto.setImageBitmap(BitmapFactory.decodeByteArray(kiyafet.getFoto(), 0,kiyafet.getFoto().length ));
            System.out.println("Güncellenecek kiyafet: "+kiyafet.getID());
            System.out.println("Güncellenecek resmi: "+kiyafet.getFoto());
        }
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(guncellemeMi.equals("evet")){
                    KiyafetGuncelleDB(turu.getText().toString(),renk.getText().toString(),desen.getText().toString(),tarih.getText().toString(),fiyat.getText().toString(),DrawableToByte(foto.getDrawable()),cekmeceAdi);
                    MesajOlustur("Kıyafet başarıyla güncellendi.");
                }else{
                    KiyafetEkleDB(turu.getText().toString(),renk.getText().toString(),desen.getText().toString(),tarih.getText().toString(),fiyat.getText().toString(),DrawableToByte(foto.getDrawable()),cekmeceAdi);
                    MesajOlustur("Kıyafet başarıyla kaydedildi.");

                }
            }
        });
        ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                            try {
                                final Uri imageUri = data.getData();
                                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                                foto.setImageBitmap(selectedImage);

                            } catch (FileNotFoundException e) {

                                e.printStackTrace();
                            }
                        }
                    }
                });

        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent .setType("image/*");
                someActivityResultLauncher.launch(intent);
            }
        });
    }
    public void MesajOlustur(String text) {
        AlertDialog alertDialog = new AlertDialog.Builder(KiyafetEkle.this).create();
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
        turu = findViewById(R.id.kiyafetTuru);
        renk = findViewById(R.id.kiyafetRenk);
        desen = findViewById(R.id.kiyafetDesen);
        tarih = findViewById(R.id.kiyafetTarih);
        fiyat = findViewById(R.id.kiyafetFiyat);
        foto = findViewById(R.id.kiyafetFoto);
        save = findViewById(R.id.saveKiyafet);
    }
    private byte[] DrawableToByte(Drawable d){
        Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
        bitmap=makeSmallerImage(bitmap,480);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 30, stream);
        return stream.toByteArray();
    }
    public Bitmap makeSmallerImage(Bitmap image,int maxSize){
        int width=image.getWidth();
        int height=image.getHeight();
        float bitmapRatio=(float) width/(float)height;
        if(bitmapRatio>1){
            width=maxSize;
            height=(int)(width/bitmapRatio);
        }else{
            height=maxSize;
            width=(int)(height*bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image,width,height,true);
    }
    private void KiyafetEkleDB(String turu,String renk,String desen,String tarih,String fiyat,byte[] foto,String cekmeceAdi){

        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.KiyafetDB.turu, turu);
        values.put(FeedReaderContract.KiyafetDB.renk, renk);
        values.put(FeedReaderContract.KiyafetDB.desen, desen);
        values.put(FeedReaderContract.KiyafetDB.tarih, tarih);
        values.put(FeedReaderContract.KiyafetDB.fiyat, fiyat);
        values.put(FeedReaderContract.KiyafetDB.cekmeceAdi, cekmeceAdi);
        values.put(FeedReaderContract.KiyafetDB.foto, foto);
        //System.out.println("FOTO BU : "+foto);
        long newRowId = db.insert(FeedReaderContract.KiyafetDB.TABLE_NAME, null, values);

        db.close();
        dbHelper.close();
    }

    private void KiyafetGuncelleDB(String turu,String renk,String desen,String tarih,String fiyat,byte[] foto,String cekmeceAdi){

        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Kiyafet k = new Kiyafet(turu,renk,desen,tarih,fiyat,foto,cekmeceAdi);
        k.setID(kiyafet.getID());

        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.KiyafetDB.turu, turu);
        values.put(FeedReaderContract.KiyafetDB.renk, renk);
        values.put(FeedReaderContract.KiyafetDB.desen, desen);
        values.put(FeedReaderContract.KiyafetDB.tarih, tarih);
        values.put(FeedReaderContract.KiyafetDB.fiyat, fiyat);
        values.put(FeedReaderContract.KiyafetDB.cekmeceAdi, cekmeceAdi);
        values.put(FeedReaderContract.KiyafetDB.foto, foto);

        //System.out.println("FOTO BU : "+foto);
        long newRowId = db.update(FeedReaderContract.KiyafetDB.TABLE_NAME,  values,"_ID='"+kiyafet.getID()+"'",null);
        //dbHelper.UpdateKiyafet(db,k);
        db.close();
        dbHelper.close();

    }

}