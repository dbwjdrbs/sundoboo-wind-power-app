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
    private boolean isOnServer;

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
        return new BusinessSelectViewHolder(view, listener, list, isOnServer);
    }

    @Override
    public void onBindViewHolder(@NonNull BusinessSelectViewHolder holder, int position) {
        BusinessData businessData = list.get(position);
        holder.getCheckBox().setChecked(businessData.isChecked());

        holder.getTitle().setText(list.get(position).getTitle());
        holder.getCreatedAt().setText("등록 일자 : " + list.get(position).getCreatedAt());
        holder.getCheckBox().setOnCheckedChangeListener((buttonView, isChecked) -> {
            businessData.setChecked(isChecked);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addItem(BusinessData businessData) {
        list.add(0, businessData);
    }

    public void searchMode(ArrayList<BusinessData> resultDatas) {
        list = resultDatas;
    }

    public void removeItem(long businessId) {
        list.removeIf(data -> data.getBusinessId() == businessId);
    }

    public void isOnServer(boolean bool) {
        isOnServer = bool;
    }
}

class BusinessSelectViewHolder extends RecyclerView.ViewHolder {
    private TextView title;
    private TextView createdAt;
    private CheckBox checkBox;

    public BusinessSelectViewHolder(@NonNull View itemView, BusinessSelectItemClickListener listener, ArrayList<BusinessData> list, boolean isOnServer) {
        super(itemView);
        title = itemView.findViewById(R.id.tv_businessName);
        createdAt = itemView.findViewById(R.id.tv_businessCreatedAt);
        checkBox = itemView.findViewById(R.id.checkBox);

        checkBox.setOnClickListener(v -> {
            int pos = getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                BusinessData data = new BusinessData(list.get(pos).getBusinessId(), title.getText().toString(), createdAt.getText().toString()); // 수정 필요
                listener.onBusinessItemClick(data, pos);
            }
        });

        itemView.setOnClickListener(view -> {
            int pos = getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                BusinessData data = new BusinessData(list.get(pos).getBusinessId(), title.getText().toString(), createdAt.getText().toString()); // 수정 필요
                Intent intent = new Intent(view.getContext(), MapActivity.class);
                intent.putExtra("businessId", data.getBusinessId());
                intent.putExtra("businessTitle", data.getTitle());
                intent.putExtra("isOnServer", isOnServer);
                view.getContext().startActivity(intent);
            }
        });
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public TextView getTitle() {
        return this.title;
    }

    public TextView getCreatedAt() {
        return this.createdAt;
    }
}