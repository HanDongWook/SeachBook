package com.example.searchbook

import android.app.Application
import android.content.Context
import com.example.searchbook.di.appModule
import org.koin.core.context.startKoin

class Application : Application() {

    companion object {
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(appModule)
        }

        context = this.applicationContext
    }
}