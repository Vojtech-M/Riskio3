package cz.cvut.riskio

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import cz.cvut.riskio.ui.screens.GambaScreen
import cz.cvut.riskio.ui.screens.RewardHome
import cz.cvut.riskio.ui.screens.TodoScreen

// Define the modes for the spinning wheel
enum class GambaMode { JETBRAINS, CLASSIC }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    // --- State Management ---
    var lang by remember { mutableStateOf(Language.EN) }
    val s = remember(lang) { Strings(lang) }

    var darkTheme by remember { mutableStateOf(true) }
    var gambaMode by remember { mutableStateOf(GambaMode.JETBRAINS) }
    var coins by remember { mutableStateOf(50) }

    val todoList = remember { mutableStateListOf<TodoItem>() }
    val myCollection = remember { mutableStateListOf<GambaWin>() }
    val rewardPool = remember { mutableStateListOf<String>() }

    var currentMainScreen by remember { mutableStateOf(MainScreen.Home) }

    val colors = if (darkTheme) darkColorScheme() else lightColorScheme()

    MaterialTheme(colorScheme = colors) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(s.appTitle, fontWeight = FontWeight.Bold) },
                    actions = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(end = 12.dp)
                        ) {
                            // Gamba Mode Toggle (JetBrains vs Classic)
                            IconButton(onClick = {
                                gambaMode = if (gambaMode == GambaMode.JETBRAINS) GambaMode.CLASSIC else GambaMode.JETBRAINS
                            }) {
                                Icon(
                                    imageVector = if (gambaMode == GambaMode.JETBRAINS) Icons.Default.Code else Icons.Default.Casino,
                                    contentDescription = "Switch Gamba Mode",
                                    tint = if (gambaMode == GambaMode.JETBRAINS) MaterialTheme.colorScheme.primary else Color(0xFF4CAF50)
                                )
                            }

                            // Language Toggle
                            TextButton(
                                onClick = { lang = if (lang == Language.EN) Language.CZ else Language.EN },
                                contentPadding = PaddingValues(horizontal = 8.dp)
                            ) {
                                Text(lang.name, fontWeight = FontWeight.ExtraBold)
                            }

                            // Dark Mode Toggle
                            IconButton(onClick = { darkTheme = !darkTheme }) {
                                Icon(if (darkTheme) Icons.Default.LightMode else Icons.Default.DarkMode, null)
                            }

                            Spacer(Modifier.width(8.dp))

                            // Coins Display
                            Surface(
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "Coins",
                                        tint = Color(0xFFFFD700),
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(Modifier.width(4.dp))
                                    Text(
                                        text = "$coins",
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                )
            },
            bottomBar = {
                NavigationBar {
                    MainScreen.entries.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, null) },
                            label = {
                                Text(when(screen) {
                                    MainScreen.Home -> s.home
                                    MainScreen.Gamba -> s.gamba
                                    MainScreen.Todo -> s.todo
                                })
                            },
                            selected = currentMainScreen == screen,
                            onClick = { currentMainScreen = screen }
                        )
                    }
                }
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                when (currentMainScreen) {
                    MainScreen.Home -> RewardHome(s, myCollection, rewardPool)
                    MainScreen.Todo -> TodoScreen(s, todoList) { item ->
                        coins += item.coinValue
                        todoList.remove(item)
                    }
                    MainScreen.Gamba -> GambaScreen(
                        s = s,
                        currentCoins = coins,
                        rewardPool = rewardPool,
                        mode = gambaMode, // Passing the mode to the screen
                        onSpend = { coins -= 20 },
                        onWin = { symbol, label ->
                            myCollection.add(GambaWin(symbol, label))
                            rewardPool.remove(label)
                        }
                    )
                }
            }
        }
    }
}