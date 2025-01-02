package com.project.pengelolakeuangan.ui.navigation

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.project.pengelolakeuangan.R

data class NavigationItem(
    val label: String,
    val route: String,
    val icon: @Composable () -> Unit
)

@Composable
fun getNavigationItems(): List<NavigationItem> {
    return listOf(
        NavigationItem(
            label = "Beranda",
            route = "home",
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.home), // Ganti dengan ikon custom
                    contentDescription = "Beranda",
                    modifier = Modifier.size(24.dp)
                )
            }
        ),
        NavigationItem(
            label = "Transaksi",
            route = "transactions",
            icon = {
                Icon(
                    imageVector = Icons.Default.Add, // Gunakan ikon default Material
                    contentDescription = "Transaksi",
                    modifier = Modifier.size(24.dp)
                )
            }
        ),
        NavigationItem(
            label = "Rekap",
            route = "rekap",
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.rekap), // Ganti dengan ikon custom
                    contentDescription = "Rekap",
                    modifier = Modifier.size(24.dp)
                )
            }
        ),
        NavigationItem(
            label = "Akun",
            route = "profile",
            icon = {
                Icon(
                    imageVector = Icons.Default.AccountCircle, // Gunakan ikon default Material
                    contentDescription = "Akun",
                    modifier = Modifier.size(24.dp)
                )
            }
        )
    )
}


