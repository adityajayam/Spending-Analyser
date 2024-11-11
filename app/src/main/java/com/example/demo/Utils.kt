package com.example.demo

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.demo.room.TransactionData
import java.time.LocalDate
import java.time.format.DateTimeFormatterBuilder

class Utils {

    companion object {
        private const val TAG = "Utils"
        fun parseTransactionDetails(
            messageId: String,
            transactionString: String
        ): TransactionData? {
            try {
                val parts = transactionString.split(" ")

                val currency = parts[0]
                val amount = parts[1].replace(",", "").toDouble()

                // Define a custom formatter to handle two-digit years
                val dateFormatter = DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .appendPattern("d-MMM-yy")
                    .toFormatter()
                    .withChronology(java.time.chrono.IsoChronology.INSTANCE)

                val dateString = parts.find { it.matches(Regex("[0-9]{2}-[a-zA-Z]{3}-[0-9]{2}")) }!!
                val date = LocalDate.parse(dateString, dateFormatter)

                val place =
                    parts.subList(parts.lastIndexOf("on") + 1, parts.indexOf("Avl"))
                        .joinToString(" ")
                        .trim().replace(".", "")
                val bankName = "ICICI"
//                    parts.subList(parts.indexOf("using") + 1, parts.indexOf("bank"))
//                        .joinToString(" ")
//                        .trim()
                val cardNumber =
                    parts.subList(parts.indexOf("card") + 1, parts.indexOf("on")).joinToString(" ")
                        .trim()
                val availableLimit =
                    parts[parts.indexOf("limit:") + 2].removeSuffix(".").replace(",", "").trim()
                        .toDouble()

                val txn = TransactionData(
                    messageId.toInt(),
                    currency,
                    amount,
                    date.dayOfMonth,
                    date.monthValue,
                    date.year,
                    place,
                    bankName,
                    cardNumber,
                    availableLimit
                )
                txn.date = date
                return txn
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
                Log.e(TAG, transactionString)
                return null
            }
        }

        fun parseTransactionDetailsUsingRegex(
            messageId: String,
            transactionString: String
        ): TransactionData? {
            try {
//                val regex = Regex(
//                    """(?<currency>\b[A-Z]{3}\b)\s*(?<amount>\d+\.\d+)\s*(?:spent|using)\s*(?<bank>[A-Za-z\s]+)\s*Card\s*(?<cardNumber>\w+)\s*on\s*(?<date>\d{2}-[A-Za-z]{3}-\d{2})\s*at\s*(?<place>[A-Za-z\s]+)\.\s*Avl Lmt:\s*(?<limit>\b[A-Z]{3}\s*\d{1,3}(?:,\d{3})*(?:\.\d{2})?)\."""
//                )
                val regex = Regex(
                    """(?<currency>\w{3}) (?<amount>[\d,]+\.\d{2,3}) spent (?<method>(?:on|using)) (?<bank>.*?) Bank Card (?<cardNumber>XX\d{4}) on (?<date>\d{2}-\w{3}-\d{2}) (?:on|at) (?<place>.*?)\. Avl (?:Lmt|Limit): (?<lmtCcurrency>\w{3}) (?<limit>[\d,]+\.\d{2})"""
                )

                val matchResult = regex.find(transactionString)
                if (matchResult != null) {
                    val currency = matchResult.groups["currency"]?.value!!
                    val amount =
                        matchResult.groups["amount"]?.value?.replace(",", "")?.replace("INR ", "")
                            ?.toDouble()!!
                    val bankName = matchResult.groups["bank"]?.value!!
                    val cardNumber = matchResult.groups["cardNumber"]?.value!!
                    val place = matchResult.groups["place"]?.value!!
                    val availableLimit =
                        matchResult.groups["limit"]?.value?.replace(",", "")?.replace("INR ", "")
                            ?.toDouble()!!

                    val dateFormatter = DateTimeFormatterBuilder()
                        .parseCaseInsensitive()
                        .appendPattern("d-MMM-yy")
                        .toFormatter()
                        .withChronology(java.time.chrono.IsoChronology.INSTANCE)

                    val dateString = matchResult.groups["date"]?.value
                    val date = LocalDate.parse(dateString, dateFormatter)

                    val txn = TransactionData(
                        messageId.toInt(),
                        currency,
                        amount,
                        date.dayOfMonth,
                        date.monthValue,
                        date.year,
                        place,
                        bankName,
                        cardNumber,
                        availableLimit
                    )
                    txn.date = date
                    return txn
                } else {
                    Log.e(TAG, messageId + transactionString)
                    return null
                }
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
                Log.e(TAG, transactionString)
                return null
            }
        }

        fun checkForPermission(context: Context, permission: String): Boolean {
            return ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }
    }
}