package com.ddoniddoi.lotto.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ddoniddoi.lotto.Entity.LottoNumber;

public class LottoNumberHelper extends SQLiteOpenHelper {

    String TABLE_NAME = "LOTTONUMBER";
    SQLiteDatabase db;

    public LottoNumberHelper(Context context, String DATABASE_NAME) {
        super(context, DATABASE_NAME, null, 1);
        db = getReadableDatabase();

    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(LottoNumber lottoNumber) {


        try {
            db.beginTransaction();
            ContentValues contentValues = new ContentValues();
            contentValues.put("DRWNODATE", lottoNumber.getDrwNoDate());
            contentValues.put("BNUSNO", lottoNumber.getBnusNo());
            contentValues.put("DRWNO",  lottoNumber.getDrwNo());
            contentValues.put("DRWTNO1", lottoNumber.getDrwtNo1());
            contentValues.put("DRWTNO2", lottoNumber.getDrwtNo2());
            contentValues.put("DRWTNO3", lottoNumber.getDrwtNo3());
            contentValues.put("DRWTNO4", lottoNumber.getDrwtNo4());
            contentValues.put("DRWTNO5", lottoNumber.getDrwtNo5());
            contentValues.put("DRWTNO6", lottoNumber.getDrwtNo6());


            Long id = db.insert(TABLE_NAME,null, contentValues);

            Log.d("DATA", "id: " + id);
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public String getLastNumber(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DRWNO FROM "+TABLE_NAME+" ORDER BY DRWNO LIMIT 1",null);
        int recordCount = cursor.getCount();
        if(recordCount < 1 ){
            return "0";
        }
        String lastNumber = "";
        for (int i = 0 ; i < cursor.getCount() ; i++){
            cursor.moveToNext();
            lastNumber = cursor.getString(cursor.getColumnIndex("DRWNO"));
            Log.i("data",lastNumber);
        }
        return lastNumber;
    }


}
