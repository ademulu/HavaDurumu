package com.ademulu.havadurumu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Adem Ulu on 26.11.2017.
 */

public class IslemEkrani extends AppCompatActivity{

    public TextView isim,isi,durum,ulke,ikon;
    String ism,sicaklik,drm,ulk,ikn;
    public Button ekle,listele,sil,detay,guncelle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.islem_ekrani);

        isim=(TextView)findViewById(R.id.Isim);
        isi=(TextView)findViewById(R.id.Sicaklik);
        durum=(TextView)findViewById(R.id.Durum);
        ulke=(TextView)findViewById(R.id.Ulke);
        ikon=(TextView)findViewById(R.id.Ikon);
        ekle=(Button)findViewById(R.id.btnEkle);
        listele=(Button)findViewById(R.id.btnListele);
        sil=(Button)findViewById((R.id.btnSil));
        detay=(Button)findViewById(R.id.btnDetay);
        guncelle=(Button)findViewById(R.id.btnGuncel);

        Bundle extras=getIntent().getExtras();

        ism=extras.getString("isim");
        isim.setText(ism);

        sicaklik=extras.getString("isi");
        isi.setText(sicaklik);

        drm=extras.getString("durum");
        durum.setText(drm);

        ulk=extras.getString("ulke");
        ulke.setText(ulk);

        ikn=extras.getString("ikon");
        ikon.setText(ikn);

        ekle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String adi,isisi,durumu,ulkesi,iconu;
                adi = isim.getText().toString();
                isisi = isi.getText().toString();
                durumu = durum.getText().toString();
                ulkesi = ulke.getText().toString();
                iconu=ikon.getText().toString();

                if(adi.matches("") || isisi.matches("") || durumu.matches("") || ulkesi.matches("") || iconu.matches("")  ){
                    Toast.makeText(getApplicationContext(), "Tüm Bilgileri Eksiksiz Doldurunuz", Toast.LENGTH_SHORT).show();
                    Intent intAdem = new Intent(IslemEkrani.this, HavaDurumu.class);
                    startActivity(intAdem);
                }else{
                    VeriTabani db = new VeriTabani(getApplicationContext());
                    db.sehirEkle(adi, isisi, durumu, ulkesi,iconu);
                    db.close();
                    Toast.makeText(getApplicationContext(), "Şehir Başarıyla Eklendi.", Toast.LENGTH_SHORT).show();
                    isim.setText("");
                    isi.setText("");
                    durum.setText("");
                    ulke.setText("");
                    ikon.setText("");
                }
            }
        });
        listele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intAdem = new Intent(IslemEkrani.this, ListeEkrani.class);
                startActivity(intAdem);
            }
        });

        sil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ad;
                ad = isim.getText().toString();

                if(ad.matches("")){
                    Toast.makeText(getApplicationContext(), "Eksik Bilgi", Toast.LENGTH_SHORT).show();
                }
                else{
                VeriTabani db = new VeriTabani(getApplicationContext());
                db.sehirSil(ad);
                db.close();
                Toast.makeText(getApplicationContext(), "Şehir Başarıyla Silindi.", Toast.LENGTH_SHORT).show();
                isim.setText("");
                isi.setText("");
                durum.setText("");
                ulke.setText("");
                ikon.setText("");
                }
            }
        });
        detay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ad;
                ad = isim.getText().toString();
                if(ad=="")
                {
                    Toast.makeText(getApplicationContext(), "Bilgi Gösterilecek Sehir Bulunamadi.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                VeriTabani db = new VeriTabani(getApplicationContext());
                HashMap<String, String> map = db.sehirDetay(ad);
                isi.setText(map.get("sehir_sicaklik").toString());
                durum.setText(map.get("sehir_durum").toString());
                ulke.setText(map.get("sehir_ulke").toString());
                ikon.setText(map.get("sehir_icon").toString());
                }
            }
        });

        guncelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intAdem = new Intent(IslemEkrani.this, VeriGuncelle.class);
                startActivity(intAdem);

            }
        });

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }
}

