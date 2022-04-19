package com.example.helpwheel.ui.notifications;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.helpwheel.R;
import com.example.helpwheel.databinding.FragmentNotificationsBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    public static final String APP_PREFERENCES = "fuelStats";
    public static final String APP_PREFERENCES_ODOMETER = " ";
    public static final String APP_PREFERENCES_PRICE = " ";
    public Float oldOdometerValue;
    SharedPreferences fuelStats;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        fuelStats = requireContext().getSharedPreferences(APP_PREFERENCES, getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = fuelStats.edit();
        binding.fuelInputButton.setOnClickListener(view -> {
            if (fuelStats.getFloat(APP_PREFERENCES_ODOMETER, 0) == 0.0){
                Toast.makeText(getContext(), "Test", Toast.LENGTH_SHORT).show();
            }
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
            bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog);
            bottomSheetDialog.setCanceledOnTouchOutside(true);
            bottomSheetDialog.show();
            Button submitBtn = bottomSheetDialog.findViewById(R.id.submit_btn_fuel);

            submitBtn.setOnClickListener(view1 -> {

                EditText odometer = bottomSheetDialog.findViewById(R.id.odometer_edit_text);
                EditText pricePerLiter = bottomSheetDialog.findViewById(R.id.price_edit_text);
                String odometerValue = odometer.getText().toString();
                String priceValue = pricePerLiter.getText().toString();

                if (!odometerValue.isEmpty() && !priceValue.isEmpty()){
                    editor.putFloat(APP_PREFERENCES_ODOMETER, Float.parseFloat(odometerValue));
                    editor.putFloat(APP_PREFERENCES_PRICE, Float.parseFloat(priceValue));
                } else if (!odometerValue.isEmpty()) {
                    editor.putFloat(APP_PREFERENCES_ODOMETER, Float.parseFloat(odometerValue));
                } else if(!priceValue.isEmpty()) {
                    editor.putFloat(APP_PREFERENCES_PRICE, Float.parseFloat(priceValue));
                }
                editor.apply();
                bottomSheetDialog.dismiss();
            });
        });
        return binding.getRoot();
    }
    public void variableDefinition(){

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}