package com.android.timwaite.simpleweather;

import android.app.Fragment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    public TextView temperatureTV;
    public Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.fragment_weather);
        Button button = (Button) findViewById(R.id.cityBut);
        handler = new Handler();
        temperatureTV = (TextView) findViewById(R.id.tempTV);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateWeatherData("Golden, CO");
            }
        });
    }
    public void updateWeatherData(final String city) {
        new Thread() {
            public void run() {
                final JSONObject json = RemoteFetch.getJSON(getApplication(), city);
                if (json == null) {
                    handler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplication(), "Place Not Found", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        public void run() {
                            renderWeather(json);
                        }
                    });
                }
            }
        }.start();
    }
    private void renderWeather(JSONObject json) {
        try {
            JSONObject main = json.getJSONObject("main");
            temperatureTV.setText(String.format("%.2f", main.getDouble("temp"))+ " ℃");

        } catch (Exception e) {
            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
        }
    }
}






