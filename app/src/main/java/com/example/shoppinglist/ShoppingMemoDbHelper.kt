package com.example.shoppinglist

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class ShoppingMemoDbHelper(context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        // wird bereits beim Kompilieren initialisiert
        private const val TAG = "ShoppingMemoDbHelper"
        private const val DB_NAME = "shopping_list.db"
        private const val DB_VERSION = 2

        // Deklaration der Tabelleneigenschaften
        const val TABLE_SHOPPING_LIST = "shopping_list"
        const val COLUMN_ID = "_id"
        const val COLUMN_QUANTITY = "quantity"
        const val COLUMN_PRODUCT = "product"
        const val COLUMN_ISSELECTED = "selected"
    }

    private val SQL_CREATE_TABLE = """CREATE TABLE $TABLE_SHOPPING_LIST
        ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
        $COLUMN_QUANTITY INTEGER NOT NULL,
        $COLUMN_PRODUCT TEXT NOT NULL,
        $COLUMN_ISSELECTED BOOLEAN NOT NULL DEFAULT 0);
    """

    private val SQL_UPGRADE_TABLE = """ALTER TABLE $TABLE_SHOPPING_LIST
        ADD $COLUMN_ISSELECTED BOOLEAN NOT NULL DEFAULT 0;
    """

    override fun onCreate(db: SQLiteDatabase?) {
        Log.d(TAG, "onCreateDbHelper: Die Tabelle $TABLE_SHOPPING_LIST wird erstellt")
        try {
            db?.execSQL(SQL_CREATE_TABLE)
        } catch (e: Exception) {
            Log.e(TAG, "Fehler beim Erstellen der Datenbank", e)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_UPGRADE_TABLE)
    }
}
