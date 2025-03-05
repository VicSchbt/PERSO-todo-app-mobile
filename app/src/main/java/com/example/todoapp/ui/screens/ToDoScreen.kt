package com.example.todoapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todoapp.data.model.Todo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToDoScreen(modifier: Modifier) {
    var todos by remember { mutableStateOf(listOf<Todo>()) }
    var newTodoText by remember { mutableStateOf("") }
    var filter by remember { mutableStateOf("All") }

    val filterOptions = listOf("Active", "Completed", "All")

    val filteredTodos = when(filter) {
        "Active" -> todos.filter { !it.isCompleted }
        "Completed" -> todos.filter { it.isCompleted }
        else -> todos
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = newTodoText,
                onValueChange = { newTodoText = it },
                placeholder = { Text("Enter a task") }
            )
            Button(
                onClick = {
                    if (newTodoText.isNotBlank()) {
                        todos = todos + Todo(
                            id = todos.size + 1,
                            title = newTodoText,
                            isCompleted = false
                        )
                    }
                    newTodoText = ""
                }
            ) {
                Text("Add")
            }
        }

        SingleChoiceSegmentedButtonRow {
            filterOptions.forEachIndexed { index, option ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = filterOptions.size
                    ),
                    onClick = { filter = option },
                    selected = filter == option,
                    label = {
                        Text(option)
                    },
                )
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredTodos) { todo ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            onClick = {
                                todos = todos.map {
                                    if (it.id == todo.id) it.copy(isCompleted = !todo.isCompleted) else it
                                }
                            },
                            role = Role.Checkbox
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = todo.isCompleted,
                            onCheckedChange = { isChecked ->
                                todos = todos.map {
                                    if (it.id == todo.id) it.copy(isCompleted = isChecked) else it
                                }
                            }
                        )
                        Text(
                            text = todo.title,
                            textDecoration = if (todo.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                            color = if (todo.isCompleted) Color.Gray else Color.Black
                        )
                    }

                    IconButton(
                        onClick = {
                            todos = todos.filter { it.id != todo.id }
                        }
                    ) {
                        Icon(Icons.Outlined.Delete, contentDescription = "Delete ${todo.title}")

                    }
                }
            }
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
fun ToDoScreenPreview() {
    ToDoScreen(modifier = Modifier.padding(16.dp))
}