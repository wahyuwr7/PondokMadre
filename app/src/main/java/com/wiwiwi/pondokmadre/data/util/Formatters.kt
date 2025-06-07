package com.wiwiwi.pondokmadre.data.util

import java.text.NumberFormat
import java.util.Locale

fun formatRupiah(amount: Double): String {
    val localeID = Locale("in", "ID")
    val format = NumberFormat.getCurrencyInstance(localeID)
    format.maximumFractionDigits = 0 // Tidak menampilkan desimal
    return format.format(amount)
}