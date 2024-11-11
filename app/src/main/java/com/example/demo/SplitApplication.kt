package com.example.demo

import android.app.Application
import com.example.demo.room.AppDatabase

class SplitApplication : Application() {
    val database by lazy { AppDatabase.getAppDatabase(this) }
    val repository by lazy { TransactionRepository(database) }
}