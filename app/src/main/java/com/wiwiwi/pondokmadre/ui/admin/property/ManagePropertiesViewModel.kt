package com.wiwiwi.pondokmadre.ui.admin.property

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wiwiwi.pondokmadre.data.model.entity.PropertyEntity
import com.wiwiwi.pondokmadre.data.repository.AppRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ManagePropertiesViewModel(private val repository: AppRepository) : ViewModel() {

    val properties: StateFlow<List<PropertyEntity>> = repository.getAllProperties()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addProperty(name: String, code: String) {
        viewModelScope.launch {
            repository.insertProperty(PropertyEntity(name = name, code = code))
        }
    }

    fun updateProperty(property: PropertyEntity) {
        viewModelScope.launch {
            repository.updateProperty(property)
        }
    }

    fun deleteProperty(property: PropertyEntity) {
        viewModelScope.launch {
            repository.deleteProperty(property)
        }
    }
}
