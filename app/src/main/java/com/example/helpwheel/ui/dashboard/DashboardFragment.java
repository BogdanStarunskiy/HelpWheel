package com.example.helpwheel.ui.dashboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.helpwheel.R;
import com.example.helpwheel.databinding.FragmentDashboardBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class DashboardFragment extends AppCompatActivity{
    private EditText enter_weather;
    private Button weather_button;
    private TextView current_weather;
    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);

        enter_weather = findViewById(R.id.enter_city);
        weather_button = findViewById(R.id.weather_btn);
        current_weather = findViewById(R.id.current_weather);
        weather_button.setOnClickListener(v -> {
            if(enter_weather.getText().toString().trim().equals("")){
                Toast toast = Toast.makeText(getApplicationContext(), "Enter city!", Toast.LENGTH_LONG);
                toast.show();
            }
            else{
                String user_city = enter_weather.getText().toString().trim();
                String key = "a98ca7720a8fd711bb8548bf2373e263";
                String url = "https://api.openweathermap.org/data/2.5/weather?q="+user_city+"&appid="+key+"&units=metric&lang=ru";
                new GetUrlData().execute(url);
            }
        });
    }

    private class GetUrlData extends AsyncTask<String, String, String> {
        @SuppressLint("SetTextI18n")
        protected void onPreExecute(){
            super.onPreExecute();
            current_weather.setText("Please,wait");
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpsURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(strings[0]);
                connection = (HttpsURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while((line = reader.readLine()) != null){
                    buffer.append(line).append("/n");
                }
                return buffer.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } finally{
                if(connection != null){
                    connection.disconnect();
                }
                if(reader != null){
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;



        }
        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String result){
            try {
                JSONObject jsonObject = new JSONObject(result);
                current_weather.setText("Temp "+ jsonObject.getJSONObject("main").getDouble("temp"));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            super.onPostExecute(result);

        }
    }


    /*public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textDashboard;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }*/


}