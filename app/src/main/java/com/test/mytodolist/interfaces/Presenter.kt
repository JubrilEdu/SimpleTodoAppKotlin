package com.test.mytodolist.interfaces

import com.test.mytodolist.data.TodoItem

interface Presenter {

    val items: List<TodoItem>

    fun saveTodoItem(Item: String)

    fun getTodoItem(id: String): TodoItem

    fun deleteTodoItem(id: Int)

}
