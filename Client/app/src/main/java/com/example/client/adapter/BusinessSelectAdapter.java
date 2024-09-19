package com.example.client.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client.Interface.BusinessSelectItemClickListener;
import com.example.client.R;
import com.example.client.common.MapActivity;
import com.example.client.data.BusinessData;

import java.util.ArrayList;

public class BusinessSelectAdapter extends RecyclerView.Adapter<BusinessSelectViewHolder> {
    private ArrayList<BusinessData> list;
    private BusinessSelectItemClickListener listener;

    public BusinessSelectAdapter(ArrayList<BusinessData> list, BusinessSelectItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BusinessSelectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.rv_item_business, parent, false);
        return new BusinessSelectViewHolder(view, listener, list);
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

    public void addItem(BusinessData businessData) {
        list.add(businessData);
    }

    public void removeItem(int position) {
        list.remove(position);
    }
}

class BusinessSelectViewHolder extends RecyclerView.ViewHolder {
    private TextView title;
    private TextView createdAt;
    private CheckBox checkBox;
    private boolean isChecked = false;

    public BusinessSelectViewHolder(@NonNull View itemView, BusinessSelectItemClickListener listener, ArrayList<BusinessData> list) {
        super(itemView);
        title = itemView.findViewById(R.id.tv_businessName);
        createdAt = itemView.findViewById(R.id.tv_businessCreatedAt);
        checkBox = itemView.findViewById(R.id.checkBox);

        checkBox.setOnClickListener(v -> {
            isChecked = !isChecked;
            int pos = getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                if (isChecked) {
                    BusinessData data = new BusinessData(list.get(pos).getBusinessId(), title.getText().toString(), createdAt.getText().toString()); // 수정 필요
                    listener.onBusinessItemClick(data, pos);
                } else {
                    listener.onBusinessItemClick(null, 0);
                }

            }
        });

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