package com.example.mysplashs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_cityId,btn_getWeatherById,btn_getWeatherByName;
    private EditText et_dataInput;
    private ListView lv_weatherReport;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_cityId = findViewById(R.id.btn_getCityId);
        btn_getWeatherById = findViewById(R.id.btn_getWeatherByCityId);
        btn_getWeatherByName = findViewById(R.id.btn_getWeatherByCityName);
        et_dataInput = findViewById(R.id.et_dataInput);
        lv_weatherReport = findViewById(R.id.listViewId);

        btn_cityId.setOnClickListener(this);
        btn_getWeatherById.setOnClickListener(this);
        btn_getWeatherByName.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        final WeatherDataService weatherDataService = new WeatherDataService(MainActivity.this);
        if (view.getId()==R.id.btn_getCityId){
            weatherDataService.getCityID(et_dataInput.getText().toString(), new WeatherDataService.VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    Toast.makeText(MainActivity.this, "Something wrong", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(String cityID) {
                    Toast.makeText(MainActivity.this, "Returned an ID of "+cityID, Toast.LENGTH_SHORT).show();
                }
            });

        }
        if (view.getId()==R.id.btn_getWeatherByCityId){
            weatherDataService.getCityForecastById(et_dataInput.getText().toString(), new WeatherDataService.ForeCastByIDResponse() {
                @Override
                public void onError(String message) {
                    Toast.makeText(MainActivity.this, "Error occurd ", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(List<weatherModelPage> weatherModelPages) {
                    ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this,
                            android.R.layout.simple_list_item_1,weatherModelPages);
                    lv_weatherReport.setAdapter(arrayAdapter);

                }
            });

        }
        if (view.getId()==R.id.btn_getWeatherByCityName){
            weatherDataService.getCityForecastByName(et_dataInput.getText().toString(), new WeatherDataService.ForeCastByNameResponse() {
                @Override
                public void onError(String message) {
                    Toast.makeText(MainActivity.this, "Error occurd ", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(List<weatherModelPage> weatherModelPages) {
                    ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this,
                            android.R.layout.simple_list_item_1,weatherModelPages);
                    lv_weatherReport.setAdapter(arrayAdapter);

                }
            });

        }

    }
}