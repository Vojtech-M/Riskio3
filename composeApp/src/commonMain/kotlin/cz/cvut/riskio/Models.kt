package cz.cvut.riskio

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.DrawableResource

sealed class SlotSymbol {
    data class Vector(val icon: ImageVector) : SlotSymbol()
    data class ImageRes(val res: DrawableResource) : SlotSymbol()
}

enum class MainScreen(val icon: ImageVector) {
    Home(Icons.Default.Home),
    Gamba(Icons.Default.PlayArrow),
    Todo(Icons.AutoMirrored.Filled.List)
}

data class TodoItem(
    val id: Long,
    val taskName: String,
    val coinValue: Int
)

data class GambaWin(val symbol: SlotSymbol, val label: String)
