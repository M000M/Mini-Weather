package com.example.tc.miniweather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.pku.tc.app.MyApplication;
import cn.edu.pku.tc.bean.City;

/**
 * Created by 18012 on 2019/1/20.
 */
public class TestSelectCity extends Activity implements View.OnClickListener{

    //监听按钮
    private ImageView mBackBtn;
    private TextView mTextBtn;

    //搜索栏和ListView
    private ListView listView; //用于绑定select_city布局文件中的ListView
    private TextView cityselected; //声明TextView对象，用于绑定布局文件中顶部栏的显示内容

    private List<City> listcity = MyApplication.getInstance().getCityList();
    private int listSize = listcity.size();
    private String[] city = new String[listSize]; //用于存储要在ListView中展示的内容

    private ArrayList<String> mSearchResult = new ArrayList<>(); //搜索结果，只存放城市名
    private Map<String, String> nameToCode = new HashMap<>(); //城市名到编码
    private Map<String, String> nameToPinyin = new HashMap<>(); //城市名到拼音

    //搜索功能
    private EditText mSearch;

    String returnCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_city);

        //onClick()方法要在这里写绑定监听事件
        mBackBtn = (ImageView) findViewById(R.id.title_back);
        mBackBtn.setOnClickListener(this);


        mTextBtn = (TextView) findViewById(R.id.title_name);
        mTextBtn.setOnClickListener(this);
        cityselected = (TextView) findViewById(R.id.title_name);

        for(int i = 0; i < listSize; i++){
            city[i] = listcity.get(i).getCity();
            Log.d("City: ", city[i]);
        }
    //
//        //建立映射
//        String strName;
//        String strNamePinyin;
//        String strCode;
//
//        for(City city1: listcity){
//            strCode = city1.getNumber();
//            strName = city1.getCity();
//            strNamePinyin = city1.getFirstPY();
//            nameToCode.put(strName, strCode);
//            nameToPinyin.put(strName, strNamePinyin);
//            mSearchResult.add(strName);
//        }
//
//        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
//                this, android.R.layout.simple_list_item_single_choice, mSearchResult);
//        listView = (ListView) findViewById(R.id.list_view);
//        listView.setAdapter(arrayAdapter); //设置适配器
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                String returnCityName = mSearchResult.get(i);
//                Toast.makeText(
//                        TestSelectCity.this, "你已选择: " + returnCityName, Toast.LENGTH_SHORT).show();
//                cityselected.setText("当前城市: " + returnCityName);
//                returnCode = nameToCode.get(returnCityName);
//                Log.d("returnCityCode", returnCode);
//            }
//        });
//
//        mSearch = (EditText)findViewById(R.id.search_edit);
//        mSearch.addTextChangedListener(new TextWatcher(){
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after){
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count){
//                int temp = mSearchResult.size();
//                String str = Integer.toString(temp);
//                Log.d("选择前：", str);
//                arrayAdapter.getFilter().filter(s);
//                temp = mSearchResult.size();
//                str = Integer.toString(temp);
//                Log.d("选择后：", str);
////                String str = mSearch.getText().toString();
////                for(int i = 0; i < listSize; i++){
////                    if(city[i].indexOf(str) != -1){
////                        mSearchResult.add(city[i]);
////                    }
////                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s){
//
//            }
//        });
    }

    public void onClick(View v){
        if(v.getId() == R.id.title_back) {
            Intent i = new Intent();
            if(returnCode.equals("")){
                returnCode = "101010300"; //没有选中城市时选择北京的代码作为默认代码
            }
            i.putExtra("cityCode", returnCode);
            setResult(RESULT_OK, i);
            finish();
        }
    }

}
