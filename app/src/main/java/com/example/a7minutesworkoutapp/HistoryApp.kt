package com.example.a7minutesworkoutapp

import android.app.Application

class HistoryApp: Application() {

    // this class helps us to get access for dao for our function
    // by lazy is late int for val
    // lazy will load the data when needed not directly
    val db by lazy{
        HistoryDatabase.getInstance(this)
    }

}