package ni.edu.aum.navegacion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*


// 1. Definición de las pantallas de la barra inferior
sealed class BottomBarScreen(val route: String, val title: String, val icon: ImageVector) {
    object Home : BottomBarScreen("home", "Inicio", Icons.Default.Home)
    object Promos : BottomBarScreen("promos", "Ofertas", Icons.Default.LocalOffer)
    object Prices : BottomBarScreen("prices", "Precios", Icons.Default.Payments)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainAppContainer()
        }
    }
}

@Composable
fun MainAppContainer() {
    val navController = rememberNavController()

    // 2. IMPORTANTE: Instanciamos el ViewModel una sola vez aquí
    // para compartirlo entre la pantalla de Promos y Prices.
    val viewModel: MainAppViewModel = viewModel()

    val items = listOf(
        BottomBarScreen.Home,
        BottomBarScreen.Promos,
        BottomBarScreen.Prices
    )

    MaterialTheme(colorScheme = lightColorScheme(primary = Color(0xFF2D31FA))) {
        Scaffold(
            bottomBar = {
                NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination

                    items.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = null) },
                            label = { Text(screen.title) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    // Evita acumular copias de la misma pantalla en el historial
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        ) { innerPadding ->
            // 3. Configuración del Navegador
            NavHost(
                navController = navController,
                startDestination = BottomBarScreen.Home.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(BottomBarScreen.Home.route) {
                    HomeScreen()
                }

                composable(BottomBarScreen.Promos.route) {
                    // Pasamos el viewModel para que los cupones afecten al total
                    PromotionsScreen(viewModel)
                }

                composable(BottomBarScreen.Prices.route) {
                    // Pasamos el viewModel para gestionar el carrito y el total
                    PricesScreen(viewModel)
                }
            }
        }
    }
}