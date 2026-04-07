package cz.cvut.riskio

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// --- Data Models ---
enum class MainScreen(val icon: ImageVector) {
    Home(Icons.Default.Home),
    Gamba(Icons.Default.PlayArrow),
    Todo(Icons.Default.List)
}

data class TodoItem(
    val id: Int,
    val taskName: String,
    val coinValue: Int,
    var isDone: Boolean = false
)

data class GambaWin(val icon: ImageVector, val label: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    var darkTheme by remember { mutableStateOf(true) }
    var coins by remember { mutableStateOf(50) }
    val todoList = remember { mutableStateListOf<TodoItem>() }
    // Collection now only tracks actual Gamba rewards
    val myCollection = remember { mutableStateListOf<GambaWin>() }
    var currentMainScreen by remember { mutableStateOf(MainScreen.Home) }

    val colors = if (darkTheme) darkColorScheme() else lightColorScheme()

    MaterialTheme(colorScheme = colors) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("RISKIO REWARDS", fontWeight = FontWeight.Bold) },
                    actions = {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(end = 12.dp)) {
                            Icon(Icons.Default.Star, "Coins", tint = Color(0xFFFFD700))
                            Text(" $coins", fontWeight = FontWeight.Bold)
                            IconButton(onClick = { darkTheme = !darkTheme }) {
                                Icon(if (darkTheme) Icons.Default.LightMode else Icons.Default.DarkMode, null)
                            }
                        }
                    }
                )
            },
            bottomBar = {
                NavigationBar {
                    MainScreen.values().forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, null) },
                            label = { Text(screen.name) },
                            selected = currentMainScreen == screen,
                            onClick = { currentMainScreen = screen }
                        )
                    }
                }
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                when (currentMainScreen) {
                    MainScreen.Home -> RewardHome(myCollection)
                    MainScreen.Todo -> TodoScreen(todoList) { item ->
                        coins += item.coinValue
                    }
                    MainScreen.Gamba -> GambaScreen(coins, onSpend = { coins -= 20 }, onWin = { icon, label ->
                        myCollection.add(GambaWin(icon, label))
                    })
                }
            }
        }
    }
}

@Composable
fun RewardHome(collection: List<GambaWin>) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Trophy Room", style = MaterialTheme.typography.headlineMedium)
        Text("Your Gamba winnings", style = MaterialTheme.typography.bodySmall, color = Color.Gray)

        Spacer(modifier = Modifier.height(16.dp))

        if (collection.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No trophies yet. Spin the slots!", color = Color.Gray)
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(100.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(collection) { win ->
                    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
                        Column(
                            modifier = Modifier.padding(8.dp).fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(win.icon, null, modifier = Modifier.size(32.dp))
                            Text(win.label, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TodoScreen(todoList: MutableList<TodoItem>, onTaskFinished: (TodoItem) -> Unit) {
    var taskInput by remember { mutableStateOf("") }
    var coinInput by remember { mutableStateOf("10") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Card(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text("Add Task", fontWeight = FontWeight.Bold)
                TextField(
                    value = taskInput,
                    onValueChange = { taskInput = it },
                    placeholder = { Text("What needs doing?") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                TextField(
                    value = coinInput,
                    onValueChange = { coinInput = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Coins Earned") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Button(
                    modifier = Modifier.align(Alignment.End).padding(top = 8.dp),
                    onClick = {
                        val amount = coinInput.toIntOrNull() ?: 10
                        if (taskInput.isNotBlank()) {
                            todoList.add(TodoItem(todoList.size, taskInput, amount))
                            taskInput = ""
                            coinInput = "10"
                        }
                    }
                ) { Text("Create Task") }
            }
        }

        LazyColumn {
            items(todoList) { item ->
                Card(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text(item.taskName, style = if (item.isDone) MaterialTheme.typography.bodyLarge.copy(color = Color.Gray) else MaterialTheme.typography.bodyLarge)
                            Text("+${item.coinValue}💰 Reward", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                        }
                        if (!item.isDone) {
                            Button(onClick = {
                                val idx = todoList.indexOf(item)
                                if (idx != -1) {
                                    todoList[idx] = item.copy(isDone = true)
                                    onTaskFinished(todoList[idx])
                                }
                            }) { Text("Claim") }
                        } else {
                            Icon(Icons.Default.CheckCircle, "Done", tint = Color.Green)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GambaScreen(currentCoins: Int, onSpend: () -> Unit, onWin: (ImageVector, String) -> Unit) {
    val symbols = listOf(Icons.Default.Pets, Icons.Default.Face, Icons.Default.Favorite, Icons.Default.Star, Icons.Default.AutoAwesome, Icons.Default.Bolt)
    val scope = rememberCoroutineScope()
    var r1 by remember { mutableStateOf<ImageVector?>(null) }
    var r2 by remember { mutableStateOf<ImageVector?>(null) }
    var r3 by remember { mutableStateOf<ImageVector?>(null) }
    var isSpinning by remember { mutableStateOf(false) }
    var msg by remember { mutableStateOf("Spin for 20 Coins!") }

    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Row(Modifier.padding(24.dp)) {
            Reel(r1); Spacer(Modifier.width(8.dp)); Reel(r2); Spacer(Modifier.width(8.dp)); Reel(r3)
        }
        Text(msg, fontSize = 18.sp, modifier = Modifier.padding(bottom = 16.dp))
        Button(
            enabled = !isSpinning && currentCoins >= 20,
            onClick = {
                onSpend()
                scope.launch {
                    isSpinning = true
                    repeat(15) { i ->
                        r1 = symbols.random(); if(i>5) r2 = symbols.random(); if(i>10) r3 = symbols.random()
                        delay(70)
                    }
                    isSpinning = false
                    if (r1 == r2 && r2 == r3) {
                        msg = "JACKPOT! 🏆"
                        onWin(r1!!, "JACKPOT WIN")
                    }
                    else if (r1 == r2 || r2 == r3 || r1 == r3) {
                        msg = "Win! 🎁"
                        val winningIcon = listOfNotNull(r1, r2, r3).groupingBy { it }.eachCount().maxBy { it.value }.key
                        onWin(winningIcon, "Lucky Prize")
                    }
                    else msg = "Try again!"
                }
            }
        ) { Text(if (currentCoins < 20) "Low Balance" else "SPIN 20💰") }
    }
}

@Composable
fun Reel(icon: ImageVector?) {
    Box(Modifier.size(80.dp).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp)).border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
        if (icon != null) Icon(icon, null, Modifier.size(40.dp), tint = MaterialTheme.colorScheme.primary)
        else Text("?", fontSize = 30.sp)
    }
}