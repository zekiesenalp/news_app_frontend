package com.example.myapplication;

import android.content.Intent;
import android.media.Image;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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

public class MainActivity extends AppCompatActivity {
    static List<String> kontrol = new ArrayList<String>();
    static  int haber_sayi= haber_sayisi();

    TextView tv1,tv2,tv3,tv4;
    ImageView img1,img2,img3,img4;
    static String kategori="";

    int news_count = haber_sayisi();
    int bir=news_count,iki=news_count-1,uc=news_count-2,dort=news_count-3;
    int secilen=0;

    RadioGroup radioGroup;
    RadioButton radioButton;

    List<Integer> tum_haberler = new ArrayList<Integer>();
    List<Integer> gundem = new ArrayList<Integer>();
    List<Integer> egitim = new ArrayList<Integer>();
    List<Integer> spor = new ArrayList<Integer>();
    List<Integer> ekonomi = new ArrayList<Integer>();
    List<Integer> secilen_liste = new ArrayList<Integer>();



    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn1 = (Button)findViewById(R.id.button1);
        Button btn2 = (Button)findViewById(R.id.button2);


        btn1.setOnClickListener(new View.OnClickListener() { // soldaki buton
            @Override
            public void onClick(View v) {
                bir+=4; iki+=4; uc+=4; dort+=4;
                if(dort<=haber_sayisi()){
                    haber_yukle();
                }else{
                    bir-=4; iki-=4; uc-=4; dort-=4;
                }

            }
        });


        btn2.setOnClickListener(new View.OnClickListener() { // sağ buton
            @Override
            public void onClick(View v) {
                bir-=4; iki-=4; uc-=4; dort-=4;
                if(dort>=0){
                    haber_yukle();
                }else{
                    bir+=4; iki+=4; uc+=4; dort+=4;
                }

            }
        });




        haber_yukle();
        startService(new Intent(this,NotificationService.class));


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

            //startService(new Intent(this,NotificationService.class));




            return dizi;

        }catch(Exception e){
            Log.d("sa","ilk");
            e.printStackTrace();
            ArrayList hata =new ArrayList();
            hata.add("hata");
            return hata;
        }


    }

    public void haber_yukle(){
        tv1 = findViewById(R.id.textView1);
        tv2 = findViewById(R.id.textView2);
        tv3 = findViewById(R.id.textView3);
        tv4 = findViewById(R.id.textView4);

        img1 = findViewById(R.id.imageView1);
        img2 = findViewById(R.id.imageView2);
        img3 = findViewById(R.id.imageView3);
        img4 = findViewById(R.id.imageView4);

        radioGroup=(RadioGroup)findViewById(R.id.radioGroup1);
        int selectedId=radioGroup.getCheckedRadioButtonId();
        radioButton=(RadioButton)findViewById(selectedId);
        String kontrol = radioButton.getText().toString();


        ArrayList <String> haber = tek_haber("http://192.168.43.103/yazlab/api/news/"+bir);
        if(kontrol.equals("Tümü") || kontrol.equals(haber.get(3))){
            tv1.setText(haber.get(0));
            Picasso.get().load(haber.get(2)).into(img1);
        }else{
            tv1.setText("");
            Picasso.get().load("sa").into(img1);

        }


        haber= tek_haber("http://192.168.43.103/yazlab/api/news/"+iki);
        if(kontrol.equals("Tümü") || kontrol.equals(haber.get(3))){
            tv2.setText(haber.get(0));
            Picasso.get().load(haber.get(2)).into(img2);
        }else{
            tv2.setText("");
            Picasso.get().load("sa").into(img2);
        }


        haber= tek_haber("http://192.168.43.103/yazlab/api/news/"+uc);
        if(kontrol.equals("Tümü") || kontrol.equals(haber.get(3))){
            tv3.setText(haber.get(0));
            Picasso.get().load(haber.get(2)).into(img3);
        }else{
            tv3.setText("");
            Picasso.get().load("sa").into(img3);
        }


        haber= tek_haber("http://192.168.43.103/yazlab/api/news/"+dort);
        if(kontrol.equals("Tümü") || kontrol.equals(haber.get(3))){
            tv4.setText(haber.get(0));
            Picasso.get().load(haber.get(2)).into(img4);
        }else{
            tv4.setText("");
            Picasso.get().load("sa").into(img4);
        }

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


    public void gonder4(View view) {
        Intent intent = new Intent(getApplicationContext(),yeni_sayfa.class);
        ArrayList<String> haber = tek_haber("http://192.168.43.103/yazlab/api/news/"+dort);
        intent.putExtra("haber",haber);
        startActivity(intent);
    }

    public void gonder3(View view) {
        Intent intent = new Intent(getApplicationContext(),yeni_sayfa.class);
        ArrayList<String> haber = tek_haber("http://192.168.43.103/yazlab/api/news/"+uc);
        intent.putExtra("haber",haber);
        startActivity(intent);
    }

    public void gonder2(View view) {
        Intent intent = new Intent(getApplicationContext(),yeni_sayfa.class);
        ArrayList<String> haber = tek_haber("http://192.168.43.103/yazlab/api/news/"+iki);
        intent.putExtra("haber",haber);
        startActivity(intent);
    }

    public void gonder1(View view) {
        Intent intent = new Intent(getApplicationContext(),yeni_sayfa.class);
        ArrayList<String> haber = tek_haber("http://192.168.43.103/yazlab/api/news/"+bir);
        intent.putExtra("haber",haber);
        startActivity(intent);
    }


    public void kategori_ayir(){
        for (int i = 1; i <=news_count; i++) {
            ArrayList <String> haber = tek_haber("http://192.168.43.103/yazlab/api/news/"+i);
            if(haber.get(3)=="Gündem"){
                gundem.add(i);
                tum_haberler.add(i);
            }
            else if(haber.get(3)=="Eğitim"){
                egitim.add(i);
                tum_haberler.add(i);
            }
            else if(haber.get(3)=="Spor"){
                spor.add(i);
                tum_haberler.add(i);
            }
            else if(haber.get(3)=="Ekonomi"){
                ekonomi.add(i);
                tum_haberler.add(i);
            }
        }
    }
public String kategori_dondur(){
    radioGroup=(RadioGroup)findViewById(R.id.radioGroup1);
    int selectedId=radioGroup.getCheckedRadioButtonId();
    radioButton=(RadioButton)findViewById(selectedId);

    return radioButton.getText().toString();
}


    public void sifirla(View view) {
        bir=news_count;
        iki=news_count-1;
        uc=news_count-2;
        dort=news_count-3;
        haber_yukle();
    }
}











