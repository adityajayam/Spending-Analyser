package com.example.demo.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(transactionData: List<TransactionData>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactionData(transactionData: TransactionData)

    @Query("SELECT * FROM spending_details")
    fun getAllTransactionData(): Flow<List<TransactionData>>

    @Query("DELETE FROM spending_details")
    suspend fun deleteAllDetails()

    @Query("SELECT MAX(message_id) FROM spending_details")
    fun getLatestMessageId(): Flow<Int>

    @Query("SELECT MAX(message_id) FROM spending_details")
    suspend fun getLastMessageIdAsInt():Int?
}