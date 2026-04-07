package cz.cvut.riskio.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cz.cvut.riskio.Strings
import cz.cvut.riskio.TodoItem

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
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("+${item.coinValue}", color = MaterialTheme.colorScheme.primary)
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color(0xFFFFD700),
                                    modifier = Modifier.size(16.dp).padding(start = 2.dp)
                                )
                            }
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
