package com.fajar.angkringanpos

// Data class harus punya semua variabel ini agar fitur Jual & Laba berjalan
data class Produk(
    val id: Int,
    val nama: String,
    val hargaJual: Int, // Pastikan namanya hargaJual (bukan cuma harga)
    val stok: Int,
    val hargaModal: Int = 0 // Tambahkan ini untuk hitungan laba bersih
)