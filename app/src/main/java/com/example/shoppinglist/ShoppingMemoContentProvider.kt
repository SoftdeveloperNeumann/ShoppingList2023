package com.example.shoppinglist

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import java.net.URL

class ShoppingMemoContentProvider : ContentProvider() {

    companion object{
        val PROVIDER_NAME = this.javaClass.name //"com.example.shoppinglist.ShoppingMemoContentProvider"
        val URL = "content://$PROVIDER_NAME/${ShoppingMemoDbHelper.TABLE_SHOPPING_LIST}"
        val CONTENT_URI = Uri.parse(URL)
        val uriMatcher: UriMatcher
        val MEMOS = 1
        val MEMO_ID =2

        init{
            uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
                addURI(PROVIDER_NAME,ShoppingMemoDbHelper.TABLE_SHOPPING_LIST, MEMOS)
                addURI(PROVIDER_NAME, "${ShoppingMemoDbHelper.TABLE_SHOPPING_LIST}/#", MEMO_ID)
            }
        }
    }

    private lateinit var db:SQLiteDatabase

    override fun onCreate(): Boolean {
        db = ShoppingMemoDbHelper(context!!).writableDatabase
        return db!=null
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val cursor = db.query(ShoppingMemoDbHelper.TABLE_SHOPPING_LIST,
        projection,
        selection,
        selectionArgs,
        null,
        null,
        sortOrder)
        cursor.setNotificationUri(context!!.contentResolver,uri)
        return cursor
    }

    override fun getType(uri: Uri): String? {
        return when(uriMatcher.match(uri)){
            MEMO_ID -> "ShoppingListItem"
            MEMOS -> "ShoppingListDir"
            else -> throw IllegalArgumentException("Unsupported URI: $uri")
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        TODO("Not yet implemented")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        TODO("Not yet implemented")
    }
}