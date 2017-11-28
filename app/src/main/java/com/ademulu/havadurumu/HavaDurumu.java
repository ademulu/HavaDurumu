package com.ademulu.havadurumu;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.widget.ImageButton;
import android.widget.Toast;
import android.speech.tts.TextToSpeech;
import android.util.Log;

public class HavaDurumu extends AppCompatActivity {


    public TextView txtSehir, txtSicaklik, txtDurum,txtUlke,txtIkon;
    public TextView Sehir, Sicaklik, Durum,Ulke;
    public EditText etGiris;

    private ImageView icon;
    private Button goster,islem,listele;
    String sehir,ad,isi,durum,ulke,ikon;
    String isim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hava_durumu);

        txtSehir = (TextView) findViewById(R.id.tvIsim);
        txtSicaklik = (TextView) findViewById(R.id.tvSicaklik);
        txtDurum = (TextView) findViewById(R.id.tvDurum);
        txtUlke=(TextView)findViewById(R.id.tvUlke);
        txtIkon=(TextView)findViewById(R.id.tvIcon);

        Sehir = (TextView) findViewById(R.id.Isim);
        Sicaklik = (TextView) findViewById(R.id.Sicaklik);
        Durum = (TextView) findViewById(R.id.Durum);
        Ulke = (TextView) findViewById(R.id.Ulke);

        etGiris = (EditText) findViewById(R.id.etGiris);

        icon = (ImageView) findViewById(R.id.imgIcon);

        islem=(Button)findViewById(R.id.btnIslem);
        goster=(Button)findViewById(R.id.btnGoster);
        listele=(Button)findViewById(R.id.btnListe);

        Bundle extras=getIntent().getExtras();
        if(extras == null) {
            isim = "";
        }else {
            isim=extras.getString("isim");
        }
        etGiris.setText(isim);


        goster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JsonParse jsonParse = new JsonParse();
                sehir = String.valueOf(etGiris.getText());//edit textten veriyi sehir adlı değişkene atadık
                new JsonParse().execute();//jsonParse AsynTask metodumuzu çalıştırdık.
            }
        });

        listele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intAdem = new Intent(HavaDurumu.this, ListeEkrani.class);
                startActivity(intAdem);
            }
        });

        islem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sehir = String.valueOf(etGiris.getText());
                Intent intAdem = new Intent(HavaDurumu.this, IslemEkrani.class);
                //Gönderilecek veriler:Sehir, Sicaklik, Durum,Ulke,İkon
                intAdem.putExtra("isim",sehir);
                intAdem.putExtra("isi",isi);
                intAdem.putExtra("durum",durum);
                intAdem.putExtra("ulke",ulke);
                intAdem.putExtra("ikon",ikon);
                startActivity(intAdem);
            }
        });

    }

    protected class JsonParse extends AsyncTask<Void, Void, Void>{
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
                URL hava_url = new URL("http://api.openweathermap.org/data/2.5/weather?q="+sehir+"&appid=cda57a3812f7e95994a604fff6ff2431");

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

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Sicaklik.setText(String.valueOf(result_temp));
            isi=String.valueOf(result_temp);
            StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(result_temp);


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

            Durum.setText(result_main);
            durum=String.valueOf(result_main);

            Sehir.setText(result_city);
            ad=String.valueOf(result_city);

            Ulke.setText(result_ulke);
            ulke=String.valueOf(result_ulke);

            icon.setImageBitmap(bitImage);
            ikon=String.valueOf(result_icon);

        }
    }

}

