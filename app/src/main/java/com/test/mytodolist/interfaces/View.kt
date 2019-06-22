package com.test.mytodolist.interfaces

interface View {
    fun setUpView()

    fun reloadData()

    fun showTodoInputView()

    fun createTodoItem(input: String)
}
