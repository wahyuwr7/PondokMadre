package com.wiwiwi.pondokmadre.ui.admin.property

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Domain
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.wiwiwi.pondokmadre.data.dummy.SampleDataProvider
import com.wiwiwi.pondokmadre.data.model.entity.PropertyEntity
import com.wiwiwi.pondokmadre.ui.theme.PondokMadreTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagePropertiesScreen(
    properties: List<PropertyEntity>,
    onAddProperty: (name: String, code: String) -> Unit,
    onUpdateProperty: (PropertyEntity) -> Unit,
    onDeleteProperty: (PropertyEntity) -> Unit,
    onBackClick: () -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var propertyToEdit by remember { mutableStateOf<PropertyEntity?>(null) }
    var propertyToDelete by remember { mutableStateOf<PropertyEntity?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kelola Properti") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Properti")
            }
        }
    ) { paddingValues ->
        if (properties.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Belum ada properti.", style = MaterialTheme.typography.titleMedium)
                Text("Klik tombol + untuk menambahkan.", style = MaterialTheme.typography.bodyMedium)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(properties) { property ->
                    PropertyItem(
                        property = property,
                        onEditClick = { propertyToEdit = property },
                        onDeleteClick = { propertyToDelete = property }
                    )
                }
            }
        }
    }

    if (showAddDialog || propertyToEdit != null) {
        AddOrEditPropertyDialog(
            existingProperty = propertyToEdit,
            onDismiss = {
                showAddDialog = false
                propertyToEdit = null
            },
            onConfirm = { name, code, id ->
                if (id != null) {
                    onUpdateProperty(PropertyEntity(id = id, name = name, code = code))
                } else {
                    onAddProperty(name, code)
                }
                showAddDialog = false
                propertyToEdit = null
            }
        )
    }

    propertyToDelete?.let { property ->
        DeleteConfirmationDialog(
            propertyName = property.name,
            onDismiss = { propertyToDelete = null },
            onConfirm = {
                onDeleteProperty(property)
                propertyToDelete = null
            }
        )
    }
}

@Composable
private fun PropertyItem(
    property: PropertyEntity,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Domain, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(property.name, style = MaterialTheme.typography.titleMedium)
                Text("Kode: ${property.code}", style = MaterialTheme.typography.bodyMedium)
            }
            IconButton(onClick = onEditClick) {
                Icon(Icons.Default.Edit, contentDescription = "Edit Properti", tint = MaterialTheme.colorScheme.secondary)
            }
            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Default.Delete, contentDescription = "Hapus Properti", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
private fun AddOrEditPropertyDialog(
    existingProperty: PropertyEntity? = null,
    onDismiss: () -> Unit,
    onConfirm: (name: String, code: String, id: Int?) -> Unit
) {
    var name by remember { mutableStateOf(existingProperty?.name ?: "") }
    var code by remember { mutableStateOf(existingProperty?.code ?: "") }
    val isEditMode = existingProperty != null

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = MaterialTheme.shapes.large
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    if (isEditMode) "Edit Properti" else "Tambah Properti Baru",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nama Properti") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = code,
                    onValueChange = { code = it },
                    label = { Text("Kode Unik") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text("Batal") }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(
                        onClick = { onConfirm(name, code, existingProperty?.id) },
                        enabled = name.isNotBlank() && code.isNotBlank()
                    ) {
                        Text("Simpan")
                    }
                }
            }
        }
    }
}

@Composable
private fun DeleteConfirmationDialog(
    propertyName: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = MaterialTheme.shapes.large
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Konfirmasi Hapus", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Anda yakin ingin menghapus properti \"$propertyName\"? Aksi ini tidak dapat dibatalkan.")
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text("Batal") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = onConfirm,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Hapus")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun ManagePropertiesScreenPreview() {
    val sampleProperties = listOf(
        PropertyEntity(id = 1, name = "Kontrakan Mawar", code = "MWR"),
        PropertyEntity(id = 2, name = "Paviliun Melati", code = "MLT")
    )
    PondokMadreTheme {
        ManagePropertiesScreen(
            properties = sampleProperties,
            onAddProperty = { _, _ -> },
            onUpdateProperty = {},
            onDeleteProperty = {},
            onBackClick = {}
        )
    }
}