package cz.cvut.riskio.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cz.cvut.riskio.GambaWin
import cz.cvut.riskio.Strings
import cz.cvut.riskio.ui.components.GenericIcon

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
