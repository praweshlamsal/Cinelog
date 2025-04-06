package com.example.cinelog

import android.app.Application
import android.util.Log
import com.example.cinelog.util.Constant
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging

class CineLog : Application() {
    override fun onCreate() {
        super.onCreate()
        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this)
            Log.d(Constant.CINE_LOG_MAIN, "Firebase initialized in CineLog")
        } else {
            Log.d(Constant.CINE_LOG_MAIN, "Firebase already initialized in CineLog")
        }
        FirebaseMessaging.getInstance().subscribeToTopic("global_notifications")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FCM", "Subscribed to global notifications")
                } else {
                    Log.e("FCM", "Failed to subscribe", task.exception)
                }
            }
    }
    }
