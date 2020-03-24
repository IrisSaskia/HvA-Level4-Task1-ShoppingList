package com.example.shoppinglist.UI

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.Database.ProductRepository
import com.example.shoppinglist.Model.Product
import com.example.shoppinglist.R

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var  productRepository: ProductRepository
    private val mainScope = CoroutineScope(Dispatchers.Main)

    private var products = arrayListOf<Product>()
    private val productAdapter =
        ProductAdapter(products)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Shopping List Kotlin"

        productRepository =
            ProductRepository(this)
        initViews()
    }

    private fun initViews() {
        rvShoppingList.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvShoppingList.adapter = productAdapter
        rvShoppingList.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        createItemTouchHelper().attachToRecyclerView(rvShoppingList)
        getProductsFromDatabase()

        fabAdd.setOnClickListener {
            addProduct()
        }
    }

    private fun validateFields(): Boolean {
        return if (etProduct.text.toString().isNotBlank() && etAmount.text.toString().isNotBlank()) {
            true
        } else {
            Toast.makeText(this, "Please fill in the fields", Toast.LENGTH_SHORT).show()
            false
        }
    }

    private fun addProduct() {
        if (validateFields()) {
            mainScope.launch {
                val product = Product(
                    productName = etProduct.text.toString(),
                    productAmount = etAmount.text.toString().toInt()
                )

                withContext(Dispatchers.IO) {
                    productRepository.insertProduct(product)
                }

                getProductsFromDatabase()
            }
        }
    }


    private fun getProductsFromDatabase() {
        mainScope.launch {
            val reminders = withContext(Dispatchers.IO) {
                productRepository.getAllProducts()
            }
            this@MainActivity.products.clear()
            this@MainActivity.products.addAll(reminders)
            productAdapter.notifyDataSetChanged()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    private fun deleteShoppingList() {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                productRepository.deleteAllProducts()
            }
            getProductsFromDatabase()
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_delete_shopping_list -> {
                deleteShoppingList()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun createItemTouchHelper(): ItemTouchHelper {

        // Callback which is used to create the ItemTouch helper. Only enables left swipe.
        // Use ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) to also enable right swipe.
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            // Enables or Disables the ability to move items up and down.
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            // Callback triggered when a user swiped an item.
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val productToDelete = products[position]

                mainScope.launch {
                    withContext(Dispatchers.IO) {
                        productRepository.deleteProduct(productToDelete)
                    }
                    getProductsFromDatabase()
                }
            }
        }

        return ItemTouchHelper(callback)
    }
}