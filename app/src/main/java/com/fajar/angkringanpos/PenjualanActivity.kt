package com.fajar.angkringanpos

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.fajar.angkringanpos.databinding.ActivityPenjualanBinding

class PenjualanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPenjualanBinding
    private lateinit var db: DatabaseHelper
    private var totalBayar: Int = 0 // Variabel penampung total

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPenjualanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DatabaseHelper(this)
        val listProduk = db.getAllProduk()

        // Kirim logika klik ke adapter
        val adapter = ProdukAdapter(listProduk) { produk ->
            // Logika ketika produk diklik:
            totalBayar += produk.harga // Tambah harga
            binding.tvTotalHarga.text = "Rp $totalBayar" // Update tampilan text

            Toast.makeText(this, "${produk.nama} ditambah", Toast.LENGTH_SHORT).show()
        }

        binding.rvPilihProduk.layoutManager = LinearLayoutManager(this)
        binding.rvPilihProduk.adapter = adapter

        binding.btnSimpanTransaksi.setOnClickListener {
            if (totalBayar > 0) {
                db.catatCashflow(totalBayar, "Penjualan Angkringan")
                Toast.makeText(this, "Transaksi Rp $totalBayar Berhasil!", Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(this, "Pilih barang dulu!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}