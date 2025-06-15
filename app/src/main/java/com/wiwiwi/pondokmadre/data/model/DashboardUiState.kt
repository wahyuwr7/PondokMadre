package com.wiwiwi.pondokmadre.data.model

data class DashboardUiState(
    val tenantStatusList: List<TenantStatusInfo> = emptyList(), // Diubah
    val totalIncome: Double = 0.0,
    val totalExpense: Double = 0.0,
    val netProfit: Double = 0.0
)