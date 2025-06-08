package com.wiwiwi.pondokmadre.data.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.wiwiwi.pondokmadre.PondokMadreApp
import com.wiwiwi.pondokmadre.ui.ViewModelFactory
import com.wiwiwi.pondokmadre.ui.admin.AdminScreen
import com.wiwiwi.pondokmadre.ui.admin.property.ManagePropertiesScreen
import com.wiwiwi.pondokmadre.ui.admin.property.ManagePropertiesViewModel
import com.wiwiwi.pondokmadre.ui.admin.unit.ManageUnitsScreen
import com.wiwiwi.pondokmadre.ui.admin.unit.ManageUnitsViewModel
import com.wiwiwi.pondokmadre.ui.dashboard.DashboardScreen
import com.wiwiwi.pondokmadre.ui.dashboard.DashboardViewModel
import com.wiwiwi.pondokmadre.ui.transaction.AddTransactionScreen
import com.wiwiwi.pondokmadre.ui.transaction.AddTransactionViewModel
import java.util.Date

object AppRoutes {
    const val DASHBOARD = "dashboard_route"
    const val ADMIN = "admin_route"
    const val ADD_TRANSACTION = "add_transaction_route"
    const val MANAGE_PROPERTIES = "manage_properties_route"
    const val MANAGE_UNITS = "manage_units_route"
}

@Composable
fun AppNavigation(navController: NavHostController, modifier: Modifier = Modifier) {
    val application = LocalContext.current.applicationContext as PondokMadreApp
    val viewModelFactory = ViewModelFactory(application.repository)

    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Dashboard.route,
        modifier = modifier
    ) {
        // Rute untuk Bottom Nav Item "Dasbor"
        composable(BottomNavItem.Dashboard.route) {
            val viewModel: DashboardViewModel = viewModel(factory = viewModelFactory)
            val tenants by viewModel.tenants.collectAsState()
            val units by viewModel.propertyUnits.collectAsState()
            val properties by viewModel.properties.collectAsState()

            DashboardScreen(
                tenants = tenants,
                propertyUnits = units,
                properties = properties,
                onAddTransactionClick = { navController.navigate(AppRoutes.ADD_TRANSACTION) }
            )
        }

        // Rute untuk Bottom Nav Item "Admin"
        composable(BottomNavItem.Admin.route) {
            AdminScreen(onMenuClick = { route ->
                navController.navigate(route)
            })
        }

        // ---Rute untuk halaman yang bukan bagian dari bottom nav (misal: Tambah Transaksi)---
        composable(AppRoutes.ADD_TRANSACTION) {
            val viewModel: AddTransactionViewModel = viewModel(factory = viewModelFactory)
            val tenants by viewModel.allTenants.collectAsState()
            val units by viewModel.allUnits.collectAsState()
            val paymentMethods by viewModel.allPaymentMethods.collectAsState()

            AddTransactionScreen(
                tenants = tenants,
                units = units,
                paymentMethods = paymentMethods,
                onSaveClick = { amount, type, desc, tenant, method ->
                    viewModel.saveTransaction(
                        amount = amount,
                        type = type,
                        description = desc,
                        transactionDate = Date(),
                        tenant = tenant,
                        paymentMethod = method
                    )
                    navController.popBackStack()
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(AppRoutes.MANAGE_PROPERTIES) {
            val viewModel: ManagePropertiesViewModel = viewModel(factory = viewModelFactory)
            val properties by viewModel.properties.collectAsState()

            ManagePropertiesScreen(
                properties = properties,
                onAddProperty = viewModel::addProperty,
                onUpdateProperty = viewModel::updateProperty,
                onDeleteProperty = viewModel::deleteProperty,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(AppRoutes.MANAGE_UNITS) {
            val viewModel: ManageUnitsViewModel = viewModel(factory = viewModelFactory)
            val units by viewModel.units.collectAsState()
            val properties by viewModel.properties.collectAsState()
            ManageUnitsScreen(
                units = units,
                properties = properties,
                onAddUnit = viewModel::addUnit,
                onUpdateUnit = viewModel::updateUnit,
                onDeleteUnit = viewModel::deleteUnit,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
