package com.wiwiwi.pondokmadre.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.wiwiwi.pondokmadre.data.model.TransactionType
import java.util.Date

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val amount: Double,
    val type: TransactionType,
    val description: String,
    val transactionDate: Date,
    val unitId: Int?,
    val paymentMethodId: Int?
)
