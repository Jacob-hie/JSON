package com.hie2j.json;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Spinner spinner1;
    private Spinner spinner2;
    private Spinner spinner3;

    private ArrayList<String> provinceArrayList = new ArrayList<>();
    private ArrayList<String> cityArrayList = new ArrayList<>();
    private ArrayList<String> areaArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner1 = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);
        spinner3 = findViewById(R.id.spinner3);

        try {
            InputStream inputStream = getResources().getAssets().open("data.txt");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuffer.append(line);
                line = bufferedReader.readLine();
            }
//            Log.e(TAG,"stringBuffer = " + stringBuffer.toString());

            final JSONArray provinceArray = new JSONArray(stringBuffer.toString());
            for (int i = 0; i < provinceArray.length(); i++) {
                JSONObject provinceObject = provinceArray.getJSONObject(i);
                String province = provinceObject.getString("name");
                provinceArrayList.add(province);
                Log.e(TAG, "province = " + province);
            }

            spinner1.setAdapter(new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, provinceArrayList));
            spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        final JSONArray cityArray = provinceArray.getJSONObject(position).getJSONArray("city");
                        if (cityArrayList.size() != 0){
                            cityArrayList.clear();
                        }
                        for (int j = 0; j < cityArray.length(); j++) {
                            JSONObject cityObject = cityArray.getJSONObject(j);
                            String city = cityObject.getString("name");
                            cityArrayList.add(city);
                            Log.e(TAG, "city = " + city);
                        }

                        spinner2.setAdapter(new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, cityArrayList));
                        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                try {
                                    JSONArray areaArray = cityArray.getJSONObject(position).getJSONArray("area");
                                    if (areaArrayList.size() != 0){
                                        areaArrayList.clear();
                                    }
                                    for (int k = 0; k < areaArray.length(); k++) {
                                        String area = (String) areaArray.get(k);
                                        areaArrayList.add(area);
                                        Log.e(TAG, "area = " + area);
                                    }
                                    spinner3.setAdapter(new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, areaArrayList));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
