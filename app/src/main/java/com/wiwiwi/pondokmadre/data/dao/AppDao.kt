package com.wiwiwi.pondokmadre.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wiwiwi.pondokmadre.data.model.PaymentMethod
import com.wiwiwi.pondokmadre.data.model.entity.PaymentMethodEntity
import com.wiwiwi.pondokmadre.data.model.entity.PropertyEntity
import com.wiwiwi.pondokmadre.data.model.entity.PropertyUnitEntity
import com.wiwiwi.pondokmadre.data.model.entity.TenantEntity
import com.wiwiwi.pondokmadre.data.model.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {

    // --- Operasi untuk Property ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProperty(property: PropertyEntity)

    @Query("SELECT * FROM properties ORDER BY name ASC")
    fun getAllProperties(): Flow<List<PropertyEntity>>


    // --- Operasi untuk PropertyUnit ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPropertyUnit(unit: PropertyUnitEntity)

    @Query("SELECT * FROM property_units WHERE propertyId = :propertyId ORDER BY name ASC")
    fun getUnitsForProperty(propertyId: Int): Flow<List<PropertyUnitEntity>>

    @Query("SELECT * FROM property_units ORDER BY name ASC")
    fun getAllPropertyUnits(): Flow<List<PropertyUnitEntity>>

    // --- Operasi untuk Tenant ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTenant(tenant: TenantEntity)

    @Query("SELECT * FROM tenants ORDER BY name ASC")
    fun getAllTenants(): Flow<List<TenantEntity>>

    // --- Operasi untuk Transaction ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Query("SELECT * FROM transactions ORDER BY transactionDate DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    // --- Operasi untuk Payment Method ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPaymentMethod(paymentMethod: PaymentMethodEntity)

    @Delete()
    suspend fun deletePaymentMethod(paymentMethod: PaymentMethodEntity)

    @Query("SELECT * FROM payment_methods ORDER BY name ASC")
    fun getAllPaymentMethods(): Flow<List<PaymentMethodEntity>>
}