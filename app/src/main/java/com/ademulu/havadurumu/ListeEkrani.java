package com.ademulu.havadurumu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Adem Ulu on 26.11.2017.
 */

public class ListeEkrani extends Activity {

    Button btn;
    int sayac=0;
    ListView lvSehir;
    String sehir_adlari[],sehir_ad[];
    ArrayAdapter<String> adapter;
    ArrayList<HashMap<String, String>> sehir_liste;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.liste_ekrani);
        btn=(Button)findViewById(R.id.btnDegis);


        lvSehir = (ListView) findViewById(R.id.list_view);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int j=0;
                for(int i=0;i<sehir_liste.size();i++){

                        if(sayac<=i && i<sayac+3)
                        {
                            sehir_adlari[j] = sehir_liste.get(i).get("sehir_adi");
                            j++;
                        }
                        else{
                            sehir_adlari[i]="";
                        }
                }

                sayac=sayac+3;
                sayac=sayac%sehir_liste.size();
                adapter.notifyDataSetChanged();
                lvSehir.setAdapter(adapter);

            }

        });

    }

    @Override
    public void onResume(){
        super.onResume();
        VeriTabani db = new VeriTabani(getApplicationContext());
        sehir_liste = db.sehirler();
        if(sehir_liste.size()==0){
            Toast.makeText(getApplicationContext(), "Henüz Sehir Eklenmemiş.", Toast.LENGTH_LONG).show();
        }
        else{
            sehir_adlari = new String[sehir_liste.size()];

            for(int i=0;i<sehir_liste.size();i++){
                sehir_adlari[i] = sehir_liste.get(i).get("sehir_adi");
            }
            lvSehir = (ListView) findViewById(R.id.list_view);
            adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.sehir_adi, sehir_adlari);
            lvSehir.setAdapter(adapter);


            lvSehir.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent intAdem = new Intent(ListeEkrani.this, HavaDurumu.class);
                    intAdem.putExtra("isim", sehir_adlari[position]);
                    startActivity(intAdem);
                }
            });

        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        return super.onOptionsItemSelected(item);
        }
}
