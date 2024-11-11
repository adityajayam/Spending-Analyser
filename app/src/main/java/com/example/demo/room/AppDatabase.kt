package com.example.demo.room

import android.annotation.SuppressLint
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [TransactionData::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao

    companion object {
        private var db: AppDatabase? = null
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            @SuppressLint("Range")
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE spending_details ADD COLUMN dateInFormat TEXT NOT NULL DEFAULT ''")
                val cursor = db.query("SELECT message_id, day, month, year FROM spending_details")
                cursor.moveToFirst()
                while (!cursor.isLast) {
                    val day = cursor.getString(cursor.getColumnIndex("day"))
                    val month = cursor.getString(cursor.getColumnIndex("month"))
                    val year = cursor.getString(cursor.getColumnIndex("year"))
                    val messageId = cursor.getInt(cursor.getColumnIndex("message_id"))
                    val dateInFormat = "$day/$month/$year"
                    db.execSQL(
                        "UPDATE spending_details SET dateInFormat = ? WHERE message_id = ?",
                        arrayOf(dateInFormat, messageId)
                    )
                    cursor.moveToNext()
                }
            }
        }

        fun getAppDatabase(context: Context): AppDatabase {
            if (db != null) {
                return db as AppDatabase
            } else synchronized(this) {
                val db = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "spent_analyzer"
                ).addMigrations(MIGRATION_2_3).build()
                return db
            }
        }
    }
}
