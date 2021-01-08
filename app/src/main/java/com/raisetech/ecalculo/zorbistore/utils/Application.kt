package com.raisetech.ecalculo.zorbistore.utils


import android.app.Application
import com.raisetech.ecalculo.zorbistore.di.*
import io.paperdb.Paper
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.core.context.startKoin

class ZorbiStore:Application(){
    override fun onCreate() {
        super.onCreate()
        Paper.init(applicationContext)
        startKoin {
            androidContext(this@ZorbiStore)
            androidFileProperties()
            modules(
                listOf(
                   viewModelModule, apiModule, appModule, repositoryModule, persistenceDataModule

                )
            )
        }
    }
}