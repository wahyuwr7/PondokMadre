package com.wiwiwi.pondokmadre.data.repository

import com.wiwiwi.pondokmadre.data.dao.AppDao
import com.wiwiwi.pondokmadre.data.model.entity.PaymentMethodEntity
import com.wiwiwi.pondokmadre.data.model.entity.PropertyEntity
import com.wiwiwi.pondokmadre.data.model.entity.PropertyUnitEntity
import com.wiwiwi.pondokmadre.data.model.entity.TenantEntity
import com.wiwiwi.pondokmadre.data.model.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

class AppRepository(private val appDao: AppDao) {
    fun getAllTenants(): Flow<List<TenantEntity>> = appDao.getAllTenants()
    fun getAllPropertyUnits(): Flow<List<PropertyUnitEntity>> = appDao.getAllPropertyUnits()
    fun getAllProperties(): Flow<List<PropertyEntity>> = appDao.getAllProperties()
    fun getAllPaymentMethods(): Flow<List<PaymentMethodEntity>> = appDao.getAllPaymentMethods()
    fun getAllTransactions(): Flow<List<TransactionEntity>> = appDao.getAllTransactions()

    suspend fun insertTransaction(transaction: TransactionEntity) {
        appDao.insertTransaction(transaction)
    }

    suspend fun insertProperty(property: PropertyEntity) {
        appDao.insertProperty(property)
    }

    suspend fun updateProperty(property: PropertyEntity) {
        appDao.updateProperty(property)
    }

    suspend fun deleteProperty(property: PropertyEntity) {
        appDao.deleteProperty(property)
    }

    suspend fun insertPropertyUnit(propertyUnit: PropertyUnitEntity) {
        appDao.insertPropertyUnit(propertyUnit)
    }

    suspend fun updatePropertyUnit(propertyUnit: PropertyUnitEntity) {
        appDao.updatePropertyUnit(propertyUnit)
    }

    suspend fun deletePropertyUnit(propertyUnit: PropertyUnitEntity) {
        appDao.deletePropertyUnit(propertyUnit)
    }

    suspend fun insertPaymentMethod(paymentMethodEntity: PaymentMethodEntity) {
        appDao.insertPaymentMethod(paymentMethodEntity)
    }
}