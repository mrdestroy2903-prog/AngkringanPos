package com.fajar.angkringanpos

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.fajar.angkringanpos.databinding.ActivityProdukBinding

class ProdukActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProdukBinding
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var adapter: ProdukAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Menggunakan View Binding agar tidak perlu findViewById lagi
        binding = ActivityProdukBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)

        // Setup RecyclerView
        binding.recyclerProduk.layoutManager = LinearLayoutManager(this)

        // Memuat data pertama kali
        muatDataProduk()

        // Logika Tombol Tambah (Asumsi ID tombol di XML kamu adalah btnTambah atau fabTambah)
        // Jika di XML kamu ada tombol untuk ke halaman tambah, hubungkan di sini:
        // Cari bagian onCreate, lalu tambahkan ini di bawah muatDataProduk()
        binding.btnTambahProduk.setOnClickListener {
            val intent = Intent(this, TambahProdukActivity::class.java)
            startActivity(intent)
        }
    }

    // Fungsi untuk mengambil data dari Database dan menampilkannya ke List
    private fun muatDataProduk() {
        val listProduk = dbHelper.getAllProduk()
        adapter = ProdukAdapter(listProduk)
        binding.recyclerProduk.adapter = adapter
    }

    // Fungsi ini penting: Agar saat kita selesai tambah produk dan kembali ke sini,
    // daftar produknya langsung terupdate otomatis (refresh).
    override fun onResume() {
        super.onResume()
        muatDataProduk()
    }
}