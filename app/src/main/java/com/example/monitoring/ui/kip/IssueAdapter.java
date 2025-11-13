package com.example.monitoring.ui.kip;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.monitoring.R;
import com.example.monitoring.model.KipResponse;
import java.util.List;

public class IssueAdapter extends RecyclerView.Adapter<IssueAdapter.IssueViewHolder> {

    private final List<KipResponse.IssueItem> issues;

    public IssueAdapter(List<KipResponse.IssueItem> issues) {
        this.issues = issues;
    }

    @Override
    public IssueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_issue, parent, false);
        return new IssueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IssueViewHolder holder, int position) {
        KipResponse.IssueItem item = issues.get(position);
        holder.textIssue.setText(item.getIssue());
        holder.textTotal.setText(String.valueOf(item.getTotal()));
    }

    @Override
    public int getItemCount() {
        return issues == null ? 0 : issues.size();
    }

    static class IssueViewHolder extends RecyclerView.ViewHolder {
        TextView textIssue, textTotal;

        IssueViewHolder(View itemView) {
            super(itemView);
            textIssue = itemView.findViewById(R.id.textIssue);
            textTotal = itemView.findViewById(R.id.textTotal);
        }
    }
}
