package ni.edu.aum.navegacion

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.LocalLaundryService
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Clase de datos local
data class ServiceItem(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val color: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PricesScreen(viewModel: MainAppViewModel) {
    val subtotal = viewModel.calculateSubtotal()
    val total = viewModel.calculateTotal()

    // Estado para mostrar la pantalla de éxito (la captura)
    var showSuccess by remember { mutableStateOf(false) }

    val blueGradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFF2D31FA), Color(0xFF00D4FF))
    )

    if (showSuccess) {
        // --- VISTA DE ÉXITO (CAPTURA DE PEDIDO) ---
        Box(
            modifier = Modifier.fillMaxSize().background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(120.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("¡PEDIDO ENVIADO!", fontWeight = FontWeight.Black, fontSize = 24.sp)
                Text("Total pagado: $${String.format("%.2f", total)}", color = Color.Gray, fontSize = 18.sp)

                if (viewModel.appliedDiscount > 0) {
                    Text("Cupón: ${viewModel.activeCouponName}", color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        viewModel.clearCart() // Limpia carrito y cupones
                        showSuccess = false    // Vuelve a la lista
                    },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("VOLVER A LA TIENDA")
                }
            }
        }
    } else {
        // --- VISTA PRINCIPAL DE SELECCIÓN ---
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("CONFIRMAR PEDIDO", fontWeight = FontWeight.Black, fontSize = 16.sp) },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
                )
            },
            containerColor = Color(0xFFF8F9FA)
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // PANEL DE RESUMEN (DESGLOSE)
                Surface(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    shape = RoundedCornerShape(24.dp),
                    color = Color.White,
                    shadowElevation = 2.dp
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                            Text("Subtotal", color = Color.Gray)
                            Text("$${String.format("%.2f", subtotal)}", fontWeight = FontWeight.Bold)
                        }

                        if (viewModel.appliedDiscount > 0) {
                            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                                Text(viewModel.activeCouponName, color = Color(0xFF4CAF50))
                                Text("- $${String.format("%.2f", subtotal * viewModel.appliedDiscount)}", color = Color(0xFF4CAF50))
                            }
                        }

                        HorizontalDivider(Modifier.padding(vertical = 12.dp), thickness = 1.dp, color = Color(0xFFF1F1F1))

                        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                            Text("TOTAL", fontWeight = FontWeight.ExtraBold)
                            Text("$${String.format("%.2f", total)}", fontWeight = FontWeight.Black, fontSize = 24.sp, color = Color(0xFF2D31FA))
                        }
                    }
                }

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(viewModel.services) { service ->
                        val quantity = viewModel.cartItems[service.id] ?: 0
                        PriceCardItem(
                            service = service,
                            quantity = quantity,
                            onAdd = { viewModel.addToCart(service.id) },
                            onRemove = { viewModel.removeFromCart(service.id) }
                        )
                    }
                }

                // BOTÓN DE CONFIRMAR FUNCIONAL
                Box(Modifier.fillMaxWidth().padding(20.dp), contentAlignment = Alignment.Center) {
                    Button(
                        onClick = {
                            if (total > 0) showSuccess = true
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(56.dp)
                            .background(blueGradient, shape = RoundedCornerShape(16.dp)),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text("CONFIRMAR PEDIDO", fontWeight = FontWeight.ExtraBold, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun PriceCardItem(service: ServiceItem, quantity: Int, onAdd: () -> Unit, onRemove: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(48.dp).clip(RoundedCornerShape(12.dp)).background(service.color), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.LocalLaundryService, null, tint = Color.DarkGray, modifier = Modifier.size(20.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(service.name, fontWeight = FontWeight.Bold)
                Text("$${String.format("%.2f", service.price)}", color = Color(0xFF2D31FA), fontWeight = FontWeight.ExtraBold)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (quantity > 0) {
                    IconButton(
                        onClick = onRemove,
                        modifier = Modifier.size(32.dp).clip(CircleShape).background(Color(0xFFFFEBEE))
                    ) {
                        Icon(Icons.Default.Remove, null, tint = Color.Red, modifier = Modifier.size(16.dp))
                    }
                    Text("$quantity", Modifier.padding(horizontal = 8.dp), fontWeight = FontWeight.Bold)
                }
                IconButton(
                    onClick = onAdd,
                    modifier = Modifier.size(32.dp).clip(CircleShape).background(Color(0xFFE8EAF6))
                ) {
                    Icon(Icons.Default.Add, null, tint = Color(0xFF2D31FA), modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}