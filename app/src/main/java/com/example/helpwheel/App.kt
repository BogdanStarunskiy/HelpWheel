package com.example.helpwheel

import android.app.Application
import androidx.lifecycle.LifecycleOwner

class App: Application() {
    companion object{
        @JvmStatic lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}