package com.tour.app.admin.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.tour.app.R;
import com.tour.app.admin.model.AdminMenuItem;
import java.util.List;

public class AdminMenuAdapter extends RecyclerView.Adapter<AdminMenuAdapter.ViewHolder> {
    
    private List<AdminMenuItem> menuItems;
    private OnItemClickListener onItemClickListener;
    
    public AdminMenuAdapter(List<AdminMenuItem> menuItems) {
        this.menuItems = menuItems;
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_admin_menu, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AdminMenuItem item = menuItems.get(position);
        
        holder.ivIcon.setImageResource(item.getIconResId());
        holder.tvTitle.setText(item.getTitle());
        holder.tvDescription.setText(item.getDescription());
        
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(item);
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return menuItems.size();
    }
    
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    
    public interface OnItemClickListener {
        void onItemClick(AdminMenuItem item);
    }
    
    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvTitle;
        TextView tvDescription;
        
        ViewHolder(View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_menu_icon);
            tvTitle = itemView.findViewById(R.id.tv_menu_title);
            tvDescription = itemView.findViewById(R.id.tv_menu_description);
        }
    }
}