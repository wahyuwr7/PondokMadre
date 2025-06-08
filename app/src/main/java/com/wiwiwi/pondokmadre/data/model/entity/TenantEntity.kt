package com.wiwiwi.pondokmadre.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.wiwiwi.pondokmadre.data.model.PaymentStatus

@Entity(tableName = "tenants")
data class TenantEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val unitId: Int?,
    val dueDate: String,
    val paymentStatus: PaymentStatus
)
