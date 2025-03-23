package com.example.cinelog.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationCountReceiver(private val updateBadge: (Int) -> Unit) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val newCount = intent?.getIntExtra("notification_count", 0) ?: 0
        updateBadge(newCount)
    }
}