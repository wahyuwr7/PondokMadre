package com.wiwiwi.pondokmadre.ui.admin.paymentmethod
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Payments
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
import com.wiwiwi.pondokmadre.data.model.entity.PaymentMethodEntity
import com.wiwiwi.pondokmadre.ui.theme.PondokMadreTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagePaymentMethodsScreen(
    paymentMethods: List<PaymentMethodEntity>,
    onAddMethod: (name: String) -> Unit,
    onUpdateMethod: (PaymentMethodEntity) -> Unit,
    onDeleteMethod: (PaymentMethodEntity) -> Unit,
    onBackClick: () -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var methodToEdit by remember { mutableStateOf<PaymentMethodEntity?>(null) }
    var methodToDelete by remember { mutableStateOf<PaymentMethodEntity?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kelola Metode Pembayaran") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Metode Pembayaran")
            }
        }
    ) { paddingValues ->
        if (paymentMethods.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Belum ada metode pembayaran.", style = MaterialTheme.typography.titleMedium)
                Text("Klik tombol + untuk menambahkan.", style = MaterialTheme.typography.bodyMedium)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(paymentMethods) { method ->
                    PaymentMethodItem(
                        method = method,
                        onEditClick = { methodToEdit = method },
                        onDeleteClick = { methodToDelete = method }
                    )
                }
            }
        }
    }

    if (showAddDialog || methodToEdit != null) {
        AddOrEditMethodDialog(
            existingMethod = methodToEdit,
            onDismiss = {
                showAddDialog = false
                methodToEdit = null
            },
            onConfirm = { name, id ->
                if (id != null) {
                    onUpdateMethod(PaymentMethodEntity(id = id, name = name))
                } else {
                    onAddMethod(name)
                }
                showAddDialog = false
                methodToEdit = null
            }
        )
    }

    methodToDelete?.let { method ->
        DeleteConfirmationDialog(
            methodName = method.name,
            onDismiss = { methodToDelete = null },
            onConfirm = {
                onDeleteMethod(method)
                methodToDelete = null
            }
        )
    }
}

@Composable
private fun PaymentMethodItem(
    method: PaymentMethodEntity,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Payments, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(16.dp))
            Text(method.name, modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleMedium)
            IconButton(onClick = onEditClick) { Icon(Icons.Default.Edit, "Edit") }
            IconButton(onClick = onDeleteClick) { Icon(Icons.Default.Delete, "Hapus", tint = MaterialTheme.colorScheme.error) }
        }
    }
}

@Composable
private fun AddOrEditMethodDialog(
    existingMethod: PaymentMethodEntity? = null,
    onDismiss: () -> Unit,
    onConfirm: (name: String, id: Int?) -> Unit
) {
    var name by remember { mutableStateOf(existingMethod?.name ?: "") }
    val isEditMode = existingMethod != null

    Dialog(onDismissRequest = onDismiss) {
        Card(shape = MaterialTheme.shapes.large) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    if (isEditMode) "Edit Metode Pembayaran" else "Tambah Metode Baru",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nama Metode (Contoh: Tunai)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text("Batal") }
                    TextButton(
                        onClick = { onConfirm(name, existingMethod?.id) },
                        enabled = name.isNotBlank()
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
    methodName: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(shape = MaterialTheme.shapes.large) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Konfirmasi Hapus", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Anda yakin ingin menghapus \"$methodName\"?")
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

@Preview(showBackground = true)
@Composable
fun ManagePaymentMethodsScreenPreview() {
    val sampleMethods = listOf(
        PaymentMethodEntity(1, "Tunai"),
        PaymentMethodEntity(2, "Transfer BCA"),
        PaymentMethodEntity(3, "QRIS")
    )
    PondokMadreTheme {
        ManagePaymentMethodsScreen(
            paymentMethods = sampleMethods,
            onAddMethod = {},
            onUpdateMethod = {},
            onDeleteMethod = {},
            onBackClick = {}
        )
    }
}