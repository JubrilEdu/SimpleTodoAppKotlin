package com.test.mytodolist.presenters

import android.content.ContentValues
import android.util.Log
import com.test.mytodolist.helpers.DatabaseHelper
import com.test.mytodolist.interfaces.Presenter
import com.test.mytodolist.data.TodoItem
import com.test.mytodolist.interfaces.View
import java.util.*

class TodoItemPresenterImpl internal constructor(private val databaseHelper: DatabaseHelper, private val view: View) : Presenter {

    override val items: List<TodoItem>
        get() {
            val todoItems = ArrayList<TodoItem>()
            val selectQuery = "SELECT  * FROM " + databaseHelper.TABLE_NAME
            val cursor = databaseHelper.readableDatabase.rawQuery(selectQuery, null)

            if (cursor.moveToFirst()) {
                do {
                    val todoItem = TodoItem(cursor.getInt(cursor.getColumnIndex(databaseHelper.COLUMN_ID)),
                            cursor.getString(cursor.getColumnIndex(databaseHelper.COLUMN_ITEM)))
                    todoItems.add(todoItem)
                } while (cursor.moveToNext())
            }

            databaseHelper.readableDatabase.close()
            return todoItems
        }

    override fun saveTodoItem(item: String) {
        var db = databaseHelper.writableDatabase
        val values = ContentValues()
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(databaseHelper.COLUMN_ITEM, item)
        db.insert(databaseHelper.TABLE_NAME, null, values)
        val countQuery = "SELECT  * FROM " + databaseHelper.TABLE_NAME
        db = databaseHelper.readableDatabase
        val cursor = db.rawQuery(countQuery, null)

        val count = cursor.count
        cursor.close()
        Log.e("size", count.toString())
        db.close()
    }

    override fun getTodoItem(id: String): TodoItem {
        val db = databaseHelper.readableDatabase

        val cursor = db.query(databaseHelper.TABLE_NAME,
                arrayOf(databaseHelper.COLUMN_ID, databaseHelper.COLUMN_ITEM),
                databaseHelper.COLUMN_ID + "=?",
                arrayOf(id), null, null, null, null)
        cursor?.moveToFirst()

        val todoItem = TodoItem(cursor!!.getInt(cursor.getColumnIndex(databaseHelper.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(databaseHelper.COLUMN_ITEM)))
        cursor.close()

        return todoItem
    }

    override fun deleteTodoItem(id: Int) {
        val db = databaseHelper.writableDatabase
        db.delete(databaseHelper.TABLE_NAME, databaseHelper.COLUMN_ID + " = ?",
                arrayOf(id.toString()))
        db.close()
        view.reloadData()
    }
}
