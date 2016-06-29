package com.itsronald.twenty2020

import android.app.Application
import com.itsronald.twenty2020.model.CycleComponent
import com.itsronald.twenty2020.model.CycleModule
import com.itsronald.twenty2020.model.DaggerCycleComponent
import com.squareup.leakcanary.LeakCanary

import timber.log.Timber

class Twenty2020Application : Application() {

    val cycleComponent: CycleComponent =
            DaggerCycleComponent.builder().cycleModule(CycleModule(this)).build()

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Timber.i("Timber logger planted.")
        }
        LeakCanary.install(this)
    }
}
