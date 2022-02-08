package com.example.helpwheel.ui.dashboard;

import static com.example.helpwheel.ui.dashboard.DashboardViewModel.generateURL;
import static com.example.helpwheel.ui.dashboard.DashboardViewModel.getResponseFromURL;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.helpwheel.R;
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

    private void showErrorMessage() {
        binding.currentWeather.setVisibility(View.VISIBLE);
        binding.currentWeather.setText(R.string.error_weather);
    }

    private void showSuccessMessage() {
        binding.currentWeather.setVisibility(View.VISIBLE);
    }

    class WeatherQueryTask extends AsyncTask<URL, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            binding.weatherLoading.setVisibility(View.VISIBLE);
        }

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
        protected void onPostExecute(String response) {
            if (response != null && !response.equals("")) {
                String temperature = null;
                String main_description = null;
                String description = null;
                String wind = null;
                String feel_temperature = null;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    temperature = "Temperature in your city is " + jsonObject.getJSONObject("main").getDouble("temp") + "℃";
                    feel_temperature = "Feels like: " + jsonObject.getJSONObject("main").getDouble("feels_like") + "℃";
                    wind = "Wind`s speed: " + jsonObject.getJSONObject("wind").getDouble("speed") + " KM/H";
                    JSONArray jsonArray = jsonObject.getJSONArray("weather");
                    JSONObject Json_description = jsonArray.getJSONObject(0);
                    main_description = Json_description.getString("main");
                    description = "Description: " + Json_description.getString("description");

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
                } else {
                    binding.currentWeather.setText(temperature + "\n");
                    binding.currentWeather.append(feel_temperature + "\n");
                    binding.currentWeather.append(main_description + "\n");
                    binding.currentWeather.append(description + "\n");
                    binding.currentWeather.append(wind + "\n");
                }
                binding.weatherLoading.setVisibility(View.INVISIBLE);
                showSuccessMessage();
            } else {
                showErrorMessage();
                binding.weatherLoading.setVisibility(View.INVISIBLE);

            }

        }

    }


    public View onCreateView(@NonNull LayoutInflater inflater,
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

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}