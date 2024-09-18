package com.example.desafio2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.desafio2.databinding.ActivityMenuBinding

class MenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMenuBinding
    private lateinit var dbHelper: BDD
    private lateinit var adapter: Adaptador

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = BDD(this)
        setupRecyclerView()
        populateMenuItems()

        binding.btnViewOrder.setOnClickListener {
            navigateToOrderActivity()
        }
    }

    private fun setupRecyclerView() {
        adapter = Adaptador(emptyList()) { item, quantity ->
            handleOrder(item, quantity)
        }
        binding.recyclerMenu.layoutManager = LinearLayoutManager(this)
        binding.recyclerMenu.adapter = adapter
    }

    private fun handleOrder(item: menus, quantity: Int) {
        dbHelper.insertOrder(item.name, item.price, quantity)
    }

    private fun populateMenuItems() {
        val items = getMenuItemsFromDatabase()
        adapter.updateItems(items)
    }

    private fun getMenuItemsFromDatabase(): List<menus> {
        val cursor = dbHelper.getMenuItems()
        val menuItems = mutableListOf<menus>()

        cursor.use {
            while (it.moveToNext()) {
                val id = it.getInt(it.getColumnIndexOrThrow(BDD.COLUMN_ID))
                val name = it.getString(it.getColumnIndexOrThrow(BDD.COLUMN_NAME))
                val price = it.getDouble(it.getColumnIndexOrThrow(BDD.COLUMN_PRICE))
                val type = it.getString(it.getColumnIndexOrThrow(BDD.COLUMN_TYPE))

                menuItems.add(menus(id, name, price, type))
            }
        }
        return menuItems
    }

    private fun navigateToOrderActivity() {
        startActivity(Intent(this, Ordenes::class.java))
    }
}
