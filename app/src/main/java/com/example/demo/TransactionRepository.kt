package com.example.demo

import android.util.Log
import androidx.lifecycle.asLiveData
import com.example.demo.room.AppDatabase
import com.example.demo.room.TransactionData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.ArrayList

@Suppress("UNCHECKED_CAST")
class TransactionRepository(private val appDatabase: AppDatabase) {
    suspend fun insert(transactionData: TransactionData) {
        withContext(Dispatchers.IO) {
            appDatabase.transactionDao().insertTransactionData(transactionData)
        }
    }

    suspend fun insertAll(tractionList: List<TransactionData>) {
        withContext(Dispatchers.IO) {
            appDatabase.transactionDao().insertAll(tractionList)
        }
    }

    suspend fun deleteAllDetails() {
        withContext(Dispatchers.IO) {
            appDatabase.transactionDao().deleteAllDetails()
        }
    }

    fun getTransactionData(): Flow<ArrayList<TransactionData>> {
        val data = appDatabase.transactionDao()
            .getAllTransactionData() as Flow<ArrayList<TransactionData>>
        Log.e(TAG, data.asLiveData().value?.size.toString())
        return data
    }

    fun getLatestMessageId(): Flow<Int> {
        return appDatabase.transactionDao().getLatestMessageId()
    }

    suspend fun getLasMessageIdAsInt(): Int? {
        return appDatabase.transactionDao().getLastMessageIdAsInt()
    }

    companion object {
        private const val TAG = "TransactionRepository"
    }
}