package com.example.shoppinglist

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log

class ShoppingMemoDataSource(context: Context) {

    private val columns = arrayOf(
        ShoppingMemoDbHelper.COLUMN_ID,
        ShoppingMemoDbHelper.COLUMN_QUANTITY,
        ShoppingMemoDbHelper.COLUMN_PRODUCT,
        ShoppingMemoDbHelper.COLUMN_ISSELECTED,
    )
    private val TAG = "ShoppingMemoDataSource"

    private var db: SQLiteDatabase? = null
    private val helper: ShoppingMemoDbHelper

    val allShoppingMemos: List<ShoppingMemo>
        get() {
            val shoppingMemoList: MutableList<ShoppingMemo> = ArrayList()
            val cursor = db?.query(
                ShoppingMemoDbHelper.TABLE_SHOPPING_LIST,
                columns,
                null,
                null,
                null,
                null,
                null,
                null
            )
            // der cursor muss zum Anfang zurück, wenn ich iterieren will
            cursor?.moveToFirst()
            var memo: ShoppingMemo
            while (!cursor?.isAfterLast!!) {
                memo = cursorToShoppingMemo(cursor)
                shoppingMemoList.add(memo)
                cursor.moveToNext()
            }
            cursor.close()
            return shoppingMemoList
        }

    init {
        helper = ShoppingMemoDbHelper(context)
        Log.d(TAG, "DataSource hat den Helper angelegt ")
    }

    fun open() {
        db = helper.writableDatabase
    }

    fun close() {
        helper.close()
    }

    private fun cursorToShoppingMemo(cursor: Cursor): ShoppingMemo {
        // zugriff auf Inhalte des Cursors gehen nur über den Index
        val idIndex = cursor.getColumnIndex(ShoppingMemoDbHelper.COLUMN_ID)
        val quantityIndex = cursor.getColumnIndex(ShoppingMemoDbHelper.COLUMN_QUANTITY)
        val productIndex = cursor.getColumnIndex(ShoppingMemoDbHelper.COLUMN_PRODUCT)
        val isSelectedIndex = cursor.getColumnIndex(ShoppingMemoDbHelper.COLUMN_ISSELECTED)

        val id = cursor.getLong(idIndex)
        val quantity = cursor.getInt(quantityIndex)
        val product = cursor.getString(productIndex)
        val isSelected = cursor.getInt(isSelectedIndex) != 0

        return ShoppingMemo(quantity, product, id, isSelected)
    }

    fun createShoppingMemo(quantity: Int, product: String): ShoppingMemo {

        val values = ContentValues().apply {
            put(ShoppingMemoDbHelper.COLUMN_QUANTITY, quantity)
            put(ShoppingMemoDbHelper.COLUMN_PRODUCT, product)
        }

        val insertId = db?.insert(ShoppingMemoDbHelper.TABLE_SHOPPING_LIST, null, values) ?: -1
        val cursor = db?.query(
            ShoppingMemoDbHelper.TABLE_SHOPPING_LIST, columns,
            "${ShoppingMemoDbHelper.COLUMN_ID} = $insertId", null, null, null, null
        )
        cursor?.moveToFirst()
        val memo = cursorToShoppingMemo(cursor!!)
        cursor.close()
        return memo
    }

    fun updateShoppingMemo(quantity: Int, product: String, id: Long, isSelected: Boolean) {
        val intSelected = if (isSelected) 1 else 0
        val values = ContentValues().apply {
            put(ShoppingMemoDbHelper.COLUMN_QUANTITY, quantity)
            put(ShoppingMemoDbHelper.COLUMN_PRODUCT, product)
            put(ShoppingMemoDbHelper.COLUMN_ISSELECTED, intSelected)
        }

        db!!.update(
            ShoppingMemoDbHelper.TABLE_SHOPPING_LIST,
            values,
            "${ShoppingMemoDbHelper.COLUMN_ID} = $id", null
        )
    }

    fun deleteShoppingMemo(memo: ShoppingMemo) {
        db!!.delete(ShoppingMemoDbHelper.TABLE_SHOPPING_LIST,"${ShoppingMemoDbHelper.COLUMN_ID} = ${memo.id}", null)
    }


}