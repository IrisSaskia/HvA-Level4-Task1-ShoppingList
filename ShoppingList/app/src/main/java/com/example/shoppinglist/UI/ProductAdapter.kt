package com.example.shoppinglist.UI

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.Model.Product
import com.example.shoppinglist.R
import kotlinx.android.synthetic.main.item_product.view.*

class ProductAdapter(private val products: List<Product>) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>(){

    /**
     * Creates and returns a ViewHolder object, inflating the layout called item_reminder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        )
    }
    /* Returns the size of the list*/

    override fun getItemCount(): Int {
        return products.size
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(products[position])
    }




    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView){

        fun bind(product: Product){
            itemView.tvAmount.text = product.productAmount.toString()
            itemView.tvProduct.text = product.productName
        }
    }
}