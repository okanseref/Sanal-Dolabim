package com.example.sanaldolabim;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.sanaldolabim.Adapter.CekmeceAdapter;
import com.example.sanaldolabim.Adapter.KombinAdapter;
import com.example.sanaldolabim.DB.FeedReaderContract;
import com.example.sanaldolabim.DB.FeedReaderDbHelper;

import java.util.ArrayList;
import java.util.List;

public class KabinOdasi extends AppCompatActivity {
    Button kombinKaydet,kombinPaylas;
    RecyclerView kombinRec;
    ImageButton basustu,surat,ustbeden,altbeden,ayakkabi;
    int indexOfPart;
    String basustuID,suratID,ustbedenID,altbedenID,ayakkabiID;
    public static String SelectedID;
    ActivityResultLauncher<Intent> someActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kabin_odasi);
        FillValues();
        kombinKaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertKombin();
            }
        });
        kombinPaylas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    String[] PERMISSIONS = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    if (!hasPermissions(mContext, PERMISSIONS)) {
                        ActivityCompat.requestPermissions((Activity) mContext, PERMISSIONS, REQUEST );
                    } else {
                        //do here
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        Bitmap b = createSingleImageFromMultipleImages(((BitmapDrawable)basustu.getDrawable()).getBitmap(),
                                ((BitmapDrawable)surat.getDrawable()).getBitmap(),
                                ((BitmapDrawable)ustbeden.getDrawable()).getBitmap(),
                                ((BitmapDrawable)altbeden.getDrawable()).getBitmap(),
                                ((BitmapDrawable)ayakkabi.getDrawable()).getBitmap());
                        String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), b,"title", null);
                        Uri bitmapUri = Uri.parse(bitmapPath);
                        sendIntent.putExtra(Intent.EXTRA_STREAM,bitmapUri );
                        sendIntent.setType("image/png");
                        startActivity(sendIntent);
                    }
                }

            }
        });
        basustu.setOnClickListener(SetListener(0));
        surat.setOnClickListener(SetListener(1));
        ustbeden.setOnClickListener(SetListener(2));
        altbeden.setOnClickListener(SetListener(3));
        ayakkabi.setOnClickListener(SetListener(4));
    }
    private Context mContext=KabinOdasi.this;

    private static final int REQUEST = 112;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST: {
                if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(getApplicationContext(), "The app was not allowed to write in your storage", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    private Bitmap createSingleImageFromMultipleImages(Bitmap image1, Bitmap image2,Bitmap image3,Bitmap image4,Bitmap image5){
        int width=image1.getWidth()*5,height=image1.getHeight()+image2.getHeight()+image3.getHeight()+image4.getHeight()+image5.getHeight();
        Bitmap result = Bitmap.createBitmap(width, height, image1.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(image1, image1.getWidth()*2, 0f, null);
        canvas.drawBitmap(image2, image1.getWidth()*2, image1.getHeight(), null);
        canvas.drawBitmap(image3, image1.getWidth()*2, image1.getHeight()+image2.getHeight(), null);
        canvas.drawBitmap(image4, image1.getWidth()*2, image1.getHeight()+image2.getHeight()+image3.getHeight(), null);
        canvas.drawBitmap(image5, image1.getWidth()*2, image1.getHeight()+image2.getHeight()+image3.getHeight()+image4.getHeight(), null);
        return result;
    }
    private View.OnClickListener SetListener(int index){
        View.OnClickListener temp =new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                indexOfPart=index;
                LaunchSelectKiyafet();
            }
        };
        return temp;
    }
    private void LaunchSelectKiyafet(){
        Intent intent = new Intent(KabinOdasi.this,KiyafetSecme.class);
        someActivityResultLauncher.launch(intent);
    }
    private void FillValues(){
        kombinPaylas=findViewById(R.id.kombinPaylas);
        kombinRec=findViewById(R.id.kombinRecView);
        kombinKaydet=findViewById(R.id.kombinKaydet);
        basustu=findViewById(R.id.BasUstu);
        surat=findViewById(R.id.Surat);
        ustbeden=findViewById(R.id.UstBeden);
        altbeden=findViewById(R.id.AltBeden);
        ayakkabi=findViewById(R.id.Ayak);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        kombinRec.setLayoutManager(layoutManager);
        RecyclerView.Adapter adapter = new KombinAdapter(GetKombinler());
        kombinRec.setAdapter(adapter);

        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        switch (indexOfPart){
                            case 0:
                                basustu.setImageBitmap(GetKiyafetImageFromID(SelectedID));
                                basustuID=SelectedID;
                                break;
                            case 1:
                                surat.setImageBitmap(GetKiyafetImageFromID(SelectedID));
                                suratID=SelectedID;
                                break;
                            case 2:
                                ustbeden.setImageBitmap(GetKiyafetImageFromID(SelectedID));
                                ustbedenID=SelectedID;
                                break;
                            case 3:
                                altbeden.setImageBitmap(GetKiyafetImageFromID(SelectedID));
                                altbedenID=SelectedID;
                                break;
                            case 4:
                                ayakkabi.setImageBitmap(GetKiyafetImageFromID(SelectedID));
                                ayakkabiID=SelectedID;
                                break;
                        }
                    }
                });
    }
    public Bitmap GetKiyafetImageFromID(String ID){

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
    public void KombinDegistir(Kombin k){
        basustu.setImageBitmap(GetKiyafetImageFromID(k.getBasustu()));
        basustuID=k.getBasustu();
        surat.setImageBitmap(GetKiyafetImageFromID(k.getSurat()));
        suratID=k.getSurat();
        ustbeden.setImageBitmap(GetKiyafetImageFromID(k.getUstbeden()));
        ustbedenID=k.getUstbeden();
        altbeden.setImageBitmap(GetKiyafetImageFromID(k.getAltbeden()));
        altbedenID=k.getAltbeden();
        ayakkabi.setImageBitmap(GetKiyafetImageFromID(k.getAyakkabi()));
        ayakkabiID=k.getAyakkabi();

    }
    private void InsertKombin(){
        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.KombinDB.ustbeden, ustbedenID);
        values.put(FeedReaderContract.KombinDB.altbeden, altbedenID);
        values.put(FeedReaderContract.KombinDB.surat, suratID);
        values.put(FeedReaderContract.KombinDB.ayakkabi, ayakkabiID);
        values.put(FeedReaderContract.KombinDB.basustu, basustuID);

        long newRowId = db.insert(FeedReaderContract.KombinDB.TABLE_NAME, null, values);
        db.close();
        dbHelper.close();
        RefreshList();
        MesajOlustur("Kombin başarıyla kaydedildi.");
    }
    public void MesajOlustur(String text) {
        AlertDialog alertDialog = new AlertDialog.Builder(KabinOdasi.this).create();
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
    public void RefreshList(){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        kombinRec.setLayoutManager(layoutManager);
        RecyclerView.Adapter adapter = new KombinAdapter(GetKombinler());
        kombinRec.setAdapter(adapter);
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
        }
        cursor.close();
        db.close();
        dbHelper.close();

        return kombins;
    }

}