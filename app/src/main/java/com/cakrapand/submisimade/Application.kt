package com.cakrapand.submisimade

import android.app.Application
import com.cakrapand.submisimade.di.useCaseModule
import com.cakrapand.submisimade.di.viewModelModule
import databaseModule
import networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import repositoryModule

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@Application)
            modules(
                databaseModule,
                networkModule,
                useCaseModule,
                viewModelModule,
                repositoryModule,
            )
        }
    }
}