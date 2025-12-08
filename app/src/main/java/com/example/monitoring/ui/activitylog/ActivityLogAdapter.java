package com.example.monitoring.ui.activitylog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.monitoring.R;
import com.example.monitoring.model.ActivityLogResponse;

import java.util.List;

public class ActivityLogAdapter extends RecyclerView.Adapter<ActivityLogAdapter.ViewHolder> {

    private List<ActivityLogResponse.LogData> list;

    public ActivityLogAdapter(List<ActivityLogResponse.LogData> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_activity_log, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ActivityLogResponse.LogData log = list.get(position);

        holder.txtUser.setText(log.getUsername());
        holder.txtActivity.setText(log.getActivity());
        holder.txtStatus.setText(log.getStatus());
        holder.txtDesc.setText(log.getDescription());
        holder.txtDate.setText(log.getCreated_at());
        holder.txtIP.setText(log.getIp_address());
        holder.txtAgent.setText(log.getUser_agent());
    }

    @Override
    public int getItemCount() { return list.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtUser, txtActivity, txtStatus, txtDesc, txtDate, txtIP, txtAgent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtUser = itemView.findViewById(R.id.txtUser);
            txtActivity = itemView.findViewById(R.id.txtActivity);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            txtDesc = itemView.findViewById(R.id.txtDesc);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtIP = itemView.findViewById(R.id.txtIP);
            txtAgent = itemView.findViewById(R.id.txtAgent);
        }
    }
}
