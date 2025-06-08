package com.wiwiwi.pondokmadre.data.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val icon: ImageVector, val title: String) {
    object Dashboard : BottomNavItem("dashboard", Icons.Default.Dashboard, "Dasbor")
    object Admin : BottomNavItem("admin", Icons.Default.Settings, "Admin")
}