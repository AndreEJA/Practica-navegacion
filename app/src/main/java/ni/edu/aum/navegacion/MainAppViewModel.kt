package ni.edu.aum.navegacion

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel

class MainAppViewModel : ViewModel() {
    private val _cartItems = mutableStateMapOf<Int, Int>()
    val cartItems: Map<Int, Int> = _cartItems

    val services = listOf(
        ServiceItem(1, "Lavado Diario", "Ropa mixta color/blanca", 4.50, Color(0xFFE1F5FE)),
        ServiceItem(2, "Planchado Pro", "Camisas y trajes", 2.00, Color(0xFFFCE4EC)),
        ServiceItem(3, "Edredones", "Lavado profundo", 15.00, Color(0xFFE8F5E9)),
        ServiceItem(4, "Tintorería", "Prendas delicadas", 12.00, Color(0xFFFFF3E0)),
        ServiceItem(5, "Solo Secado", "Express 30 min", 3.00, Color(0xFFF3E5F5))
    )

    fun addToCart(serviceId: Int) {
        _cartItems[serviceId] = (_cartItems[serviceId] ?: 0) + 1
    }

    fun calculateTotal(): Double {
        return services.sumOf { (cartItems[it.id] ?: 0) * it.price }
    }
}