package com.wiwiwi.pondokmadre.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiwiwi.pondokmadre.data.model.DashboardUiState
import com.wiwiwi.pondokmadre.data.model.PaymentStatus
import com.wiwiwi.pondokmadre.data.model.TenantStatusInfo
import com.wiwiwi.pondokmadre.data.util.formatRupiah
import com.wiwiwi.pondokmadre.ui.theme.PondokMadreTheme
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    uiState: DashboardUiState, // Menerima UI State tunggal
    onAddTransactionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddTransactionClick) {
                Icon(Icons.Filled.Add, contentDescription = "Tambah Transaksi")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item { GreetingHeader(userName = "Pemilik") }
            item {
                ModernSummaryCard(
                    netProfit = uiState.netProfit,
                    income = uiState.totalIncome,
                    expense = uiState.totalExpense
                )
            }
            item {
                Text(
                    text = "Status Pembayaran",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
            items(uiState.tenantStatusList) { tenantInfo ->
                TenantStatusRow(tenantInfo = tenantInfo)
            }
        }
    }
}

// ... (GreetingHeader dan ModernSummaryCard tidak berubah)
@Composable
private fun GreetingHeader(userName: String) {
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)

    val greeting = when (hour) {
        in 0..10 -> "Selamat Pagi"
        in 11..14 -> "Selamat Siang"
        in 15..18 -> "Selamat Sore"
        else -> "Selamat Malam"
    }

    Column {
        Text(
            text = "$greeting, $userName!",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Berikut ringkasan kontrakan Anda.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

@Composable
private fun ModernSummaryCard(netProfit: Double, income: Double, expense: Double) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = "Laba Bersih Bulan Ini",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )
            Text(
                text = formatRupiah(netProfit),
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                SummaryItem(
                    title = "Pemasukan",
                    amount = income,
                    icon = Icons.Default.ArrowUpward,
                    color = Color(0xFF388E3C)
                )
            }
            Spacer(Modifier.padding(8.dp))
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Absolute.Right
            ){
                SummaryItem(
                    title = "Pengeluaran",
                    amount = expense,
                    icon = Icons.Default.ArrowDownward,
                    color = Color(0xFFD32F2F)
                )
            }
        }
    }
}
@Composable
private fun SummaryItem(
    title: String,
    amount: Double,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = color,
            modifier = Modifier
                .size(32.dp)
                .background(color.copy(alpha = 0.1f), CircleShape)
                .padding(6.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Text(
                text = formatRupiah(amount),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}


@Composable
private fun TenantStatusRow(tenantInfo: TenantStatusInfo) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = tenantInfo.tenantName,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${tenantInfo.unitName} (${tenantInfo.propertyName})",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
        PaymentStatusChip(status = tenantInfo.paymentStatus)
    }
}

@Composable
private fun PaymentStatusChip(status: PaymentStatus) {
    val (text, color) = when (status) {
        PaymentStatus.PAID -> "Lunas" to Color(0xFF388E3C)
        PaymentStatus.PARTIALLY_PAID -> "Sebagian" to Color(0xFFFFA000)
        PaymentStatus.UNPAID -> "Belum Bayar" to Color(0xFFD32F2F)
    }
    Box(
        modifier = Modifier
            .background(color.copy(alpha = 0.1f), RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            color = color,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 780)
@Composable
fun DashboardScreenPreview() {
    val sampleState = DashboardUiState(
        tenantStatusList = listOf(
            TenantStatusInfo("Budi Santoso", "Kamar A-01", "Kontrakan Mawar", PaymentStatus.PAID),
            TenantStatusInfo("Citra Lestari", "Kamar A-02", "Kontrakan Mawar", PaymentStatus.UNPAID),
            TenantStatusInfo("Doni Firmansyah", "Paviliun B", "Paviliun Melati", PaymentStatus.PARTIALLY_PAID)
        ),
        totalIncome = 12500000.0,
        totalExpense = 1250000.0,
        netProfit = 11250000.0
    )
    PondokMadreTheme {
        DashboardScreen(uiState = sampleState, onAddTransactionClick = {})
    }
}