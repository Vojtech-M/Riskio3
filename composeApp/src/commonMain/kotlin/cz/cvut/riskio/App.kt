package cz.cvut.riskio

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import riskio.composeapp.generated.resources.Res
import riskio.composeapp.generated.resources.compose_multiplatform
import riskio.composeapp.generated.resources.datagrip
import riskio.composeapp.generated.resources.*


// --- Localization ---

enum class Language { EN, CZ }

class Strings(val lang: Language) {
    val appTitle = if (lang == Language.EN) "RISKIO REWARDS" else "RISKIO ODMĚNY"
    val home = if (lang == Language.EN) "Home" else "Domů"
    val gamba = if (lang == Language.EN) "Gamba" else "Gamba"
    val todo = if (lang == Language.EN) "Tasks" else "Úkoly"
    val setupPool = if (lang == Language.EN) "Gamba Pool Setup" else "Nastavení výher"
    val rewardPlaceholder = if (lang == Language.EN) "Reward name..." else "Název odměny..."
    val add = if (lang == Language.EN) "Add" else "Přidat"
    val itemsInPool = if (lang == Language.EN) "Items in Pool:" else "Položky v osudí:"
    val currentReward = if (lang == Language.EN) "Current Reward" else "Aktuální odměna"
    val latestWin = if (lang == Language.EN) "LATEST WIN" else "POSLEDNÍ VÝHRA"
    val noWinYet = if (lang == Language.EN) "Win something in Gamba to see it here!" else "Vyhraj něco v Gambě, aby se to zde ukázalo!"
    val newTask = if (lang == Language.EN) "New Task" else "Nový úkol"
    val taskPlaceholder = if (lang == Language.EN) "What to do?" else "Co je třeba udělat?"
    val coinReward = if (lang == Language.EN) "Coin Reward" else "Odměna (mince)"
    val done = if (lang == Language.EN) "Done" else "Hotovo"
    val spinCost = if (lang == Language.EN) "Spin for 20 Coins!" else "Zatoč za 20 mincí!"
    val spinning = if (lang == Language.EN) "Spinning..." else "Točím..."
    val tryAgain = if (lang == Language.EN) "Try again!" else "Zkus to znovu!"
    val jackpot = if (lang == Language.EN) "JACKPOT!" else "JACKPOT!"
    val win = if (lang == Language.EN) "WIN!" else "VÝHRA!"
    val addRewardsFirst = if (lang == Language.EN) "Add Rewards First" else "Nejdřív přidej odměny"
    val needCoins = if (lang == Language.EN) "Need 20💰" else "Potřebuješ 20💰"
    val spinBtn = if (lang == Language.EN) "SPIN 20💰" else "HRAVAT 20💰"
}

// --- Data Models ---

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

// --- Components ---

@Composable
fun GenericIcon(
    symbol: SlotSymbol,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current
) {
    when (symbol) {
        is SlotSymbol.Vector -> Icon(
            imageVector = symbol.icon,
            contentDescription = contentDescription,
            modifier = modifier,
            tint = tint
        )
        is SlotSymbol.ImageRes -> Icon(
            painter = painterResource(symbol.res),
            contentDescription = contentDescription,
            modifier = modifier,
            tint = Color.Unspecified
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    var lang by remember { mutableStateOf(Language.EN) }
    val s = Strings(lang)

    var darkTheme by remember { mutableStateOf(true) }
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

                            // Coins Display (Pill Shape on the right)
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
                        s,
                        currentCoins = coins,
                        rewardPool = rewardPool,
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RewardHome(s: Strings, collection: List<GambaWin>, rewardPool: MutableList<String>) {
    var rewardInput by remember { mutableStateOf("") }
    val lastReward = collection.lastOrNull()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Card(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(s.setupPool, fontWeight = FontWeight.Bold)
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 8.dp)) {
                    TextField(
                        value = rewardInput,
                        onValueChange = { rewardInput = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text(s.rewardPlaceholder) }
                    )
                    Spacer(Modifier.width(8.dp))
                    Button(onClick = {
                        if (rewardInput.isNotBlank()) {
                            rewardPool.add(rewardInput)
                            rewardInput = ""
                        }
                    }) { Text(s.add) }
                }

                if (rewardPool.isNotEmpty()) {
                    Spacer(Modifier.height(12.dp))
                    Text(s.itemsInPool, style = MaterialTheme.typography.labelMedium)
                    FlowRow(
                        modifier = Modifier.padding(top = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rewardPool.forEach { item ->
                            SuggestionChip(
                                onClick = { rewardPool.remove(item) },
                                label = { Text(item) },
                                icon = { Icon(Icons.Default.Close, null, Modifier.size(14.dp)) }
                            )
                        }
                    }
                }
            }
        }

        Text(s.currentReward, style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        if (lastReward == null) {
            Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Text(s.noWinYet, color = Color.Gray, textAlign = TextAlign.Center)
            }
        } else {
            Card(
                modifier = Modifier.size(250.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    GenericIcon(
                        symbol = lastReward.symbol,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        lastReward.label,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center
                    )
                    Text(s.latestWin, style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(top = 8.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TodoScreen(s: Strings, todoList: MutableList<TodoItem>, onTaskClaimed: (TodoItem) -> Unit) {
    var taskInput by remember { mutableStateOf("") }
    var coinInput by remember { mutableStateOf("10") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Card(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(s.newTask, fontWeight = FontWeight.Bold)
                TextField(
                    value = taskInput,
                    onValueChange = { taskInput = it },
                    placeholder = { Text(s.taskPlaceholder) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                TextField(
                    value = coinInput,
                    onValueChange = { coinInput = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(s.coinReward) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Button(
                    modifier = Modifier.align(Alignment.End).padding(top = 8.dp),
                    onClick = {
                        val amount = coinInput.toIntOrNull() ?: 10
                        if (taskInput.isNotBlank()) {
                            todoList.add(TodoItem(todoList.size.toLong(), taskInput, amount))
                            taskInput = ""
                            coinInput = "10"
                        }
                    }
                ) { Text(s.add) }
            }
        }

        LazyColumn {
            items(todoList, key = { it.id }) { item ->
                Card(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text(item.taskName, style = MaterialTheme.typography.bodyLarge)
                            Text("+${item.coinValue}💰", color = MaterialTheme.colorScheme.primary)
                        }
                        Button(onClick = { onTaskClaimed(item) }) {
                            Text(s.done)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GambaScreen(
    s: Strings,
    currentCoins: Int,
    rewardPool: List<String>,
    onSpend: () -> Unit,
    onWin: (SlotSymbol, String) -> Unit
) {
    val symbols = remember {
        listOf(
            SlotSymbol.ImageRes(Res.drawable.datagrip),
            SlotSymbol.ImageRes(Res.drawable.clion),
            SlotSymbol.ImageRes(Res.drawable.datagrip),
            SlotSymbol.ImageRes(Res.drawable.idea),
            SlotSymbol.ImageRes(Res.drawable.kotlin),
            SlotSymbol.ImageRes(Res.drawable.php),
            SlotSymbol.ImageRes(Res.drawable.py),
            SlotSymbol.ImageRes(Res.drawable.we),
        )
    }

    val scope = rememberCoroutineScope()
    var r1 by remember { mutableStateOf<SlotSymbol?>(null) }
    var r2 by remember { mutableStateOf<SlotSymbol?>(null) }
    var r3 by remember { mutableStateOf<SlotSymbol?>(null) }
    var isSpinning by remember { mutableStateOf(false) }
    var msg by remember { mutableStateOf(s.spinCost) }

    LaunchedEffect(s) {
        if (!isSpinning) msg = s.spinCost
    }

    val canSpin = !isSpinning && currentCoins >= 20 && rewardPool.isNotEmpty()

    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Row(Modifier.padding(24.dp)) {
            Reel(r1); Spacer(Modifier.width(8.dp)); Reel(r2); Spacer(Modifier.width(8.dp)); Reel(r3)
        }

        Text(
            text = msg,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 24.dp, start = 20.dp, end = 20.dp)
        )

        Button(
            enabled = canSpin,
            modifier = Modifier.height(56.dp).fillMaxWidth(0.6f),
            onClick = {
                onSpend()
                scope.launch {
                    isSpinning = true
                    msg = s.spinning
                    repeat(15) { i ->
                        r1 = symbols.random()
                        if(i > 5) r2 = symbols.random()
                        if(i > 10) r3 = symbols.random()
                        delay(70)
                    }
                    isSpinning = false

                    val final1 = r1
                    val final2 = r2
                    val final3 = r3

                    if (final1 != null && final1 == final2 && final2 == final3) {
                        val prize = rewardPool.random()
                        msg = "${s.jackpot}\n$prize"
                        onWin(final1, prize)
                    }
                    else if (final1 != null && final2 != null && final3 != null && (final1 == final2 || final2 == final3 || final1 == final3)) {
                        val prize = rewardPool.random()
                        val winningSymbol = listOf(final1, final2, final3)
                            .groupingBy { it }
                            .eachCount()
                            .maxByOrNull { it.value }?.key ?: final1
                        msg = "${s.win}\n$prize"
                        onWin(winningSymbol, prize)
                    }
                    else {
                        msg = s.tryAgain
                    }
                }
            }
        ) {
            Text(if (rewardPool.isEmpty()) s.addRewardsFirst else if (currentCoins < 20) s.needCoins else s.spinBtn)
        }
    }
}

@Composable
fun Reel(symbol: SlotSymbol?) {
    Box(
        Modifier
            .size(90.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
            .border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        if (symbol != null) {
            GenericIcon(
                symbol = symbol,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        } else {
            Text("?", fontSize = 36.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
        }
    }
}