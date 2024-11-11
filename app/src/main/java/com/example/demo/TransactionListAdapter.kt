package com.example.demo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.demo.room.TransactionData
import java.util.ArrayList

class TransactionListAdapter(private val txnList :ArrayList<TransactionData>) : RecyclerView.Adapter<TransactionListAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val placeTextView: TextView = view.findViewById(R.id.txt_place_name)
        val amountTextView: TextView = view.findViewById(R.id.txt_amount)
        val dateTextView:TextView = view.findViewById(R.id.txt_date)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.transaction_list_row_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val rowItem = txnList[position]
        viewHolder.placeTextView.text = rowItem.place
        viewHolder.amountTextView.text = rowItem.amount.toString()
        viewHolder.dateTextView.text = "${rowItem.day}/${rowItem.month}/${rowItem.year}"
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = txnList.size

    companion object {
        private const val TAG = "TransactionListAdapter"
    }

}