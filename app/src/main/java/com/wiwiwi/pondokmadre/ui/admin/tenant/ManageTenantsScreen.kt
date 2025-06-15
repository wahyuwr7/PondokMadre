package com.wiwiwi.pondokmadre.ui.admin.tenant

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.wiwiwi.pondokmadre.data.model.PaymentStatus
import com.wiwiwi.pondokmadre.data.model.entity.PropertyUnitEntity
import com.wiwiwi.pondokmadre.data.model.entity.TenantEntity
import com.wiwiwi.pondokmadre.ui.theme.PondokMadreTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageTenantsScreen(
    tenants: List<TenantEntity>,
    units: List<PropertyUnitEntity>,
    onAddTenant: (name: String, unitId: Int, dueDate: String, paymentStatus: PaymentStatus) -> Unit,
    onUpdateTenant: (TenantEntity) -> Unit,
    onDeleteTenant: (TenantEntity) -> Unit,
    onBackClick: () -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var tenantToEdit by remember { mutableStateOf<TenantEntity?>(null) }
    var tenantToDelete by remember { mutableStateOf<TenantEntity?>(null) }

    val unitMap = units.associateBy { it.id }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kelola Penyewa") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Penyewa")
            }
        }
    ) { paddingValues ->
        if (tenants.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Belum ada data penyewa.", style = MaterialTheme.typography.titleMedium)
                Text("Klik tombol + untuk menambahkan.", style = MaterialTheme.typography.bodyMedium)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(tenants) { tenant ->
                    TenantItem(
                        tenant = tenant,
                        unitName = tenant.unitId?.let { unitMap[it]?.name } ?: "Belum ada unit",
                        onEditClick = { tenantToEdit = tenant },
                        onDeleteClick = { tenantToDelete = tenant }
                    )
                }
            }
        }
    }

    if (showAddDialog || tenantToEdit != null) {
        AddOrEditTenantDialog(
            existingTenant = tenantToEdit,
            availableUnits = units,
            onDismiss = {
                showAddDialog = false
                tenantToEdit = null
            },
            onConfirm = { name, unitId, dueDate, status, id ->
                if (id != null) {
                    onUpdateTenant(TenantEntity(id, name, unitId, dueDate, status))
                } else {
                    onAddTenant(name, unitId, dueDate, status)
                }
                showAddDialog = false
                tenantToEdit = null
            }
        )
    }

    tenantToDelete?.let { tenant ->
        DeleteConfirmationDialog(
            tenantName = tenant.name,
            onDismiss = { tenantToDelete = null },
            onConfirm = {
                onDeleteTenant(tenant)
                tenantToDelete = null
            }
        )
    }
}

@Composable
private fun TenantItem(
    tenant: TenantEntity,
    unitName: String,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(tenant.name, style = MaterialTheme.typography.titleMedium)
                Text("Unit: $unitName", style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = onEditClick) { Icon(Icons.Default.Edit, "Edit") }
            IconButton(onClick = onDeleteClick) { Icon(Icons.Default.Delete, "Hapus", tint = MaterialTheme.colorScheme.error
            ) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddOrEditTenantDialog(
    existingTenant: TenantEntity? = null,
    availableUnits: List<PropertyUnitEntity>,
    onDismiss: () -> Unit,
    onConfirm: (name: String, unitId: Int, dueDate: String, paymentStatus: PaymentStatus, id: Int?) -> Unit
) {
    var name by remember { mutableStateOf(existingTenant?.name ?: "") }
    var dueDate by remember { mutableStateOf(existingTenant?.dueDate ?: "") }

    var expanded by remember { mutableStateOf(false) }
    var selectedUnit by remember {
        mutableStateOf(availableUnits.find { it.id == existingTenant?.unitId })
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(shape = MaterialTheme.shapes.large) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Tambah Penyewa Baru", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nama Penyewa") })
                Spacer(modifier = Modifier.height(8.dp))

                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                    OutlinedTextField(
                        value = selectedUnit?.name ?: "Pilih Unit",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Unit yang Disewa") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        availableUnits.forEach { unit ->
                            DropdownMenuItem(
                                text = { Text(unit.name) },
                                onClick = {
                                    selectedUnit = unit
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = dueDate, onValueChange = { dueDate = it }, label = { Text("Tanggal Jatuh Tempo (contoh: 05 Jun)") })

                Spacer(modifier = Modifier.height(24.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) { Text("Batal") }
                    TextButton(onClick = {
                        selectedUnit?.let {
                            onConfirm(name, it.id, dueDate, existingTenant?.paymentStatus ?: PaymentStatus.UNPAID, existingTenant?.id)
                        }
                    }) {
                        Text("Simpan")
                    }
                }
            }
        }
    }
}

@Composable
private fun DeleteConfirmationDialog(
    tenantName: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(shape = MaterialTheme.shapes.large) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Konfirmasi Hapus", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Anda yakin ingin menghapus penyewa \"$tenantName\"?")
                Spacer(modifier = Modifier.height(24.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) { Text("Batal") }
                    Button(onClick = onConfirm, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) {
                        Text("Hapus")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun ManageTenantsScreenPreview() {
    val sampleTenants = listOf(
        TenantEntity(id = 1, name = "Budi Santoso", unitId = 101, dueDate = "05 Jun", paymentStatus = PaymentStatus.PAID),
        TenantEntity(id = 2, name = "Citra Lestari", unitId = 102, dueDate = "10 Jun", paymentStatus = PaymentStatus.UNPAID)
    )
    val sampleUnits = listOf(
        PropertyUnitEntity(id = 101, name = "Kamar A-01", code = "MWR-01", price = 1000000.0, propertyId = 1, propertyCode = "PS1"),
        PropertyUnitEntity(id = 102, name = "Paviliun Melati", code = "MLT-P", price = 2500000.0, propertyId = 2, propertyCode = "PS2")
    )
    PondokMadreTheme {
        ManageTenantsScreen(
            tenants = sampleTenants,
            units = sampleUnits,
            onAddTenant = { _, _, _, _ -> },
            onUpdateTenant = {},
            onDeleteTenant = {},
            onBackClick = {}
        )
    }
}