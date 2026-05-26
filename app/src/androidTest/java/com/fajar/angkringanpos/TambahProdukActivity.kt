package com.fajar.angkringanpos

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fajar.angkringanpos.databinding.ActivityTambahProdukBinding

class TambahProdukActivity : AppCompatActivity() {

    // Ini adalah binding yang kita aktifkan tadi di Gradle
    private lateinit var binding: ActivityTambahProdukBinding
    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Menghubungkan layout XML dengan kode Kotlin ini
        binding = ActivityTambahProdukBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DatabaseHelper(this)

        // Logika ketika tombol simpan diklik
        binding.btnSimpan.setOnClickListener {
            val nama = binding.etNama.text.toString()
            val modalStr = binding.etModal.text.toString()
            val jualStr = binding.etJual.text.toString()
            val stokStr = binding.etStok.text.toString()

            // Validasi: pastikan nama tidak kosong
            if (nama.isNotEmpty() && modalStr.isNotEmpty() && jualStr.isNotEmpty() && stokStr.isNotEmpty()) {

                val modal = modalStr.toInt()
                val jual = jualStr.toInt()
                val stok = stokStr.toInt()

                // Masukkan ke Database
                val hasil = db.addProduk(nama, modal, jual, stok)

                if (hasil != -1L) {
                    Toast.makeText(this, "Produk $nama berhasil disimpan!", Toast.LENGTH_SHORT).show()
                    finish() // Menutup halaman ini dan kembali ke sebelumnya
                } else {
                    Toast.makeText(this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Semua kolom harus diisi!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}