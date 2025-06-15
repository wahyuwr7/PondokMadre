package com.wiwiwi.pondokmadre.data.model

data class TenantStatusInfo(
    val tenantName: String,
    val unitName: String,
    val propertyName: String,
    val paymentStatus: PaymentStatus
)