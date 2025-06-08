package com.wiwiwi.pondokmadre.data.model

data class PropertyUnit(
    val id: Int,
    val code: String,
    val name: String,
    val price: Double,
    val propertyId: Int,
    val propertyCode: String
)
