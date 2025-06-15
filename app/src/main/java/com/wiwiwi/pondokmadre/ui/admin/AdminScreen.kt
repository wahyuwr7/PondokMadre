package com.wiwiwi.pondokmadre.ui.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Domain
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wiwiwi.pondokmadre.data.navigation.AppRoutes
import com.wiwiwi.pondokmadre.ui.theme.PondokMadreTheme

data class AdminMenu(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val route: String,
)

val adminMenuList = listOf(
    AdminMenu("Kelola Properti", "Tambah atau ubah data properti", Icons.Default.Domain, "manage_properties"),
    AdminMenu("Kelola Unit", "Atur unit atau kamar di setiap properti", Icons.Default.MeetingRoom, "manage_units"),
    AdminMenu("Kelola Penyewa", "Lihat dan kelola data penyewa", Icons.Default.Group, "manage_tenants"),
    AdminMenu("Metode Pembayaran", "Atur metode pembayaran yang diterima", Icons.Default.Payments, "manage_payment_methods")
)

@Composable
fun AdminScreen(onMenuClick: (String) -> Unit) {
    Scaffold { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    "Admin",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            items(adminMenuList) { menu ->

                AdminMenuItem(
                    title = menu.title,
                    description = menu.description,
                    icon = menu.icon,
                    onClick = {
                        val route = when (menu.title) {
                            "Kelola Properti" -> AppRoutes.MANAGE_PROPERTIES
                            "Kelola Unit" -> AppRoutes.MANAGE_UNITS
                            "Kelola Penyewa" -> AppRoutes.MANAGE_TENANTS
                            "Metode Pembayaran" -> AppRoutes.MANAGE_PAYMENT_METHODS
                            else -> ""
                        }
                        if (route.isNotEmpty()) {
                            onMenuClick(route)
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun AdminMenuItem(
    title: String,
    description: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview
@Composable
fun ShowAdminScreen(){
    PondokMadreTheme {
        AdminScreen(onMenuClick = { _ ->
        })
    }
}