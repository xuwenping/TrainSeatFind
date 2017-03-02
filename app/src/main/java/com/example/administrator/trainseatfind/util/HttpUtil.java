package com.example.administrator.trainseatfind.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/1/24.
 */
public class HttpUtil {

    private static final String TAG = "HttpUtil";

    public static void sendHttpRequest(final String address,
                                       final HttpCallbackListener listener) {
        Log.d(TAG, "sendHttpRequest");
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);

                    if (connection.getResponseCode() == 200) {
                        InputStream in = connection.getInputStream();
                        String charset = "gb2312";
                        Pattern pattern = Pattern.compile("charset=\\S*");

                        Matcher matcher  = pattern.matcher(connection.getContentType());
                        if (matcher.find()) {
                            charset = matcher.group().replace("charset=", "");
                            Log.d(TAG, "the charset is " + charset);
                        }
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in, charset));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        in.close();
                        if (listener != null) {
                            listener.onFinish(response.toString());
                        }
                    }


                } catch (Exception e) {
                    if (listener != null) {
                        listener.onError(e);
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
}
