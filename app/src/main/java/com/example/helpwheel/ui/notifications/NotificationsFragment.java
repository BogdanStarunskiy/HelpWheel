package com.example.helpwheel.ui.notifications;

import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.helpwheel.R;
import com.example.helpwheel.databinding.FragmentNotificationsBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.math.BigDecimal;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    public static final String APP_PREFERENCES = "fuelStats";
    public static final String APP_PREFERENCES_ODOMETER = " ";
    public static final String APP_PREFERENCES_ODOMETER_OLD = " ";
    public static final String APP_PREFERENCES_PRICE = " ";
    public static final String APP_PREFERENCES_RESULT = " ";
    public Float oldOdometerValue;
    SharedPreferences fuelStats;
    AlertDialog alertDialog;
    private SharedPreferences.Editor editor;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        fuelStats = requireContext().getSharedPreferences(APP_PREFERENCES, getContext().MODE_PRIVATE);
        editor = fuelStats.edit();
        binding.fuelInputButton.setOnClickListener(view -> {
                    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
                    bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog);
                    bottomSheetDialog.show();
                    Button submitBtn = bottomSheetDialog.findViewById(R.id.submit_btn_fuel);
                    submitBtn.setOnClickListener(view1 -> {
                        EditText odometer = bottomSheetDialog.findViewById(R.id.odometer_edit_text);
                        EditText pricePerLiter = bottomSheetDialog.findViewById(R.id.price_edit_text);
                        String odometerValue = odometer.getText().toString();
                        String priceValue = pricePerLiter.getText().toString();
                        if (!odometerValue.isEmpty() && !priceValue.isEmpty()){
                            setOldOdometerValue();
                            editor.putFloat(APP_PREFERENCES_ODOMETER, Float.parseFloat(odometerValue));
                            editor.putFloat(APP_PREFERENCES_PRICE, Float.parseFloat(priceValue));
                            calculatingDifferences(oldOdometerValue, Float.parseFloat(odometerValue));
                        } else if (!odometerValue.isEmpty()) {
                            setOldOdometerValue();
                            editor.putFloat(APP_PREFERENCES_ODOMETER, Float.parseFloat(odometerValue));
                            calculatingDifferences(oldOdometerValue, Float.parseFloat(odometerValue));
                        } else if(!priceValue.isEmpty()) {
                            editor.putFloat(APP_PREFERENCES_PRICE, Float.parseFloat(priceValue));
                        }
                        editor.apply();
                        bottomSheetDialog.dismiss();
                    });
        });
        if (fuelStats.getFloat(APP_PREFERENCES_RESULT, 0.0f) == 0.0f){
            binding.distance.setText("0.0");
        } else {
            binding.distance.setText(Float.toString(fuelStats.getFloat(APP_PREFERENCES_RESULT, 0.0f)));
        }
        return binding.getRoot();
    }

    public void setOldOdometerValue(){
        if (fuelStats.getFloat(APP_PREFERENCES_ODOMETER, 0) == 0.0){
            oldOdometerValue = 0.0f;
            showCustomDialog();
        } else{
            editor.putFloat(APP_PREFERENCES_ODOMETER_OLD, fuelStats.getFloat(APP_PREFERENCES_ODOMETER, 0));
            oldOdometerValue = fuelStats.getFloat(APP_PREFERENCES_ODOMETER_OLD, 0.0f);
            editor.apply();
        }
    }

    public void calculatingDifferences(Float oldOdometerValue, Float odometerValue){
        if (fuelStats.getFloat(APP_PREFERENCES_ODOMETER, 0) == 0.0) {
            binding.distance.setText("0.0");
        } else {
            float number = BigDecimal.valueOf(odometerValue - oldOdometerValue)
                    .setScale(2, BigDecimal.ROUND_HALF_DOWN)
                    .floatValue();
            binding.distance.setText(Float.toString(number));
            editor.putFloat(APP_PREFERENCES_RESULT, number);
        }
    }

    public void showCustomDialog (){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.WelcomeAlertDialog);
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.fuel_arelt_dialog_layout, null);
        builder.setView(view);
        alertDialog = builder.create();
        view.findViewById(R.id.alert_dialog_fuel_ok_button).setOnClickListener(view1 -> alertDialog.dismiss());
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}