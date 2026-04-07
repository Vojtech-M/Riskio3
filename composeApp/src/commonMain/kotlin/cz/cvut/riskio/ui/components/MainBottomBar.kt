package cz.cvut.riskio.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import cz.cvut.riskio.navigate.*

fun NavDestination?.isRoute(route: Any): Boolean =
    this?.hierarchy?.any { it.hasRoute(route::class) } == true

@Composable
fun MainBottomBar(
    currentDestination: NavDestination?,
    onNavigate: (Any) -> Unit,
) {
    NavigationBar {
        // NavBar items
        mainNavBarItems.forEach { barItem ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = barItem.icon,
                        contentDescription = barItem.label
                    )
                },
                label = { Text(barItem.label) },
                selected = currentDestination.isRoute(barItem.route),
                onClick = { onNavigate(barItem.route) },
            )
        }
    }
}
