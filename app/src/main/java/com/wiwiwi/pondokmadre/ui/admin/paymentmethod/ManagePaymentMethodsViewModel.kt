package com.wiwiwi.pondokmadre.ui.admin.paymentmethod

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wiwiwi.pondokmadre.data.model.entity.PaymentMethodEntity
import com.wiwiwi.pondokmadre.data.repository.AppRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ManagePaymentMethodsViewModel(private val repository: AppRepository) : ViewModel() {

    val paymentMethods: StateFlow<List<PaymentMethodEntity>> = repository.getAllPaymentMethods()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addMethod(name: String) {
        viewModelScope.launch {
            repository.insertPaymentMethod(PaymentMethodEntity(name = name))
        }
    }

    fun updateMethod(method: PaymentMethodEntity) {
        viewModelScope.launch {
            repository.updatePaymentMethod(method)
        }
    }

    fun deleteMethod(method: PaymentMethodEntity) {
        viewModelScope.launch {
            repository.deletePaymentMethod(method)
        }
    }
}