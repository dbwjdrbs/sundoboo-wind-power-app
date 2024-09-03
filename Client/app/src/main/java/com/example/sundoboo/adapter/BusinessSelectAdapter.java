package com.example.sundoboo.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client.R;
import com.example.sundoboo.common.BusinessSelectActivity;
import com.example.sundoboo.common.MapActivity;
import com.example.sundoboo.data.BusinessData;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class BusinessSelectAdapter extends RecyclerView.Adapter<CustomViewHolder> {
    ArrayList<BusinessData> list;

    public BusinessSelectAdapter(ArrayList<BusinessData> list) {
        this.list = list;
    }


    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.business_item_list, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.getTitle().setText(list.get(position).getTitle());
        holder.getCreatedAt().setText("등록 일자 : " + list.get(position).getCreatedAt());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class CustomViewHolder extends RecyclerView.ViewHolder {
    private TextView title;
    private TextView createdAt;

    public CustomViewHolder(@NonNull View itemView) {
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
