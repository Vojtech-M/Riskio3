package cz.cvut.riskio.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.painterResource
import cz.cvut.riskio.SlotSymbol

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
