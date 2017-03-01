package com.example.administrator.trainseatfind.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/2/28.
 */
public class TrainOpenHelper extends SQLiteOpenHelper {

    public static final String CREATE_TRAIN = "create table Train (" +
            "id integer primary key autoincrement, " +
            "train_no text, " +
            "train_type integer, " +
            "reference text)";

    public TrainOpenHelper(Context context,
                           String dbName,
                           SQLiteDatabase.CursorFactory factory,
                           int version) {
        super(context, dbName, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TRAIN);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
