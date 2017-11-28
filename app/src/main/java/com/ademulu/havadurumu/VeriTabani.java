package com.ademulu.havadurumu;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.CompoundButton;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Adem Ulu on 26.11.2017.
 */

public class VeriTabani extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="sqlite_veritabani";

    private static final String TABLE_NAME="sehir_listesi";
    private static String SEHIR_ADI="sehir_adi";
    private static String SEHIR_SICAKLIK="sehir_sicaklik";
    private static String SEHIR_DURUM="sehir_durum";
    private static String SEHIR_ULKE="sehir_ulke";
    private static String SEHIR_ICON="sehir_icon";

    public VeriTabani(Context context){
        super (context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db){

        String CREATE_TABLE="CREATE TABLE " + TABLE_NAME + "("
                + SEHIR_ADI +" TEXT PRIMARY KEY,"
                + SEHIR_SICAKLIK +" TEXT,"
                + SEHIR_DURUM +" TEXT,"
                + SEHIR_ULKE +" TEXT,"
                + SEHIR_ICON +" TEXT " + ")";
        db.execSQL(CREATE_TABLE);
    }
    public void sehirEkle(String sehir_adi,String sehir_sicaklik,String sehir_durum,String sehir_ulke,String sehir_icon ){

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values =new ContentValues();

        values.put(SEHIR_ADI,sehir_adi);
        values.put(SEHIR_SICAKLIK,sehir_sicaklik);
        values.put(SEHIR_DURUM,sehir_durum);
        values.put(SEHIR_ULKE,sehir_ulke);
        values.put(SEHIR_ICON,sehir_icon);

        db.insert(TABLE_NAME,null,values);
        db.close();
    }
    public void sehirSil(String sehir_adi){

        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_NAME,SEHIR_ADI + "=?",
                new String[]{String.valueOf(sehir_adi)});
        db.close();
    }
    public HashMap<String,String> sehirDetay(String sehir_adi){

        HashMap<String,String> sehir=  new HashMap<String, String>();
        String selectQuery="SELECT * FROM "+ TABLE_NAME + " WHERE sehir_adi = "+"'" + sehir_adi+"'";

        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery(selectQuery,null);
        cursor.moveToFirst();

        if(cursor.getCount()>0){
            sehir.put(SEHIR_ADI, cursor.getString(0));
            sehir.put(SEHIR_SICAKLIK, cursor.getString(1));
            sehir.put(SEHIR_DURUM, cursor.getString(2));
            sehir.put(SEHIR_ULKE, cursor.getString(3));
            sehir.put(SEHIR_ICON, cursor.getString(4));
        }

        cursor.close();
        db.close();

        return sehir;
    }
    public ArrayList<HashMap<String,String>> sehirler(){

        SQLiteDatabase db=getReadableDatabase();
        String selectQuery="SELECT * FROM "+ TABLE_NAME;
        Cursor cursor=db.rawQuery(selectQuery,null);
        ArrayList<HashMap<String,String>> sehirlist=new ArrayList<HashMap<String, String>>();

        if (cursor.moveToFirst()){
            do{
                HashMap<String,String> map=new HashMap<String, String>();
                for (int i=0;i<cursor.getColumnCount();i++){
                    map.put(cursor.getColumnName(i),cursor.getString(i));
                }
                sehirlist.add(map);
            }while(cursor.moveToNext());
        }

        db.close();
        return sehirlist;
    }
    public void sehirGuncelle(String sehir_adi, String sehir_sicaklik, String sehir_durum, String sehir_ulke, String sehir_icon) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(SEHIR_SICAKLIK, sehir_sicaklik);
        values.put(SEHIR_DURUM, sehir_durum);
        values.put(SEHIR_ULKE, sehir_ulke);
        values.put(SEHIR_ICON,sehir_icon);

        db.update(TABLE_NAME, values, SEHIR_ADI + " = ?",
                new String[]{String.valueOf(sehir_adi)});
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub

    }
}