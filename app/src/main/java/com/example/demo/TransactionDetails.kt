package com.example.demo

import java.time.LocalDate

data class TransactionDetails(
    val currency: String,
    val amount: Int,
    val date: LocalDate,
    val place: String,
    val bankName: String,
    val cardNumber: String,
    val availableLimit: Double
)
