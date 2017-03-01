package com.example.administrator.trainseatfind.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.administrator.trainseatfind.db.TrainOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/28.
 */
public class TrainDB {

    /**
     * 数据库名
     */
    public static final String DB_NAME = "train";

    /**
     * 数据库版本
     */
    public static final int VERSION = 1;

    private static TrainDB trainDB;

    private SQLiteDatabase db;

    /**
     * 将构造函数私有化
     */
    private TrainDB(Context context) {
        TrainOpenHelper dbHelper = new TrainOpenHelper(context, DB_NAME, null, VERSION);
        db = dbHelper.getWritableDatabase();
    }

    /**
     * 获取TrainDB的实例
     */
    public synchronized static TrainDB getInstance(Context context) {
        if (trainDB == null) {
            trainDB = new TrainDB(context);
        }

        return trainDB;
    }

    /**
     * 将train实例存储在数据库中
     * @param train
     */
    public void saveTrain(Train train) {
        if (train != null) {
            ContentValues values = new ContentValues();
            values.put("train_no", train.getTrainNo());
            values.put("train_type", train.getTrainType());
            values.put("reference", train.getReference());
            db.insert("Train", null, values);
        }
    }

    public List<Train> loadTrain(String[] selectionArray) {
        List<Train> list = new ArrayList<Train>();
        Cursor cursor = null;
        if (selectionArray == null) {
            cursor = db.query("Train", null, null, null, null, null, null);
        }
        else {
            cursor = db.rawQuery("select * from Train where train_no=?", selectionArray);
        }

        if (cursor.moveToFirst()) {
            do {
                Train train = new Train();
                train.setId(cursor.getInt(cursor.getColumnIndex("id")));
                train.setTrainNo(cursor.getString(cursor.getColumnIndex("train_no")));
                train.setTrainType(cursor.getInt(cursor.getColumnIndex("train_type")));
                train.setReference(cursor.getString(cursor.getColumnIndex("reference")));
                list.add(train);
            } while (cursor.moveToNext());
        }

        return list;
    }
}
