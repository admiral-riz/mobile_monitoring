package com.example.monitoring.ui.auxx;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.monitoring.databinding.FragmentAuxBinding;

public class AuxFragment extends Fragment {

    private FragmentAuxBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AuxViewModel auxViewModel =
                new ViewModelProvider(this).get(AuxViewModel.class);

        binding = FragmentAuxBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textAux;
        auxViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}