package com.project.pengelolakeuangan.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.project.pengelolakeuangan.ui.screens.beranda.HomeScreen
import com.project.pengelolakeuangan.ui.screens.profile.ProfileScreen
import com.project.pengelolakeuangan.ui.screens.rekap.RekapScreen
import com.project.pengelolakeuangan.ui.screens.transaksi.TransactionFormScreen
import com.project.pengelolakeuangan.ui.screens.transaksi.TransactionsScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Transaction : Screen("transaction")
    object Rekap : Screen("rekap")
    object Account : Screen("profile")
    object Form : Screen("form/{isIncome}") {
        fun createRoute(isIncome: Boolean) = "form/$isIncome"
    }
}

@Composable
fun AppNavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = Screen.Home.route, modifier = Modifier) {
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(Screen.Transaction.route) {
            TransactionsScreen { isIncome ->
                navController.navigate(Screen.Form.createRoute(isIncome))
            }
        }
        composable(Screen.Rekap.route) {
            RekapScreen()
        }
        composable(Screen.Account.route) {
            ProfileScreen()
        }
        composable(Screen.Form.route) { backStackEntry ->
            val isIncome = backStackEntry.arguments?.getString("isIncome")?.toBoolean() ?: true
            TransactionFormScreen(isIncome = isIncome, onSave = { /* Save logic */ }, onCancel = {
                navController.popBackStack()
            })
        }
    }
}