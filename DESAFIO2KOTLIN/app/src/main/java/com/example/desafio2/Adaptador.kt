package com.example.desafio2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Adaptador(private var items: List<menus>, private val onItemSelected: (menus, Int) -> Unit) : RecyclerView.Adapter<Adaptador.MenuViewHolder>() {

    class MenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameText: TextView = view.findViewById(R.id.itemName)
        val priceText: TextView = view.findViewById(R.id.itemPrice)
        val quantityText: TextView = view.findViewById(R.id.itemQuantity)
        val addButton: Button = view.findViewById(R.id.btnAdd)
        val removeButton: Button = view.findViewById(R.id.btnRemove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_menus, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val item = items[position]
        holder.nameText.text = item.name
        holder.priceText.text = "$${item.price}"


        var quantity = 0
        holder.quantityText.text = quantity.toString()

        holder.addButton.setOnClickListener {
            quantity++
            holder.quantityText.text = quantity.toString()
            onItemSelected(item, quantity)
        }


        holder.removeButton.setOnClickListener {
            if (quantity > 0) {
                quantity--
                holder.quantityText.text = quantity.toString()
                onItemSelected(item, quantity)
            }
        }
    }

    override fun getItemCount() = items.size

    fun updateItems(newItems: List<menus>) {
        items = newItems
        notifyDataSetChanged()
    }
}
