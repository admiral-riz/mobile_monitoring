package com.example.monitoring.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.monitoring.api.RegisterAPI;
import com.example.monitoring.api.ServerAPI;
import com.example.monitoring.databinding.FragmentProfileBinding;
import com.example.monitoring.model.ProfileResponse;
import com.example.monitoring.ui.activitylog.ActivityLogActivity;
import com.example.monitoring.ui.kelola.KelolaDataActivity;
import com.example.monitoring.ui.login.Login;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        sharedPreferences = requireActivity().getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);

        loadProfile();

        binding.btnActivityLog.setOnClickListener(v -> loadProfile());
        binding.btnActivityLog.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), ActivityLogActivity.class);
            startActivity(i);
        });


        binding.btnKelolaData.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), KelolaDataActivity.class);
            startActivity(i);
        });

        binding.btnLogout.setOnClickListener(v -> logout());

        return root;
    }

    private void loadProfile() {

        String userId = sharedPreferences.getString("id", null);

        if (userId == null) {
            Toast.makeText(getContext(), "User ID tidak ditemukan!", Toast.LENGTH_SHORT).show();
            return;
        }

        RegisterAPI api = ServerAPI.getClient().create(RegisterAPI.class);
        Call<ProfileResponse> call = api.getProfile(userId);

        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {

                    ProfileResponse.ProfileData data = response.body().getData();

                    // Header
                    binding.tvName.setText(data.getName());
                    binding.tvRole.setText(data.getRole());

                    // Card Detail
                    binding.tvUsername.setText("Username : " + data.getName());
                    binding.tvRolename.setText("Role : " + data.getRole());
                    binding.tvCreated.setText("Created At : " + data.getCreated_at());
                    binding.tvUpdated.setText("Updated At : " + data.getUpdated_at());

                } else {
                    Toast.makeText(getContext(), "Gagal mengambil data!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void logout() {

        // Hapus session
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Toast.makeText(getContext(), "Logged out successfully!", Toast.LENGTH_SHORT).show();

        // Arahkan ke Login Activity
        Intent intent = new Intent(getActivity(), Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
