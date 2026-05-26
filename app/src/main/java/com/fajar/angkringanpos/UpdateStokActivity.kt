package com.fajar.angkringanpos

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fajar.angkringanpos.databinding.ActivityUpdateStokBinding // Nanti kita buat XML-nya

class UpdateStokActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateStokBinding
    private lateinit var db: DatabaseHelper
    private var listProduk = listOf<Produk>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateStokBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DatabaseHelper(this)

        // Ambil data produk untuk ditampilkan di pilihan (Spinner)
        listProduk = db.getAllProduk()
        val namaProduk = listProduk.map { it.nama }

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, namaProduk)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerProduk.adapter = adapter

        binding.btnSimpanStok.setOnClickListener {
            val jumlahStr = binding.etJumlahTambah.text.toString()
            val posisi = binding.spinnerProduk.selectedItemPosition

            if (jumlahStr.isNotEmpty() && posisi != -1) {
                val jumlah = jumlahStr.toInt()
                val idProduk = listProduk[posisi].id

                // Eksekusi tambah stok
                db.tambahStok(idProduk, jumlah)

                // Sekalian catat sebagai pengeluaran (Opsional sesuai blueprint)
                // db.catatCashflow(0, "Tambah Stok ${listProduk[posisi].nama}", "KELUAR")

                Toast.makeText(this, "Stok berhasil ditambah!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Isi jumlah tambah stok!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}