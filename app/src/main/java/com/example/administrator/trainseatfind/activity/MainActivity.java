package com.example.administrator.trainseatfind.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.administrator.trainseatfind.R;
import com.example.administrator.trainseatfind.model.Train;
import com.example.administrator.trainseatfind.model.TrainDB;
import com.example.administrator.trainseatfind.util.HttpCallbackListener;
import com.example.administrator.trainseatfind.util.HttpUtil;
import com.example.administrator.trainseatfind.util.SharepreferencesUtilSystemSettings;
import com.example.administrator.trainseatfind.util.Utility;

import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener{

    private static final class Address {
        public static final String address             = "http://www.59178.com/checi/";
    }

    private static final String TAG = "MainActivity";

    private TrainDB trainDB;

    private EditText trainNoEditText;

    private EditText trainSeatNoEditText;

    private Button startFindBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        trainDB = TrainDB.getInstance(this);

        SharedPreferences settingPrefs = getSharedPreferences("settingPrefs", MODE_PRIVATE);
        // 如果是第一次安装，初始化相关设置，如果不是首次安装，不更新配置
        if (!settingPrefs.getBoolean("first_instatll", false)) {
            SharepreferencesUtilSystemSettings.putValue(this, "first_instatll", true);
            initDB();
        }

        trainNoEditText = (EditText) findViewById(R.id.car_code);

        trainSeatNoEditText = (EditText) findViewById(R.id.seat_no);

        startFindBtn = (Button) findViewById(R.id.start_find);
        startFindBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_find:
                String str = trainNoEditText.getText().toString();
                String[] arrayStr = {str};
                List<Train> list = trainDB.loadTrain(arrayStr);
                Log.d(TAG, list.toString());
        }
    }

    private void initDB() {
        saveTrainInfoToDB(Address.address);
    }

    private void saveTrainInfoToDB(String address) {
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.d(TAG, "onFinish");
                Utility.handleTrainResponseDeriveHtml(trainDB, response);
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
}
