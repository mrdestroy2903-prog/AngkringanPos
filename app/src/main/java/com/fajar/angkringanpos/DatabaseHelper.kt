package com.fajar.angkringanpos

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "AngkringanDB", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {

        val createTable = "
        CREATE TABLE produk (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            nama TEXT,
            harga TEXT,
            stok TEXT
        )
        ".trimIndent()

        db.execSQL(createTable)
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {

    }

    fun tambahProduk(
        nama: String,
        harga: String,
        stok: String
    ) {

        val db = writableDatabase

        val values = ContentValues()

        values.put("nama", nama)
        values.put("harga", harga)
        values.put("stok", stok)

        db.insert("produk", null, values)

        db.close()
    }
}