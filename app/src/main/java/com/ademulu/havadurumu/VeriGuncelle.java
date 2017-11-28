package com.ademulu.havadurumu;

/**
 * Created by Adem Ulu on 27.11.2017.
 */


import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
 * Created by landforce on 10.08.2014.
 */
public class VeriGuncelle extends Activity {

    Button guncelle,listele;
    ArrayList<HashMap<String, String>> sehir_liste;
    String sehir_adlari[];
    String city,isi,ulk,ikon,main;
    String sehir;
    int temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guncelleme_ekrani);


        guncelle = (Button)findViewById(R.id.button1);
        listele=(Button)findViewById(R.id.button2);

        guncelle.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                temp=0;
                String adi="",sicaklik="",durum="",ulke="",icon="";

                VeriTabani db = new VeriTabani(getApplicationContext());
                sehir_liste = db.sehirler();
                db.close();

                sehir_adlari = new String[sehir_liste.size()];

                for(int i=0;i<sehir_liste.size();i++){
                    sehir_adlari[i] = sehir_liste.get(i).get("sehir_adi");
                }

                for (int j=0;j<sehir_liste.size();j++){
                    //hava durmuu bilgi edinme işlemleri

                    JsonParse json = new JsonParse();
                    json.execute();//jsonParse AsynTask metodumuzu çalıştırdık


                }
            }
        });
        listele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intAdem = new Intent(VeriGuncelle.this, ListeEkrani.class);
                startActivity(intAdem);
            }
        });



    }
    protected class JsonParse extends AsyncTask<Void, Void, Void> {
        String result_main ="";
        String result_aciklama = "";
        String result_icon = "";
        int result_temp;
        String result_ulke="";
        String result_city;
        Bitmap bitImage;
        @Override
        protected Void doInBackground(Void... params) {

            String result="";
            try {
                URL hava_url = new URL("http://api.openweathermap.org/data/2.5/weather?q="+sehir_adlari[temp]+"&appid=cda57a3812f7e95994a604fff6ff2431");
                temp=temp+1;
                BufferedReader bufferedReader = null;//BufferedReader tarafından okunan veri, ilgili karekter ya da byte dizisine dönüşür
                bufferedReader = new BufferedReader(new InputStreamReader(hava_url.openStream()));//url'yi okuyarak bufferReader'a gönderdik
                String line = null;
                while((line = bufferedReader.readLine()) != null){//satırları tek tek aldık ve ekledik
                    result += line;
                }
                bufferedReader.close();

                JSONObject jsonNesne = new JSONObject(result);//string ifadeye çevirdik
                JSONArray jsonDizi = jsonNesne.getJSONArray("weather");//şimdi jsona bakarsanız weather isimli bir dizi var o diziyi aldık
                JSONObject jsonNesne_hava = jsonDizi.getJSONObject(0);//ilk indexi aldık
                result_main = jsonNesne_hava.getString("main");//ilk indexin main adlı değişkenini çektik
                result_aciklama = jsonNesne_hava.getString("description");
                result_icon = jsonNesne_hava.getString("icon");//tek tek işimize yarayacakları aldık

                JSONObject jsonNesne_main = jsonNesne.getJSONObject("main");//main diye bir değişken var onuda aldık
                Double temp = jsonNesne_main.getDouble("temp");//main'in içinden sıcaklığı aldık

                JSONObject jsonNesne_sys = jsonNesne.getJSONObject("sys");//sys isimli degiskeni aldık
                result_ulke=jsonNesne_sys.getString("country");//sys icerisindeki ülke ismini aldık


                result_city = jsonNesne.getString("name");//en sondaki kısımdan city ismini aldık

                result_temp = (int) (temp-273);//Kelvin olduğu için Celcius'a çevirdik


                URL icon_url = new URL("http://openweathermap.org/img/w/"+result_icon+".png");//resimde saklıyor api adresimiz
                bitImage = BitmapFactory.decodeStream(icon_url.openConnection().getInputStream());//Android'de image olarak kullanamadığımız için bitmap formatına çevirdik

                if(result_main .equals("Clouds")) {
                    result_main = ("Bulutlu");
                }
                if (result_main .equals("Rain")){
                    result_main="Yağmurlu";
                }
                if (result_main .equals("Sun")){
                    result_main="Güneşli";
                }
                if (result_main .equals("Fog")){
                    result_main="Sisli";
                }
                if (result_main .equals("Clear")){
                    result_main="Açık";
                }
                if (result_main .equals("Haze")){
                    result_main="Sisli";
                }
                if (result_main .equals("Thunderstorm")){
                    result_main="Yıldırımlı";
                }
                city=result_city;
                isi=Integer.toString(result_temp);
                main=result_main;
                ulk=result_ulke;
                ikon=result_icon;
                if(city.matches("") || isi.matches("") || main.matches("") || ulk.matches("") || ikon.matches("") ){

                }
                else{
                    VeriTabani db2 = new VeriTabani(getApplicationContext());
                    db2.sehirGuncelle(city, isi, main, ulk,ikon);
                    db2.close();
                }


            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(result_temp);

            Toast.makeText(getApplicationContext(), result_city+" Başarıyla Güncellendi.", Toast.LENGTH_SHORT).show();




        }
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
