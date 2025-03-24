package com.example.cinelog.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.cinelog.data.local.sharedPref.SharedPrefHelper

class NotificationCountReceiver(private val updateBadge: (Int) -> Unit) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        var sharedPrefHelper = SharedPrefHelper(context!!)
        val newCount = sharedPrefHelper.getNotificationCount();
        updateBadge(newCount)
    }
}