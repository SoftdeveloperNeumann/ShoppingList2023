package com.example.shoppinglist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import com.example.shoppinglist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val TAG = "MainActivity"
    private lateinit var dataSource: ShoppingMemoDataSource


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataSource = ShoppingMemoDataSource(this)

        initShoppingMemoListView()
    }

    private fun initShoppingMemoListView() {
       val emptyListForInit: List<ShoppingMemo> = ArrayList()

        val adapter = object : ArrayAdapter<ShoppingMemo>(
            this,
            android.R.layout.simple_list_item_multiple_choice,
            emptyListForInit
        ){

        }

        binding.lvShoppingMemos.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: DataSource wird geöffnet")
        dataSource.open()
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: DataSource wird geschlossen")
        dataSource.close()
    }
}