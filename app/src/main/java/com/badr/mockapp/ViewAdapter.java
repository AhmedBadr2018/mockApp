package com.badr.mockapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.ViewHolder> {

    private Context context;
    private List<DataItem> dataItems = Collections.emptyList();

    public ViewAdapter(List<DataItem> items, Context context) {
        this.dataItems = items;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.textViewRSRP.setText(String.valueOf(dataItems.get(position).getRSRP()));
        holder.textViewRSRP.setBackgroundColor(new ResColors().resColorRSRP(dataItems.get(position).getRSRP()));

        holder.textViewRSRQ.setText(String.valueOf(dataItems.get(position).getRSRQ()));
        holder.textViewRSRQ.setBackgroundColor(new ResColors().resColorRSRQ(dataItems.get(position).getRSRQ()));

        holder.textViewSINR.setText(String.valueOf(dataItems.get(position).getSINR()));
        holder.textViewSINR.setBackgroundColor(new ResColors().resColorSINR(dataItems.get(position).getSINR()));

        holder.textViewTime.setText(dataItems.get(position).getTime());

        holder.linearLayoutData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(context, String.valueOf(dataItems.get(position).getTime().replace(".", ":")), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataItems.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Insert a new item to the RecyclerView on a predefined position
    private void insert(int position, DataItem itemData) {
        dataItems.add(position, itemData);
        notifyItemInserted(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayoutData;
        TextView textViewRSRP;
        TextView textViewRSRQ;
        TextView textViewSINR;
        TextView textViewTime;

        ViewHolder(View itemView) {
            super(itemView);

            linearLayoutData = (LinearLayout) itemView.findViewById(R.id.linearLayoutData);
            textViewRSRP = (TextView) itemView.findViewById(R.id.textViewRSRP);
            textViewRSRQ = (TextView) itemView.findViewById(R.id.textViewRSRQ);
            textViewSINR = (TextView) itemView.findViewById(R.id.textViewSINR);
            textViewTime = (TextView) itemView.findViewById(R.id.textViewTime);
        }

    }

}
