package com.wiwiwi.pondokmadre.ui.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wiwiwi.pondokmadre.data.model.TransactionType
import com.wiwiwi.pondokmadre.data.model.entity.PaymentMethodEntity
import com.wiwiwi.pondokmadre.data.model.entity.PropertyUnitEntity
import com.wiwiwi.pondokmadre.data.model.entity.TenantEntity
import com.wiwiwi.pondokmadre.data.model.entity.TransactionEntity
import com.wiwiwi.pondokmadre.data.repository.AppRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date

class AddTransactionViewModel(private val repository: AppRepository) : ViewModel() {

    val allTenants: StateFlow<List<TenantEntity>> = repository.getAllTenants()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allUnits: StateFlow<List<PropertyUnitEntity>> = repository.getAllPropertyUnits()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allPaymentMethods: StateFlow<List<PaymentMethodEntity>> = repository.getAllPaymentMethods()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun saveTransaction(
        amount: Double,
        type: TransactionType,
        description: String,
        transactionDate: Date,
        tenant: TenantEntity?,
        paymentMethod: PaymentMethodEntity?
    ) {
        viewModelScope.launch {
            val newTransaction = TransactionEntity(
                amount = amount,
                type = type,
                description = description,
                transactionDate = transactionDate,
                unitId = tenant?.unitId,
                paymentMethodId = paymentMethod?.id
            )
            repository.insertTransaction(newTransaction)
        }
    }
}