package com.wiwiwi.pondokmadre.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wiwiwi.pondokmadre.data.repository.AppRepository
import com.wiwiwi.pondokmadre.ui.admin.paymentmethod.ManagePaymentMethodsViewModel
import com.wiwiwi.pondokmadre.ui.admin.property.ManagePropertiesViewModel
import com.wiwiwi.pondokmadre.ui.admin.tenant.ManageTenantsViewModel
import com.wiwiwi.pondokmadre.ui.admin.unit.ManageUnitsViewModel
import com.wiwiwi.pondokmadre.ui.dashboard.DashboardViewModel
import com.wiwiwi.pondokmadre.ui.transaction.AddTransactionViewModel

class ViewModelFactory(private val repository: AppRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AddTransactionViewModel::class.java) -> AddTransactionViewModel(repository) as T
            modelClass.isAssignableFrom(DashboardViewModel::class.java) -> DashboardViewModel(repository) as T
            modelClass.isAssignableFrom(ManagePropertiesViewModel::class.java) -> ManagePropertiesViewModel(repository) as T
            modelClass.isAssignableFrom(ManageUnitsViewModel::class.java) -> ManageUnitsViewModel(repository) as T
            modelClass.isAssignableFrom(ManageTenantsViewModel::class.java) -> ManageTenantsViewModel(repository) as T
            modelClass.isAssignableFrom(ManagePaymentMethodsViewModel::class.java) -> ManagePaymentMethodsViewModel(repository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}