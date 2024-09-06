package com.example.client.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client.R;
import com.example.client.common.MapActivity;
import com.example.client.data.BusinessData;

import java.util.ArrayList;

public class BusinessSelectAdapter extends RecyclerView.Adapter<BusinessSelectViewHolder> {
    ArrayList<BusinessData> list;

    public BusinessSelectAdapter(ArrayList<BusinessData> list) {
        this.list = list;
    }


    @NonNull
    @Override
    public BusinessSelectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.rv_item_business, parent, false);
        return new BusinessSelectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BusinessSelectViewHolder holder, int position) {
        holder.getTitle().setText(list.get(position).getTitle());
        holder.getCreatedAt().setText("등록 일자 : " + list.get(position).getCreatedAt());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class BusinessSelectViewHolder extends RecyclerView.ViewHolder {
    private TextView title;
    private TextView createdAt;

    public BusinessSelectViewHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.tv_businessName);
        createdAt = itemView.findViewById(R.id.tv_businessCreatedAt);

        itemView.setOnClickListener(view -> {
            int pos = getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                Intent intent = new Intent(view.getContext(), MapActivity.class);
                view.getContext().startActivity(intent);
            }
        });
    }

    public TextView getTitle() {
        return this.title;
    }

    public TextView getCreatedAt() {
        return this.createdAt;
    }
}
