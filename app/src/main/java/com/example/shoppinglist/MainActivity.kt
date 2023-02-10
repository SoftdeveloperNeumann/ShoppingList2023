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
        activateAddButton()
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

    private fun activateAddButton(){
        binding.btnAddProduct.setOnClickListener {
            val quantityString = binding.etQuantity.text.toString()
            val product = binding.etProduct.text.toString()

            if(binding.etQuantity.text.isNullOrBlank()){
                binding.etQuantity.error = "Anzahl darf nicht leer sein"
                return@setOnClickListener
            }

            if(binding.etProduct.text.isNullOrBlank()){
                binding.etProduct.error = "Artikel darf nicht leer sein"
                return@setOnClickListener
            }

            val quantity = quantityString.toInt()
            binding.etQuantity.text.clear()
            binding.etProduct.text.clear()
            binding.etQuantity.requestFocus()
            dataSource.createShoppingMemo(quantity,product)
            showAllShoppingMemos()
        }
    }

    private fun showAllShoppingMemos(){
        val list = dataSource.allShoppingMemos
        val adapter = binding.lvShoppingMemos.adapter as ArrayAdapter<ShoppingMemo>
        adapter.clear()
        adapter.addAll(list)
        adapter.notifyDataSetChanged()

    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: DataSource wird geöffnet")
        dataSource.open()
        showAllShoppingMemos()
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: DataSource wird geschlossen")
        dataSource.close()
    }
}