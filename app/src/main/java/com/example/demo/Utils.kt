package com.example.demo

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatterBuilder

class Utils {
    @RequiresApi(Build.VERSION_CODES.O)
    fun parseTransactionDetails(transactionString: String): TransactionDetails {
        val parts = transactionString.split(" ")

        val currency = parts[0]
        val amount = parts[1].replace(",", "").toInt()

        // Define a custom formatter to handle two-digit years
        val dateFormatter = DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendPattern("d-MMM-yy")
            .toFormatter()
            .withChronology(java.time.chrono.IsoChronology.INSTANCE)

        val dateString = parts.find { it.matches(Regex("[0-9]{2}-[a-zA-Z]{3}-[0-9]{2}")) }!!
        val date = LocalDate.parse(dateString, dateFormatter)

        val place =
            parts.subList(parts.lastIndexOf("on") + 1, parts.indexOf("Avl")).joinToString(" ")
                .trim().replace(".", "")
        val bankName =
            parts.subList(parts.indexOf("using") + 1, parts.indexOf("bank")).joinToString(" ")
                .trim()
        val cardNumber =
            parts.subList(parts.indexOf("card") + 1, parts.indexOf("on")).joinToString(" ").trim()
        val availableLimit =
            parts[parts.indexOf("limit:") + 2].removeSuffix(".").replace(",", "").trim().toDouble()

        return TransactionDetails(
            currency,
            amount,
            date,
            place,
            bankName,
            cardNumber,
            availableLimit
        )
    }
}