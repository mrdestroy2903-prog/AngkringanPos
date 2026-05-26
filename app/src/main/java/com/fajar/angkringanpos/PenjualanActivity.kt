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

    // 1. Tipe data diperjelas agar tidak ambigu
    private val keranjangBelanja = mutableListOf<Produk>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPenjualanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DatabaseHelper(this)
        val listProduk = db.getAllProduk()

        val adapter = ProdukAdapter(listProduk) { produk ->
            // Menghitung total harga jual
            totalBayar += produk.hargaJual // Pastikan menggunakan variabel hargaJual dari class Produk
            binding.tvTotalHarga.text = "Rp $totalBayar"

            // 2. Masukkan produk yang diklik ke keranjang belanja
            keranjangBelanja.add(produk)

            Toast.makeText(this, "${produk.nama} ditambah", Toast.LENGTH_SHORT).show()
        }

        binding.rvPilihProduk.layoutManager = LinearLayoutManager(this)
        binding.rvPilihProduk.adapter = adapter

        binding.btnSimpanTransaksi.setOnClickListener {
            if (totalBayar > 0) {
                var totalModalTerjual = 0

                // 3. Menggunakan forEach agar tidak ambigu dan nama variabel konsisten
                keranjangBelanja.forEach { item ->
                    // Ambil harga modal asli produk untuk hitungan laba akurat
                    totalModalTerjual += item.hargaModal

                    // Potong stok barang di database
                    db.kurangiStok(item.id, 1)
                }

                // 4. Catat ke Cashflow (Urutan: Harga Jual, Harga Modal, Keterangan)
                val keterangan = "Penjualan: " + keranjangBelanja.joinToString { it.nama }
                db.catatCashflow(totalBayar, totalModalTerjual, keterangan)

                Toast.makeText(this, "Transaksi Berhasil Disimpan!", Toast.LENGTH_SHORT).show()

                // Bersihkan keranjang belanja sebelum pindah halaman
                keranjangBelanja.clear()
                finish()
            } else {
                Toast.makeText(this, "Keranjang masih kosong!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}