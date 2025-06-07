package com.wiwiwi.pondokmadre.data.util

import androidx.room.TypeConverter
import com.wiwiwi.pondokmadre.data.model.PaymentStatus
import com.wiwiwi.pondokmadre.data.model.TransactionType
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromPaymentStatus(value: String) = enumValueOf<PaymentStatus>(value)

    @TypeConverter
    fun toPaymentStatus(value: PaymentStatus) = value.name

    @TypeConverter
    fun fromTransactionType(value: String) = enumValueOf<TransactionType>(value)

    @TypeConverter
    fun toTransactionType(value: TransactionType) = value.name
}