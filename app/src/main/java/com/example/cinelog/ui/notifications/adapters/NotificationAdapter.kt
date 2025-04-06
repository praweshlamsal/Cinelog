package com.example.cinelog.ui.notifications.adapters

import Notification
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cinelog.databinding.ItemNotificationBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NotificationAdapter(private var notificationEvent: List<Notification>) :
    RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding = ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notificationEvent[position]
        holder.bind(notification)
    }

    override fun getItemCount(): Int = notificationEvent.size


    class NotificationViewHolder(private val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(notification: Notification) {
            binding.movieName.text = notification.movieName
            binding.releaseDate.text = notification.releaseDate
            Glide.with(itemView.context).load(notification.posterUrl).into(binding.posterImage)
            binding.timestamp.text = SimpleDateFormat("MMM dd, yyyy HH:mm:ss", Locale.getDefault())
                .format(notification.timestamp.toDate())

        }
    }
}