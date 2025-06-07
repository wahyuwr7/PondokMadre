package com.wiwiwi.pondokmadre.data.model

data class Tenant(
    val id: Int,
    val name: String,
    val unitId: Int,
    val dueDate: String,
    val paymentStatus: PaymentStatus
)
