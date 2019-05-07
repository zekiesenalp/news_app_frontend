package com.example.myapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.TimerTask;

import static com.example.myapplication.MainActivity.haber_sayi;

public class NotificationService extends Service {
    int yeni_haber=haber_sayi;
    int i=0;

    @Override
    public void onCreate() {
        new Thread(new Runnable() {  // Yeni bir Thread (iş parcacığı) oluşturuyorum.
            @Override
            public void run() { // Thread'ım başladığında bitmemesi için while
                // ile sonsuz döngüye soktum. senaryo gereği
                while (1 == 1){
                    try {
                        Thread.sleep(3000); // Her döngümde Thread'ımı 15000 ms uyutuyorum.
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // ve 1.5 saniyem dolduktan sonra bildirimimi basıyorum.

                    NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        NotificationChannel channel = new NotificationChannel("YOUR_CHANNEL_ID","YOUR_CHANNEL_NAME", NotificationManager.IMPORTANCE_DEFAULT);
                        channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DISCRIPTION");
                        mNotificationManager.createNotificationChannel(channel);
                    }

                    if(haber_sayisi()>haber_sayi){
                        ArrayList<String> haber = tek_haber("http://192.168.43.103/yazlab/api/news/"+haber_sayisi());
                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "YOUR_CHANNEL_ID")
                                .setSmallIcon(R.drawable.ic_launcher_background) // notification icon
                                .setContentTitle(haber.get(0)) // title for notification
                                .setContentText(haber.get(1))// message for notification
                                .setAutoCancel(true); // clear notification after click

                        Intent intent = new Intent(getApplicationContext(), yeni_sayfa.class);
                        //ArrayList<String> haber = tek_haber("http://192.168.0.16/yazlab/api/news/"+haber_sayisi());
                        intent.putExtra("haber",haber);

                        PendingIntent pi = PendingIntent.getActivity( getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        mBuilder.setContentIntent(pi);
                        mNotificationManager.notify(0, mBuilder.build());
                        haber_sayi=haber_sayisi();
                    }

                }
            }
        }).start();  // burada Thread'ımı başlatıyorum.
    }




    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public NotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }







    public static int haber_sayisi(){
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        HttpURLConnection connection =null;
        BufferedReader br = null;

        try{
            URL url = new URL("http://192.168.43.103/yazlab/api/news_count"); // url parametresi
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream is = connection.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            String satir;
            String dosya="";
            while((satir=br.readLine())!=null){
                //Log.d("satir:",satir);
                dosya += satir;
            }

            return Integer.parseInt(dosya);

        }catch(Exception e){
            Log.d("sa","ilk");
            e.printStackTrace();
            return 0;
        }
    }






    public ArrayList<String> tek_haber(String link){ // tek haberin url'sini param. alıp haberin detaylarını arraylist içinde yolluyor. parametreler aşağıda.
        HttpURLConnection connection =null;
        BufferedReader br = null;


        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        try{
            URL url = new URL(link); // url parametresi
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream is = connection.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            String satir;
            String dosya="";
            while((satir=br.readLine())!=null){
                //Log.d("satir:",satir);
                dosya += satir;
            }
            JSONObject jobj = new JSONObject(dosya);
            ArrayList dizi =new ArrayList();

            dizi.add(jobj.getString("subject"));                                      // 0 subj
            dizi.add(jobj.getString("body"));                                         // 1 body
            dizi.add("http://192.168.43.103/yazlab/dosyalar/"+jobj.getString("image")); // 2 image url
            if(jobj.getInt("category")==1){
                dizi.add("Gündem");
            }
            else if(jobj.getInt("category")==2){
                dizi.add("Spor");
            }                                                             // 3 kategori isim
            else if(jobj.getInt("category")==3){
                dizi.add("Eğitim");
            }
            else if(jobj.getInt("category")==4){
                dizi.add("Ekonomi");
            }

            dizi.add(jobj.getString("date"));                       // 4 date
            dizi.add(""+jobj.getInt("read_count"));                 // 5 read count
            dizi.add(jobj.getString("n_like"));                     // 6 like
            dizi.add(jobj.getString("n_unlike"));                   // 7 unlike
            String[] parts = link.split("/");
            dizi.add(""+parts[parts.length-1]);                            //8 id

            startService(new Intent(this,NotificationService.class));




            return dizi;

        }catch(Exception e){
            Log.d("sa","ilk");
            e.printStackTrace();
            ArrayList hata =new ArrayList();
            hata.add("hata");
            return hata;
        }


    }
}
