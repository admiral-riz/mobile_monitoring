package com.example.monitoring.ui.auxx;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.monitoring.R;
import com.example.monitoring.model.AuxResponse;

import java.util.List;

public class AuxAdapter extends RecyclerView.Adapter<AuxAdapter.ViewHolder> {

    private final List<AuxResponse.AuxItem> auxList;
    private final String selectedDate; // tanggal untuk dikirim ke detail

    public AuxAdapter(List<AuxResponse.AuxItem> auxList, String selectedDate) {
        this.auxList = auxList;
        this.selectedDate = selectedDate;
    }

    @NonNull
    @Override
    public AuxAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_aux, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AuxAdapter.ViewHolder holder, int position) {
        AuxResponse.AuxItem it = auxList.get(position);
        holder.tvNo.setText(String.valueOf(position + 1));
        holder.tvAgent.setText(it.getAgentName());
        holder.tvSite.setText(it.getSite());
        holder.tvIst1.setText(formatNumber(it.getIstirahat1()));
        holder.tvIst2.setText(formatNumber(it.getIstirahat2()));
        holder.tvIst3.setText(formatNumber(it.getIstirahat3()));
        holder.tvToilet.setText(formatNumber(it.getToilet()));
        holder.tvIbadah.setText(formatNumber(it.getIbadah()));
        holder.tvTotal.setText(formatNumber(it.getTotal()));
        holder.tvStatus.setText(it.getStatus());

        // warna status
        String s = it.getStatus();
        if (s.equalsIgnoreCase("WARNING")) {
            holder.tvStatus.setBackgroundColor(Color.parseColor("#FFA726"));
            holder.tvStatus.setTextColor(Color.WHITE);
        } else if (s.equalsIgnoreCase("NORMAL")) {
            holder.tvStatus.setBackgroundColor(Color.parseColor("#66BB6A"));
            holder.tvStatus.setTextColor(Color.WHITE);
        } else if (s.equalsIgnoreCase("DANGER")) {
            holder.tvStatus.setBackgroundColor(Color.parseColor("#EF5350"));
            holder.tvStatus.setTextColor(Color.WHITE);
        } else {
            holder.tvStatus.setBackgroundColor(Color.TRANSPARENT);
            holder.tvStatus.setTextColor(Color.BLACK);
        }

        // tombol detail
        holder.btnDetail.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), AuxDetailActivity.class);
            intent.putExtra("date", selectedDate);
            intent.putExtra("agent", it.getAgentName());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return auxList == null ? 0 : auxList.size();
    }

    private String formatNumber(double v) {
        if (v == (long) v) return String.format("%d", (long) v);
        return String.format("%.2f", v).replaceAll("\\.?0+$", "");
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNo, tvAgent, tvSite, tvIst1, tvIst2, tvIst3, tvToilet, tvIbadah, tvTotal, tvStatus;
        Button btnDetail;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNo = itemView.findViewById(R.id.tvNo);
            tvAgent = itemView.findViewById(R.id.tvAgent);
            tvSite = itemView.findViewById(R.id.tvSite);
            tvIst1 = itemView.findViewById(R.id.tvIst1);
            tvIst2 = itemView.findViewById(R.id.tvIst2);
            tvIst3 = itemView.findViewById(R.id.tvIst3);
            tvToilet = itemView.findViewById(R.id.tvToilet);
            tvIbadah = itemView.findViewById(R.id.tvIbadah);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnDetail = itemView.findViewById(R.id.btnDetail);
        }
    }
}
