package cz.cvut.riskio.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cz.cvut.riskio.GambaMode
import cz.cvut.riskio.SlotSymbol
import cz.cvut.riskio.Strings
import cz.cvut.riskio.ui.components.GenericIcon
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import riskio.composeapp.generated.resources.*
import kotlin.random.Random

@Composable
fun GambaScreen(
    s: Strings,
    currentCoins: Int,
    rewardPool: List<String>,
    mode: GambaMode, // Added parameter
    onSpend: () -> Unit,
    onWin: (SlotSymbol, String) -> Unit
) {
    // Dynamically choose symbols based on the mode
    val symbols = remember(mode) {
        if (mode == GambaMode.JETBRAINS) {
            listOf(
                SlotSymbol.ImageRes(Res.drawable.datagrip),
                SlotSymbol.ImageRes(Res.drawable.clion),
                SlotSymbol.ImageRes(Res.drawable.idea),
                SlotSymbol.ImageRes(Res.drawable.kotlin),
                SlotSymbol.ImageRes(Res.drawable.php),
                SlotSymbol.ImageRes(Res.drawable.py),
                SlotSymbol.ImageRes(Res.drawable.we),
            )
        } else {
            // Classic Mode Icons
            listOf(
                SlotSymbol.ImageRes(Res.drawable.dia),
                SlotSymbol.ImageRes(Res.drawable.mone),
                SlotSymbol.ImageRes(Res.drawable.seven),
                SlotSymbol.ImageRes(Res.drawable.cher),
                SlotSymbol.ImageRes(Res.drawable.lem),
                SlotSymbol.ImageRes(Res.drawable.luck),
                SlotSymbol.ImageRes(Res.drawable.pod)

            )
        }
    }

    val scope = rememberCoroutineScope()
    var r1 by remember { mutableStateOf<SlotSymbol?>(null) }
    var r2 by remember { mutableStateOf<SlotSymbol?>(null) }
    var r3 by remember { mutableStateOf<SlotSymbol?>(null) }
    var isSpinning by remember { mutableStateOf(false) }
    var msg by remember { mutableStateOf(s.spinCost) }

    // Clear symbols if user switches mode while not spinning
    LaunchedEffect(mode) {
        if (!isSpinning) {
            r1 = null
            r2 = null
            r3 = null
            msg = s.spinCost
        }
    }

    LaunchedEffect(s) {
        if (!isSpinning) msg = s.spinCost
    }

    val canSpin = !isSpinning && currentCoins >= 20 && rewardPool.isNotEmpty()

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
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

                    val willWin = Random.nextFloat() < 0.40f
                    val winningSymbol = symbols.random()

                    repeat(15) { i ->
                        r1 = if (i == 14 && willWin) winningSymbol else symbols.random()
                        if(i > 5) {
                            r2 = if (i == 14 && willWin) winningSymbol else symbols.random()
                        }
                        if(i > 10) {
                            r3 = if (i == 14 && willWin) winningSymbol else symbols.random()
                        }
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
                    } else {
                        msg = s.tryAgain
                    }
                }
            }
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    if (rewardPool.isEmpty()) s.addRewardsFirst
                    else if (currentCoins < 20) s.needCoins
                    else s.spinBtn
                )
                if (rewardPool.isNotEmpty() && currentCoins >= 20) {
                    Spacer(Modifier.width(4.dp))
                    Icon(
                        Icons.Default.Star,
                        null,
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
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
        AnimatedContent(
            targetState = symbol,
            transitionSpec = {
                (slideInVertically(animationSpec = tween(70)) { height -> -height } + fadeIn(animationSpec = tween(70))) togetherWith
                        (slideOutVertically(animationSpec = tween(70)) { height -> height } + fadeOut(animationSpec = tween(70)))
            },
            label = "reel_spin_animation"
        ) { targetSymbol ->
            if (targetSymbol != null) {
                GenericIcon(
                    symbol = targetSymbol,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            } else {
                Text("?", fontSize = 36.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
            }
        }
    }
}