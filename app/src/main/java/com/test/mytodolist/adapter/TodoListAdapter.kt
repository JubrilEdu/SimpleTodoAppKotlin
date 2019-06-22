package com.test.mytodolist.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.test.mytodolist.R
import com.test.mytodolist.presenters.TodoItemPresenterImpl

class TodoListAdapter internal constructor(internal var todoItemPresenter: TodoItemPresenterImpl) :
        RecyclerView.Adapter<TodoListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.individual_todo_item, viewGroup, false))
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val todoItem = todoItemPresenter.items.get(i)
        viewHolder.todoItemTextField.setText(todoItem.item)
        viewHolder.deleteButton.setOnClickListener { todoItemPresenter.deleteTodoItem(todoItem.id) }
    }


    override fun getItemCount(): Int {
        return todoItemPresenter.items.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var todoItemTextField: TextView
        var deleteButton: ImageButton

        init {
            todoItemTextField = itemView.findViewById<View>(R.id.textView) as TextView
            deleteButton = itemView.findViewById<View>(R.id.imageButton) as ImageButton
        }


    }

}
