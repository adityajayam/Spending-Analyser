package com.example.demo

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.demo.room.TransactionData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.ArrayList

class MainViewModel(private val transactionRepository: TransactionRepository) : ViewModel() {
    private var transactionList: ArrayList<TransactionData> = arrayListOf()

    var txnDbData: LiveData<ArrayList<TransactionData>> =
        transactionRepository.getTransactionData().asLiveData()
    private var lastMessageId = transactionRepository.getLatestMessageId().asLiveData()

    @SuppressLint("Range")
    suspend fun readSms(context: Context, lastMessageId: Int) {
        withContext(Dispatchers.IO) {
            val readSmsCursor =
                context.contentResolver.query(
                    Uri.parse("content://sms/inbox"),
                    null,
                    null,
                    null,
                    null
                )
            //Log.e(TAG, readSmsCursor?.columnNames.toString())
            if (readSmsCursor?.moveToFirst() == true) {
                for (i in 0..<readSmsCursor.count) {
                    val messageBody = readSmsCursor.getString(readSmsCursor.getColumnIndex("body"))
                    val messageId = readSmsCursor.getString(readSmsCursor.getColumnIndex("_id"))
                    if (messageBody.contains("ICICI Bank Card XX") && messageId.toInt() > lastMessageId.toInt()) {
                        Log.e(TAG, messageBody)
                        val txnData = Utils.parseTransactionDetailsUsingRegex(
                            readSmsCursor.getString(
                                readSmsCursor.getColumnIndex("_id")
                            ), messageBody
                        )
                        if (txnData != null) {
                            transactionList.add(txnData)
                        }
                    }
                    readSmsCursor.moveToNext()
                }
            }
            readSmsCursor?.close()
        }
    }

    fun saveTxnDataInDb(context: Context) {
        Log.e(TAG,"saveTxnDataInDb()..")
        viewModelScope.launch {
            readSms(context, transactionRepository.getLasMessageIdAsInt()?:0)
            Log.e(TAG, "LatestMessageId:${transactionRepository.getLasMessageIdAsInt()}")
            transactionRepository.insertAll(transactionList.toList())
            txnDbData = transactionRepository.getTransactionData().asLiveData()
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(private val transactionRepository: TransactionRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(transactionRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}