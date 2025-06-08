package com.wiwiwi.pondokmadre.ui.admin.unit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.wiwiwi.pondokmadre.data.model.entity.PropertyEntity
import com.wiwiwi.pondokmadre.data.model.entity.PropertyUnitEntity
import com.wiwiwi.pondokmadre.ui.theme.PondokMadreTheme
import kotlin.reflect.KFunction5

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageUnitsScreen(
    units: List<PropertyUnitEntity>,
    properties: List<PropertyEntity>,
    onAddUnit: (String, String, Double, Int, String,) -> Unit,
    onUpdateUnit: (PropertyUnitEntity) -> Unit,
    onDeleteUnit: (PropertyUnitEntity) -> Unit,
    onBackClick: () -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var unitToEdit by remember { mutableStateOf<PropertyUnitEntity?>(null) }
    var unitToDelete by remember { mutableStateOf<PropertyUnitEntity?>(null) }

    val unitsGroupedByProperty = units.groupBy { it.propertyId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kelola Unit") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Unit")
            }
        }
    ) { paddingValues ->
        if (units.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Belum ada unit.", style = MaterialTheme.typography.titleMedium)
                Text("Klik tombol + untuk menambahkan.", style = MaterialTheme.typography.bodyMedium)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                unitsGroupedByProperty.forEach { (propertyId, unitList) ->
                    val propertyName = properties.find { it.id == propertyId }?.name ?: "Properti Tidak Dikenal"
                    item {
                        Text(
                            propertyName,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                    items(unitList) { unit ->
                        UnitItem(
                            unit = unit,
                            onEditClick = { unitToEdit = unit },
                            onDeleteClick = { unitToDelete = unit }
                        )
                    }
                }
            }
        }
    }

    if (showAddDialog || unitToEdit != null) {
        AddOrEditUnitDialog(
            existingUnit = unitToEdit,
            properties = properties,
            onDismiss = {
                showAddDialog = false
                unitToEdit = null
            },
            onConfirm = { name, code, price, propertyId, id, propertyCode ->
                if (id != null) {
                    onUpdateUnit(PropertyUnitEntity(id, code, name, price, propertyId, propertyCode))
                } else {
                    onAddUnit(name, code, price, propertyId, propertyCode)
                }
                showAddDialog = false
                unitToEdit = null
            }
        )
    }

    unitToDelete?.let { unit ->
        DeleteConfirmationDialog(
            unitName = unit.name,
            onDismiss = { unitToDelete = null },
            onConfirm = {
                onDeleteUnit(unit)
                unitToDelete = null
            }
        )
    }
}

@Composable
private fun UnitItem(
    unit: PropertyUnitEntity,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.MeetingRoom, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(unit.name, style = MaterialTheme.typography.titleMedium)
                Text("Kode: ${unit.propertyCode}.${unit.code}", style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = onEditClick) { Icon(Icons.Default.Edit, "Edit") }
            IconButton(onClick = onDeleteClick) { Icon(Icons.Default.Delete, "Hapus") }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddOrEditUnitDialog(
    existingUnit: PropertyUnitEntity? = null,
    properties: List<PropertyEntity>,
    onDismiss: () -> Unit,
    onConfirm: (name: String, code: String, price: Double, propertyId: Int, id: Int?, propertyCode: String) -> Unit
) {
    var name by remember { mutableStateOf(existingUnit?.name ?: "") }
    var code by remember { mutableStateOf(existingUnit?.code ?: "") }
    var price by remember { mutableStateOf(existingUnit?.price?.toString() ?: "") }

    var expanded by remember { mutableStateOf(false) }
    var selectedProperty by remember {
        mutableStateOf(properties.find { it.id == existingUnit?.propertyId } ?: properties.firstOrNull())
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(shape = MaterialTheme.shapes.large) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Tambah Unit Baru", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(16.dp))

                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                    OutlinedTextField(
                        value = selectedProperty?.name ?: "Pilih Properti",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Properti") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        properties.forEach { property ->
                            DropdownMenuItem(
                                text = { Text(property.name) },
                                onClick = {
                                    selectedProperty = property
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nama Unit") })
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = code, onValueChange = { code = it }, label = { Text("Kode Unit") })
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Harga Sewa") })

                Spacer(modifier = Modifier.height(24.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) { Text("Batal") }
                    TextButton(onClick = {
                        onConfirm(name, code, price.toDoubleOrNull() ?: 0.0, selectedProperty?.id ?: 0, existingUnit?.id,
                            existingUnit?.propertyCode ?: ""
                        )
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
    unitName: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(shape = MaterialTheme.shapes.large) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Konfirmasi Hapus", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Anda yakin ingin menghapus unit \"$unitName\"?")
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
fun ManageUnitsScreenPreview() {
    val sampleProperties = listOf(
        PropertyEntity(id = 1, name = "Pondok Sakinah", code = "PS1"),
        PropertyEntity(id = 2, name = "Pondok Sakinah 2", code = "PS2")
    )
    val sampleUnits = listOf(
        PropertyUnitEntity(id = 1, name = "Kamar A1-01", code = "A1-01", propertyId = 1, price = 2_000_000.0, propertyCode = "PS1"),
        PropertyUnitEntity(id = 2, name = "Kamar A1-02", code = "A1-02", propertyId = 1, price = 2_000_000.0, propertyCode = "PS1"),
        PropertyUnitEntity(id = 3, name = "Kamar A1-01", code = "A1-01", propertyId = 2, price = 1_800_000.0, propertyCode = "PS2"),
        PropertyUnitEntity(id = 4, name = "Kamar A1-02", code = "A1-02", propertyId = 2, price = 1_800_000.0, propertyCode = "PS2"),
    )
    PondokMadreTheme {
        ManageUnitsScreen(
            units = sampleUnits,
            properties = sampleProperties,
            onDeleteUnit = {},
            onUpdateUnit = {},
            onAddUnit = { _, _, _, _, _ ->},
            onBackClick = {}
        )
    }
}