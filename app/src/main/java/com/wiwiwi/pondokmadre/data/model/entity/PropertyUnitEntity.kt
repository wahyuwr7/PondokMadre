package com.wiwiwi.pondokmadre.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "property_units")
data class PropertyUnitEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val code: String,
    val name: String,
    val price: Double,
    val propertyId: Int,
    val propertyCode: String
)
