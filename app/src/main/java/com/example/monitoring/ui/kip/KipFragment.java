package com.example.monitoring.ui.kip;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.monitoring.databinding.FragmentKipBinding;

public class KipFragment extends Fragment {

    private FragmentKipBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        KipViewModel kipViewModel =
                new ViewModelProvider(this).get(KipViewModel.class);

        binding = FragmentKipBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textKip;
        kipViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}