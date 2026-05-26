package com.fajar.angkringanpos

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ProdukActivity : AppCompatActivity() {

    private lateinit var recyclerProduk: RecyclerView
    private lateinit var produkList: ArrayList<Produk>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_produk)

        val dbHelper = DatabaseHelper(this)

        dbHelper.tambahProduk(
            "Mie Goreng",
            "Rp10.000",
            "Stok : 15"
        )

        recyclerProduk = findViewById(R.id.recyclerProduk)

        produkList = ArrayList()

        produkList.add(
            Produk(
                "Fanta Susu",
                "Rp5.000",
                "Stok : 12"
            )
        )

        produkList.add(
            Produk(
                "Risol Mayo",
                "Rp7.000",
                "Stok : 8"
            )
        )

        produkList.add(
            Produk(
                "Tahu Bakar",
                "Rp2.000",
                "Stok : 20"
            )
        )

        recyclerProduk.layoutManager =
            LinearLayoutManager(this)

        recyclerProduk.adapter =
            ProdukAdapter(produkList)
    }
}