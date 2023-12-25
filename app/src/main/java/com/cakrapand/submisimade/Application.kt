package com.cakrapand.submisimade

import android.app.Application
import com.cakrapand.core.di.databaseModule
import com.cakrapand.core.di.networkModule
import com.cakrapand.core.di.repositoryModule
import com.cakrapand.submisimade.di.useCaseModule
import com.cakrapand.submisimade.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

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