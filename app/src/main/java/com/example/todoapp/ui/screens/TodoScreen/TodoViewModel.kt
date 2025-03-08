package com.example.todoapp.ui.screens.TodoScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.database.TodoDatabase
import com.example.todoapp.data.model.Todo
import com.example.todoapp.data.repository.TodoRepository
import kotlinx.coroutines.launch

class TodoViewModel(application: Application): AndroidViewModel(application) {
    private val repository: TodoRepository
    val todos: LiveData<List<Todo>>

    init {
        val dao = TodoDatabase.getDatabase(application).todoDao()
        repository = TodoRepository(dao)
        todos = repository.allTodos
    }

    fun addTodo(title: String) = viewModelScope.launch {
        repository.addTodo(Todo(title = title, isCompleted = false))
    }

    fun toggleTodo(todo: Todo) = viewModelScope.launch {
        repository.updateTodo(todo.copy(isCompleted = !todo.isCompleted))
    }

    fun deleteTodo(todo: Todo) = viewModelScope.launch {
        repository.deleteTodo(todo)
    }

    fun clearCompletedTodos() = viewModelScope.launch {
        repository.clearCompletedTodos()
    }
}