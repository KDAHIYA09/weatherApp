package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout homeRL;
    private TextView cityNameTV, tempratureTV, conditionTV;
    private ProgressBar loadingPB;
    private RecyclerView weatherRV;
    private TextInputEditText cityEDT;
    private ImageView backIV, iconIV, searchIV;
    private ArrayList<weatherRVModel> weatherRVModelArrayList;
    private weatherRVAdapter weatherRVAdapter;
    private LocationManager locationManager;
    private int PERMISSION_CODE = 1;
    private String cityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        //THIS COMmAND HELPS US TO GET FULL SCREEN APP AND DO NOT DISPLAY STATUS BAR
        setContentView(R.layout.activity_main);

        homeRL = findViewById(R.id.idRLHome);
        cityNameTV = findViewById(R.id.idIVCityName);
        tempratureTV = findViewById(R.id.idTVTTemprature);
        conditionTV = findViewById(R.id.idTVCondition);
        loadingPB = findViewById(R.id.idPBLoading);
        weatherRV = findViewById(R.id.idRvWeather);
        cityEDT = findViewById(R.id.idEDTCity);
        backIV = findViewById(R.id.idIVBack);
        iconIV = findViewById(R.id.idIVIcon);
        searchIV = findViewById(R.id.idIVSearch);

        weatherRVModelArrayList = new ArrayList<>();
        weatherRVAdapter = new weatherRVAdapter(this, weatherRVModelArrayList);
        weatherRV.setAdapter(weatherRVAdapter);

        locationManager = (LocationManager) getSystemService(Context.LOCALE_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION},PERMISSION_CODE);
            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        cityName = getCityName(location.getLongitude(),location.getLatitude());
        getWeatherInfo(cityName);

        searchIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = cityEDT.getText().toString();
                if(city.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please Enter City Name", Toast.LENGTH_SHORT).show();
                }else {
                    getWeatherInfo(city);
                }
            }
        });

        }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "PERMISSION_GRANTED", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, "Please provide the permissions", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private String getCityName(double longitude, double latitude){
        String cityName = "NOT FOUND";
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(latitude,longitude,10);

            for (Address adr : addresses){
                if (adr!=null){
                    String city = adr.getLocality();
                    if(city!=null && !city.equals("")){
                        cityName = city;
                    }else {
                        Log.d("TAG", "getCityName: CITY NOT FOUND");
                        Toast.makeText(this, "USER CITY NOT FOUND...", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        }catch (IOException e){
            e.printStackTrace();
        }
        return cityName;
        }

    

    private void getWeatherInfo(String cityName){
        String url = "http://api.weatherapi.com/v1/forecast.json?key=6280ff14882b4f1dabd50252231803&q="+ cityName +"&days=1&aqi=yes&alerts=yes";
        cityNameTV.setText(cityName);
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loadingPB.setVisibility(View.GONE);
                homeRL.setVisibility(View.VISIBLE);

                weatherRVModelArrayList.clear();
                try {
                    String temprature = response.getJSONObject("current").getString("temp_c");
                    tempratureTV.setText(temprature + "°c");
                    int isDay = response.getJSONObject("current").getInt("is_day");
                    String condition = response.getJSONObject("current").getJSONObject("condition").getString("text");
                    String conditionIcon = response.getJSONObject("current").getJSONObject("condition").getString("icon");
                    Picasso.get().load("http:".concat(conditionIcon)).into(iconIV);
                    conditionTV.setText(condition);
                    if (isDay == 1) {
                        Picasso.get().load("https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.dreamstime.com%2Fphotos-images%2Fblue-cloudy-sky.html&psig=AOvVaw2vFhdLJGtfHmhyvy_0R8Md&ust=1679336878673000&source=images&cd=vfe&ved=0CA8QjRxqFwoTCJiy8sHP6P0CFQAAAAAdAAAAABAE").into(backIV);
                    } else {
                        Picasso.get().load("https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.istockphoto.com%2Fphotos%2Fnight-sky&psig=AOvVaw19dLf5bRCbju296WohWiuo&ust=1679336926627000&source=images&cd=vfe&ved=0CA8QjRxqFwoTCIjU29jP6P0CFQAAAAAdAAAAABAE").into(backIV);
                    }

                    JSONObject forcastObj = response.getJSONObject("forecast");
                    JSONObject forcastO = forcastObj.getJSONArray("forecastday").getJSONObject(0);
                    JSONArray hourArray = forcastO.getJSONArray("hour");

                    for (int i=0; i<hourArray.length(); i++) {
                        JSONObject hourObj = hourArray.getJSONObject(i);
                        String time = hourObj.getString("time");
                        String temper = hourObj.getString("temp_c");
                        String img = hourObj.getJSONObject("condition").getString("icon");
                        String wind = hourObj.getString("wind_kph");

                        weatherRVModelArrayList.add(new weatherRVModel(time, temper, img, wind));
                    }

                    weatherRVAdapter.notifyDataSetChanged();

                    }catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Please enter valid city name.", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);

    }

}