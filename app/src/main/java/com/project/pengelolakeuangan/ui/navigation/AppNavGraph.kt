package com.project.pengelolakeuangan.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.project.pengelolakeuangan.R
import com.project.pengelolakeuangan.ui.viewModel.TransaksiViewModel
import com.project.pengelolakeuangan.utils.poppinsFamily


@Composable
fun MainScreen(navController: NavHostController, viewModel: TransaksiViewModel) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute in listOf(
                    Screen.Home.route,
                    Screen.Transaction.route,
                    Screen.Rekap.route,
                    Screen.Account.route
                )
            ) {
                BottomNavigationBar(
                    currentRoute = currentRoute,
                    onItemSelected = { route -> navController.navigate(route) }
                )
            }
        }
    ) { innerPadding ->
        // Terapkan padding dari innerPadding ke konten utama
        Box(modifier = Modifier.padding(innerPadding)) {
            AppNavGraph(
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavigationPreview() {
    // Simulasi currentRoute untuk menampilkan navigasi yang dipilih
    val currentRoute = remember { mutableStateOf(Screen.Home.route) }

    // Menampilkan BottomNavigationBar
    BottomNavigationBar(
        currentRoute = currentRoute.value,
        onItemSelected = { selectedRoute -> currentRoute.value = selectedRoute }
    )
}

@Composable
fun BottomNavigationBar(currentRoute: String?, onItemSelected: (String) -> Unit) {
    val items = listOf(
        Screen.Home to R.drawable.home,
        Screen.Transaction to R.drawable.add,
        Screen.Rekap to R.drawable.rekap,
        Screen.Account to R.drawable.account_circle
    )

    BottomNavigation(
        modifier = Modifier.height(80.dp),
        backgroundColor = Color.Transparent, // Menghilangkan latar belakang BottomNavigation
        elevation = 0.dp // Menghilangkan shadow
    ) {
        items.forEach { (screen, iconRes) ->
            BottomNavigationItem(
                selected = currentRoute == screen.route,
                onClick = { onItemSelected(screen.route) },
                icon = {
                    Box(
                        modifier = Modifier
                            .size(50.dp) // Ukuran lingkaran diperbesar
                            .clip(CircleShape) // Membuat lingkaran
                            .background(Color(0xFFF58B76)) // Warna #F58B76
                            .padding(10.dp) // Padding lebih besar agar ikon sedikit lebih besar
                    ) {
                        Icon(
                            painter = painterResource(id = iconRes),
                            contentDescription = screen.route,
                            modifier = Modifier.align(Alignment.Center) // Menyusun icon di tengah
                        )
                    }

                },

                label = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally, // Menyusun label di tengah
                        modifier = Modifier.fillMaxWidth().height(25.dp)
                    ) {
//                        Spacer(modifier = Modifier.height(7.dp)) // Menambah jarak antara icon dan label

                        Text(
                            fontFamily = poppinsFamily,
                            text = screen.route.capitalize(),
                            fontSize = 10.sp,
                            color = Color.Black, // Menentukan warna teks
                            maxLines = 1, // Membatasi satu baris
                            overflow = TextOverflow.Ellipsis, // Menambahkan elipsis jika teks terlalu panjang
                            textAlign = TextAlign.Center, // Menyusun teks di tengah
                            modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                        )
                    }
                }
            )
        }
    }
}


@Composable
fun BottomNavigationBar1(currentRoute: String?, onItemSelected: (String) -> Unit) {
    val items = listOf(
        Screen.Home to R.drawable.home,
        Screen.Transaction to R.drawable.add,
        Screen.Rekap to R.drawable.rekap,
        Screen.Account to R.drawable.account_circle
    )

    BottomNavigation(
        modifier = Modifier,
        backgroundColor = Color.Transparent, // Menghilangkan latar belakang BottomNavigation
        elevation = 0.dp // Menghilangkan shadow
    ) {
        items.forEach { (screen, iconRes) ->
            BottomNavigationItem(
                selected = currentRoute == screen.route,
                onClick = { onItemSelected(screen.route) },
                icon = {
                    Box(
                        modifier = Modifier
                            .size(50.dp) // Ukuran lingkaran diperbesar
                            .clip(CircleShape) // Membuat lingkaran
                            .background(Color(0xFFF58B76)) // Warna #F58B76
                            .padding(10.dp) // Padding lebih besar agar ikon sedikit lebih besar
                    ) {
                        Icon(
                            painter = painterResource(id = iconRes),
                            contentDescription = screen.route,
                            modifier = Modifier.align(Alignment.Center) // Menyusun icon di tengah
                        )
                    }

                },

                label = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f) // Mengatur label agar berbagi ruang proporsional
                            .padding(top = 5.dp)
                    ) {
                        Text(
                            fontFamily = poppinsFamily,
                            text = screen.route.capitalize(),
                            fontSize = 14.sp,
                            color = Color.Black, // Menentukan warna teks
                            maxLines = 1, // Membatasi satu baris
                            overflow = TextOverflow.Ellipsis, // Menambahkan elipsis jika teks terlalu panjang
                            textAlign = TextAlign.Center, // Menyusun teks di tengah
                            modifier = Modifier.fillMaxWidth().padding(top = 10.dp)
                        )
                    }
                }
            )
        }
    }
}




