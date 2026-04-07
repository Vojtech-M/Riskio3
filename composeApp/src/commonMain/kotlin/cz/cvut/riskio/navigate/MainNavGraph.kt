package cz.cvut.riskio.navigate

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import cz.cvut.riskio.ui.components.MainBottomBar
import cz.cvut.riskio.ui.screens.AddTaskScreen
import cz.cvut.riskio.ui.screens.GambaScreen
import cz.cvut.riskio.ui.screens.RewardsScreen

@Composable
fun MainNavGraph() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    Scaffold(
        bottomBar = {
            MainBottomBar(
                currentDestination = currentDestination,
                onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
            startDestination = Rewards
        ) {
            composable<Rewards> { RewardsScreen() }
            composable<Gamba> { GambaScreen() }
            composable<AddTask> { AddTaskScreen() }
        }
    }
}
