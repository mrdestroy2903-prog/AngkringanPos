package com.fajar.angkringanpos

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fajar.angkringanpos.databinding.ActivityTambahProdukBinding

// WAJIB ada ": AppCompatActivity()" setelah nama class
class TambahProdukActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTambahProdukBinding
    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi View Binding
        binding = ActivityTambahProdukBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DatabaseHelper(this)

        // Logika tombol simpan
        binding.btnSimpan.setOnClickListener {
            val nama = binding.etNama.text.toString()
            val modalStr = binding.etModal.text.toString()
            val jualStr = binding.etJual.text.toString()
            val stokStr = binding.etStok.text.toString()

            if (nama.isNotEmpty() && modalStr.isNotEmpty() && jualStr.isNotEmpty() && stokStr.isNotEmpty()) {
                val modal = modalStr.toInt()
                val jual = jualStr.toInt()
                val stok = stokStr.toInt()

                val hasil = db.addProduk(nama, modal, jual, stok)

                if (hasil != -1L) {
                    Toast.makeText(this, "Produk $nama berhasil disimpan!", Toast.LENGTH_SHORT).show()
                    finish() // Tutup halaman dan balik ke list
                } else {
                    Toast.makeText(this, "Gagal simpan ke database", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Lengkapi semua data!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}