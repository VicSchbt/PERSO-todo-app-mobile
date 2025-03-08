package com.example.todoapp.ui.screens.TodoScreen

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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.todoapp.data.model.Todo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToDoScreen(viewModel: TodoViewModel, modifier: Modifier) {
    val todos by viewModel.todos.observeAsState(emptyList())
    var filter by remember { mutableStateOf("All") }

    val filterOptions = listOf("Active", "Completed", "All")

    val filteredTodos = when (filter) {
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
        TodoInput(viewModel)


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
            items(filteredTodos) { todo -> TodoItem(todo, viewModel) }
        }
    }
}


@Composable
fun TodoInput(viewModel: TodoViewModel) {
    var text by remember { mutableStateOf("") }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            placeholder = { Text("Enter a task") }
        )
        Button(
            onClick = {
                if (text.isNotBlank()) {
                    viewModel.addTodo(text)
                }
                text = ""
            }
        ) {
            Text("Add")
        }
    }
}

@Composable
fun TodoItem(todo: Todo, viewModel: TodoViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = todo.isCompleted,
                onCheckedChange = { viewModel.toggleTodo(todo) }
            )
            Text(
                text = todo.title,
                textDecoration = if (todo.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                color = if (todo.isCompleted) Color.Gray else Color.Black
            )
        }

        IconButton(
            onClick = { viewModel.deleteTodo(todo) }
        ) {
            Icon(Icons.Outlined.Delete, contentDescription = "Delete ${todo.title}")

        }
    }
}