package cz.cvut.riskio.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun RewardsScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Rewards", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Text("You have 1,000 Risk Points")
        }
    }
}

@Composable
fun AddTaskScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Add Task", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("Task Name") })
            Button(onClick = {}, modifier = Modifier.padding(top = 8.dp)) {
                Text("Save Task")
            }
        }
    }
}

@Composable
fun GambaScreen() {
    val symbols = listOf("🍒", "🍋", "🔔", "⭐", "💎", "7️⃣")
    var slot1 by remember { mutableStateOf(symbols[0]) }
    var slot2 by remember { mutableStateOf(symbols[1]) }
    var slot3 by remember { mutableStateOf(symbols[2]) }
    var isSpinning by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("🎰 LAS VEGAS SLOTS 🎰", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        
        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier
                .background(Color.DarkGray, RoundedCornerShape(8.dp))
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SlotItem(slot1)
            SlotItem(slot2)
            SlotItem(slot3)
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if (!isSpinning) {
                    isSpinning = true
                    scope.launch {
                        repeat(20) { i ->
                            if (i < 10) slot1 = symbols.random()
                            if (i < 15) slot2 = symbols.random()
                            slot3 = symbols.random()
                            delay(100)
                        }
                        isSpinning = false
                    }
                }
            },
            enabled = !isSpinning,
            modifier = Modifier.height(56.dp).width(200.dp)
        ) {
            Text(if (isSpinning) "SPINNING..." else "SPIN! (100 pts)")
        }
        
        if (!isSpinning && slot1 == slot2 && slot2 == slot3) {
            Text(
                "JACKPOT! 🎉", 
                color = Color.Yellow, 
                fontSize = 32.sp, 
                fontWeight = FontWeight.Black,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Composable
fun SlotItem(symbol: String) {
    Surface(
        modifier = Modifier.size(80.dp),
        shape = RoundedCornerShape(4.dp),
        color = Color.White,
        tonalElevation = 4.dp
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(symbol, fontSize = 40.sp)
        }
    }
}
