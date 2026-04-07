package cz.cvut.riskio.navigate

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable

@Serializable
data object Rewards

@Serializable
data object Gamba

@Serializable
data object AddTask

data class NavBarItem(
    val route: Any,
    val label: String,
    val icon: ImageVector,
)

val mainNavBarItems = listOf(
    NavBarItem(Rewards, "Rewards", Icons.Default.Star),
    NavBarItem(Gamba, "Gamba", Icons.Default.Casino),
    NavBarItem(AddTask, "Add Task", Icons.Default.Add),
)
