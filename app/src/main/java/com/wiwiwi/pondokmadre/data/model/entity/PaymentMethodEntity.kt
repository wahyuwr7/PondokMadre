package com.wiwiwi.pondokmadre.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "payment_methods")
data class PaymentMethodEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String // Contoh: "Tunai", "Transfer Bank", "QRIS"
)