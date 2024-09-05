package com.example.client.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client.R;
import com.example.client.common.MapActivity;
import com.example.client.data.TurbinesData;

import java.util.ArrayList;

public class TurbinesSelectAdapter extends RecyclerView.Adapter<TurbinesSelectViewHolder> {
    ArrayList<TurbinesData> list;

    public TurbinesSelectAdapter(ArrayList<TurbinesData> list) {
        this.list = list;
    }

    // INFO : 뷰 홀더에 레이아웃을 연결해주는 코드
    @NonNull
    @Override
    public TurbinesSelectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        // NOTE : LayoutInflater -> 레이아웃 XML 파일을 통해, View 객체를 실체화 하는 역할
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.rv_item_turbines, parent, false);
        return new TurbinesSelectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TurbinesSelectViewHolder holder, int position) {
        // NOTE : 뷰 홀더에 보이는 엘리먼트들을 정의해줌.
        holder.getKorName().setText(list.get(position).getTitle());
        holder.getEngName().setText(list.get(position).getEngTitle());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class TurbinesSelectViewHolder extends RecyclerView.ViewHolder {
    private TextView tv_korName;
    private TextView tv_engName;
    private Button btn_select;

    // INFO : 뷰홀더 내부의 요소들 정의
    public TurbinesSelectViewHolder(@NonNull View itemView) {
        super(itemView);
        // NOTE : 엘리먼트들 가져오기
        tv_korName = itemView.findViewById(R.id.tv_turbineKorName);
        tv_engName = itemView.findViewById(R.id.tv_turbineEngName);
        btn_select = itemView.findViewById(R.id.btn_turbineSelect);
    }

    public TextView getKorName() {
        return this.tv_korName;
    }

    public TextView getEngName() {
        return this.tv_engName;
    }
}