package com.example.tc.miniweather;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.edu.pku.tc.bean.TodayWeather;

/**
 * Created by 18012 on 2019/1/20.
 */
public class Location extends AppCompatActivity {

    public LocationClient mLocationClient;

    private TextView positionText;

    private String country;
    private String province;
    private String city;
    private String district;

    private TextView test_text;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());

        setContentView(R.layout.activity_main);
        positionText = (TextView)findViewById(R.id.position_text_view);
        List<String> permissionList = new ArrayList<>();
        if(ContextCompat.checkSelfPermission(Location.this, Manifest.permission.
                ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(Location.this, Manifest.permission.
                READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if(ContextCompat.checkSelfPermission(Location.this, Manifest.permission.
                WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(!permissionList.isEmpty()){
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(Location.this, permissions, 1);
        }else{
            requestLocation();
        }

    }

    private void requestLocation(){
        initLocation();
        mLocationClient.start();
    }

    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(2000);
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        mLocationClient.setLocOption(option);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mLocationClient.stop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch(requestCode){
            case 1:
                if(grantResults.length > 0){
                    for(int result: grantResults){
                        if(result != PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this, "必须同意所有权限才能使用本程序", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                }else{
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    public class MyLocationListener implements BDLocationListener{

        @Override
        public void onReceiveLocation(final BDLocation location){
            Log.d("MyLocationListener", "此方法回调了");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    StringBuilder currentPosition = new StringBuilder();
                    currentPosition.append("纬度: ").append(location.getLatitude()).append("\n");
                    currentPosition.append("经度: ").append(location.getLongitude()).append("\n");
                    currentPosition.append("定位方式: ");
                    if (location.getLocType() == BDLocation.TypeGpsLocation) {
                        currentPosition.append("GPS");
                    } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                        currentPosition.append("网络");
                    }
                    currentPosition.append("\n");
//                    String latitude = "" + location.getLatitude();
//                    String longitude = "" + location.getLongitude();
//                    getAddress(latitude, longitude);
                    currentPosition.append("国家").append(location.getCountry()).append("\n");
                    currentPosition.append("省").append(location.getProvince()).append("\n");

                    positionText.setText(currentPosition);
                }
            });
        }
    }

//    public void getAddress(String lat, String log){
            // log lat
            // 参数解释: 纬度,经度 type 001 (100代表道路，010代表POI，001代表门址，111可以同时显示前三项)
//            String ak = "C2ayGoeTG7wdZOAYEnNxHF6cgt58Gb6Y";
//            final String address = "https://api.map.baidu.com/geocoder/v2/?"+"callback=renderReverse&location="+
//                lat + "," + log + "&output=json&pois=1&ak=" + ak;
//            String res = "";
//            try {
//                URL url = new URL(address);
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                conn.setDoOutput(true);
//                conn.setRequestMethod("POST");
//                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
//                String line;
//                while ((line = in.readLine()) != null) {
//                    res += line + "\n";
//                }
//                in.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        test_text = (TextView)findViewById(R.id.test_text);
//        test_text.setText(res);
//    }

    public void getAddress(String latitude, String longitude){
        test_text = (TextView)findViewById(R.id.test_text);
        String ak = "6CHtnUkBKseMtMGYVmTSkLlhUPg9xcNS";
        final String address = "https://api.map.baidu.com/geocoder/v2/?"+ "ak=" + ak +
                "output=" + "json" + "location=" + latitude + "," + longitude + "&output=json&pois=1&ak=";
        Log.d("myWeather", address);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection con = null;
                try {
                    URL url = new URL(address);
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    con.setConnectTimeout(8000);
                    con.setReadTimeout(8000);
                    InputStream in = con.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String str;
                    while ((str = reader.readLine()) != null) {
                        response.append(str);
                        Log.d("Location", str);
                    }
                    String responseStr = response.toString();
                    Log.d("Location", responseStr);
                    test_text.setText(responseStr);

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (con != null) {
                        con.disconnect();
                    }
                }
            }
        }).start();
    }
}
