package com.fajar.angkringanpos

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        db = DatabaseHelper(this)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        // Ambil ID TextView dari XML
        val tvOmzet = findViewById<TextView>(R.id.tvOmzetHariIni)
        val tvLaba = findViewById<TextView>(R.id.tvLabaHariIni)

        // MENGHUBUNGKAN MENU CEPAT
        val btnJual = findViewById<CardView>(R.id.btnTambahJualan)
        val btnStok = findViewById<CardView>(R.id.btnTambahStok)

        // Logika Klik Tambah Jualan
        btnJual.setOnClickListener {
            startActivity(Intent(this, PenjualanActivity::class.java))
        }

        // Logika Klik Tambah Stok
        btnStok.setOnClickListener {
            startActivity(Intent(this, UpdateStokActivity::class.java))
        }

        // Update data dashboard saat pertama buka
        updateDashboard(tvOmzet, tvLaba)

        bottomNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.nav_produk -> {
                    startActivity(Intent(this, ProdukActivity::class.java))
                    true
                }
                R.id.nav_jual -> {
                    startActivity(Intent(this, PenjualanActivity::class.java))
                    true
                }
                else -> true
            }
        }
    }

    // Fungsi updateDashboard yang menerima dua TextView agar tidak merah
    private fun updateDashboard(tvOmzet: TextView, tvLaba: TextView) {
        val omzet = db.getOmzetHariIni()
        val laba = db.getLabaHariIni()

        tvOmzet.text = "Rp $omzet"
        tvLaba.text = "Rp $laba"
    }

    override fun onResume() {
        super.onResume()
        // Panggil ulang ID saat kembali ke halaman ini agar data terbaru muncul
        val tvOmzet = findViewById<TextView>(R.id.tvOmzetHariIni)
        val tvLaba = findViewById<TextView>(R.id.tvLabaHariIni)
        updateDashboard(tvOmzet, tvLaba)
    }
}