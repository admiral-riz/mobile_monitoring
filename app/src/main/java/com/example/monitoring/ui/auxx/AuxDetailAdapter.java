package com.example.monitoring.ui.auxx;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.monitoring.R;
import com.example.monitoring.model.AuxDetailResponse;

import java.util.List;

public class AuxDetailAdapter extends RecyclerView.Adapter<AuxDetailAdapter.ViewHolder> {

    private final List<AuxDetailResponse.Detail> list;

    public AuxDetailAdapter(List<AuxDetailResponse.Detail> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_aux_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AuxDetailResponse.Detail item = list.get(position);
        holder.tvMulai.setText(item.getWaktu_mulai());
        holder.tvSelesai.setText(item.getWaktu_selesai());
        holder.tvDurasi.setText(item.getDurasi());
        holder.tvStatus.setText(item.getStatus());
        holder.tvKeterangan.setText(item.getKeterangan());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMulai, tvSelesai, tvDurasi, tvStatus, tvKeterangan;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMulai = itemView.findViewById(R.id.tvMulai);
            tvSelesai = itemView.findViewById(R.id.tvSelesai);
            tvDurasi = itemView.findViewById(R.id.tvDurasi);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvKeterangan = itemView.findViewById(R.id.tvKeterangan);
        }
    }
}
