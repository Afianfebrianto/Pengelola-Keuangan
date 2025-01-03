package com.project.pengelolakeuangan.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.project.pengelolakeuangan.R
import com.project.pengelolakeuangan.ui.screens.transaksi.TransaksiViewModel


//@Composable
//fun MainScreen(navController: NavHostController) {
//    val navBackStackEntry = navController.currentBackStackEntryAsState()
//    val currentRoute = navBackStackEntry.value?.destination?.route
//
//    Scaffold(
//        bottomBar = {
//            BottomNavigationBar(
//                currentRoute = currentRoute,
//                onItemSelected = { route -> navController.navigate(route) }
//            )
//        }
//    ) { innerPadding ->
//        // Gunakan innerPadding untuk memastikan konten tidak terhalang bottom bar
//        AppNavGraph(navController = navController, modifier = Modifier.padding(innerPadding))
//    }
//}

@Composable
fun MainScreen(navController: NavHostController, viewModel: TransaksiViewModel) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                currentRoute = currentRoute,
                onItemSelected = { route -> navController.navigate(route) }
            )
        }
    ) { innerPadding ->
        // Gunakan innerPadding untuk memastikan konten tidak terhalang bottom bar
        AppNavGraph(navController = navController, viewModel = viewModel, modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun BottomNavigationBar(currentRoute: String?, onItemSelected: (String) -> Unit) {
    val items = listOf(
        Screen.Home to R.drawable.home,
        Screen.Transaction to R.drawable.add,
        Screen.Rekap to R.drawable.rekap,
        Screen.Account to R.drawable.account_circle
    )

    BottomNavigation(backgroundColor = Color.White) {
        items.forEach { (screen, iconRes) ->
            BottomNavigationItem(
                selected = currentRoute == screen.route,
                onClick = { onItemSelected(screen.route) },
                icon = {
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = screen.route
                    )
                },
                label = { Text(text = screen.route.capitalize()) }
            )
        }
    }
}

//@Composable
//fun BottomNavigationBar(navController: NavController) {
//    val items = listOf(
//        NavigationItem("Home", "home", Icons.Default.Home),
//        NavigationItem("Transactions", "transactions", Icons.Default.List),
//        NavigationItem("Analytics", "analytics", Icons.Default.Add),
//        NavigationItem("Settings", "settings", Icons.Default.Settings)
//    )
//
//    BottomNavigation{
//        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
//        items.forEach { item ->
//            BottomNavigationItem(
//                icon = { Icon(item.icon, contentDescription = item.label) },
//                label = { Text(item.label) },
//                selected = currentRoute == item.route,
//                onClick = {
//                    navController.navigate(item.route) {
//                        popUpTo(navController.graph.startDestinationId) { saveState = true }
//                        launchSingleTop = true
//                        restoreState = true
//                    }
//                }
//            )
//        }
//    }
//}

//data class NavigationItem(val label: String, val route: String, val icon: ImageVector)
