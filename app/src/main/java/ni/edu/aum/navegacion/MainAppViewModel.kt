package ni.edu.aum.navegacion
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel

class MainAppViewModel : ViewModel() {
    private val _cartItems = mutableStateMapOf<Int, Int>()
    val cartItems: Map<Int, Int> = _cartItems

    // Estado para el cupón aplicado
    var appliedDiscount by mutableStateOf(0.0)
    var activeCouponName by mutableStateOf("")

    val services = listOf(
        ServiceItem(1, "Lavado Diario", "Ropa mixta color/blanca", 4.50, Color(0xFFE1F5FE)),
        ServiceItem(2, "Planchado Pro", "Camisas y trajes", 2.00, Color(0xFFFCE4EC)),
        ServiceItem(3, "Edredones", "Lavado profundo", 15.00, Color(0xFFE8F5E9)),
        ServiceItem(4, "Tintorería", "Prendas delicadas", 12.00, Color(0xFFFFF3E0)),
        ServiceItem(5, "Solo Secado", "Express 30 min", 3.00, Color(0xFFF3E5F5))
    )

    fun calculateSubtotal(): Double {
        return services.sumOf { (cartItems[it.id] ?: 0) * it.price }
    }

    fun calculateTotal(): Double {
        val subtotal = calculateSubtotal()
        return subtotal - (subtotal * appliedDiscount)
    }

    // Lógica de Cupones
    fun applyCoupon(code: String): String {
        val subtotal = calculateSubtotal()
        return when (code) {
            "HOLA15" -> {
                appliedDiscount = 0.15
                activeCouponName = "15% OFF aplicado"
                "¡Cupón aplicado!"
            }
            "WEEKEND" -> {
                if (subtotal >= 20.0) {
                    appliedDiscount = 0.20
                    activeCouponName = "20% OFF aplicado"
                    "¡Cupón aplicado!"
                } else {
                    "Mínimo $20 para usar este cupón"
                }
            }
            else -> "Cupón no válido"
        }
    }
    fun clearCart() {
        _cartItems.clear()
        appliedDiscount = 0.0
        activeCouponName = ""
    }
    fun addToCart(serviceId: Int) {
        _cartItems[serviceId] = (_cartItems[serviceId] ?: 0) + 1
        validateCoupon() // Validar tras sumar
    }

    fun removeFromCart(serviceId: Int) {
        val current = _cartItems[serviceId] ?: 0
        if (current > 0) {
            _cartItems[serviceId] = current - 1
            validateCoupon() // Validar tras restar
        }
    }

    // Nueva función para quitar el cupón si el subtotal baja del mínimo
    private fun validateCoupon() {
        val subtotal = calculateSubtotal()
        if (activeCouponName == "20% OFF aplicado" && subtotal < 20.0) {
            appliedDiscount = 0.0
            activeCouponName = ""
            // Aquí podrías disparar un mensaje de "Cupón removido por monto insuficiente"
        }
    }
}