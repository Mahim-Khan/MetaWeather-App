package com.example.mysplashs;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherDataService<WeatherReportModel> {
    String cityID;
    Context context;

    public WeatherDataService(Context context) {
        this.context = context;
    }
    public interface VolleyResponseListener {
        void onError(String message);

        void onResponse(String cityID);
    }

    public void getCityID(String cityName, VolleyResponseListener volleyResponseListener){
        String url ="https://www.metaweather.com/api/location/search/?query="+cityName;

        JsonArrayRequest request =new JsonArrayRequest(Request.Method.GET,url,null, new
                Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        cityID="";
                        try {
                            JSONObject cityInfo = response.getJSONObject(0);
                            cityID= cityInfo.getString("woeid");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//
//                        Toast.makeText(context,
//                                "City ID= "+cityID, Toast.LENGTH_SHORT).show();
                        volleyResponseListener.onResponse(cityID);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(context, "Error occured", Toast.LENGTH_SHORT).show();
                volleyResponseListener.onError("Something wrong");
            }
        }
        );
        MySingleton.getInstance(context).addToRequestQueue(request);
       // return cityID;
    }
    public interface ForeCastByIDResponse {
        void onError(String message);

        void onResponse(List<weatherModelPage> weatherModelPages);
    }

    public  void getCityForecastById(String cityID, ForeCastByIDResponse foreCastByIDResponse){
        String url ="https://www.metaweather.com/api/location/"+cityID;
        List<weatherModelPage> weatherModelPages = new ArrayList<>();
        // get the json object
        JsonObjectRequest request =new JsonObjectRequest(Request.Method.GET, url,
                null, new
                Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray consolidated_weather = response.getJSONArray
                                    ("consolidated_weather");

                            for (int i=0;i<consolidated_weather.length();i++){
                                weatherModelPage first_day = new weatherModelPage();
                                JSONObject first_day_from_api = (JSONObject) consolidated_weather.get(i);
                                first_day.setId(first_day_from_api.getInt("id"));
                                first_day.setWeather_state_name(first_day_from_api.getString("weather_state_name"));
                                first_day.setWeather_state_abbr(first_day_from_api.getString("weather_state_abbr"));
                                first_day.setWind_direction_compass(first_day_from_api.getString("wind_direction_compass"));
                                first_day.setCreated(first_day_from_api.getString("created"));
                                first_day.setApplicable_date(first_day_from_api.getString("applicable_date"));
                                first_day.setMin_temp(first_day_from_api.getLong("min_temp"));
                                first_day.setMax_temp(first_day_from_api.getLong("max_temp"));
                                first_day.setThe_temp(first_day_from_api.getLong("the_temp"));
                                first_day.setWind_speed(first_day_from_api.getLong("wind_speed"));
                                first_day.setWind_direction(first_day_from_api.getLong("wind_direction"));
                                first_day.setAir_pressure(first_day_from_api.getInt("air_pressure"));
                                first_day.setHumidity(first_day_from_api.getInt("humidity"));
                                first_day.setVisibility(first_day_from_api.getLong("visibility"));
                                first_day.setId(first_day_from_api.getInt("predictability"));
                                weatherModelPages.add(first_day);

                            }
                            foreCastByIDResponse.onResponse(weatherModelPages);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                        Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MySingleton.getInstance(context).addToRequestQueue(request);

    }
    public interface ForeCastByNameResponse {
        void onError(String message);

        void onResponse(List<weatherModelPage> weatherModelPages);
    }


    public void getCityForecastByName(String cityName, ForeCastByNameResponse foreCastByNameResponse){
        //fatch the city id given the city name
        getCityID(cityName, new VolleyResponseListener() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponse(String cityID) {
                //now we have city id
                getCityForecastById(cityID, new ForeCastByIDResponse() {
                    @Override
                    public void onError(String message) {

                    }

                    @Override
                    public void onResponse(List<weatherModelPage> weatherModelPages) {
                        //we have wather report
                        foreCastByNameResponse.onResponse(weatherModelPages);


                    }
                });
            }
        });

    }
}
