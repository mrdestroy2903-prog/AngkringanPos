package com.fajar.angkringanpos

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "angkringan_pos.db"
        private const val DATABASE_VERSION = 3

        const val TABLE_PRODUK = "produk"
        const val COL_ID = "id"
        const val COL_NAMA = "nama"
        const val COL_HARGA_MODAL = "harga_modal"
        const val COL_HARGA_JUAL = "harga_jual"
        const val COL_STOK = "stok"

        const val TABLE_CASHFLOW = "cashflow"
        const val COL_CF_ID = "id"
        const val COL_CF_TIPE = "tipe" // 'MASUK' atau 'KELUAR'
        const val COL_CF_JUMLAH = "jumlah"
        const val COL_CF_KETERANGAN = "keterangan"
        const val COL_CF_TANGGAL = "tanggal" // Kolom tambahan untuk filter hari ini
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE $TABLE_PRODUK ($COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COL_NAMA TEXT, $COL_HARGA_MODAL INTEGER, $COL_HARGA_JUAL INTEGER, $COL_STOK INTEGER)")
        // Menambahkan kolom tanggal di tabel cashflow agar bisa difilter per hari
        db.execSQL("CREATE TABLE $TABLE_CASHFLOW ($COL_CF_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COL_CF_TIPE TEXT, $COL_CF_JUMLAH INTEGER, $COL_CF_KETERANGAN TEXT, $COL_CF_TANGGAL TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUK")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CASHFLOW")
        onCreate(db)
    }

    // Fungsi untuk mengambil semua produk dari DB
    fun getAllProduk(): List<Produk> {
        val list = mutableListOf<Produk>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_PRODUK", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val nama = cursor.getString(1)
                val hargaJual = cursor.getInt(3)
                val stok = cursor.getInt(4)

                list.add(Produk(id, nama, hargaJual, stok))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun addProduk(nama: String, modal: Int, jual: Int, stok: Int): Long {
        val db = this.writableDatabase
        val v = ContentValues().apply {
            put(COL_NAMA, nama); put(COL_HARGA_MODAL, modal); put(COL_HARGA_JUAL, jual); put(COL_STOK, stok)
        }
        return db.insert(TABLE_PRODUK, null, v)
    }

    fun kurangiStok(idProduk: Int, jumlahTerjual: Int) {
        val db = this.writableDatabase
        db.execSQL("UPDATE $TABLE_PRODUK SET $COL_STOK = $COL_STOK - $jumlahTerjual WHERE $COL_ID = $idProduk")
    }
    // Fungsi untuk menambah stok saat kulakan/belanja
    fun tambahStok(idProduk: Int, jumlahTambah: Int) {
        val db = this.writableDatabase
        db.execSQL("UPDATE $TABLE_PRODUK SET $COL_STOK = $COL_STOK + $jumlahTambah WHERE $COL_ID = $idProduk")
    }

    // --- PERUBAHAN DI SINI: Menambahkan tanggal otomatis ---
    fun catatCashflow(jumlah: Int, keterangan: String) {
        val db = this.writableDatabase
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val tanggalSekarang = sdf.format(Date())

        val v = ContentValues().apply {
            put(COL_CF_TIPE, "MASUK")
            put(COL_CF_JUMLAH, jumlah)
            put(COL_CF_KETERANGAN, keterangan)
            put(COL_CF_TANGGAL, tanggalSekarang)
        }
        db.insert(TABLE_CASHFLOW, null, v)
    }

    // --- FUNGSI BARU: Untuk menghitung total jualan hari ini ---
    fun getOmzetHariIni(): Int {
        var total = 0
        val db = this.readableDatabase
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val tanggalSekarang = sdf.format(Date())

        val cursor = db.rawQuery(
            "SELECT SUM($COL_CF_JUMLAH) FROM $TABLE_CASHFLOW WHERE $COL_CF_TANGGAL = ? AND $COL_CF_TIPE = 'MASUK'",
            arrayOf(tanggalSekarang)
        )

        if (cursor.moveToFirst()) {
            total = cursor.getInt(0)
        }
        cursor.close()
        return total
    }
}