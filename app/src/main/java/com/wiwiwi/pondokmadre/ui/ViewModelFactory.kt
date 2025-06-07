package com.wiwiwi.pondokmadre.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wiwiwi.pondokmadre.data.repository.AppRepository
import com.wiwiwi.pondokmadre.ui.dashboard.DashboardViewModel
import com.wiwiwi.pondokmadre.ui.transaction.AddTransactionViewModel

class ViewModelFactory(private val repository: AppRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddTransactionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddTransactionViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DashboardViewModel(repository) as T
        }
        // TODO: Tambahkan pengecekan untuk ViewModel lain (misal: DashboardViewModel)
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}