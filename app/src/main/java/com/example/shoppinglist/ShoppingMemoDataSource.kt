package com.example.shoppinglist

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log

class ShoppingMemoDataSource(context: Context) {

    private val columns= arrayOf(
            ShoppingMemoDbHelper.COLUMN_ID,
            ShoppingMemoDbHelper.COLUMN_QUANTITY,
            ShoppingMemoDbHelper.COLUMN_PRODUCT,
    )
    private val TAG = "ShoppingMemoDataSource"

    private var db: SQLiteDatabase? = null
    private val helper: ShoppingMemoDbHelper

    val allShoppingMemos: List<ShoppingMemo>
    get(){
        val shoppingMemoList:MutableList<ShoppingMemo> = ArrayList()
        val cursor = db?.query(ShoppingMemoDbHelper.TABLE_SHOPPING_LIST,columns,null,null,null,null,null,null)
        // der cursor muss zum Anfang zurück, wenn ich iterieren will
        cursor?.moveToFirst()
        var memo:ShoppingMemo
        while (!cursor?.isAfterLast!!){
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

    fun open(){
        db = helper.writableDatabase
    }

    fun close(){
        helper.close()
    }

    private fun cursorToShoppingMemo(cursor: Cursor): ShoppingMemo {
        // zugriff auf Inhalte des Cursors gehen nur über den Index
        val idIndex = cursor.getColumnIndex(ShoppingMemoDbHelper.COLUMN_ID)
        val quantityIndex = cursor.getColumnIndex(ShoppingMemoDbHelper.COLUMN_QUANTITY)
        val productIndex = cursor.getColumnIndex(ShoppingMemoDbHelper.COLUMN_PRODUCT)

        val id = cursor.getLong(idIndex)
        val quantity = cursor.getInt(quantityIndex)
        val product = cursor.getString(productIndex)

        return ShoppingMemo(quantity, product, id)
    }

    fun createShoppingMemo(quantity: Int, product: String):ShoppingMemo {

        val values = ContentValues().apply {
            put(ShoppingMemoDbHelper.COLUMN_QUANTITY,quantity)
            put(ShoppingMemoDbHelper.COLUMN_PRODUCT,product)
        }

        val insertId = db?.insert(ShoppingMemoDbHelper.TABLE_SHOPPING_LIST,null,values) ?: -1
        val cursor = db?.query(ShoppingMemoDbHelper.TABLE_SHOPPING_LIST,columns,
            "${ShoppingMemoDbHelper.COLUMN_ID} = $insertId",null,null,null,null)
        cursor?.moveToFirst()
        val memo = cursorToShoppingMemo(cursor!!)
        cursor.close()
        return memo
    }


}