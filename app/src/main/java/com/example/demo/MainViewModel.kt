package com.example.demo

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    @SuppressLint("Range")
    fun readSms(context: Context) {
        val readSmsCursor =
            context.contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, null)
        Log.e(TAG, readSmsCursor?.columnNames.toString())
        if (readSmsCursor?.moveToFirst() == true) {
            for (i in 0..<readSmsCursor.count) {
                if(readSmsCursor.getString(readSmsCursor.getColumnIndex("body")).contains("spent on ICICI Bank Card")){
                    Log.e(TAG, readSmsCursor.getString(readSmsCursor.getColumnIndex("body")).toString())
                }
                readSmsCursor.moveToNext()
            }
        }
        readSmsCursor?.close()
    }

    companion object {
        private const val TAG = "MainViewModel"
    }

}