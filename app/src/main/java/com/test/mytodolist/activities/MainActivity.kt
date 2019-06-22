package com.test.mytodolist.activities

import android.app.AlertDialog
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import com.test.mytodolist.helpers.DatabaseHelper
import com.test.mytodolist.R
import com.test.mytodolist.presenters.TodoItemPresenterImpl
import com.test.mytodolist.adapter.TodoListAdapter

class MainActivity : AppCompatActivity(), com.test.mytodolist.interfaces.View {
    internal lateinit var recyclerView: RecyclerView
    internal lateinit var todoItemPresenter: TodoItemPresenterImpl
    internal lateinit var todoListAdapter: TodoListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        recyclerView = findViewById(R.id.recycler_view)
        setUpView()
        val fab = findViewById<View>(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { showTodoInputView() }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)

    }

    override fun setUpView() {
        val databaseHelper = DatabaseHelper(this.baseContext)
        todoItemPresenter = TodoItemPresenterImpl(databaseHelper, this)
        todoListAdapter = TodoListAdapter(todoItemPresenter)
        val linearLayoutManager = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(recyclerView.context,
                DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(dividerItemDecoration)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = todoListAdapter
    }

    override fun showTodoInputView() {
        val layoutInflaterAndroid = LayoutInflater.from(applicationContext)
        val view = layoutInflaterAndroid.inflate(R.layout.add_todoitem_view, null)

        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setView(view)

        val inputNote = view.findViewById<EditText>(R.id.editText)

        builder.setCancelable(false).setPositiveButton("Add") { dialogBox, id -> }
                .setNegativeButton("Cancel"
                ) { dialogBox, id -> dialogBox.cancel() }

        val alertDialog = builder.create()
        alertDialog.show()
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(View.OnClickListener {
            // Show toast message when no text is entered
            if (TextUtils.isEmpty(inputNote.text.toString())) {
                inputNote.error = "Cannot Add empty entry"
                return@OnClickListener
            } else {
                createTodoItem(inputNote.text.toString())
                alertDialog.dismiss()
            }
        })

    }

    override fun createTodoItem(input: String) {
        todoItemPresenter.saveTodoItem(input)
        todoListAdapter.notifyDataSetChanged()

    }

    override fun reloadData() {
        todoListAdapter.notifyDataSetChanged()
    }
}
