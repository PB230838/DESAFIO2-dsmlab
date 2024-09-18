package com.example.desafio2

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Historial : AppCompatActivity() {
    private lateinit var databaseHelper: BDD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historial)

        databaseHelper = BDD(this)

        showHistory()
    }

    private fun showHistory() {
        val historyCursor = databaseHelper.getHistory()
        val historyEntries = mutableListOf<String>()

        historyCursor.use { cursor ->
            while (cursor.moveToNext()) {
                val itemName = cursor.getString(cursor.getColumnIndexOrThrow(BDD.COLUMN_NAME))
                val itemPrice = cursor.getDouble(cursor.getColumnIndexOrThrow(BDD.COLUMN_PRICE))
                val itemQuantity = cursor.getInt(cursor.getColumnIndexOrThrow(BDD.COLUMN_QUANTITY))
                val orderTime = cursor.getString(cursor.getColumnIndexOrThrow("order_time"))

                val totalAmount = itemPrice * itemQuantity
                val formattedEntry = "$orderTime: $itemName x$itemQuantity - $${String.format("%.2f", totalAmount)}"
                historyEntries.add(formattedEntry)
            }
        }

        val listView: ListView = findViewById(R.id.historyListView)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, historyEntries)
        listView.adapter = adapter
    }
}
