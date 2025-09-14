package com.example.student.ui.notifications;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.student.R;
import com.example.student.models.Notification;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder> {
    private List<Notification> notificationList;
    private SimpleDateFormat timeFormat;

    public NotificationsAdapter(List<Notification> notificationList) {
        this.notificationList = notificationList;
        this.timeFormat = new SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault());
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification = notificationList.get(position);
        holder.bind(notification);
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    static class NotificationViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvMessage, tvTime, tvType;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvMessage = itemView.findViewById(R.id.tv_message);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvType = itemView.findViewById(R.id.tv_type);
        }

        public void bind(Notification notification) {
            tvTitle.setText(notification.getTitle());
            tvMessage.setText(notification.getMessage());
            
            if (notification.getCreatedAt() != null) {
                SimpleDateFormat timeFormat = new SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault());
                tvTime.setText(timeFormat.format(notification.getCreatedAt()));
            }
            
            // Set type indicator
            String typeText = "";
            switch (notification.getType()) {
                case Notification.TYPE_BUS_ARRIVAL:
                    typeText = "Arrival";
                    break;
                case Notification.TYPE_BUS_DELAY:
                    typeText = "Delay";
                    break;
                case Notification.TYPE_EMERGENCY:
                    typeText = "Emergency";
                    break;
                case Notification.TYPE_SCHEDULE_CHANGE:
                    typeText = "Schedule";
                    break;
                default:
                    typeText = "General";
                    break;
            }
            tvType.setText(typeText);
        }
    }
}

