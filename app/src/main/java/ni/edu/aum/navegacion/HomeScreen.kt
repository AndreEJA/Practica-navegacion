package ni.edu.aum.navegacion

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
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

@Composable
fun HomeScreen() {
    val gradient = Brush.verticalGradient(colors = listOf(Color(0xFF2D31FA), Color(0xFF00D4FF)))

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F9FA))) {
        // Cabecera Ajustada con Logo y Nombre
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                .background(gradient)
        ) {
            Column(Modifier.padding(20.dp).align(Alignment.CenterStart)) {
                // Identidad de la Tienda
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(32.dp).clip(CircleShape).background(Color.White.copy(0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.LocalLaundryService, null, tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        "LAVANDERÍA PRO EXPRESS",
                        color = Color.White.copy(0.8f),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text("¡Hola, Sarah!", color = Color.White, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold)
                Text("Tus prendas están en buenas manos.", color = Color.White.copy(0.9f), style = MaterialTheme.typography.bodyMedium)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Categorías Top", Modifier.padding(horizontal = 24.dp), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)

        LazyRow(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            val cats = listOf("Express", "Hogar", "Cuero", "Delicado", "Edredones")
            items(cats.size) { index ->
                Surface(color = Color.White, shape = RoundedCornerShape(16.dp), shadowElevation = 2.dp) {
                    Text(cats[index], Modifier.padding(horizontal = 20.dp, vertical = 12.dp), fontWeight = FontWeight.Medium)
                }
            }
        }

        // Card de Oferta Flash
        Card(
            modifier = Modifier.padding(24.dp).fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFD600))
        ) {
            Row(Modifier.padding(24.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("FLASH SALE", fontWeight = FontWeight.Black, fontSize = 12.sp, color = Color.Red)
                    Text("50% Descuento", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text("Solo por hoy en lavado seco", style = MaterialTheme.typography.bodySmall)
                }
                Icon(Icons.Default.Bolt, null, modifier = Modifier.size(45.dp), tint = Color.Black)
            }
        }
    }
}