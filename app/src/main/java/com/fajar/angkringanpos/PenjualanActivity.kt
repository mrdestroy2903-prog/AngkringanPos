package com.fajar.angkringanpos

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.fajar.angkringanpos.databinding.ActivityPenjualanBinding

class PenjualanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPenjualanBinding
    private lateinit var db: DatabaseHelper
    private var totalBayar: Int = 0

    // 1. TAMBAHKAN INI: Keranjang belanja sementara
    private val keranjangBelanja = mutableListOf<Produk>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPenjualanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DatabaseHelper(this)
        val listProduk = db.getAllProduk()

        val adapter = ProdukAdapter(listProduk) { produk ->
            totalBayar += produk.harga
            binding.tvTotalHarga.text = "Rp $totalBayar"

            // 2. TAMBAHKAN INI: Masukkan produk yang diklik ke keranjang
            keranjangBelanja.add(produk)

            Toast.makeText(this, "${produk.nama} ditambah", Toast.LENGTH_SHORT).show()
        }

        binding.rvPilihProduk.layoutManager = LinearLayoutManager(this)
        binding.rvPilihProduk.adapter = adapter

        binding.btnSimpanTransaksi.setOnClickListener {
            if (totalBayar > 0) {
                // 3. TAMBAHKAN INI: Proses potong stok untuk setiap barang di keranjang
                for (item in keranjangBelanja) {
                    db.kurangiStok(item.id, 1) // Memotong stok 1 per item
                }

                db.catatCashflow(totalBayar, "Penjualan Angkringan")
                Toast.makeText(this, "Transaksi Rp $totalBayar Berhasil!", Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(this, "Pilih barang dulu!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}