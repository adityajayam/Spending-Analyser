package com.example.demo

import android.os.Bundle
import android.Manifest
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var txnListRecyclerView: RecyclerView
    private lateinit var permission: ActivityResultLauncher<String>
    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory((application as SplitApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.mainactivity)
        init()
        if (!Utils.checkForPermission(this, Manifest.permission.READ_SMS)) {
            permission.launch(Manifest.permission.READ_SMS)
        } else {
            dbOperations()
        }
    }

    private fun init() {
        txnListRecyclerView = findViewById(R.id.txn_list)
        permission =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    dbOperations()
                } else {
                    //TODO kill the app, ideally show rationale UI
                    finish()
                }
            }
    }

    private fun dbOperations() {
        viewModel.saveTxnDataInDb(this)
        viewModel.txnDbData.observe(this) { txnList ->
            Log.e(TAG,"txnlist changed")
            val txnAdapter = TransactionListAdapter(txnList)
            txnListRecyclerView.layoutManager = LinearLayoutManager(this)
            txnListRecyclerView.adapter = txnAdapter
        }
//        viewModel.lastMessageId.observe(this){
//            viewModel.readSms(this,it)
//        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
