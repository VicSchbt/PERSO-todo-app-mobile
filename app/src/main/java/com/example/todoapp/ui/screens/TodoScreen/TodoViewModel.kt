package com.example.todoapp.ui.screens.TodoScreen

import androidx.lifecycle.ViewModel
import com.example.todoapp.data.model.Todo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TodoViewModel: ViewModel() {
    private val _todos = MutableStateFlow<List<Todo>>(emptyList())
    val todos: StateFlow<List<Todo>> = _todos

    fun addTodo(title: String) {
        val newTodo = Todo(
            _todos.value.size + 1,
            title,
            false
        )
        _todos.value += newTodo
    }

    fun toggleTodo(todo: Todo) {
        _todos.value = _todos.value.map {
            if (todo.id == it.id) {
                it.copy(isCompleted = !todo.isCompleted)
            } else {
                it
            }
        }
    }

    fun deleteTodo(todo: Todo) {
        _todos.value = _todos.value.filter { it.id != todo.id }
    }

    fun clearCompletedTodo() {
        _todos.value = _todos.value.filter { !it.isCompleted }
    }
}