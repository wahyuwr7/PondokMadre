package com.wiwiwi.pondokmadre.ui.transaction

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiwiwi.pondokmadre.data.model.PaymentStatus
import com.wiwiwi.pondokmadre.data.model.TransactionType
import com.wiwiwi.pondokmadre.data.model.entity.PaymentMethodEntity
import com.wiwiwi.pondokmadre.data.model.entity.PropertyUnitEntity
import com.wiwiwi.pondokmadre.data.model.entity.TenantEntity
import com.wiwiwi.pondokmadre.ui.theme.PondokMadreTheme
import java.text.DecimalFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    tenants: List<TenantEntity>,
    units: List<PropertyUnitEntity>,
    paymentMethods: List<PaymentMethodEntity>,
    onSaveClick: (amount: Double, type: TransactionType, description: String, tenant: TenantEntity?, paymentMethod: PaymentMethodEntity?) -> Unit,
    onBackClick: () -> Unit
) {
    var selectedTransactionType by remember { mutableStateOf(TransactionType.INCOME) }
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedTenant by remember { mutableStateOf<TenantEntity?>(null) }
    var selectedPaymentMethod by remember { mutableStateOf<PaymentMethodEntity?>(null) }
    var transactionDate by remember { mutableStateOf("07 Juni 2025") }

    val unitMap = units.associateBy { it.id }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Catat Transaksi Baru") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text("Jumlah Transaksi", style = MaterialTheme.typography.titleMedium)
            AmountInput(
                amount = amount,
                onAmountChange = { amount = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TransactionTypeSelector(
                    selectedType = selectedTransactionType,
                    onTypeSelected = {
                        selectedTransactionType = it
                        if (it == TransactionType.EXPENSE) {
                            selectedTenant = null
                            selectedPaymentMethod = null
                        }
                    }
                )

                AnimatedVisibility(
                    visible = selectedTransactionType == TransactionType.INCOME,
                    enter = expandVertically(animationSpec = tween(300)),
                    exit = shrinkVertically(animationSpec = tween(300))
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        TenantDropdown(
                            tenants = tenants,
                            selectedTenant = selectedTenant,
                            onTenantSelected = { selectedTenant = it }
                        )

                        val selectedUnitInfo = selectedTenant?.unitId?.let { unitMap[it] }
                        if (selectedUnitInfo != null) {
                            Text(
                                text = "Unit terkait: ${selectedUnitInfo.name}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(start = 8.dp, top = 0.dp)
                            )
                        }

                        PaymentMethodDropdown(
                            paymentMethods = paymentMethods,
                            selectedMethod = selectedPaymentMethod,
                            onMethodSelected = { selectedPaymentMethod = it }
                        )
                    }
                }

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Deskripsi") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = transactionDate,
                    onValueChange = { /* TODO */ },
                    label = { Text("Tanggal Transaksi") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    trailingIcon = { Icon(Icons.Default.DateRange, "Pilih Tanggal") }
                )
            }


            Spacer(modifier = Modifier.weight(1f))

            // Tombol Simpan
            Button(
                onClick = {
                    val finalAmount = amount.toDoubleOrNull() ?: 0.0
                    onSaveClick(finalAmount, selectedTransactionType, description, selectedTenant, selectedPaymentMethod)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(bottom = 8.dp)
            ) {
                Text("SIMPAN", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun AmountInput(amount: String, onAmountChange: (String) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Text(
            text = "Rp",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
        BasicTextField(
            value = amount,
            onValueChange = {
                // Hanya simpan digit ke dalam state, batasi panjang agar tidak overflow
                if (it.length <= 15) {
                    onAmountChange(it.filter { char -> char.isDigit() })
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            visualTransformation = ThousandSeparatorVisualTransformation(),
            textStyle = TextStyle(
                fontSize = 48.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.weight(1f)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TransactionTypeSelector(
    selectedType: TransactionType,
    onTypeSelected: (TransactionType) -> Unit
) {
    val options = TransactionType.entries
    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
        options.forEach { option ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = options.indexOf(option),
                    count = options.size
                ),
                onClick = { onTypeSelected(option) },
                selected = option == selectedType
            ) {
                Text(option.label)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TenantDropdown(
    tenants: List<TenantEntity>,
    selectedTenant: TenantEntity?,
    onTenantSelected: (TenantEntity) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedTenant?.name ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text("Pilih Penyewa") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            tenants.forEach { tenant ->
                DropdownMenuItem(
                    text = { Text(tenant.name) },
                    onClick = {
                        onTenantSelected(tenant)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PaymentMethodDropdown(
    paymentMethods: List<PaymentMethodEntity>,
    selectedMethod: PaymentMethodEntity?,
    onMethodSelected: (PaymentMethodEntity) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedMethod?.name ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text("Metode Pembayaran") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            paymentMethods.forEach { method ->
                DropdownMenuItem(
                    text = { Text(method.name) },
                    onClick = {
                        onMethodSelected(method)
                        expanded = false
                    }
                )
            }
        }
    }
}


@Preview(showBackground = true, widthDp = 360)
@Composable
fun AddTransactionScreenPreview() {
    val sampleTenants = listOf(
        TenantEntity(1, "Budi Santoso", 101, "05 Jun 2025", PaymentStatus.UNPAID),
        TenantEntity(2, "Citra Lestari", 102, "05 Jun 2025", PaymentStatus.PAID)
    )
    val sampleUnits = listOf(
        PropertyUnitEntity(101, "A-01", "Kamar A-01", 1250000.0, 1, propertyCode = "PS1"),
        PropertyUnitEntity(102, "A-02", "Kamar A-02", 1250000.0, 1,  propertyCode = "PS1")
    )
    val samplePaymentMethods = listOf(
        PaymentMethodEntity(1, "Transfer Bank BRI"),
        PaymentMethodEntity(2, "Transfer Bank BCA"),
        PaymentMethodEntity(3, "Tunai")
    )

    PondokMadreTheme {
        AddTransactionScreen(
            tenants = sampleTenants,
            units = sampleUnits,
            paymentMethods = samplePaymentMethods,
            onSaveClick = {
                amount, type, description, tenant, paymentMethod ->  {}
            },
            onBackClick = {}
        )
    }
}

class ThousandSeparatorVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text
        if (originalText.isEmpty()) {
            return TransformedText(text, OffsetMapping.Identity)
        }

        // Pastikan hanya memproses digit
        val digitsOnly = originalText.filter { it.isDigit() }
        if (digitsOnly.isEmpty()) {
            return TransformedText(AnnotatedString(""), OffsetMapping.Identity)
        }

        val formatter = DecimalFormat("#,###")
        val formattedText = formatter.format(digitsOnly.toLong()).replace(",", ".")

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                val dotsBefore = (offset - 1).coerceAtLeast(0) / 3
                return offset + dotsBefore
            }

            override fun transformedToOriginal(offset: Int): Int {
                val dotsBefore = formattedText.take(offset).count { it == '.' }
                return offset - dotsBefore
            }
        }

        return TransformedText(
            AnnotatedString(formattedText),
            offsetMapping
        )
    }
}