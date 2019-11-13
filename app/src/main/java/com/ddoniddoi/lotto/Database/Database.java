package com.ddoniddoi.lotto.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Database {

    Context context;
    //SQLite DB
    SQLiteDatabase db;


    //SQLite(DB 이름)
    String DATABASE_NAME = "LOTTO";


    public Database(Context context) {
        this.context = context;
    }

    //데이터베이스 접속 또는 만들기
    public void connectDatabase() {
        try {
            db = context.openOrCreateDatabase(DATABASE_NAME, context.MODE_PRIVATE, null);
            Log.i("Database", "OK");
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("Database", "NO");
        }
    }

    //데이터베이스 테이블 만들기
    public void createTable() {
        db.execSQL("CREATE TABLE IF NOT EXISTS LOTTONUMBER(DRWNO VARCHAR2(20) NOT NULL, DRWTNO1 VARCHAR2(2) NOT NULL, DRWTNO2 VARCHAR2(2) NOT NULL, DRWTNO3 VARCHAR2(2) NOT NULL, DRWTNO4 VARCHAR2(2) NOT NULL, DRWTNO5 VARCHAR2(2) NOT NULL, DRWTNO6 VARCHAR2(2) NOT NULL, BNUSNO VARCHAR2(2) NOT NULL, DRWNODATE VARCHAR2(2) NOT NULL);");
    }
}

