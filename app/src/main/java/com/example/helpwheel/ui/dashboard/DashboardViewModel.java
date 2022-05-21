
package com.example.helpwheel.ui.dashboard;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;

public class DashboardViewModel extends ViewModel {

    private final MutableLiveData<String> temperature;
    private final MutableLiveData<String> wind;
    private final MutableLiveData<String> description;
    private final MutableLiveData<String> city;
    private final MutableLiveData<Boolean> isGpsTurnedOn;
    String currentTemperature = "0";
    String currentWind = "0";
    String currentDescription = "Clear sky";
    Handler handler;

    public DashboardViewModel() {
        temperature = new MutableLiveData<>();
        wind = new MutableLiveData<>();
        description = new MutableLiveData<>();
        city = new MutableLiveData<>();
        isGpsTurnedOn = new MutableLiveData<>();
        handler = new Handler(Looper.getMainLooper());
    }
    void setIsGpsTurnedOn(Boolean isTurnedOn){
        handler.post(() -> isGpsTurnedOn.setValue(isTurnedOn));
    }

    void setCity(String userCity){
        handler.post(() -> city.setValue(userCity));
    }

    void getWeatherDataAutomatically(Double latitude, Double longitude, String key) {
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&appid=" + key + "&units=metric&lang=en";
        getWeatherData(url);
    }

    void getWeatherDataManualInput(String userCity, String key) {
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + userCity + "&appid=" + key + "&units=metric&lang=en";
        getWeatherData(url);
        handler.post(() -> city.setValue(userCity));
    }

    void getWeatherData(String urlRequest) {
        ExecutorService executor = Executors.newSingleThreadExecutor();


        executor.execute(() -> {
            HttpsURLConnection connection = null;
            BufferedReader reader = null;
            StringBuilder buffer = new StringBuilder();
            try {
                URL url = new URL(urlRequest);
                connection = (HttpsURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("/n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                JSONObject jsonObject = new JSONObject(buffer.toString());
                JSONArray jsonArray = jsonObject.getJSONArray("weather");
                JSONObject Json_description = jsonArray.getJSONObject(0);
                currentTemperature = (int) jsonObject.getJSONObject("main").getDouble("temp") + " ";
                currentWind = jsonObject.getJSONObject("wind").getDouble("speed") + " ";
                currentDescription = Json_description.getString("description");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            handler.post(() -> temperature.setValue(currentTemperature));
            handler.post(() -> wind.setValue(currentWind));
            handler.post(() -> description.setValue(currentDescription));
        });
    }

    public LiveData<String> getTemperature() {
        return temperature;
    }
    public LiveData<Boolean> getIsGPSTurnedOn(){return isGpsTurnedOn;}
    public LiveData<String> getWind() {
        return wind;
    }
    public LiveData<String> getCity() {return city;}
    public LiveData<String> getDescription() {
        return description;
    }
}