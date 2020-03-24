package com.example.shoppinglist

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shoppingTable")
data class Product (
    @ColumnInfo(name = "name")
    var productName: String,

    @ColumnInfo(name = "amount")
    var productAmount: Int,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null
)