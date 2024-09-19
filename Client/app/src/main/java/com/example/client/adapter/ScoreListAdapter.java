package com.example.client.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client.R;
import com.example.client.data.BusinessData;
import com.example.client.data.ScoreData;
import com.example.client.data.TurbinesData;

import java.util.ArrayList;

public class ScoreListAdapter extends RecyclerView.Adapter<ScoreListAdapterViewHolder> {
    ArrayList<ScoreData> list;

    public ScoreListAdapter(ArrayList<ScoreData> list) {
        this.list = list;
    }

    // INFO : 뷰 홀더에 레이아웃을 연결해주는 코드
    @NonNull
    @Override
    public ScoreListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext(); // NOTE : context -> 현재 사용되고 있는 앱에 대한 포괄적인 정보를 지니고 있는 객체
        // NOTE : LayoutInflater -> 레이아웃 XML 파일을 통해, View 객체를 실체화 하는 역할
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.rv_item_scorelist, parent, false);
        return new ScoreListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreListAdapterViewHolder holder, int position) {
        // NOTE : 뷰 홀더에 보이는 엘리먼트들을 정의해줌.
        holder.getTv_title().setText(list.get(position).getTitle());
        holder.getTv_observerName().setText(list.get(position).getObserverName());
        holder.getCreatedAt().setText(list.get(position).getCreatedAt());
        holder.getSkb1().setProgress(list.get(position).getScore1());
        holder.getSkb2().setProgress(list.get(position).getScore2());
        holder.getSkb3().setProgress(list.get(position).getScore3());
        holder.getSkb4().setProgress(list.get(position).getScore4());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void pageChange(ArrayList<ScoreData> resultList) {
        list = resultList;
    }
}

class ScoreListAdapterViewHolder extends RecyclerView.ViewHolder {
    private TextView tv_title;
    private TextView tv_observerName;
    private TextView createdAt;
    private SeekBar skb1;
    private SeekBar skb2;
    private SeekBar skb3;
    private SeekBar skb4;

    // INFO : 뷰홀더 내부의 요소들 정의
    public ScoreListAdapterViewHolder(@NonNull View itemView) {
        super(itemView);
        tv_title = itemView.findViewById(R.id.tv_scorelist_businessName);
        tv_observerName = itemView.findViewById(R.id.tv_scorelist_observerName);
        createdAt = itemView.findViewById(R.id.tv_scorelist_createdAt);
        skb1 = itemView.findViewById(R.id.seekBar_score1);
        skb2 = itemView.findViewById(R.id.seekBar_score2);
        skb3 = itemView.findViewById(R.id.seekBar_score3);
        skb4 = itemView.findViewById(R.id.seekBar_score4);
        skb1.setEnabled(false);
        skb2.setEnabled(false);
        skb3.setEnabled(false);
        skb4.setEnabled(false);
    }

    public TextView getTv_title() {
        return tv_title;
    }

    public TextView getTv_observerName() {
        return tv_observerName;
    }

    public TextView getCreatedAt() {
        return createdAt;
    }

    public SeekBar getSkb1() {
        return skb1;
    }

    public SeekBar getSkb2() {
        return skb2;
    }

    public SeekBar getSkb3() {
        return skb3;
    }

    public SeekBar getSkb4() {
        return skb4;
    }

}