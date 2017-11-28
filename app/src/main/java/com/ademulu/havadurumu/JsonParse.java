package com.ademulu.havadurumu;

/**
 * Created by Adem Ulu on 25.11.2017.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;


public class JsonParse extends AsyncTask<Void ,Void, Void>{

    String sehir ="";
    String result = "";
    Hava hava = new Hava();
    @Override
    protected Void doInBackground(Void... params) {

        try {
            URL weather_url = new URL("http://api.openweathermap.org/data/2.5/weather?q="+sehir+"&appid=cda57a3812f7e95994a604fff6ff2431");
            BufferedReader  bufferedReader = null;
            bufferedReader = new BufferedReader(new InputStreamReader(weather_url.openStream()));//url'yi okuyacak bufferReader'a gönderdik
            String line = null;
            while((line = bufferedReader.readLine()) != null){//satırları tek tek aldık ve ekledik
                result += line;
            }
            bufferedReader.close();

            JSONObject jsonNesne = new JSONObject(result);//string ifadeye çevirdik
            JSONArray jsonDizi = jsonNesne.getJSONArray("weather");//şimdi jsona bakarsanız weather isimli bir dizi var o diziyi aldık
            JSONObject jsonNesne_hava = jsonDizi.getJSONObject(0);//ilk indexi aldık
            String result_main = jsonNesne_hava.getString("main");//ilk indexin main adlı değişkenini çektik
            String result_description = jsonNesne_hava.getString("description");
            String result_icon = jsonNesne_hava.getString("icon");//tek tek işimize yarayacakları aldık

            JSONObject jsonObject_main = jsonNesne.getJSONObject("main");//main diye bir değişken var onuda aldık
            Double temp = jsonObject_main.getDouble("temp");//main'in içinden sıcaklığı aldık,double olarak

            String result_city = jsonNesne.getString("city");//en sondaki kısımdan city ismini aldık

            int result_temp = (int) (temp-273);//Kelvin olduğu için Celcius'a çevirdik

            URL icon_url = new URL("http://openweathermap.org/img/w/"+result_icon+".png");//api içerisinde resimde bulunmaktadır.
            Bitmap bitImage = BitmapFactory.decodeStream(icon_url.openConnection().getInputStream());//Android'de image olarak kullanamadığımız için bitmap formatına çevirdik

            hava.setCity(result_city);
            hava.setAciklama(result_description);
            hava.setImage(bitImage);
            hava.setTemperature(result_temp);
            hava.setWeather(result_main);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {

        super.onPostExecute(result);
    }
}
