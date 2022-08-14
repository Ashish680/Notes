package com.example.notes.application

import android.annotation.SuppressLint
import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication

class NoteApplication : MultiDexApplication() {
    private lateinit var mContext: Context
    override fun onCreate() {
        super.onCreate()
        instance = this
        mContext = this

    }

    override fun attachBaseContext(context: Context) {
        MultiDex.install(context)
        super.attachBaseContext(context)
    }

    companion object {
        val TAG: String = NoteApplication::class.java.simpleName

        @SuppressLint("StaticFieldLeak")
        @get:Synchronized
        lateinit var instance: NoteApplication
            private set
    }

    fun getContextFromApplication(): Context {
        return mContext
    }
}