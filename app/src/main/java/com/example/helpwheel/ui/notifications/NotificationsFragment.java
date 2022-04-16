package com.example.helpwheel.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.helpwheel.R;
import com.example.helpwheel.databinding.BottomSheetDialogBinding;
import com.example.helpwheel.databinding.FragmentNotificationsBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        binding.fuelInputButton.setOnClickListener(view -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
            bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog);
            bottomSheetDialog.setCanceledOnTouchOutside(true);
            bottomSheetDialog.show();
        });
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}