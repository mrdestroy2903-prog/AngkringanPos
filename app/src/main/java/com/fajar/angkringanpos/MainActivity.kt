package com.fajar.angkringanpos

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        bottomNav.setOnItemSelectedListener {

            when(it.itemId){

                R.id.nav_produk -> {

                    startActivity(
                        Intent(this, ProdukActivity::class.java)
                    )

                    true
                }

                else -> true
            }

        }

    }
}