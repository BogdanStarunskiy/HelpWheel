package com.example.helpwheel.ui.dashboard;

import static com.example.helpwheel.ui.dashboard.DashboardViewModel.generateURL;
import static com.example.helpwheel.ui.dashboard.DashboardViewModel.getResponseFromURL;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.helpwheel.databinding.FragmentDashboardBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;
    Button weather_btn;
    TextView current_weather;
    EditText enter_city;
    class WeatherQueryTask extends AsyncTask<URL,Void,String> {

        @Override
        protected String doInBackground(URL... urls) {
            String response = null;
            try {
                response = getResponseFromURL(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }
        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String response){
            String temperature = null;
            String main_description = null;
            String description = null;
            String wind = null;
            String feel_temperature = null;
            try {
                JSONObject jsonObject = new JSONObject(response);
                temperature = "Temperature in your city is " + jsonObject.getJSONObject("main").getDouble("temp")+"℃";
                feel_temperature = "Feels like: " + jsonObject.getJSONObject("main").getDouble("feels_like")+"℃";
                wind = "Wind`s speed: " + jsonObject.getJSONObject("wind").getDouble("speed")+" Km/Hour";
                JSONArray jsonArray = jsonObject.getJSONArray("weather");
                JSONObject Json_description = jsonArray.getJSONObject(0);
                main_description = Json_description.getString("main");
                description = "Description: "+Json_description.getString("description");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            assert main_description != null;
            assert description != null;
            if (main_description.trim().toLowerCase(Locale.ROOT).
                    equals(description.trim().
                            toLowerCase(Locale.ROOT))) {
                binding.currentWeather.setText(temperature + "\n");
                binding.currentWeather.append(feel_temperature + "\n");
                binding.currentWeather.append(main_description + "\n");
                binding.currentWeather.append(wind + "\n");
            }
            else{
                binding.currentWeather.setText(temperature + "\n");
                binding.currentWeather.append(feel_temperature + "\n");
                binding.currentWeather.append(main_description + "\n");
                binding.currentWeather.append(description + "\n");
                binding.currentWeather.append(wind + "\n");
            }
        }

    }


    public  View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.weatherBtn.setOnClickListener(v -> {
            //присваиваю преременной generatedURL значение, которое возвращает метод
            URL generatedURL = generateURL(binding.enterCity.getText().toString().trim());
            new WeatherQueryTask().execute(generatedURL);
        });

    return root;}
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}