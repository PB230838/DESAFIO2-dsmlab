package com.example.desafio2

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.desafio2.databinding.ActivityOrderBinding

class Ordenes : AppCompatActivity() {
    private lateinit var binding: ActivityOrderBinding
    private lateinit var dbHelper: BDD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = BDD(this)

        populateOrderList()

        binding.btnConfirmOrder.setOnClickListener {
            processOrder()
            navigateToHistory()
        }
    }

    private fun populateOrderList() {
        val orders = getFormattedOrders()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, orders)
        binding.orderListView.adapter = adapter
        binding.tvTotal.text = "Total: ${calculateTotal()}"
    }

    private fun getFormattedOrders(): List<String> {
        val cursor = dbHelper.getOrders()
        val orderItems = mutableListOf<String>()

        while (cursor.moveToNext()) {
            val name = cursor.getString(cursor.getColumnIndexOrThrow(BDD.COLUMN_NAME))
            val price = cursor.getDouble(cursor.getColumnIndexOrThrow(BDD.COLUMN_PRICE))
            val quantity = cursor.getInt(cursor.getColumnIndexOrThrow(BDD.COLUMN_QUANTITY))

            val itemTotal = price * quantity
            orderItems.add("$name x$quantity - $${String.format("%.2f", itemTotal)}")
        }
        cursor.close()
        return orderItems
    }

    private fun calculateTotal(): String {
        val cursor = dbHelper.getOrders()
        var total = 0.0

        while (cursor.moveToNext()) {
            val price = cursor.getDouble(cursor.getColumnIndexOrThrow(BDD.COLUMN_PRICE))
            val quantity = cursor.getInt(cursor.getColumnIndexOrThrow(BDD.COLUMN_QUANTITY))
            total += price * quantity
        }
        cursor.close()
        return "$${String.format("%.2f", total)}"
    }

    private fun processOrder() {
        val cursor = dbHelper.getOrders()

        while (cursor.moveToNext()) {
            val name = cursor.getString(cursor.getColumnIndexOrThrow(BDD.COLUMN_NAME))
            val price = cursor.getDouble(cursor.getColumnIndexOrThrow(BDD.COLUMN_PRICE))
            val quantity = cursor.getInt(cursor.getColumnIndexOrThrow(BDD.COLUMN_QUANTITY))

            dbHelper.insertHistory(name, price, quantity)
        }
        cursor.close()

        dbHelper.clearOrders()
    }

    private fun navigateToHistory() {
        startActivity(Intent(this, Historial::class.java))
    }
}
