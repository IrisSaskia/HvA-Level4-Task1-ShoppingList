package com.example.shoppinglist.Database

import android.content.Context
import com.example.shoppinglist.Model.Product

class ProductRepository(context: Context) {

    private val productDao: ProductDao

    init {
        val database =
            ShoppingListRoomDatabase.getDatabase(
                context
            )
        productDao = database!!.productDao()
    }

    suspend fun getAllProducts(): List<Product> {
        return productDao.getAllProducts()
    }

    suspend fun insertProduct(product: Product) {
        productDao.insertProduct(product)
    }

    suspend fun deleteProduct(product: Product) {
        productDao.deleteProduct(product)
    }

    suspend fun deleteAllProducts() {
        productDao.deleteAllProducts()
    }

}