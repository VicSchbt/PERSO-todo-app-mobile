package com.example.todoapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.todoapp.data.dao.TodoDao
import com.example.todoapp.data.model.Todo

class TodoRepository(private val todoDao: TodoDao) {

    val allTodos: LiveData<List<Todo>> = todoDao.getAllTodos()

    suspend fun addTodo(todo: Todo) {
        todoDao.addTodo(todo)
    }

    suspend fun updateTodo(todo: Todo) {
        todoDao.updateTodo(todo)
    }

    suspend fun deleteTodo(todo: Todo) {
        todoDao.deleteTodo(todo)
    }

    suspend fun clearCompletedTodos() {
        todoDao.clearCompletedTodos()
    }

    fun getTodos(): LiveData<List<Todo>> {
        return this.allTodos.map { todoEntities -> todoEntities.map {
            todoEntity -> Todo(todoEntity.id, todoEntity.title, todoEntity.isCompleted)
        }}
    }
}


