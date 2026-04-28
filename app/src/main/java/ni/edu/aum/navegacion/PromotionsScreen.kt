package ni.edu.aum.navegacion

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Modelo de datos para las ofertas
data class Promotion(
    val title: String,
    val description: String,
    val code: String,
    val color: Color,
    val icon: ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PromotionsScreen() {
    // Lista de promociones dinámicas
    val promotions = listOf(
        Promotion("LAVADO EXPRESS", "15% de descuento en tu primer pedido", "HOLA15", Color(0xFFE3F2FD), Icons.Default.Star),
        Promotion("SUPER PLANCHADO", "Planchado gratis en más de 10 prendas", "IRONFREE", Color(0xFFFFF3E0), Icons.Default.Timer),
        Promotion("OFERTA FIN DE SEMANA", "2x1 en lavado de edredones", "WEEKEND", Color(0xFFF1F8E9), Icons.Default.LocalOffer),
        Promotion("CLIENTE VIP", "Descuento acumulable del 10%", "VIP2024", Color(0xFFF3E5F5), Icons.Default.Star)
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("OFERTAS Y CUPONES",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 2.sp
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = Color(0xFFF8F9FA) // Fondo gris claro para que resalten los cupones
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Disponibles para ti",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            items(promotions) { promo ->
                CouponItem(promo)
            }
        }
    }
}

@Composable
fun CouponItem(promo: Promotion) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = promo.color),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Sección Izquierda: Icono
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .fillMaxHeight()
                    .background(Brush.horizontalGradient(listOf(Color.Black.copy(0.05f), Color.Transparent))),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    modifier = Modifier.size(45.dp)
                ) {
                    Icon(
                        imageVector = promo.icon,
                        contentDescription = null,
                        modifier = Modifier.padding(10.dp),
                        tint = Color.DarkGray
                    )
                }
            }

            // Sección Central: Textos
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            ) {
                Text(
                    text = promo.title,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Black,
                    color = Color.DarkGray
                )
                Text(
                    text = promo.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    lineHeight = 18.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                // Badge del Código
                Surface(
                    color = Color.White.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "CÓDIGO: ${promo.code}",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Sección Derecha: Botón
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(end = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = { /* Acción */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("USAR", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}