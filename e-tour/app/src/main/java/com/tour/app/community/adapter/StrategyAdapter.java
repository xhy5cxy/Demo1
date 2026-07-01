package com.tour.app.community.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tour.app.R;
import com.tour.app.model.Strategy;
import com.tour.app.strategy.ui.StrategyDetailActivity;

import java.util.List;

public class StrategyAdapter extends RecyclerView.Adapter<StrategyAdapter.StrategyViewHolder> {
    
    private Context context;
    private List<Strategy> strategyList;
    
    public StrategyAdapter(Context context, List<Strategy> strategyList) {
        this.context = context;
        this.strategyList = strategyList;
    }
    
    @NonNull
    @Override
    public StrategyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_strategy, parent, false);
        return new StrategyViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull StrategyViewHolder holder, int position) {
        Strategy strategy = strategyList.get(position);
        
        holder.tvTitle.setText(strategy.getTitle());
        holder.tvContent.setText(strategy.getContent());
        
        // 设置点击事件 - 跳转到攻略详情页
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, StrategyDetailActivity.class);
                intent.putExtra("strategy_id", strategy.getId());
                intent.putExtra("strategy_title", strategy.getTitle());
                intent.putExtra("strategy_content", strategy.getContent());
                intent.putExtra("strategy_destination", strategy.getDestination());
                intent.putExtra("strategy_like_count", strategy.getLikeCount());
                intent.putExtra("strategy_view_count", strategy.getViewCount());
                context.startActivity(intent);
            }
        });
        
        // 设置点赞点击事件
        holder.ivLike.setOnClickListener(v -> {
            int currentLikes = strategy.getLikeCount() != null ? strategy.getLikeCount() : 0;
            strategy.setLikeCount(currentLikes + 1);
            Toast.makeText(context, "点赞成功！", Toast.LENGTH_SHORT).show();
        });
    }
    
    @Override
    public int getItemCount() {
        return strategyList != null ? strategyList.size() : 0;
    }
    
    public static class StrategyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvContent, tvUserName, tvDays;
        ImageView ivLike;
        
        public StrategyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvContent = itemView.findViewById(R.id.tv_content);
            tvUserName = itemView.findViewById(R.id.tv_user_name);
            tvDays = itemView.findViewById(R.id.tv_days);
            ivLike = itemView.findViewById(R.id.iv_like);
        }
    }
}