package com.fajar.angkringanpos

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fajar.angkringanpos.databinding.ActivityTambahProdukBinding
import java.text.NumberFormat
import java.util.Locale

class TambahProdukActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTambahProdukBinding
    private lateinit var db: DatabaseHelper

    // Fungsi untuk mengubah angka murni menjadi format Rupiah
    private fun formatRupiah(number: Long): String {
        val localeID = Locale("in", "ID")
        val numberFormat = NumberFormat.getCurrencyInstance(localeID)
        numberFormat.maximumFractionDigits = 0
        return numberFormat.format(number).replace("Rp", "Rp ").trim()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahProdukBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DatabaseHelper(this)

        // Pasang format otomatis untuk kolom Modal
        setupAutoFormat(binding.etModal)
        // Pasang format otomatis untuk kolom Harga Jual
        setupAutoFormat(binding.etJual)

        binding.btnSimpan.setOnClickListener {
            val nama = binding.etNama.text.toString()

            // Bersihkan format titik dan Rp sebelum dikonversi ke angka murni
            val modalStr = binding.etModal.text.toString().replace("[Rp,. ]".toRegex(), "")
            val jualStr = binding.etJual.text.toString().replace("[Rp,. ]".toRegex(), "")
            val stokStr = binding.etStok.text.toString()

            if (nama.isNotEmpty() && modalStr.isNotEmpty() && jualStr.isNotEmpty() && stokStr.isNotEmpty()) {
                try {
                    val modal = modalStr.toInt()
                    val jual = jualStr.toInt()
                    val stok = stokStr.toInt()

                    val hasil = db.addProduk(nama, modal, jual, stok)

                    if (hasil != -1L) {
                        Toast.makeText(this, "Produk $nama berhasil disimpan!", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Gagal simpan ke database", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "Input angka terlalu besar!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Lengkapi semua data!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Fungsi bantuan untuk memasang TextWatcher secara otomatis
    private fun setupAutoFormat(editText: android.widget.EditText) {
        editText.addTextChangedListener(object : TextWatcher {
            private var current = ""
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s.toString() != current) {
                    editText.removeTextChangedListener(this)

                    val cleanString = s.toString().replace("[Rp,. ]".toRegex(), "")
                    if (cleanString.isNotEmpty()) {
                        val parsed = cleanString.toLong()
                        val formatted = formatRupiah(parsed)
                        current = formatted
                        editText.setText(formatted)
                        editText.setSelection(formatted.length)
                    }

                    editText.addTextChangedListener(this)
                }
            }
        })
    }
}