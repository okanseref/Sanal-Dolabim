package com.example.sanaldolabim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.sanaldolabim.DB.FeedReaderContract;
import com.example.sanaldolabim.DB.FeedReaderDbHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MyBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "MyBroadcastReceiver";
    public static List<Kiyafet> kiyafetList;
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println(intent.getAction()+"intent bu");
        if(intent.getAction().equals(Intent.ACTION_POWER_CONNECTED) ){
            final PendingResult pendingResult = goAsync();
            Task asyncTask = new Task(pendingResult, intent);
            asyncTask.execute();
        }

    }

    private static class Task extends AsyncTask<String, Integer, String> {

        private final PendingResult pendingResult;
        private final Intent intent;

        private Task(PendingResult pendingResult, Intent intent) {
            this.pendingResult = pendingResult;
            this.intent = intent;
        }

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder sb = new StringBuilder();
            sb.append("Action: " + intent.getAction() + "\n");
            sb.append("URI: " + intent.toUri(Intent.URI_INTENT_SCHEME).toString() + "\n");
            String log = sb.toString();
            Log.d(TAG, log);

            StorageReference mStorageRef;


            for(int i=0;i<kiyafetList.size();i++){
                mStorageRef = FirebaseStorage.getInstance().getReference();
                mStorageRef = mStorageRef.child("images/"+kiyafetList.get(i).getID()+".png");
                mStorageRef.putBytes(kiyafetList.get(i).getFoto());
            }
            return log;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // Must call finish() so the BroadcastReceiver can be recycled.

            pendingResult.finish();
        }
    }


}