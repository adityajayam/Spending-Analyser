package com.example.demo.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "spending_details")
data class TransactionData(
    @PrimaryKey @ColumnInfo(name = "message_id") val mId: Int,
    @ColumnInfo(name = "currency") val currency: String,
    @ColumnInfo(name = "price") val amount: Double,
    @ColumnInfo(name = "day") val day: Int,
    @ColumnInfo(name = "month") val month: Int,
    @ColumnInfo(name = "year") val year: Int,
    @ColumnInfo(name = "place") val place: String,
    @ColumnInfo(name = "bank_name") val bankName: String,
    @ColumnInfo(name = "card_number") val cardNumber: String,
    @ColumnInfo(name = "available_limit") val availableLimit: Double
) {
    @Ignore
    var date: LocalDate? = null

    @Ignore
    @ColumnInfo(name = "date")
    lateinit var dateInFormat: String
}