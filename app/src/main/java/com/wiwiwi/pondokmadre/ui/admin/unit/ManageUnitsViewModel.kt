package com.wiwiwi.pondokmadre.ui.admin.unit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wiwiwi.pondokmadre.data.model.entity.PropertyEntity
import com.wiwiwi.pondokmadre.data.model.entity.PropertyUnitEntity
import com.wiwiwi.pondokmadre.data.repository.AppRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ManageUnitsViewModel(private val repository: AppRepository) : ViewModel() {

    val units: StateFlow<List<PropertyUnitEntity>> = repository.getAllPropertyUnits()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val properties: StateFlow<List<PropertyEntity>> = repository.getAllProperties()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addUnit(name: String, code: String, price: Double, propertyId: Int, propertyCode: String) {
        viewModelScope.launch {
            repository.insertPropertyUnit(PropertyUnitEntity(name = name, code = code, price = price, propertyId = propertyId, propertyCode = propertyCode))
        }
    }

    fun updateUnit(unit: PropertyUnitEntity) {
        viewModelScope.launch {
            repository.updatePropertyUnit(unit)
        }
    }

    fun deleteUnit(unit: PropertyUnitEntity) {
        viewModelScope.launch {
            repository.deletePropertyUnit(unit)
        }
    }
}