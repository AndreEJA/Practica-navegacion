package ni.edu.aum.navegacion

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocalLaundryService
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// 1. DEFINICIÓN DE CLASE DE DATOS (Importante para evitar errores de name/description)
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
    val total = viewModel.calculateTotal()

    // Gradiente azul/celeste del home
    val blueGradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFF2D31FA), Color(0xFF00D4FF))
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("TARIFAS Y SERVICIOS",
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp,
                        letterSpacing = 1.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color(0xFFF8F9FA)
    ) { paddingValues -> // Corregido: nombre estándar para el Scaffold
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // --- TOTAL ESTIMADO EN LA PARTE SUPERIOR ---
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                color = Color.White,
                shape = RoundedCornerShape(24.dp),
                shadowElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier.padding(24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Total estimado", color = Color.Gray, style = MaterialTheme.typography.labelMedium)
                        // Cambio a Dólares ($)
                        Text("$${String.format("%.2f", total)}",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.Black
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF2D31FA).copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.LocalLaundryService, null, tint = Color(0xFF2D31FA))
                    }
                }
            }

            Text(
                "Selecciona tus servicios",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
            )

            // --- LISTA DE SERVICIOS (CORRECCIÓN PADDINGVALUES) ---
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(
                    start = 20.dp,
                    top = 8.dp,
                    end = 20.dp,
                    bottom = 20.dp // Espacio inferior estándar
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(viewModel.services) { service ->
                    val quantity = viewModel.cartItems[service.id] ?: 0
                    PriceCardItem(
                        service = service,
                        quantity = quantity,
                        onAdd = { viewModel.addToCart(service.id) }
                    )
                }
            }

            // --- BOTÓN FINAL CON GRADIENTE ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = { /* Acción pagar */ },
                    modifier = Modifier
                        .fillMaxWidth(0.85f) // Centrado al 85% del ancho
                        .height(56.dp)
                        .background(blueGradient, shape = RoundedCornerShape(16.dp)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent // Importante para ver el gradiente
                    ),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("REVISAR PEDIDO", fontWeight = FontWeight.ExtraBold, color = Color.White)
                }
            }
        }
    }
}

@Composable
fun PriceCardItem(service: ServiceItem, quantity: Int, onAdd: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(service.color),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.LocalLaundryService, null, tint = Color.DarkGray)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(service.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
                Text(service.description, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                Text("$${String.format("%.2f", service.price)} / unidad",
                    color = Color(0xFF2D31FA),
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (quantity > 0) {
                    Text("$quantity",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(end = 8.dp),
                        color = Color.Black
                    )
                }
                IconButton(
                    onClick = onAdd,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF1F1F1))
                ) {
                    Icon(Icons.Default.Add, null, tint = Color.Black)
                }
            }
        }
    }
}