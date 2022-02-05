package com.example.helpwheel.ui.dashboard;

import android.net.Uri;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;

public class DashboardViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    public DashboardViewModel() {
        mText = new MutableLiveData<>();
    }

    public static final String weather_api_base = "https://api.openweathermap.org/";
    public static final String weather_api_get = "data/2.5/weather";
    public static final String param_q = "q";
    public static final String key = "a98ca7720a8fd711bb8548bf2373e263";
    public static final String param_key = "appid";
    public static final String param_units = "units";
    public static final String param_units_val = "metric";
    public static final String param_lang = "lang";
    public static final String param_lang_val = "en";
    //создаю метод, которые возвращает урлу с уже подставленым городом
    public static URL generateURL(String userCity){
        //билджу урлу
        Uri builtUri = Uri.parse(weather_api_base+weather_api_get).buildUpon().
                appendQueryParameter(param_q,userCity).
                appendQueryParameter(param_key,key).
                appendQueryParameter(param_units,param_units_val).
                appendQueryParameter(param_lang,param_lang_val).
                build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
    public static String getResponseFromURL(URL url) throws IOException {
        HttpURLConnection urlConnection =(HttpURLConnection) url.openConnection();
        try{
            //открывю конект
            InputStream in = urlConnection.getInputStream();
            //с помощью сканера считываю
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if(hasInput){
                return scanner.next();
            }else{
                return null;
            }}
        // в любом случае надо закрыть конект
        finally{
            urlConnection.disconnect();
        }


    }
}