package com.example.tc.miniweather;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public LocationClient mLocationClient;
    private TextView positionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        setContentView(R.layout.layout_main);

        positionText = (TextView)findViewById(R.id.position_text_view);
        List<String> permissionList = new ArrayList<>();
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.
                permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.
                permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.
                permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if(!permissionList.isEmpty()){
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
        }else{
            requestLocation();
        }
    }

    private void initLocation(){
        LocationClientOption option;// = new LocationClientOption();
        option = mLocationClient.getLocOption();
        option.setScanSpan(2000);
        option.setIsNeedAddress(true);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        mLocationClient.setLocOption(option);
    }

    private void requestLocation(){
        initLocation();
        mLocationClient.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case 1:
                if(grantResults.length > 0){
                    for(int result: grantResults){
                        if(result != PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this, "必须同意所有权限才能使用本程序",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                }else{
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
    }

    public class MyLocationListener extends BDAbstractLocationListener{
        @Override
        public void onReceiveLocation(final BDLocation location) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    StringBuilder currentPosition = new StringBuilder();
                    currentPosition.append("纬度: ").append(location.getLatitude()).append("\n");
                    currentPosition.append("经线: ").append(location.getLongitude()).append("\n");
                    currentPosition.append("国家: ").append(location.getCountry()).append("\n");
                    currentPosition.append("省份: ").append(location.getProvince()).append("\n");
                    currentPosition.append("城市: ").append(location.getCity()).append("\n");
                    currentPosition.append("区: ").append(location.getDistrict()).append("\n");
                    currentPosition.append("街道: ").append(location.getStreet()).append("\n");
                    positionText.setText(currentPosition);
                }
            });
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i){

        }
    }
}

//import android.Manifest;
//import android.app.Activity;
//import android.app.Application;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.content.pm.PackageManager;
//import android.location.LocationManager;
//import android.net.Uri;
//import android.os.Message;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import org.xmlpull.v1.XmlPullParser;
//import org.xmlpull.v1.XmlPullParserException;
//import org.xmlpull.v1.XmlPullParserFactory;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.StringReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.List;
//
//import android.os.Handler;
//
//import com.baidu.location.BDAbstractLocationListener;
//import com.baidu.location.BDLocation;
//import com.baidu.location.BDLocationListener;
//import com.baidu.location.LocationClient;
//import com.baidu.location.LocationClientOption;
//import com.baidu.mapapi.SDKInitializer;
//
//import cn.edu.pku.tc.app.MyApplication;
//import cn.edu.pku.tc.bean.City;
//import cn.edu.pku.tc.bean.TodayWeather;
//import cn.edu.pku.tc.util.NetUtil;

//public class MainActivity extends AppCompatActivity{ //} implements View.OnClickListener {
//
////    private static final int UPDATE_TODAY_WEATHER = 1;
////
////    private ImageView mUpdateBtn;
////
////    private ImageView mCitySelect;
////
////    private TextView cityTv, timeTv, humidityTv, weekTv, pmDataTv, pmQualityTv,
////            temperatureTv, climateTv, windTv, city_name_Tv, current_temperatureTv;
////    private ImageView weatherImg, pmImg;
////
////    private ProgressBar progressBar;
////
////
//    public LocationClient mLocationClient;
//    private TextView positionText;
////
////    String locationCity, locationCode;
////
////    private Handler mHandler = new Handler() {
////        public void handleMessage(Message msg) {
////            switch (msg.what) {
////                case UPDATE_TODAY_WEATHER:
////                    updateTodayWeather((TodayWeather) msg.obj);
////                    break;
////                default:
////                    break;
////            }
////        }
////    };
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mLocationClient = new LocationClient(getApplicationContext());
//        mLocationClient.registerLocationListener(new MyLocationListener());
//        setContentView(R.layout.weather_info);
//
//        positionText = (TextView)findViewById(R.id.position_text_view);
//        List<String> permissionList = new ArrayList<>();
//        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.
//                permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
//            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
//        }
//        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.
//                permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
//            permissionList.add(Manifest.permission.READ_PHONE_STATE);
//        }
//        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.
//                permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
//            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        }
//
//        if(!permissionList.isEmpty()){
//            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
//            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
//        }else{
//            requestLocation();
//        }
//
////        mUpdateBtn = (ImageView) findViewById(R.id.title_update_btn);
////        mUpdateBtn.setOnClickListener(this);
////
////        progressBar = (ProgressBar) findViewById(R.id.title_update_progess);
////
////        if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
////            Log.d("myWeather", "网络OK");
////            Toast.makeText(MainActivity.this, "网络OK!", Toast.LENGTH_LONG).show();
////        } else {
////            Log.d("myWeather", "网络挂了");
////            Toast.makeText(MainActivity.this, "网络挂了", Toast.LENGTH_LONG).show();
////        }
////
////        mCitySelect = (ImageView) findViewById(R.id.title_city_manager);
////        mCitySelect.setOnClickListener(this);
//
////        initView();
//
////        ImageView locateBtn = (ImageView) findViewById(R.id.title_location);
////        locateBtn.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////
////            }
////        });
//    }
//
//    private void initLocation(){
//        LocationClientOption option = new LocationClientOption();
//        option.setIsNeedAddress(true);
//        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//        option.setScanSpan(2000);
//        mLocationClient.setLocOption(option);
//    }
//
//    private void requestLocation(){
//        initLocation();
//        mLocationClient.start();
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch(requestCode){
//            case 1:
//                if(grantResults.length > 0){
//                    for(int result: grantResults){
//                        if(result != PackageManager.PERMISSION_GRANTED){
//                            Toast.makeText(this, "必须同意所有权限才能使用本程序",
//                                    Toast.LENGTH_SHORT).show();
//                            finish();
//                            return;
//                        }
//                    }
//                    requestLocation();
//                }else{
//                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
//                    finish();
//                }
//                break;
//            default:
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mLocationClient.stop();
//    }
//
//    public class MyLocationListener extends BDAbstractLocationListener{
//        @Override
//        public void onReceiveLocation(final BDLocation location) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    String country = location.getCountry();
//                    Toast.makeText(MainActivity.this, "定位城市: " + country, Toast.LENGTH_SHORT).show();
//                    StringBuilder currentPosition = new StringBuilder();
//                    currentPosition.append("纬度: ").append(location.getLatitude()).append("\n");
//                    currentPosition.append("经线: ").append(location.getLongitude()).append("\n");
//                    currentPosition.append("国家: ").append(location.getCountry()).append("\n");
//                    currentPosition.append("省份: ").append(location.getProvince()).append("\n");
//                    currentPosition.append("城市: ").append(location.getCity()).append("\n");
//                    currentPosition.append("区: ").append(location.getDistrict()).append("\n");
//                    currentPosition.append("街道: ").append(location.getStreet()).append("\n");
//                    positionText.setText(currentPosition);
//                }
//            });
//        }
//
//        @Override
//        public void onConnectHotSpotMessage(String s, int i){
//
//        }
//    }


//    void initView() {
//        city_name_Tv = (TextView) findViewById(R.id.title_city_name);
//        cityTv = (TextView) findViewById(R.id.city);
//        timeTv = (TextView) findViewById(R.id.time);
//        humidityTv = (TextView) findViewById(R.id.humidity);
//        weekTv = (TextView) findViewById(R.id.week_today);
//        pmDataTv = (TextView) findViewById(R.id.pm_data);
//        pmQualityTv = (TextView) findViewById(R.id.pm2_5_quality);
//        pmImg = (ImageView) findViewById(R.id.pm2_5_img);
//        temperatureTv = (TextView) findViewById(R.id.temperature);
//        climateTv = (TextView) findViewById(R.id.climate);
//        windTv = (TextView) findViewById(R.id.wind);
//        weatherImg = (ImageView) findViewById(R.id.weather_img);
//        current_temperatureTv = (TextView) findViewById(R.id.current_temperature);
//
//        city_name_Tv.setText("N/A");
//        cityTv.setText("N/A");
//        timeTv.setText("N/A");
//        humidityTv.setText("N/A");
//        pmDataTv.setText("N/A");
//        pmQualityTv.setText("N/A");
//        weekTv.setText("N/A");
//        temperatureTv.setText("N/A");
//        climateTv.setText("N/A");
//        windTv.setText("N/A");
//        current_temperatureTv.setText("N/A");
//    }
//
//    @Override
//    public void onClick(View view) {
//
//        if (view.getId() == R.id.title_city_manager) {
//            Toast.makeText(this, "单击了切换", Toast.LENGTH_SHORT).show();
//            Intent i = new Intent(this, SelectCity.class);
//            //startActivity(i);
//            startActivityForResult(i, 1);
//        }
//
//        if (view.getId() == R.id.title_update_btn) {
//            progressBar.setVisibility(View.VISIBLE);
//            mUpdateBtn.setVisibility(View.INVISIBLE);
//
//            SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
//            String cityCode = sharedPreferences.getString("main_city_code", "101010100");
//            Log.d("myWeather", cityCode);
//
//            if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
//                Log.d("myWeather", "网络OK");
//                queryWeatherCode(cityCode);
//            } else {
//                Log.d("myWeather", "网络挂了");
//                Toast.makeText(MainActivity.this, "网络挂了！", Toast.LENGTH_LONG).show();
//            }
//        }
//    }
//
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == 1 && resultCode == RESULT_OK) {
//            String newCityCode = data.getStringExtra("cityCode");
//            Log.d("myWeather", "选择的城市代码为" + newCityCode);
//
//            if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
//                Log.d("myWeather", "网络OK");
//                queryWeatherCode(newCityCode);
//            } else {
//                Log.d("myWeather", "网络挂了");
//                Toast.makeText(MainActivity.this, "网络挂了！", Toast.LENGTH_LONG).show();
//            }
//        }
//    }
//
//    private void queryWeatherCode(String cityCode) {
//        final String address = "http://wthrcdn.etouch.cn/WeatherApi?citykey=" + cityCode;
//        Log.d("myWeather", address);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                HttpURLConnection con = null;
//                TodayWeather todayWeather = null;
//                try {
//                    URL url = new URL(address);
//                    con = (HttpURLConnection) url.openConnection();
//                    con.setRequestMethod("GET");
//                    con.setConnectTimeout(8000);
//                    con.setReadTimeout(8000);
//                    InputStream in = con.getInputStream();
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//                    StringBuilder response = new StringBuilder();
//                    String str;
//                    while ((str = reader.readLine()) != null) {
//                        response.append(str);
//                        Log.d("myWeather", str);
//                    }
//                    String responseStr = response.toString();
//                    Log.d("myWeather", responseStr);
//
//                    todayWeather = parseXML(responseStr);
//                    if (todayWeather != null) {
//                        Log.d("myWeather", todayWeather.toString());
//
//                        Message msg = new Message();
//                        msg.what = UPDATE_TODAY_WEATHER;
//                        msg.obj = todayWeather;
//                        mHandler.sendMessage(msg);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//                    if (con != null) {
//                        con.disconnect();
//                    }
//                }
//            }
//        }).start();
//    }
//
//    private TodayWeather parseXML(String xmldata) {
//        TodayWeather todayWeather = null;
//        int fengxiangCount = 0;
//        int fengliCount = 0;
//        int dateCount = 0;
//        int highCount = 0;
//        int lowCount = 0;
//        int typeCount = 0;
//
//        try {
//            XmlPullParserFactory fac = XmlPullParserFactory.newInstance();
//            XmlPullParser xmlPullParser = fac.newPullParser();
//            xmlPullParser.setInput(new StringReader(xmldata));
//            int eventType = xmlPullParser.getEventType();
//            Log.d("myWeather", "parseXML");
//            while (eventType != XmlPullParser.END_DOCUMENT) {
//                switch (eventType) {
//                    //判断当前事件是否为文档开始事件
//                    case XmlPullParser.START_DOCUMENT:
//                        break;
//                    //判断当前事件是否为标签元素开始事件
//                    case XmlPullParser.START_TAG:
//                        if (xmlPullParser.getName().equals("resp")) {
//                            todayWeather = new TodayWeather();
//                        }
//                        if (todayWeather != null) {
//                            if (xmlPullParser.getName().equals("city")) {
//                                eventType = xmlPullParser.next();
//                                todayWeather.setCity(xmlPullParser.getText());
//                            } else if (xmlPullParser.getName().equals("updatetime")) {
//                                eventType = xmlPullParser.next();
//                                todayWeather.setUpdatetime(xmlPullParser.getText());
//                            } else if (xmlPullParser.getName().equals("shidu")) {
//                                eventType = xmlPullParser.next();
//                                todayWeather.setShidu(xmlPullParser.getText());
//                            } else if (xmlPullParser.getName().equals("wendu")) {
//                                eventType = xmlPullParser.next();
//                                todayWeather.setWendu(xmlPullParser.getText());
//                            } else if (xmlPullParser.getName().equals("pm25")) {
//                                eventType = xmlPullParser.next();
//                                todayWeather.setPm25(xmlPullParser.getText());
//                            } else if (xmlPullParser.getName().equals("quality")) {
//                                eventType = xmlPullParser.next();
//                                todayWeather.setQuality(xmlPullParser.getText());
//                            } else if (xmlPullParser.getName().equals("fengxiang") && fengxiangCount == 0) {
//                                eventType = xmlPullParser.next();
//                                todayWeather.setFengxiang(xmlPullParser.getText());
//                                fengxiangCount++;
//                            } else if (xmlPullParser.getName().equals("fengli") && fengliCount == 0) {
//                                eventType = xmlPullParser.next();
//                                todayWeather.setFengli(xmlPullParser.getText());
//                                fengliCount++;
//                            } else if (xmlPullParser.getName().equals("date") && dateCount == 0) {
//                                eventType = xmlPullParser.next();
//                                todayWeather.setDate(xmlPullParser.getText());
//                                dateCount++;
//                            } else if (xmlPullParser.getName().equals("high") && highCount == 0) {
//                                eventType = xmlPullParser.next();
//                                todayWeather.setHigh(xmlPullParser.getText().substring(2).trim());
//                                highCount++;
//                            } else if (xmlPullParser.getName().equals("low") && lowCount == 0) {
//                                eventType = xmlPullParser.next();
//                                todayWeather.setLow(xmlPullParser.getText().substring(2).trim());
//                                lowCount++;
//                            } else if (xmlPullParser.getName().equals("type") && typeCount == 0) {
//                                eventType = xmlPullParser.next();
//                                todayWeather.setType(xmlPullParser.getText());
//                                typeCount++;
//                            }
//                        }
//                        break;
//                    //判断当前事件是否为标签元素结束事件
//                    case XmlPullParser.END_TAG:
//                        break;
//                }
//                //进入下一个元素并触发相应事件
//                eventType = xmlPullParser.next();
//            }
//        } catch (XmlPullParserException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return todayWeather;
//    }
//
//    void updateTodayWeather(TodayWeather todayWeather) {
//        city_name_Tv.setText(todayWeather.getCity() + "天气");
//        cityTv.setText(todayWeather.getCity());
//        timeTv.setText(todayWeather.getUpdatetime() + "发布");
//        humidityTv.setText("湿度: " + todayWeather.getShidu());
//        pmDataTv.setText(todayWeather.getPm25());
//        pmQualityTv.setText(todayWeather.getQuality());
//        weekTv.setText(todayWeather.getDate());
//        temperatureTv.setText(todayWeather.getHigh() + "~" + todayWeather.getLow());
//        climateTv.setText(todayWeather.getType());
//        windTv.setText("风力: " + todayWeather.getFengli());
//        current_temperatureTv.setText("当前温度: " + todayWeather.getWendu() + "℃");
//        Toast.makeText(MainActivity.this, "更新成功！", Toast.LENGTH_SHORT).show();
//
//        progressBar.setVisibility(View.INVISIBLE);
//        mUpdateBtn.setVisibility(View.VISIBLE);
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//    }
//}