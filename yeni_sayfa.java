package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.myapplication.MainActivity.kontrol;

public class yeni_sayfa extends AppCompatActivity {
TextView tv1;
Button like,unlike;
int like_count,unlike_count;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yeni_sayfa);
        tv1=findViewById(R.id.textView1);
        like = findViewById(R.id.button1);
        unlike = findViewById(R.id.button2);

        Intent intent = getIntent();
        ArrayList <String> haber = getIntent().getStringArrayListExtra("haber");
        if(kontrol.contains(haber.get(8))){
            like.setEnabled(false);
            unlike.setEnabled(false);
            Toast.makeText(getApplicationContext(), "Bu haber için daha önce oy kullandınız.", Toast.LENGTH_LONG).show();
        }

        TextView sub1 = findViewById(R.id.subject1);
        sub1.setText(haber.get(0));

        TextView body1 = findViewById(R.id.body1);
        body1.setText(haber.get(1));

        ImageView resim1 = findViewById(R.id.resim1);
        Picasso.get().load(haber.get(2)).into(resim1);
        like_count=Integer.parseInt(haber.get(6));
        unlike_count= Integer.parseInt(haber.get(7));

        tv1.setText("Okunma Sayısı: "+haber.get(5)+"\n"+"Tarih: "+haber.get(4)+"\n"+"Kategori:"+haber.get(3)+"\nBu haber "+like_count+" kez beğenildi.\n"+"Bu haber "+unlike_count+" kez beğenilmedi.");


    }



    public void like(View view) {
        Intent intent = getIntent();
        ArrayList <String> haber = getIntent().getStringArrayListExtra("haber");
        like = findViewById(R.id.button1);
        unlike = findViewById(R.id.button2);
        like_count++;
        kontrol.add(haber.get(8));
        tv1.setText("Okunma Sayısı: "+haber.get(5)+"\n"+"Tarih: "+haber.get(4)+"\n"+"Kategori:"+haber.get(3)+"\nBu haber "+like_count+" kez beğenildi.\n"+"Bu haber "+unlike_count+" kez beğenilmedi.");
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        HttpURLConnection connection =null;
        BufferedReader br = null;

        try{
            URL url = new URL("http://192.168.43.103/yazlab/api/like/"+haber.get(8)); // url parametresi
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream is = connection.getInputStream();

        }catch(Exception e){
            e.printStackTrace();
        }

        Toast.makeText(getApplicationContext(), "Haberi beğendiniz.", Toast.LENGTH_LONG).show();
        like.setEnabled(false);
        unlike.setEnabled(false);

    }






    public void unlike(View view) {
        Intent intent = getIntent();
        ArrayList <String> haber = getIntent().getStringArrayListExtra("haber");
        unlike_count++;
        kontrol.add(haber.get(8));
        tv1.setText("Okunma Sayısı: "+haber.get(5)+"\n"+"Tarih: "+haber.get(4)+"\n"+"Kategori:"+haber.get(3)+"\nBu haber "+like_count+" kez beğenildi.\n"+"Bu haber "+unlike_count+" kez beğenilmedi.");
        like = findViewById(R.id.button1);
        unlike = findViewById(R.id.button2);

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        HttpURLConnection connection =null;
        BufferedReader br = null;

        try{
            URL url = new URL("http://192.168.43.103/yazlab/api/un_like/"+haber.get(8)); // url parametresi
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream is = connection.getInputStream();

        }catch(Exception e){
            e.printStackTrace();
        }

        Toast.makeText(getApplicationContext(), "Haberi beğenmediniz.", Toast.LENGTH_LONG).show();
        like.setEnabled(false);
        unlike.setEnabled(false);

    }
}
