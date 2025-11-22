package com.example.monitoring.ui.kelola;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.monitoring.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class KelolaDataAdapter extends RecyclerView.Adapter<KelolaDataAdapter.ViewHolder> {

    private List<JSONObject> dataList;
    private List<String> selectedIds = new ArrayList<>();
    private String primaryKey;

    public KelolaDataAdapter(List<JSONObject> dataList, String primaryKey) {
        this.dataList = dataList;
        this.primaryKey = primaryKey;
    }

    public List<String> getSelectedIds() {
        return selectedIds;
    }

    @NonNull
    @Override
    public KelolaDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_kelola_data, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KelolaDataAdapter.ViewHolder holder, int position) {
        JSONObject obj = dataList.get(position);
        holder.container.removeAllViews();

        try {
            String idValue = obj.getString(primaryKey);

            Iterator<String> keys = obj.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                TextView tv = new TextView(holder.itemView.getContext());
                tv.setText(key + ": " + obj.getString(key));
                holder.container.addView(tv);
            }

            holder.checkBox.setOnCheckedChangeListener(null);
            holder.checkBox.setChecked(selectedIds.contains(idValue));

            holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) selectedIds.add(idValue);
                else selectedIds.remove(idValue);
            });

        } catch (Exception e) {}
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout container;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.containerItem);
            checkBox = itemView.findViewById(R.id.checkboxItem);
        }
    }
}
