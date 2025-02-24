package com.example.cinelog

import android.app.Application
import android.util.Log
import com.example.cinelog.util.Constant
import com.google.firebase.FirebaseApp

class CineLog : Application() {
    override fun onCreate() {
        super.onCreate()
        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this)
            Log.d(Constant.CINE_LOG_MAIN, "Firebase initialized in CineLog")
        } else {
            Log.d(Constant.CINE_LOG_MAIN, "Firebase already initialized in CineLog")
        }
    }
    }
