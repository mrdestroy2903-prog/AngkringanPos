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
        // 1. NAIKKAN VERSI KE 4 (Wajib agar kolom baru terbentuk)
        private const val DATABASE_VERSION = 4

        const val TABLE_PRODUK = "produk"
        const val COL_ID = "id"
        const val COL_NAMA = "nama"
        const val COL_HARGA_MODAL = "harga_modal"
        const val COL_HARGA_JUAL = "harga_jual"
        const val COL_STOK = "stok"

        const val TABLE_CASHFLOW = "cashflow"
        const val COL_CF_ID = "id"
        const val COL_CF_TIPE = "tipe"
        const val COL_CF_JUMLAH = "jumlah"
        const val COL_CF_KETERANGAN = "keterangan"
        const val COL_CF_TANGGAL = "tanggal"
        // Nama kolom tambahan untuk simpan modal per transaksi
        const val COL_CF_MODAL = "modal_transaksi"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE $TABLE_PRODUK ($COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COL_NAMA TEXT, $COL_HARGA_MODAL INTEGER, $COL_HARGA_JUAL INTEGER, $COL_STOK INTEGER)")

        // 2. TAMBAHKAN KOLOM modal_transaksi di sini
        db.execSQL("CREATE TABLE $TABLE_CASHFLOW ($COL_CF_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COL_CF_TIPE TEXT, $COL_CF_JUMLAH INTEGER, $COL_CF_KETERANGAN TEXT, $COL_CF_TANGGAL TEXT, $COL_CF_MODAL INTEGER)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUK")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CASHFLOW")
        onCreate(db)
    }

    fun getAllProduk(): List<Produk> {
        val list = mutableListOf<Produk>()
        val db = this.readableDatabase
        // Ambil semua kolom
        val cursor = db.rawQuery("SELECT * FROM $TABLE_PRODUK", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val nama = cursor.getString(1)
                val hargaModal = cursor.getInt(2) // AMBIL KOLOM KE-2 (Harga Modal)
                val hargaJual = cursor.getInt(3)  // KOLOM KE-3 (Harga Jual)
                val stok = cursor.getInt(4)       // KOLOM KE-4 (Stok)

                // Masukkan semua data ke objek Produk termasuk hargaModal
                list.add(Produk(id, nama, hargaJual, stok, hargaModal))
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

    fun tambahStok(idProduk: Int, jumlahTambah: Int) {
        val db = this.writableDatabase
        db.execSQL("UPDATE $TABLE_PRODUK SET $COL_STOK = $COL_STOK + $jumlahTambah WHERE $COL_ID = $idProduk")
    }

    // 3. SEKARANG MENERIMA modal SEBAGAI PARAMETER
    fun catatCashflow(jumlah: Int, modal: Int, keterangan: String) {
        val db = this.writableDatabase
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val tanggalSekarang = sdf.format(Date())

        val v = ContentValues().apply {
            put(COL_CF_TIPE, "MASUK")
            put(COL_CF_JUMLAH, jumlah)
            put(COL_CF_KETERANGAN, keterangan)
            put(COL_CF_TANGGAL, tanggalSekarang)
            put(COL_CF_MODAL, modal) // Simpan modal ke DB
        }
        db.insert(TABLE_CASHFLOW, null, v)
    }

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

    fun getLabaHariIni(): Int {
        var totalLaba = 0
        val db = this.readableDatabase
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val tanggalSekarang = sdf.format(Date())

        // 4. RUMUS PALING AKURAT: Penjualan - Modal yang tercatat
        val cursor = db.rawQuery(
            "SELECT SUM($COL_CF_JUMLAH - $COL_CF_MODAL) FROM $TABLE_CASHFLOW WHERE $COL_CF_TANGGAL = ? AND $COL_CF_TIPE = 'MASUK'",
            arrayOf(tanggalSekarang)
        )

        if (cursor.moveToFirst()) {
            totalLaba = cursor.getInt(0)
        }
        cursor.close()
        return totalLaba
    }
}