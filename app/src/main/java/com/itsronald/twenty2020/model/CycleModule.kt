package com.itsronald.twenty2020.model

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Provides the singleton Cycle to components that require it.
 */
@Module
class CycleModule(private val context: Context) {

    @Provides
    @Singleton
    fun provideCycle(): Cycle = Cycle(context)
}