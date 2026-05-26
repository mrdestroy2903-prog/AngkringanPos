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
        val tvOmzet = findViewById<TextView>(R.id.tvOmzetHariIni)

        // MENGHUBUNGKAN MENU CEPAT
        val btnJual = findViewById<CardView>(R.id.btnTambahJualan)
        val btnStok = findViewById<CardView>(R.id.btnTambahStok)

        // Logika Klik Tambah Jualan
        btnJual.setOnClickListener {
            startActivity(Intent(this, PenjualanActivity::class.java))
        }

        // Logika Klik Tambah Stok (Sementara ke TambahProdukActivity)
        btnStok.setOnClickListener {
            startActivity(Intent(this, UpdateStokActivity::class.java))
        }

        updateDashboard(tvOmzet)

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

    private fun updateDashboard(view: TextView) {
        val omzet = db.getOmzetHariIni()
        view.text = "Rp $omzet"
    }

    override fun onResume() {
        super.onResume()
        val tvOmzet = findViewById<TextView>(R.id.tvOmzetHariIni)
        updateDashboard(tvOmzet)
    }
}