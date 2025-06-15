package com.wiwiwi.pondokmadre.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wiwiwi.pondokmadre.data.model.DashboardUiState
import com.wiwiwi.pondokmadre.data.model.PaymentStatus
import com.wiwiwi.pondokmadre.data.model.TenantStatusInfo
import com.wiwiwi.pondokmadre.data.model.TransactionType
import com.wiwiwi.pondokmadre.data.repository.AppRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.util.Calendar

class DashboardViewModel(private val repository: AppRepository) : ViewModel() {

    // Menggabungkan semua flow yang dibutuhkan oleh dashboard menjadi satu UI State
    val uiState: StateFlow<DashboardUiState> = combine(
        repository.getAllTenants(),
        repository.getAllPropertyUnits(),
        repository.getAllProperties(),
        repository.getAllTransactions()
    ) { tenants, units, properties, transactions ->
        // Logika kalkulasi untuk bulan berjalan
        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentYear = calendar.get(Calendar.YEAR)

        val monthlyTransactions = transactions.filter {
            calendar.time = it.transactionDate
            calendar.get(Calendar.MONTH) == currentMonth && calendar.get(Calendar.YEAR) == currentYear
        }

        val totalIncome = monthlyTransactions
            .filter { it.type == TransactionType.INCOME }
            .sumOf { it.amount }

        val totalExpense = monthlyTransactions
            .filter { it.type == TransactionType.EXPENSE }
            .sumOf { it.amount }

        // (BARU) Logika untuk menentukan status pembayaran setiap penyewa
        val tenantStatusList = tenants.map { tenant ->
            val unit = units.find { it.id == tenant.unitId }
            val property = properties.find { it.id == unit?.propertyId }

            val paymentAmount = monthlyTransactions
                .filter { it.unitId == tenant.unitId && it.type == TransactionType.INCOME }
                .sumOf { it.amount }

            val status = when {
                unit == null -> PaymentStatus.UNPAID // Jika unit tidak ditemukan
                paymentAmount >= unit.price -> PaymentStatus.PAID
                paymentAmount > 0 -> PaymentStatus.PARTIALLY_PAID
                else -> PaymentStatus.UNPAID
            }

            TenantStatusInfo(
                tenantName = tenant.name,
                unitName = unit?.name ?: "N/A",
                propertyName = property?.name ?: "N/A",
                paymentStatus = status
            )
        }

        DashboardUiState(
            tenantStatusList = tenantStatusList,
            totalIncome = totalIncome,
            totalExpense = totalExpense,
            netProfit = totalIncome - totalExpense
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardUiState() // State awal sebelum data masuk
    )
}