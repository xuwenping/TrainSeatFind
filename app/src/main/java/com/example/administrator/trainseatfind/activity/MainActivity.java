package com.example.administrator.trainseatfind.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.administrator.trainseatfind.R;
import com.example.administrator.trainseatfind.model.TrainDB;
import com.example.administrator.trainseatfind.util.HttpCallbackListener;
import com.example.administrator.trainseatfind.util.HttpUtil;
import com.example.administrator.trainseatfind.util.Utility;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    private TrainDB trainDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        trainDB = TrainDB.getInstance(this);

        Button button = (Button) findViewById(R.id.start_find);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HttpUtil.sendHttpRequest("http://www.59178.com/huoche/Puke.htm", new HttpCallbackListener() {
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
        });
    }
}
