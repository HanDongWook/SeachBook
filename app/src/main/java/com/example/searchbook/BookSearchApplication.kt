package com.example.searchbook

import android.app.Application
import com.example.searchbook.di.appModule
import org.koin.core.context.startKoin

class BookSearchApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(appModule)
        }
    }
}