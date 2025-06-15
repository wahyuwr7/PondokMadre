package com.wiwiwi.pondokmadre.ui.admin.tenant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wiwiwi.pondokmadre.data.model.PaymentStatus
import com.wiwiwi.pondokmadre.data.model.entity.PropertyUnitEntity
import com.wiwiwi.pondokmadre.data.model.entity.TenantEntity
import com.wiwiwi.pondokmadre.data.repository.AppRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ManageTenantsViewModel(private val repository: AppRepository) : ViewModel() {

    val tenants: StateFlow<List<TenantEntity>> = repository.getAllTenants()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val units: StateFlow<List<PropertyUnitEntity>> = repository.getAllPropertyUnits()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addTenant(name: String, unitId: Int, dueDate: String, paymentStatus: PaymentStatus) {
        viewModelScope.launch {
            repository.insertTenant(TenantEntity(name = name, unitId = unitId, dueDate = dueDate, paymentStatus = paymentStatus))
        }
    }

    fun updateTenant(tenant: TenantEntity) {
        viewModelScope.launch {
            repository.updateTenant(tenant)
        }
    }

    fun deleteTenant(tenant: TenantEntity) {
        viewModelScope.launch {
            repository.deleteTenant(tenant)
        }
    }
}