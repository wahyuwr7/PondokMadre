package com.wiwiwi.pondokmadre.data.dummy

import androidx.compose.ui.graphics.Color
import com.wiwiwi.pondokmadre.data.model.PaymentStatus
import com.wiwiwi.pondokmadre.data.model.SummaryData
import com.wiwiwi.pondokmadre.data.model.entity.PropertyEntity
import com.wiwiwi.pondokmadre.data.model.entity.PropertyUnitEntity
import com.wiwiwi.pondokmadre.data.model.entity.TenantEntity

object SampleDataProvider {

    val properties = listOf(
        PropertyEntity(id = 1, code = "MWR", name = "Kontrakan Mawar"),
        PropertyEntity(id = 2, code = "MLT", name = "Paviliun Melati")
    )

    val propertyUnits = listOf(
        PropertyUnitEntity(id = 101, code = "A-01", name = "Kamar A-01", price = 1250000.0, propertyId = 1, propertyCode = "PS1"),
        PropertyUnitEntity(id = 102, code = "A-02", name = "Kamar A-02", price = 1250000.0, propertyId = 1, propertyCode = "PS1"),
        PropertyUnitEntity(id = 103, code = "A-03", name = "Kamar A-03", price = 1300000.0, propertyId = 1,  propertyCode = "PS1"),
        PropertyUnitEntity(id = 201, code = "B-01", name = "Paviliun B-01", price = 2500000.0, propertyId = 2, propertyCode = "PS2"),
        PropertyUnitEntity(id = 104, code = "B-05", name = "Kamar B-05", price = 1200000.0, propertyId = 1, propertyCode = "PS1")
    )

    val sampleSummary = listOf(
        SummaryData("Total Pemasukan", 12500000.0, Color(0xFF4CAF50)),
        SummaryData("Total Pengeluaran", 1250000.0, Color(0xFFF44336)),
        SummaryData("Laba Bersih", 11250000.0, Color(0xFF2196F3))
    )

    val sampleTenants = listOf(
        TenantEntity(1, "Budi Santoso", 101, "05 Jun 2025", PaymentStatus.PAID),
        TenantEntity(2, "Citra Lestari", 102, "05 Jun 2025", PaymentStatus.UNPAID),
        TenantEntity(3, "Doni Firmansyah", 201, "10 Jun 2025", PaymentStatus.PAID),
        TenantEntity(4, "Eka Putri", 103, "15 Jun 2025", PaymentStatus.PARTIALLY_PAID), // Contoh baru
        TenantEntity(5, "Fajar Nugraha", 104, "20 Jun 2025", PaymentStatus.UNPAID)
    )
}
