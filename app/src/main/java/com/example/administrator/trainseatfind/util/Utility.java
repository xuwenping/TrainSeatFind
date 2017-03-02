package com.example.administrator.trainseatfind.util;

import android.text.TextUtils;
import android.util.Log;

import com.example.administrator.trainseatfind.model.Train;
import com.example.administrator.trainseatfind.model.TrainDB;
import com.example.administrator.trainseatfind.model.TrainTypeDefine;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;

/**
 * Created by Administrator on 2017/3/1.
 */
public class Utility {

    private static final String TAG = "Utility";

    public synchronized static boolean handleTrainResponseDeriveXml(TrainDB trainDB, String response) {
        Log.d(TAG, "the response is "+response);
        if (!TextUtils.isEmpty(response)) {
            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser xmlPullParser = factory.newPullParser();
                xmlPullParser.setInput(new StringReader(response));

                boolean bNeedProcessTable = false;

                int trainType = TrainTypeDefine.TRAINTYPE_UNDEFINE;

                int eventType = xmlPullParser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    String name = xmlPullParser.getName();
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            Log.d(TAG, "Start Parser");
                            break;
                        case XmlPullParser.START_TAG:

                            if ("h1".equals(name)) {
                                String str = xmlPullParser.nextText();
                                if (str != null) {
                                    if (str.indexOf("普客车次列表") != -1) {
                                        trainType = TrainTypeDefine.TRAINTYPE_GENERAL;
                                    }
                                    else if (str.indexOf("普快车次列表") != -1) {
                                        trainType = TrainTypeDefine.TRAINTYPE_GENERAL_SPEED;
                                    }
                                    else if (str.indexOf("特快车次列表") != -1) {
                                        trainType = TrainTypeDefine.TRAINTYPE_SPECIAL_SPEED;
                                    }
                                    else if (str.indexOf("直达特快车次列表") != -1) {
                                        trainType = TrainTypeDefine.TRAINTYPE_DIRECT_SPECIAL_SPEED;
                                    }
                                    else if (str.indexOf("快速火车车次列表") != -1) {
                                        trainType = TrainTypeDefine.TRAINTYPE_SPEED;
                                    }
                                    else if (str.indexOf("高铁车次列表") != -1) {
                                        trainType = TrainTypeDefine.TRAINTYPE_HIGH_SPEED;
                                    }
                                    else if (str.indexOf("动车车次列表") != -1) {
                                        trainType = TrainTypeDefine.TRAINTYPE_BULLET;
                                    }
                                }
                            }

                            if ("table".equals(name)
                                    && "BORDER-COLLAPSE: collapse".equals(xmlPullParser.getAttributeValue(null, "style"))) {
                                bNeedProcessTable = true;
                            }

                            if (bNeedProcessTable) {
                                if ("a".equals(name)) {
                                    Train train = new Train();
                                    train.setTrainNo(xmlPullParser.nextText());
                                    train.setReference(xmlPullParser.getAttributeValue("", "href"));
                                    train.setTrainType(trainType);
                                    Log.d(TAG, train.toString());
                                    trainDB.saveTrain(train);
                                }
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            if ("table".equals(name)
                                    && bNeedProcessTable) {
                                bNeedProcessTable = false;
                            }
                            break;
                    }

                    eventType = xmlPullParser.next();
                }

                return true;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    public synchronized static boolean handleTrainResponseDeriveHtml(TrainDB trainDB, String response) {

        try {
            int trainType = TrainTypeDefine.TRAINTYPE_UNDEFINE;
            Document doc = Jsoup.parse(response);

            /**
             * 获取列车的类型
             */
            Elements h1Elements = doc.select("h1");
            for (Element element :h1Elements) {
                String str = element.text();
                Log.d(TAG, "str is " + str);
                if (str != null) {
                    if (str.indexOf("普客车次列表") != -1) {
                        trainType = TrainTypeDefine.TRAINTYPE_GENERAL;
                    }
                    else if (str.indexOf("普快车次列表") != -1) {
                        trainType = TrainTypeDefine.TRAINTYPE_GENERAL_SPEED;
                    }
                    else if (str.indexOf("特快车次列表") != -1) {
                        trainType = TrainTypeDefine.TRAINTYPE_SPECIAL_SPEED;
                    }
                    else if (str.indexOf("直达特快车次列表") != -1) {
                        trainType = TrainTypeDefine.TRAINTYPE_DIRECT_SPECIAL_SPEED;
                    }
                    else if (str.indexOf("快速火车车次列表") != -1) {
                        trainType = TrainTypeDefine.TRAINTYPE_SPEED;
                    }
                    else if (str.indexOf("高铁车次列表") != -1) {
                        trainType = TrainTypeDefine.TRAINTYPE_HIGH_SPEED;
                    }
                    else if (str.indexOf("动车车次列表") != -1) {
                        trainType = TrainTypeDefine.TRAINTYPE_BULLET;
                    }
                }
            }

            /**
             * 通过select选择包含车辆信息的节点
             */
            Elements elements = doc.select("a[href~=/checi/((\\d)*\\.|((\\d)+-(\\d)+\\.))htm]");
            for (Element element :elements) {
                Train train = new Train();
                train.setTrainNo(element.text());
                train.setReference(element.attr("href"));
                train.setTrainType(trainType);
                Log.d(TAG, train.toString());
                //trainDB.saveTrain(train);
            }

            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
