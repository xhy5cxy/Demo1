package com.tour.app.strategy.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tour.app.R;
import com.tour.app.strategy.model.Comment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<Comment> commentList;

    public CommentAdapter(List<Comment> commentList) {
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        
        // 设置用户头像
        holder.ivAvatar.setImageResource(comment.getUserAvatar());
        
        // 设置用户信息
        holder.tvUsername.setText(comment.getUsername());
        
        // 设置评论时间
        String timeText = formatTime(comment.getCreateTime());
        holder.tvTime.setText(timeText);
        
        // 设置评论内容
        holder.tvCommentContent.setText(comment.getContent());
        
        // 设置点赞数
        holder.tvCommentLikeCount.setText(String.valueOf(comment.getLikeCount()));
        
        // 设置点赞按钮状态
        if (comment.isLiked()) {
            holder.ivCommentLike.setImageResource(R.drawable.ic_like);
            holder.ivCommentLike.setColorFilter(holder.itemView.getContext()
                    .getResources().getColor(R.color.color_primary));
        } else {
            holder.ivCommentLike.setImageResource(R.drawable.ic_like);
            holder.ivCommentLike.setColorFilter(holder.itemView.getContext()
                    .getResources().getColor(R.color.text_secondary));
        }
        
        // 点赞按钮点击事件
        holder.ivCommentLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isLiked = comment.isLiked();
                comment.setLiked(!isLiked);
                
                if (!isLiked) {
                    comment.setLikeCount(comment.getLikeCount() + 1);
                    Toast.makeText(v.getContext(), "点赞成功", Toast.LENGTH_SHORT).show();
                } else {
                    comment.setLikeCount(Math.max(0, comment.getLikeCount() - 1));
                    Toast.makeText(v.getContext(), "取消点赞", Toast.LENGTH_SHORT).show();
                }
                
                notifyItemChanged(position);
            }
        });
    }

    private String formatTime(long timestamp) {
        long currentTime = System.currentTimeMillis();
        long diff = currentTime - timestamp;
        
        if (diff < 60000) { // 1分钟内
            return "刚刚";
        } else if (diff < 3600000) { // 1小时内
            return (diff / 60000) + "分钟前";
        } else if (diff < 86400000) { // 1天内
            return (diff / 3600000) + "小时前";
        } else if (diff < 604800000) { // 1周内
            return (diff / 86400000) + "天前";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            return sdf.format(new Date(timestamp));
        }
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public void updateComments(List<Comment> newComments) {
        this.commentList = newComments;
        notifyDataSetChanged();
    }

    public void addComment(Comment comment) {
        commentList.add(0, comment); // 添加到顶部
        notifyItemInserted(0);
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAvatar;
        TextView tvUsername;
        TextView tvTime;
        TextView tvCommentContent;
        ImageView ivCommentLike;
        TextView tvCommentLikeCount;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            
            ivAvatar = itemView.findViewById(R.id.iv_avatar);
            tvUsername = itemView.findViewById(R.id.tv_username);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvCommentContent = itemView.findViewById(R.id.tv_comment_content);
            ivCommentLike = itemView.findViewById(R.id.iv_comment_like);
            tvCommentLikeCount = itemView.findViewById(R.id.tv_comment_like_count);
        }
    }
}