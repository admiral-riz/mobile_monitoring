package com.example.monitoring.ui.kip;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.monitoring.R;
import com.example.monitoring.model.KipResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KipAdapter extends RecyclerView.Adapter<KipAdapter.ChannelViewHolder> {

    private final List<Map.Entry<String, KipResponse.KipChannelData>> dataList = new ArrayList<>();

    public void setData(Map<String, KipResponse.KipChannelData> data) {
        dataList.clear();
        if (data != null) dataList.addAll(data.entrySet());
        notifyDataSetChanged();
    }

    @Override
    public ChannelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_channel_kip, parent, false);
        return new ChannelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChannelViewHolder holder, int position) {
        Map.Entry<String, KipResponse.KipChannelData> entry = dataList.get(position);
        String channelName = entry.getKey();
        KipResponse.KipChannelData channelData = entry.getValue();

        holder.title.setText(channelName + " (Top 5 Issues)");

        holder.recyclerFixed.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.recyclerFixed.setAdapter(new IssueAdapter(channelData.getFixed()));

        holder.recyclerMobile.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.recyclerMobile.setAdapter(new IssueAdapter(channelData.getMobile()));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class ChannelViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        RecyclerView recyclerFixed, recyclerMobile;

        ChannelViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textChannelTitle);
            recyclerFixed = itemView.findViewById(R.id.recyclerFixed);
            recyclerMobile = itemView.findViewById(R.id.recyclerMobile);
        }
    }
}
