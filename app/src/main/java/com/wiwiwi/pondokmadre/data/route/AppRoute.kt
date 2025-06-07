package com.wiwiwi.pondokmadre.data.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.wiwiwi.pondokmadre.PondokMadreApp
import com.wiwiwi.pondokmadre.ui.ViewModelFactory
import com.wiwiwi.pondokmadre.ui.dashboard.DashboardScreen
import com.wiwiwi.pondokmadre.ui.dashboard.DashboardViewModel
import com.wiwiwi.pondokmadre.ui.transaction.AddTransactionScreen
import com.wiwiwi.pondokmadre.ui.transaction.AddTransactionViewModel
import java.util.Date

object AppRoutes {
    const val DASHBOARD = "dashboard"
    const val ADD_TRANSACTION = "add_transaction"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val application = LocalContext.current.applicationContext as PondokMadreApp
    val viewModelFactory = ViewModelFactory(application.repository)

    NavHost(
        navController = navController,
        startDestination = AppRoutes.DASHBOARD
    ) {
        composable(AppRoutes.DASHBOARD) {
            val viewModel: DashboardViewModel = viewModel(factory = viewModelFactory)
            val tenants by viewModel.tenants.collectAsState()
            val units by viewModel.propertyUnits.collectAsState()
            val properties by viewModel.properties.collectAsState()

            DashboardScreen(
                tenants = tenants,
                propertyUnits = units,
                properties = properties,
                onAddTransactionClick = {
                    navController.navigate(AppRoutes.ADD_TRANSACTION)
                }
            )
        }

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
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}