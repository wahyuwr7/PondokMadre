package com.wiwiwi.pondokmadre.data.model

import androidx.room.PrimaryKey

data class PaymentMethod(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String
)